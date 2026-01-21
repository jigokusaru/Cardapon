package com.cardapon.commands;

import com.cardapon.core.data.Cardapon;
import com.cardapon.core.data.Species;
import com.cardapon.core.logic.CardaponManager;
import com.cardapon.registry.SpeciesRegistry;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;

import javax.annotation.Nonnull;

public class SummonCommand extends CommandBase {

    // 1. The ID is the ONLY required positional argument
    private final RequiredArg<String> idArg = this.withRequiredArg("id", "Species ID", ArgTypes.STRING);

    // 2. Everything else is a NAMED FLAG (e.g. --level 5)
    private final OptionalArg<Integer> levelArg = this.withOptionalArg("level", "Level", ArgTypes.INTEGER);
    private final OptionalArg<String> modelArg = this.withOptionalArg("model", "Model Name", ArgTypes.STRING);

    public SummonCommand() {
        super("summon", "Usage: /summon <id> [--level <num>] [--model <name>]");
        this.setPermissionGroup(GameMode.Creative);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        if (!ctx.isPlayer()) return;

        // 1. Get ID
        String input = idArg.get(ctx);

        // 2. Search Check
        if (input.equalsIgnoreCase("search")) {
            runSearch(ctx);
            return;
        }

        // 3. Get Options (Defaults handled automatically)
        int level = 5;
        if (levelArg.provided(ctx)) {
            level = levelArg.get(ctx);
        }

        String modelName = null;
        if (modelArg.provided(ctx)) {
            modelName = modelArg.get(ctx);
        }

        // 4. Resolve Logic
        String speciesId = resolveId(input);
        Species species = SpeciesRegistry.get(speciesId);

        if (species == null) {
            ctx.sendMessage(Message.raw("Unknown Species: " + speciesId));
            return;
        }

        try {
            Cardapon newCardapon = new Cardapon(species.getId(), level);
            Ref<EntityStore> senderRef = ctx.senderAsPlayerRef();

            if (senderRef != null && senderRef.isValid()) {
                World world = senderRef.getStore().getExternalData().getWorld();

                // Helper variables for lambda
                final String finalModel = modelName;
                final Cardapon finalCardapon = newCardapon;
                final int finalLevel = level;

                world.execute(() -> {
                    TransformComponent transform = senderRef.getStore().getComponent(senderRef, TransformComponent.getComponentType());
                    if (transform != null) {
                        Vector3d spawnPos = transform.getPosition().add(0, 1, 0);

                        CardaponManager.get().spawnCardapon(finalCardapon, world, spawnPos, finalModel);

                        String m = (finalModel != null) ? finalModel : "Default";
                        ctx.sendMessage(Message.raw("Summoned " + finalCardapon.getNickname() + " (Lvl " + finalLevel + ") Model: " + m));
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runSearch(CommandContext ctx) {
        ctx.sendMessage(Message.raw("Searching console..."));
        System.out.println("--- SEARCH START ---");
        try {
            var internalMap = ModelAsset.getAssetMap().getAssetMap();
            for (String key : internalMap.keySet()) {
                if (key.toLowerCase().contains("kweebec")) {
                    System.out.println("FOUND: " + key);
                }
            }
        } catch (Exception e) {
            System.out.println("Search Error: " + e.getMessage());
        }
        System.out.println("--- SEARCH END ---");
    }

    private String resolveId(String input) {
        if (input.startsWith("species_")) return input;
        try {
            int num = Integer.parseInt(input);
            return String.format("species_%03d", num);
        } catch (NumberFormatException e) {
            return input;
        }
    }
}
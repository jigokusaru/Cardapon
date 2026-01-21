package com.cardapon.commands;

import com.cardapon.core.data.Cardapon;
import com.cardapon.core.data.Species;
import com.cardapon.registry.SpeciesRegistry;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
// FIX: The Argument class is in the 'system' package
import com.hypixel.hytale.server.core.command.system.arguments.system.Argument;
// FIX: The ArgTypes registry is in the 'types' package
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;

import javax.annotation.Nonnull;

public class SpawnCommand extends CommandBase {

    // 1. Define Arguments using the Generic Argument<T> class
    private final Argument<String> speciesIdArg;
    private final Argument<Integer> levelArg;

    public SpawnCommand() {
        super("spawn", "Spawns a Cardapon. Usage: /spawn <id> [level]");
        this.setPermissionGroup(GameMode.Creative);

        // 2. Initialize using ArgTypes constants (STRING and INTEGER)
        // This links the generic "Argument" logic to the specific "Type" definition
        this.speciesIdArg = new Argument<>("species_id", ArgTypes.STRING);
        this.levelArg = new Argument<>("level", ArgTypes.INTEGER);

        // 3. Register arguments to the command
        this.addArgument(speciesIdArg);
        this.addArgument(levelArg);
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        // 4. Retrieve values using the Argument objects as keys
        // The <String> generic type ensures this returns a String directly
        String input = ctx.get(speciesIdArg);

        // Handle optional level (returns null if not provided)
        Integer levelObj = ctx.get(levelArg);
        int level = (levelObj != null) ? levelObj : 5;

        // --- Logic ---

        // Resolve ID (e.g., "0" -> "species_000")
        String speciesId = resolveId(input);
        Species species = SpeciesRegistry.get(speciesId);

        if (species == null) {
            ctx.sendMessage(Message.raw("Could not find Species with ID: " + speciesId));
            return;
        }

        try {
            // Create the Data
            Cardapon newCardapon = new Cardapon(species.getId(), level);

            // Feedback
            ctx.sendMessage(Message.raw("Spawned Data: " + newCardapon.getNickname()));
            ctx.sendMessage(Message.raw("- Lvl: " + newCardapon.getLevel()));
            ctx.sendMessage(Message.raw("- HP: " + newCardapon.getHp() + " / " + newCardapon.getStats().hp));
            ctx.sendMessage(Message.raw("- Type: " + newCardapon.getType()));

        } catch (Exception e) {
            ctx.sendMessage(Message.raw("Error: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    /**
     * Helper to make typing IDs easier.
     * Input "0" -> Returns "species_000"
     * Input "species_001" -> Returns "species_001"
     */
    private String resolveId(String input) {
        if (input.startsWith("species_")) {
            return input;
        }
        try {
            int num = Integer.parseInt(input);
            return String.format("species_%03d", num);
        } catch (NumberFormatException e) {
            return input;
        }
    }
}
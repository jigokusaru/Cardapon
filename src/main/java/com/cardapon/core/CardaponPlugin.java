package com.cardapon.core;

import com.cardapon.commands.ExampleCommand;
import com.cardapon.commands.SpawnCommand;
import com.cardapon.registry.MoveRegistry;
import com.cardapon.registry.SpeciesRegistry;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

public class CardaponPlugin extends JavaPlugin {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public CardaponPlugin(JavaPluginInit init) {
        super(init);
        // Correct Hytale syntax: atInfo().log(...)
        LOGGER.atInfo().log("Cardapon System v%s is starting...", this.getManifest().getVersion().toString());
    }

    @Override
    protected void setup() {
        // --- 1. INITIALIZE REGISTRIES ---
        LOGGER.atInfo().log("Initializing Move Registry...");
        MoveRegistry.init();

        LOGGER.atInfo().log("Initializing Species Registry...");
        SpeciesRegistry.init();

        // --- 2. REGISTER COMMANDS ---
        this.getCommandRegistry().registerCommand(new SpawnCommand("Cardapon", this.getManifest().getVersion().toString()));

        LOGGER.atInfo().log("Cardapon System loaded successfully.");
    }
}
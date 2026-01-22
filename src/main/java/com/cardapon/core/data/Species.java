package com.cardapon.core.data;

public class Species {

    // --- IDENTITY ---
    private final String id;           // Internal ID (e.g., "species_001")
    private final String displayName;  // In-Game Name (e.g., "Pyrox")
    private final String description;  // Lore / Dex Entry

    // --- BIOLOGY ---
    private final Types primaryType;
    private final Types secondaryType; // Can be Types.NONE
    private final StatBlock baseStats; // The "BP" values (Range: 50-120)

    // --- ASSETS ---
    private final String modelPath;    // Path to Hytale model file

    // --- CONSTRUCTOR ---
    public Species(String id, String name, Types type1, Types type2, StatBlock baseStats, String desc, String model) {
        this.id = id;
        this.displayName = name;
        this.primaryType = type1;
        this.secondaryType = type2;
        this.baseStats = baseStats;
        this.description = desc;
        this.modelPath = model;
    }

    // --- GETTERS ---
    public String getId() { return id; }
    public String getName() { return displayName; }
    public Types getPrimaryType() { return primaryType; }
    public Types getSecondaryType() { return secondaryType; }
    public StatBlock getBaseStats() { return baseStats; }
    public String getDescription() { return description; }
    public String getModelPath() { return modelPath; }
}
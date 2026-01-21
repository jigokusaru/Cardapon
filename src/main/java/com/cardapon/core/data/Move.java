package com.cardapon.core.data;

public class Move {

    // --- IDENTITY ---
    private final String id;         // e.g., "move_tackle"
    private final String name;       // e.g., "Tackle"
    private final String description;

    // --- COMBAT DATA ---
    private final Types type;        // e.g., Types.BRAWL
    private final MoveCategory category;
    private final int power;         // Base Power (e.g., 40)
    private final int accuracy;      // 0-100 (100 = Sure Hit usually)
    private final int baseEnergyCost; // The cost BEFORE Resonance

    // --- CONSTRUCTOR ---
    public Move(String id, String name, Types type, MoveCategory category, int power, int acc, int cost, String desc) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.category = category;
        this.power = power;
        this.accuracy = acc;
        this.baseEnergyCost = cost;
        this.description = desc;
    }

    // --- GETTERS ---
    public String getId() { return id; }
    public String getName() { return name; }
    public Types getType() { return type; }
    public MoveCategory getCategory() { return category; }
    public int getPower() { return power; }
    public int getAccuracy() { return accuracy; }
    public int getBaseCost() { return baseEnergyCost; }
}
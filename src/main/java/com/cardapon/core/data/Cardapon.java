package com.cardapon.core.data;

import com.cardapon.core.math.CardaponMath;
import com.cardapon.registry.SpeciesRegistry;
import java.util.UUID;

public class Cardapon {

    // --- IDENTITY ---
    private final UUID uniqueId;
    private String nickname;
    private final String speciesId;
    private final Species species;

    // --- BIOLOGY ---
    private final Types primaryType;
    private final Types secondaryType;

    // --- PROGRESSION ---
    private int level;

    // --- STAT DATA ---
    private final StatBlock basePoints;
    private final StatBlock innatePoints;
    private final StatBlock trainingPoints;

    // --- LIVE COMBAT STATS ---
    private StatBlock maxStats;
    private int currentHp;
    private int currentEnergy;

    // --- CONSTRUCTOR (This fixes the "Required Types" error) ---
    public Cardapon(String speciesId, int level) {
        // 1. Look up the Species Blueprint
        this.species = SpeciesRegistry.get(speciesId);

        // Safety Check
        if (this.species == null) {
            throw new IllegalArgumentException("Invalid Species ID: " + speciesId);
        }

        this.uniqueId = UUID.randomUUID();
        this.speciesId = speciesId;
        this.level = level;

        // 2. Inherit data from Blueprint
        this.primaryType = species.getPrimaryType();
        this.secondaryType = species.getSecondaryType();
        this.basePoints = species.getBaseStats();

        // 3. Randomize IPs (0-30)
        this.innatePoints = new StatBlock(
                rand(30), rand(30), rand(30), rand(30), rand(30), rand(30), rand(30)
        );

        // 4. Empty TPs
        this.trainingPoints = new StatBlock();

        // 5. Calculate Stats
        recalculateStats();

        // 6. Full Heal
        this.currentHp = maxStats.hp;
        this.currentEnergy = (int) Math.floor(maxStats.enr * 0.25);
    }

    // --- LOGIC ---
    public void recalculateStats() {
        this.maxStats = new StatBlock();
        maxStats.hp  = CardaponMath.calcHP(basePoints.hp, innatePoints.hp, trainingPoints.hp, level);
        maxStats.atk = CardaponMath.calcStat(basePoints.atk, innatePoints.atk, trainingPoints.atk, level);
        maxStats.def = CardaponMath.calcStat(basePoints.def, innatePoints.def, trainingPoints.def, level);
        maxStats.aur = CardaponMath.calcStat(basePoints.aur, innatePoints.aur, trainingPoints.aur, level);
        maxStats.res = CardaponMath.calcStat(basePoints.res, innatePoints.res, trainingPoints.res, level);
        maxStats.agi = CardaponMath.calcStat(basePoints.agi, innatePoints.agi, trainingPoints.agi, level);
        maxStats.enr = CardaponMath.calcEnergy(basePoints.enr, innatePoints.enr, trainingPoints.enr, level);
    }

    public void takeDamage(int amount) {
        this.currentHp -= amount;
        if (this.currentHp < 0) this.currentHp = 0;
    }

    // --- GETTERS (This fixes the "getLevel/getNickname not found" error) ---

    public String getNickname() {
        return (nickname != null && !nickname.isEmpty()) ? nickname : species.getName();
    }

    public int getLevel() { return level; }
    public Types getType() { return primaryType; }
    public StatBlock getStats() { return maxStats; }
    public int getHp() { return currentHp; }
    public int getEnergy() { return currentEnergy; }

    private int rand(int max) {
        return (int)(Math.random() * (max + 1));
    }
}
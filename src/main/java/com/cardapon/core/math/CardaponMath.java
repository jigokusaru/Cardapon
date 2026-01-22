package com.cardapon.core.math;

public class CardaponMath {

    // --- 1. CORE STAT CALCULATORS ---

    /**
     * Calculates Max HP based on the "Scaled RPG" formula.
     * Formula: Floor[ ((BP + IP + TP/4) * Level / 20) + (Level * 2) + 10 ]
     */
    public static int calcHP(int bp, int ip, int tp, int level) {
        double core = (bp + ip + (tp / 4.0));
        double result = (core * level / 20.0) + (level * 2.0) + 10.0;
        return (int) Math.floor(result); // Always round down
    }

    /**
     * Calculates Standard Stats (Atk, Def, Aur, Res, Agi).
     * Formula: Floor[ ((BP + IP + TP/4) * Level / 50) + 5 ]
     */
    public static int calcStat(int bp, int ip, int tp, int level) {
        double core = (bp + ip + (tp / 4.0));
        double result = (core * level / 50.0) + 5.0;
        return (int) Math.floor(result);
    }

    /**
     * Calculates Max Energy (Enr).
     * Note: Uses TP/8 divisor.
     * Formula: Floor[ ((BP + IP + TP/8) * Level / 50) + 20 ]
     */
    public static int calcEnergy(int bp, int ip, int tp, int level) {
        double core = (bp + ip + (tp / 8.0));
        double result = (core * level / 50.0) + 20.0;
        return (int) Math.floor(result);
    }

    // --- 2. COMBAT CALCULATORS ---

    /**
     * Calculates Final Damage.
     * Formula: Floor[ ((Base * Power * Ratio * Resonance) * Random) / 30 ]
     */
    public static int calcDamage(int level, int power, int atk, int def, boolean isResonance, double randomMod) {
        // 1. Base Level Scaling
        double base = (level * 0.4) + 2.0;

        // 2. Stat Ratio
        double ratio = (double) atk / (double) def;

        // 3. Resonance (STAB)
        double resonance = isResonance ? 1.1 : 1.0;

        // 4. Calculate Raw
        double raw = base * power * ratio * resonance * randomMod;

        // 5. Apply Pacing Divisor (30)
        return (int) Math.floor(raw / 30.0);
    }

    /**
     * Checks for Critical Hit.
     * Chance: ~3.57% (1 in 28)
     */
    public static boolean isCriticalHit() {
        return (Math.random() * 28) < 1;
    }
}
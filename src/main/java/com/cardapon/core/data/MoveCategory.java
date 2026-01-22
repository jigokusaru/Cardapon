package com.cardapon.core.data;

public enum MoveCategory {
    PHYSICAL, // Uses ATK vs DEF
    AURA,     // Uses AUR vs RES
    STATUS    // Non-damaging (Buffs, Debuffs, Hazards)
}
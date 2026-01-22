package com.cardapon.core.data;

public enum Types {
    VOID,
    IGNIS,
    AQUA,
    PLANT,
    TERRA,
    WIND,
    ELEC,
    ICE,
    METAL,
    TOXIC,
    MAGIC,
    SPIRIT,
    DRACO,
    LIGHT,
    SHADOW,
    BRAWL,
    NONE;

    @Override
    public String toString() {
        String name = super.toString();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }
}
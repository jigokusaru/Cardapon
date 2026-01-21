package com.cardapon.core.data;

public class StatBlock {
    // The 7 Core Stats
    public int hp;
    public int atk;
    public int def;
    public int aur;
    public int res;
    public int agi;
    public int enr;

    // Constructor to initialize empty
    public StatBlock() {}

    // Constructor to quickly clone stats
    public StatBlock(int hp, int atk, int def, int aur, int res, int agi, int enr) {
        this.hp = hp; this.atk = atk; this.def = def;
        this.aur = aur; this.res = res; this.agi = agi; this.enr = enr;
    }

    // Helper to get a specific stat by name (Useful for UI)
    public int getStat(String name) {
        switch(name.toLowerCase()) {
            case "hp": return hp;
            case "atk": return atk;
            case "def": return def;
            case "aur": return aur;
            case "res": return res;
            case "agi": return agi;
            case "enr": return enr;
            default: return 0;
        }
    }
}
package com.cardapon.registry;

import com.cardapon.core.data.Move;
import com.cardapon.core.data.MoveCategory;
import com.cardapon.core.data.Types;
import java.util.HashMap;
import java.util.Map;

public class MoveRegistry {

    public static final Map<String, Move> MOVES = new HashMap<>();

    public static void init() {

        // --- 1. THE GENERIC VOID MOVE ---
        register(new Move(
                "force_wave",           // ID
                "Force Wave",           // Name
                Types.VOID,             // Type
                MoveCategory.AURA,      // Category (Special)
                40,                     // Power (Standard low tier)
                100,                    // Accuracy
                15,                     // Energy Cost (Low cost Opener)
                "A ripple of empty energy. Basic void attack."
        ));

        // --- 2. THE DESPERATE MOVES (STRUGGLE) ---
        // These are triggered automatically when Energy is too low.

        register(new Move(
                "desperate_strike",
                "Desperate Strike",
                Types.NONE,             // Typeless (or BRAWL?) usually typeless for struggle
                MoveCategory.PHYSICAL,
                20,                     // Low Power
                1000,                   // Sure Hit (Arbitrary high number)
                0,                      // 0 Cost
                "A frantic physical blow. Recoils on user."
        ));

        register(new Move(
                "desperate_blast",
                "Desperate Blast",
                Types.NONE,             // Typeless
                MoveCategory.AURA,
                20,                     // Low Power
                1000,                   // Sure Hit
                0,                      // 0 Cost
                "A chaotic release of remaining energy. Recoils on user."
        ));
    }

    private static void register(Move move) {
        MOVES.put(move.getId(), move);
    }

    public static Move get(String id) {
        return MOVES.get(id);
    }
}
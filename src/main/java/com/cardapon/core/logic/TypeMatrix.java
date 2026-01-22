package com.cardapon.core.logic;

import com.cardapon.core.data.Types;
import java.util.EnumMap;
import java.util.Map;

public class TypeMatrix {
    private static final Map<Types, Map<Types, Float>> MATRIX = new EnumMap<>(Types.class);

    static {
        for (Types atk : Types.values()) {
            MATRIX.put(atk, new EnumMap<>(Types.class));
            for (Types def : Types.values()) {
                MATRIX.get(atk).put(def, 1.0f);
            }
        }

        // Example: IGNIS beats PLANT
        set(Types.IGNIS, Types.PLANT, 2.0f);
        // ... Fill the rest later
    }

    private static void set(Types atk, Types def, float mult) {
        MATRIX.get(atk).put(def, mult);
    }

    public static float getMultiplier(Types atk, Types def) {
        if (atk == Types.NONE || def == Types.NONE) return 1.0f;
        return MATRIX.get(atk).get(def);
    }

    public static boolean isResonance(Types userType, Types moveType) {
        return userType == moveType && userType != Types.NONE;
    }
}
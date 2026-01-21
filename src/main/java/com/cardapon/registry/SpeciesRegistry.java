package com.cardapon.registry;

import com.cardapon.core.data.Species;
import com.cardapon.core.data.StatBlock;
import com.cardapon.core.data.Types;
import java.util.HashMap;
import java.util.Map;

public class SpeciesRegistry {

    private static final Map<String, Species> SPECIES_MAP = new HashMap<>();

    public static void init() {

        // --- 000: DUMBY (The Test Subject) ---
        // Should never spawn naturally. Used for error handling and testing.
        register(new Species(
                "species_000",
                "Dummy",
                Types.VOID,
                Types.NONE,
                new StatBlock(100, 100, 100, 100, 100, 100, 100), // Flat 100s
                "A test entity. If you see this, something went wrong.",
                "models/cardapons/dummy.obj" // Placeholder model
        ));

        // Insert your real creature here when ready.
    }

    private static void register(Species s) {
        SPECIES_MAP.put(s.getId(), s);
    }

    public static Species get(String id) {
        return SPECIES_MAP.get(id);
    }
}
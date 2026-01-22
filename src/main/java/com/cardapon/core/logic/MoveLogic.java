package com.cardapon.core.logic;

import com.cardapon.core.data.Cardapon;
import com.cardapon.core.data.Move;
import com.cardapon.core.data.Types;

public class MoveLogic {

    /**
     * Calculates the ACTUAL Energy Cost for a specific user.
     * Applies the -25% Resonance discount if types match.
     */
    public static int getModifiedCost(Cardapon user, Move move) {
        int cost = move.getBaseCost();

        // Check for Resonance (Type Match)
        if (TypeMatrix.isResonance(user.getType(), move.getType())) {
            // Apply 25% Discount (Round Down)
            int discount = (int) Math.floor(cost * 0.25);
            cost -= discount;
        }

        // Safety: Cost cannot be negative
        return Math.max(0, cost);
    }

    /**
     * Calculates the Resonance Damage Multiplier.
     * Returns 1.1f if match, 1.0f if not.
     */
    public static float getResonanceBonus(Cardapon user, Move move) {
        if (TypeMatrix.isResonance(user.getType(), move.getType())) {
            return 1.1f; // +10% Damage
        }
        return 1.0f;
    }
}
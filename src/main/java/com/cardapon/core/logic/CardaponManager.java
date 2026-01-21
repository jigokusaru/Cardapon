package com.cardapon.core.logic;

import com.cardapon.core.data.Cardapon;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import com.hypixel.hytale.server.core.modules.entity.tracker.NetworkId;
import com.hypixel.hytale.component.AddReason;
import com.hypixel.hytale.server.core.modules.entity.component.ModelComponent;
import com.hypixel.hytale.server.core.asset.type.model.config.ModelAsset;
import com.hypixel.hytale.server.core.asset.type.model.config.Model;

import java.util.HashMap;
import java.util.Map;

public class CardaponManager {

    private static final CardaponManager INSTANCE = new CardaponManager();
    private final Map<Long, Cardapon> activeCardapons = new HashMap<>();

    public static CardaponManager get() {
        return INSTANCE;
    }

    // UPDATED: Now takes 'String modelName' as the 4th argument
    public void spawnCardapon(Cardapon data, World world, Vector3d position, String modelName) {
        world.execute(() -> {
            try {
                Store<EntityStore> store = world.getEntityStore().getStore();

                var holder = EntityStore.REGISTRY.newHolder();

                // 1. Position
                holder.addComponent(TransformComponent.getComponentType(),
                        new TransformComponent(position, new Vector3f(0, 0, 0))
                );

                // 2. Network ID
                int netIdInt = (int) store.getExternalData().takeNextNetworkId();
                holder.addComponent(NetworkId.getComponentType(), new NetworkId(netIdInt));

                // 3. Model Logic (Uses the passed modelName)
                ModelAsset asset = null;

                // Try to find the specific model name passed from the command
                if (modelName != null) {
                    asset = ModelAsset.getAssetMap().getAsset(modelName);
                }

                // Fallback to DEBUG if not found
                if (asset == null) {
                    System.out.println("WARN: Model '" + modelName + "' not found. Using DEBUG box.");
                    asset = ModelAsset.DEBUG;
                }

                if (asset != null) {
                    // Create Model Object using the Factory Method we found earlier
                    Model modelObject = Model.createScaledModel(asset, 1.0f);
                    holder.addComponent(ModelComponent.getComponentType(), new ModelComponent(modelObject));
                }

                // 4. Spawn
                store.addEntity(holder, AddReason.SPAWN);

                // 5. Register
                long registrationId = (long) netIdInt;
                register(registrationId, data);

                System.out.println("SUCCESS: Spawned Cardapon " + data.getNickname() + " [ID: " + registrationId + "]");

            } catch (Exception e) {
                System.out.println("Error in spawnCardapon: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void register(long entityId, Cardapon data) {
        activeCardapons.put(entityId, data);
    }

    public Cardapon getCardapon(long entityId) {
        return activeCardapons.get(entityId);
    }

    public void unregister(long entityId) {
        activeCardapons.remove(entityId);
    }
}
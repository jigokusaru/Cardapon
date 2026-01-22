package com.cardapon.core.logic;

import com.cardapon.core.data.Cardapon;
import com.cardapon.core.data.Species;
import com.cardapon.registry.SpeciesRegistry;
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
import com.hypixel.hytale.server.core.Message;

// --- COMPONENTS ---
import com.hypixel.hytale.server.core.modules.physics.component.PhysicsValues;
import com.hypixel.hytale.server.core.modules.physics.component.Velocity;
import com.hypixel.hytale.server.core.entity.damage.DamageDataComponent;
import com.hypixel.hytale.server.core.modules.entity.component.BoundingBox;
import com.hypixel.hytale.server.core.modules.entity.component.DisplayNameComponent;
import com.hypixel.hytale.server.core.entity.movement.MovementStatesComponent;
import com.hypixel.hytale.server.core.entity.entities.player.movement.MovementManager;
import com.hypixel.hytale.math.shape.Box;
import com.hypixel.hytale.server.core.modules.entity.component.CollisionResultComponent;
import com.hypixel.hytale.server.core.modules.entity.component.PositionDataComponent;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation;

// --- THE FIX: USE THE CONSTRUCTOR YOU FOUND ---
import com.hypixel.hytale.server.core.modules.entity.component.NewSpawnComponent;

import java.util.HashMap;
import java.util.Map;

public class CardaponManager {

    private static final CardaponManager INSTANCE = new CardaponManager();
    private final Map<Long, Cardapon> activeCardapons = new HashMap<>();

    public static CardaponManager get() {
        return INSTANCE;
    }

    public void spawnCardapon(Cardapon data, World world, Vector3d position, String commandModelOverride) {
        world.execute(() -> {
            try {
                Store<EntityStore> store = world.getEntityStore().getStore();
                var holder = EntityStore.REGISTRY.newHolder();

                // 1. POSITION & TRANSFORM
                holder.addComponent(TransformComponent.getComponentType(),
                        new TransformComponent(position, new Vector3f(0, 0, 0))
                );
                holder.addComponent(PositionDataComponent.getComponentType(), new PositionDataComponent());
                holder.addComponent(HeadRotation.getComponentType(), new HeadRotation(new Vector3f(0, 0, 0)));

                // 2. PHYSICS & GRAVITY
                holder.addComponent(PhysicsValues.getComponentType(), new PhysicsValues());
                holder.addComponent(Velocity.getComponentType(), new Velocity());
                holder.addComponent(MovementManager.getComponentType(), new MovementManager());
                holder.addComponent(MovementStatesComponent.getComponentType(), new MovementStatesComponent());
                holder.addComponent(CollisionResultComponent.getComponentType(), new CollisionResultComponent());

                // 3. HITBOX
                Box boxShape = new Box(-0.4, 0.0, -0.4, 0.4, 1.8, 0.4);
                holder.addComponent(BoundingBox.getComponentType(), new BoundingBox(boxShape));

                // 4. COMBAT & UI
                holder.addComponent(DamageDataComponent.getComponentType(), new DamageDataComponent());
                holder.addComponent(DisplayNameComponent.getComponentType(),
                        new DisplayNameComponent(Message.raw(data.getNickname()))
                );

                // 5. IDENTITY & LIFECYCLE
                int netIdInt = (int) store.getExternalData().takeNextNetworkId();
                holder.addComponent(NetworkId.getComponentType(), new NetworkId(netIdInt));
                holder.addComponent(UUIDComponent.getComponentType(), UUIDComponent.randomUUID());

                // FIXED: Passing a window of 1.0 seconds (adjust as needed)
                holder.addComponent(NewSpawnComponent.getComponentType(), new NewSpawnComponent(1.0f));

                // 6. VISUALS
                String modelToLoad = (commandModelOverride != null) ? commandModelOverride :
                        (SpeciesRegistry.get(data.getSpeciesId()).getModelPath());

                ModelAsset asset = ModelAsset.getAssetMap().getAsset(modelToLoad);
                if (asset == null) asset = ModelAsset.DEBUG;
                holder.addComponent(ModelComponent.getComponentType(), new ModelComponent(Model.createScaledModel(asset, 1.0f)));

                // 7. SPAWN
                store.addEntity(holder, AddReason.SPAWN);
                register((long) netIdInt, data);

                System.out.println("SUCCESS: Spawned " + data.getNickname() + " with Fixed Lifecycle.");

            } catch (Exception e) {
                System.out.println("CRITICAL SPAWN ERROR: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void register(long entityId, Cardapon data) { activeCardapons.put(entityId, data); }
    public Cardapon getCardapon(long entityId) { return activeCardapons.get(entityId); }
    public void unregister(long entityId) { activeCardapons.remove(entityId); }
}
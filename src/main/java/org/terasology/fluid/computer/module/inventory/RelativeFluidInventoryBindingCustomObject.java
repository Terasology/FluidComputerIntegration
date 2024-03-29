// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.fluid.computer.module.inventory;

import com.gempukku.lang.CustomObject;
import com.gempukku.lang.ExecutionException;
import org.joml.RoundingMode;
import org.joml.Vector3i;
import org.joml.Vector3ic;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.module.inventory.InventoryBinding;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.math.Direction;
import org.terasology.engine.math.IntegerRange;
import org.terasology.engine.world.BlockEntityRegistry;
import org.terasology.fluid.component.FluidInventoryAccessComponent;
import org.terasology.fluid.component.FluidInventoryComponent;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RelativeFluidInventoryBindingCustomObject implements CustomObject, InventoryBinding {
    private BlockEntityRegistry blockEntityRegistry;
    private Direction inventoryDirection;
    private boolean input;

    public RelativeFluidInventoryBindingCustomObject(BlockEntityRegistry blockEntityRegistry,
                                                     Direction inventoryDirection, boolean input) {
        this.blockEntityRegistry = blockEntityRegistry;
        this.inventoryDirection = inventoryDirection;
        this.input = input;
    }

    @Override
    public Collection<String> getType() {
        return Collections.singleton("FLUID_INVENTORY_BINDING");
    }

    @Override
    public boolean isInput() {
        return input;
    }

    @Override
    public int sizeOf() {
        return 4;
    }

    @Override
    public InventoryWithSlots getInventoryEntity(int line, ComputerCallback computerCallback) throws ExecutionException {
        Vector3i computerLocation = new Vector3i(computerCallback.getComputerLocation(), RoundingMode.FLOOR);
        Vector3ic directionVector = inventoryDirection.asVector3i();
        Vector3i inventoryLocation = computerLocation.add(directionVector, new Vector3i());

        EntityRef blockEntityAt = blockEntityRegistry.getBlockEntityAt(inventoryLocation);
        if (!blockEntityAt.hasComponent(FluidInventoryComponent.class)
                || !blockEntityAt.hasComponent(FluidInventoryAccessComponent.class)) {
            throw new ExecutionException(line, "Unable to locate accessible inventory with this binding");
        }

        List<Integer> slots = getAccessibleSlots(blockEntityAt);

        return new InventoryWithSlots(blockEntityAt, Collections.unmodifiableList(slots));
    }

    private List<Integer> getAccessibleSlots(EntityRef blockEntityAt) {
        FluidInventoryAccessComponent access = blockEntityAt.getComponent(FluidInventoryAccessComponent.class);

        List<Integer> slots;
        Map<String, IntegerRange> slotMap = getCorrectSlotMap(access);
        IntegerRange integerRange = slotMap.get(inventoryDirection.reverse().toSide().name().toLowerCase());
        if (integerRange == null) {
            slots = Collections.emptyList();
        } else {
            slots = new LinkedList<>();

            for (int slot : integerRange) {
                slots.add(slot);
            }
        }
        return slots;
    }

    private Map<String, IntegerRange> getCorrectSlotMap(FluidInventoryAccessComponent access) {
        Map<String, IntegerRange> slotMap;
        if (input) {
            slotMap = access.input;
        } else {
            slotMap = access.output;
        }
        return slotMap;
    }
}

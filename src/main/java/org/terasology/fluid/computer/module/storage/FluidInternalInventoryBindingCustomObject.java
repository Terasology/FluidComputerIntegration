/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.fluid.computer.module.storage;

import com.gempukku.lang.CustomObject;
import com.gempukku.lang.ExecutionException;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.module.inventory.InventoryBinding;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.fluid.component.FluidInventoryComponent;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class FluidInternalInventoryBindingCustomObject implements CustomObject, InventoryBinding {
    private boolean input;

    public FluidInternalInventoryBindingCustomObject(boolean input) {
        this.input = input;
    }

    @Override
    public Collection<String> getType() {
        return Collections.singleton("FLUID_INVENTORY_BINDING");
    }

    @Override
    public InventoryWithSlots getInventoryEntity(int line, ComputerCallback computerCallback) throws ExecutionException {
        EntityRef inventoryEntity = computerCallback.getComputerEntity().getComponent(FluidInternalStorageComponent.class).inventoryEntity;
        int slotCount = inventoryEntity.getComponent(FluidInventoryComponent.class).fluidSlots.size();
        List<Integer> slots = getSlotList(slotCount);
        return new InventoryWithSlots(inventoryEntity, Collections.unmodifiableList(slots));
    }

    @Override
    public int sizeOf() {
        return 4;
    }

    @Override
    public boolean isInput() {
        return input;
    }

    private List<Integer> getSlotList(int slotCount) {
        List<Integer> slots = new LinkedList<>();
        for (int i = 0; i < slotCount; i++) {
            slots.add(i);
        }
        return slots;
    }
}

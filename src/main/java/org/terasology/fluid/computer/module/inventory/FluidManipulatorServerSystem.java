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
package org.terasology.fluid.computer.module.inventory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.terasology.computer.system.server.lang.os.condition.InventoryCondition;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.BeforeDeactivateComponent;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.Share;
import org.terasology.fluid.component.FluidInventoryComponent;
import org.terasology.fluid.event.FluidVolumeChangedInInventory;

import java.util.Iterator;

@RegisterSystem(RegisterMode.AUTHORITY)
@Share(FluidManipulatorConditionsRegister.class)
public class FluidManipulatorServerSystem extends BaseComponentSystem implements FluidManipulatorConditionsRegister {
    private Multimap<EntityRef, InventoryCondition> fluidChangeConditions = HashMultimap.create();

    @Override
    public void addFluidInventoryChangeListener(EntityRef entity, InventoryCondition latchCondition) {
        fluidChangeConditions.put(entity, latchCondition);
    }

    @Override
    public void removeFluidInventoryChangeListener(EntityRef entity, InventoryCondition latchCondition) {
        fluidChangeConditions.remove(entity, latchCondition);
    }

    @ReceiveEvent
    public void inventoryChange(FluidVolumeChangedInInventory event, EntityRef inventory) {
        Iterator<InventoryCondition> latchIterator = fluidChangeConditions.get(inventory).iterator();
        while (latchIterator.hasNext()) {
            InventoryCondition latchCondition = latchIterator.next();
            if (latchCondition.checkRelease()) {
                latchIterator.remove();
            }
        }
    }

    @ReceiveEvent
    public void inventoryRemoved(BeforeDeactivateComponent event, EntityRef inventory, FluidInventoryComponent inventoryComponent) {
        Iterator<InventoryCondition> latchIterator = fluidChangeConditions.get(inventory).iterator();
        while (latchIterator.hasNext()) {
            InventoryCondition latchCondition = latchIterator.next();
            latchIterator.remove();
            latchCondition.cancelCondition();
        }
    }

}

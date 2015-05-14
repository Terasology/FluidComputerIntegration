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

import org.terasology.computer.system.server.lang.os.condition.AbstractConditionCustomObject;
import org.terasology.computer.system.server.lang.os.condition.InventoryCondition;
import org.terasology.computer.system.server.lang.os.condition.LatchCondition;
import org.terasology.entitySystem.entity.EntityRef;

public interface FluidManipulatorConditionsRegister {
    void addFluidInventoryChangeListener(EntityRef entity, InventoryCondition latchCondition);

    void removeFluidInventoryChangeListener(EntityRef entity, InventoryCondition latchCondition);
}

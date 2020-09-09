// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.fluid.computer.module.inventory;

import org.terasology.computer.system.server.lang.os.condition.InventoryCondition;
import org.terasology.engine.entitySystem.entity.EntityRef;

public interface FluidManipulatorConditionsRegister {
    void addFluidInventoryChangeListener(EntityRef entity, InventoryCondition latchCondition);

    void removeFluidInventoryChangeListener(EntityRef entity, InventoryCondition latchCondition);
}

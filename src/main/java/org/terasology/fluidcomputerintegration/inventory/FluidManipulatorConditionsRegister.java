// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.fluidcomputerintegration.inventory;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.modularcomputers.system.server.lang.os.condition.InventoryCondition;

public interface FluidManipulatorConditionsRegister {
    void addFluidInventoryChangeListener(EntityRef entity, InventoryCondition latchCondition);

    void removeFluidInventoryChangeListener(EntityRef entity, InventoryCondition latchCondition);
}

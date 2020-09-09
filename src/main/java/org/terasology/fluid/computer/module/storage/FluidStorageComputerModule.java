// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.fluid.computer.module.storage;

import org.terasology.computer.module.DefaultComputerModule;
import org.terasology.computer.system.server.lang.ComputerModule;

import java.util.Collection;

public class FluidStorageComputerModule extends DefaultComputerModule {
    private final String moduleType;
    private final int slotCount;
    private final float maxVolume;

    public FluidStorageComputerModule(String moduleType, String moduleName, int slotCount, float maxVolume) {
        super(moduleType, moduleName);
        this.moduleType = moduleType;
        this.slotCount = slotCount;
        this.maxVolume = maxVolume;
        addMethod("getInputInventoryBinding", new FluidStorageInventoryBindingMethod(true));
        addMethod("getOutputInventoryBinding", new FluidStorageInventoryBindingMethod(false));
    }

    @Override
    public boolean canBePlacedInComputer(Collection<ComputerModule> computerModulesInstalled) {
        // Only one storage module can be stored in a computer
        for (ComputerModule computerModule : computerModulesInstalled) {
            if (computerModule.getModuleType().equals(moduleType)) {
                return false;
            }
        }

        return true;
    }

    public int getSlotCount() {
        return slotCount;
    }

    public float getMaxVolume() {
        return maxVolume;
    }
}

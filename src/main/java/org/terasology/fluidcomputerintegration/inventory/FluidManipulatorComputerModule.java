// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.fluidcomputerintegration.inventory;

import org.terasology.engine.world.BlockEntityRegistry;
import org.terasology.fluid.system.FluidManager;
import org.terasology.modularcomputers.module.DefaultComputerModule;

public class FluidManipulatorComputerModule extends DefaultComputerModule {
    public FluidManipulatorComputerModule(FluidManipulatorConditionsRegister fluidManipulatorConditionsRegister,
                                          FluidManager fluidManager,
                                          BlockEntityRegistry blockEntityRegistry, String moduleType,
                                          String moduleName) {
        super(moduleType, moduleName);

        addMethod("getInputInventoryBinding", new FluidInventoryBindingMethod("getInputInventoryBinding",
                blockEntityRegistry, true));
        addMethod("getOutputInventoryBinding", new FluidInventoryBindingMethod("getOutputInventoryBinding",
                blockEntityRegistry, true));

        addMethod("getMaximumVolume", new FluidGetMaximumVolumeMethod("getMaximumVolume"));
        addMethod("getStoredVolume", new FluidGetStoredVolumeMethod("getStoredVolume"));
        addMethod("getFluidType", new FluidGetTypeMethod("getFluidType"));

        addMethod("move", new FluidMoveMethod("move", fluidManager));
        addMethod("getFluidsAndChangeCondition", new FluidInventoryAndChangedConditionMethod(
                "getFluidsAndChangeCondition", fluidManipulatorConditionsRegister));
    }
}

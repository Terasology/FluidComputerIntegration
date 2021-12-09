// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.fluid.computer.module.storage;

import org.terasology.computer.system.common.ComputerModuleRegistry;
import org.terasology.computer.ui.documentation.DocumentationBuilder;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.config.ModuleConfigManager;
import org.terasology.engine.registry.In;
import org.terasology.fluid.computer.module.inventory.FluidManipulatorCommonSystem;

@RegisterSystem(RegisterMode.ALWAYS)
public class FluidStorageModuleCommonSystem extends BaseComponentSystem {
    public static final String COMPUTER_FLUID_STORAGE_MODULE_TYPE = "FluidStorage";

    @In
    private ComputerModuleRegistry computerModuleRegistry;
    @In
    private ModuleConfigManager moduleConfigManager;

    @Override
    public void preBegin() {
        if (moduleConfigManager.getBooleanVariable("FluidComputerIntegration", "registerModule.storage", true)) {
            String fluidManipulatorModulePageId =
                    DocumentationBuilder.getComputerModulePageId(FluidManipulatorCommonSystem.COMPUTER_FLUID_MODULE_TYPE);

            computerModuleRegistry.registerComputerModule(
                    COMPUTER_FLUID_STORAGE_MODULE_TYPE,
                    new FluidStorageComputerModule(COMPUTER_FLUID_STORAGE_MODULE_TYPE, "Fluid internal storage", 3, 10),
                    "This module allows storing items within the computer itself. " +
                            "Only one module of this type can be installed in a computer at a time. " +
                            "Player does not have access to the storage itself via user interface, however " +
                            "<h navigate:" + fluidManipulatorModulePageId + ">Fluid manipulator</h> " +
                            "module can be used to access it and store in an external " +
                            "storage.",
                    null);
        }
    }
}

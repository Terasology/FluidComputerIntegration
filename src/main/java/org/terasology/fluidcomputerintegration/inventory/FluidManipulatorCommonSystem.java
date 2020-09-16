// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.fluidcomputerintegration.inventory;

import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.config.ModuleConfigManager;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.nui.widgets.browser.data.basic.HTMLLikeParser;
import org.terasology.engine.world.BlockEntityRegistry;
import org.terasology.fluid.system.FluidManager;
import org.terasology.modularcomputers.system.common.ComputerLanguageRegistry;
import org.terasology.modularcomputers.system.common.ComputerModuleRegistry;

import java.util.Collections;

@RegisterSystem(RegisterMode.ALWAYS)
public class FluidManipulatorCommonSystem extends BaseComponentSystem {
    public static final String COMPUTER_FLUID_MODULE_TYPE = "FluidManipulator";

    @In
    private ComputerModuleRegistry computerModuleRegistry;
    @In
    private BlockEntityRegistry blockEntityRegistry;
    @In
    private FluidManager fluidManager;
    @In
    private FluidManipulatorConditionsRegister fluidManipulatorConditionsRegister;
    @In
    private ComputerLanguageRegistry computerLanguageRegistry;
    @In
    private ModuleConfigManager moduleConfigManager;

    @Override
    public void preBegin() {
        if (moduleConfigManager.getBooleanVariable("FluidComputerIntegration", "registerModule.manipulator", true)) {
            computerLanguageRegistry.registerObjectType(
                    "FluidInventoryBinding",
                    Collections.singleton(HTMLLikeParser.parseHTMLLikeParagraph(null, "An object that tells a method " +
                            "how to access a fluid inventory. Usually used as a parameter " +
                            "for methods in Fluid Manipulator computer module. This object comes in two types defined" +
                            " upon creation:<l>" +
                            "* input - that allows to place fluids in the specified inventory,<l>" +
                            "* output - that allows to extract fluids from the specified inventory.<l>" +
                            "Attempting to use an incorrect type as a parameter of a method will result in an " +
                            "ExecutionException.")));

            computerModuleRegistry.registerComputerModule(
                    COMPUTER_FLUID_MODULE_TYPE,
                    new FluidManipulatorComputerModule(fluidManipulatorConditionsRegister, fluidManager,
                            blockEntityRegistry, COMPUTER_FLUID_MODULE_TYPE, "Fluid manipulator"),
                    "This module allows computer to manipulate fluids.",
                    null);
        }
    }
}

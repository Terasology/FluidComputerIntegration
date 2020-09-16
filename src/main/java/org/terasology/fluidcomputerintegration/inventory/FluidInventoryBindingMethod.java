// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.fluidcomputerintegration.inventory;

import org.terasology.engine.math.Direction;
import org.terasology.engine.world.BlockEntityRegistry;
import org.terasology.modularcomputers.FunctionParamValidationUtil;
import org.terasology.modularcomputers.context.ComputerCallback;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.ExecutionException;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.Variable;
import org.terasology.modularcomputers.system.server.lang.AbstractModuleMethodExecutable;

import java.util.Map;

public class FluidInventoryBindingMethod extends AbstractModuleMethodExecutable<Object> {
    private final String methodName;
    private final BlockEntityRegistry blockEntityRegistry;
    private final boolean input;

    public FluidInventoryBindingMethod(String methodName, BlockEntityRegistry blockEntityRegistry, boolean input) {
        super(input ? "Creates the input fluid inventory binding for the storage specified in the direction. " +
                        "This binding allows to insert fluids into the inventory only." :
                        "Creates the output fluid inventory binding for the storage specified in the direction. " +
                                "This binding allows to remove fluids from the inventory only.",
                "FluidInventoryBinding",
                input ? "Input binding for the direction specified." : "Output binding for the direction specified.");
        this.blockEntityRegistry = blockEntityRegistry;
        this.input = input;
        this.methodName = methodName;

        addParameter("direction", "Direction", "Direction in which the fluid manipulator is bound to.");

        if (input) {
            addExample("This example creates input fluid inventory binding to an inventory above it and prints out " +
                            "the slot count for it. Please make sure " +
                            "this computer has a module of Fluid Manipulator type in any of its slots.",
                    "var invBind = computer.bindModuleOfType(\"" + FluidManipulatorCommonSystem.COMPUTER_FLUID_MODULE_TYPE + "\");\n" +
                            "var topInv = invBind." + methodName + "(\"up\");\n" +
                            "console.append(\"Inventory above has \" + invBind.getInventorySlotCount(topInv) + \" " +
                            "number of slots available for input.\");"
            );
        } else {
            addExample("This example creates output fluid inventory binding to an inventory above it and prints out " +
                            "the slot count for it. Please make sure " +
                            "this computer has a module of Fluid Manipulator type in any of its slots.",
                    "var invBind = computer.bindModuleOfType(\"" + FluidManipulatorCommonSystem.COMPUTER_FLUID_MODULE_TYPE + "\");\n" +
                            "var topInv = invBind." + methodName + "(\"up\");\n" +
                            "console.append(\"Inventory above has \" + invBind.getInventorySlotCount(topInv) + \" " +
                            "number of slots available for output.\");"
            );
        }
    }

    @Override
    public int getCpuCycleDuration() {
        return 10;
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters,
                                Object onFunctionStartResult) throws ExecutionException {
        Direction direction = FunctionParamValidationUtil.validateDirectionParameter(line, parameters,
                "direction", methodName);

        return new RelativeFluidInventoryBindingCustomObject(blockEntityRegistry, direction, input);
    }
}

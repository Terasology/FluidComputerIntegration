// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.fluidcomputerintegration.inventory;

import org.terasology.modularcomputers.context.ComputerCallback;
import org.terasology.modularcomputers.module.inventory.InventoryBinding;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.ExecutionException;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.Variable;
import org.terasology.modularcomputers.system.server.lang.AbstractModuleMethodExecutable;

import java.util.Map;

public class FluidInventorySlotCountMethod extends AbstractModuleMethodExecutable<Object> {

    private final String methodName;

    public FluidInventorySlotCountMethod(String methodName) {
        super("Queries the specified fluid inventory to check how many slots it has available.", "Number",
                "Number of slots in the fluid inventory specified.");
        this.methodName = methodName;

        addParameter("fluidInventoryBinding", "FluidInventoryBinding", "Fluid inventory it should query for number of" +
                " slots.");

        addExample("This example creates output fluid inventory binding to an inventory above it and prints out the " +
                        "slot count for it. Please make sure " +
                        "this computer has a module of Fluid Manipulator type in any of its slots.",
                "var invBind = computer.bindModuleOfType(\"" + FluidManipulatorCommonSystem.COMPUTER_FLUID_MODULE_TYPE + "\");\n" +
                        "var topInv = invBind.getOutputInventoryBinding(\"up\");\n" +
                        "console.append(\"Inventory above has \" + invBind.getInventorySlotCount(topInv) + \" number " +
                        "of slots available for output.\");"
        );
    }

    @Override
    public int getCpuCycleDuration() {
        return 50;
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters,
                                Object onFunctionStartResult) throws ExecutionException {
        InventoryBinding.InventoryWithSlots inventory =
                FluidFunctionParamValidationUtil.validateFluidInventoryBinding(line, computer,
                parameters, "fluidInventoryBinding", methodName, null);

        return inventory.slots.size();
    }
}

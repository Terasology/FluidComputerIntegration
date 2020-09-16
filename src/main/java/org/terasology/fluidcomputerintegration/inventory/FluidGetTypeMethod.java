// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.fluidcomputerintegration.inventory;

import org.terasology.fluid.system.FluidUtils;
import org.terasology.modularcomputers.FunctionParamValidationUtil;
import org.terasology.modularcomputers.context.ComputerCallback;
import org.terasology.modularcomputers.module.inventory.InventoryBinding;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.ExecutionException;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.Variable;
import org.terasology.modularcomputers.system.server.lang.AbstractModuleMethodExecutable;

import java.util.Map;

public class FluidGetTypeMethod extends AbstractModuleMethodExecutable<Object> {
    private final String methodName;

    public FluidGetTypeMethod(String methodName) {
        super("Returns the type of the fluid in the specified inventory's slot (if any).", "String",
                "Type of fluid in the specified slot. " +
                        "If there is no fluid at the specified slot, a null value is returned.");
        this.methodName = methodName;

        addParameter("fluidInventoryBinding", "FluidInventoryBinding", "Fluid inventory it should check for the type " +
                "of fluid.");
        addParameter("slot", "Number", "Slot it should check for the type of fluid.");
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

        int slotNo = FunctionParamValidationUtil.validateSlotNo(line, parameters, inventory, "slot", methodName);

        int realSlotNo = inventory.slots.get(slotNo);
        return FluidUtils.getFluidAt(inventory.inventory, realSlotNo);
    }
}

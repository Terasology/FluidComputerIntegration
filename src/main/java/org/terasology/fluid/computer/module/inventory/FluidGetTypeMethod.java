// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.fluid.computer.module.inventory;

import com.gempukku.lang.ExecutionException;
import com.gempukku.lang.Variable;
import org.terasology.computer.FunctionParamValidationUtil;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.module.inventory.InventoryBinding;
import org.terasology.computer.system.server.lang.AbstractModuleMethodExecutable;
import org.terasology.fluid.system.FluidUtils;

import java.util.Map;

public class FluidGetTypeMethod extends AbstractModuleMethodExecutable<Object> {
    private final String methodName;

    public FluidGetTypeMethod(String methodName) {
        super("Returns the type of the fluid in the specified inventory's slot (if any).", "String",
                "Type of fluid in the specified slot. " +
                        "If there is no fluid at the specified slot, a null value is returned.");
        this.methodName = methodName;

        addParameter("fluidInventoryBinding", "FluidInventoryBinding", "Fluid inventory it should check for the type of fluid.");
        addParameter("slot", "Number", "Slot it should check for the type of fluid.");
    }

    @Override
    public int getCpuCycleDuration() {
        return 50;
    }

    @Override
    public Object onFunctionEnd(int line,
                                ComputerCallback computer,
                                Map<String, Variable> parameters,
                                Object onFunctionStartResult) throws ExecutionException {
        InventoryBinding.InventoryWithSlots inventory = FluidFunctionParamValidationUtil.validateFluidInventoryBinding(line, computer,
                parameters, "fluidInventoryBinding", methodName, null);

        int slotNo = FunctionParamValidationUtil.validateSlotNo(line, parameters, inventory, "slot", methodName);

        int realSlotNo = inventory.slots.get(slotNo);
        return FluidUtils.getFluidAt(inventory.inventory, realSlotNo);
    }
}

// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.fluidcomputerintegration.inventory;

import org.terasology.fluid.component.FluidInventoryComponent;
import org.terasology.modularcomputers.FunctionParamValidationUtil;
import org.terasology.modularcomputers.context.ComputerCallback;
import org.terasology.modularcomputers.module.inventory.InventoryBinding;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.ExecutionException;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.Variable;
import org.terasology.modularcomputers.system.server.lang.AbstractModuleMethodExecutable;

import java.util.Map;

public class FluidGetMaximumVolumeMethod extends AbstractModuleMethodExecutable<Object> {
    private final String methodName;

    public FluidGetMaximumVolumeMethod(String methodName) {
        super("Gets maximum volume of fluid a specified slot may contain.", "Number", "Maximum volume in liters " +
                "specified fluid slot may contain.");
        this.methodName = methodName;

        addParameter("fluidInventoryBinding", "FluidInventoryBinding", "Fluid inventory it should check for the " +
                "maximum volume.");
        addParameter("slot", "Number", "Slot it should check for maximum volume.");
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
        return inventory.inventory.getComponent(FluidInventoryComponent.class).maximumVolumes.get(realSlotNo);
    }
}

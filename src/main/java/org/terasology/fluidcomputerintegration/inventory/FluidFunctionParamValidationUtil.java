// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.fluidcomputerintegration.inventory;

import org.terasology.modularcomputers.FunctionParamValidationUtil;
import org.terasology.modularcomputers.context.ComputerCallback;
import org.terasology.modularcomputers.module.inventory.InventoryBinding;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.CustomObject;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.ExecutionException;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.Variable;

import java.util.Map;

public class FluidFunctionParamValidationUtil {
    private FluidFunctionParamValidationUtil() {
    }

    public static InventoryBinding.InventoryWithSlots validateFluidInventoryBinding(
            int line, ComputerCallback computer, Map<String, Variable> parameters,
            String parameterName, String functionName, Boolean input) throws ExecutionException {
        Variable inventoryBinding = FunctionParamValidationUtil.validateParameter(line, parameters, parameterName,
                functionName, Variable.Type.CUSTOM_OBJECT);
        CustomObject customObject = (CustomObject) inventoryBinding.getValue();
        if (!customObject.getType().contains("FLUID_INVENTORY_BINDING")
                || (input != null && input != ((InventoryBinding) customObject).isInput()))
            throw new ExecutionException(line, "Invalid " + parameterName + " in " + functionName + "()");

        InventoryBinding binding = (InventoryBinding) inventoryBinding.getValue();
        return binding.getInventoryEntity(line, computer);
    }
}

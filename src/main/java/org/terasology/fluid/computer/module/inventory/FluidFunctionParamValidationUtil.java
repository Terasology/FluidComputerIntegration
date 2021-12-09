// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.fluid.computer.module.inventory;

import com.gempukku.lang.CustomObject;
import com.gempukku.lang.ExecutionException;
import com.gempukku.lang.Variable;
import org.terasology.computer.FunctionParamValidationUtil;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.module.inventory.InventoryBinding;

import java.util.Map;

public final class FluidFunctionParamValidationUtil {
    private FluidFunctionParamValidationUtil() { }

    public static InventoryBinding.InventoryWithSlots validateFluidInventoryBinding(
            int line, ComputerCallback computer, Map<String, Variable> parameters,
            String parameterName, String functionName, Boolean input) throws ExecutionException {
        Variable inventoryBinding =
                FunctionParamValidationUtil.validateParameter(line, parameters, parameterName, functionName, Variable.Type.CUSTOM_OBJECT);
        CustomObject customObject = (CustomObject) inventoryBinding.getValue();
        if (!customObject.getType().contains("FLUID_INVENTORY_BINDING")
                || (input != null && input != ((InventoryBinding) customObject).isInput())) {
            throw new ExecutionException(line, "Invalid " + parameterName + " in " + functionName + "()");
        }

        InventoryBinding binding = (InventoryBinding) inventoryBinding.getValue();
        return binding.getInventoryEntity(line, computer);
    }
}

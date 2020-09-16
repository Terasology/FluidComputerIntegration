// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.fluidcomputerintegration.inventory;

import org.terasology.fluid.system.FluidUtils;
import org.terasology.modularcomputers.context.ComputerCallback;
import org.terasology.modularcomputers.module.inventory.InventoryBinding;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.ExecutionException;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.Variable;
import org.terasology.modularcomputers.system.server.lang.AbstractModuleMethodExecutable;
import org.terasology.modularcomputers.system.server.lang.os.condition.InventoryCondition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FluidInventoryAndChangedConditionMethod extends AbstractModuleMethodExecutable<Object> {
    private final String methodName;
    private final FluidManipulatorConditionsRegister fluidManipulatorConditionsRegister;

    public FluidInventoryAndChangedConditionMethod(String methodName,
                                                   FluidManipulatorConditionsRegister fluidManipulatorConditionsRegister) {
        super("Gets the information about fluids stored in the inventory as well as a Condition " +
                        "that allows to wait for the inventory's contents to be changed.", "Map",
                "Map containing to entries:<l>" +
                        "- \"inventory\" - containing a List of Maps, with each entry in the list corresponding to " +
                        "one slot " +
                        "in the fluid inventory, and each entry Map containing two keys - \"type\" with String value " +
                        "of fluid type, " +
                        "as specified in the getFluidType() method, and \"volume\" with Number value, specifying " +
                        "volume of the fluid in that slot<l>" +
                        "- \"condition\" - containing condition you could wait on to listen on a change of the fluid " +
                        "inventory from " +
                        "the state described in the \"inventory\" key. Please note, that the condition might be " +
                        "fulfilled even though " +
                        "the fluid inventory state has not changed.");
        this.fluidManipulatorConditionsRegister = fluidManipulatorConditionsRegister;
        this.methodName = methodName;

        addParameter("fluidInventoryBinding", "FluidInventoryBinding", "Fluid inventory it should get contents and " +
                "change condition for.");
    }

    @Override
    public int getCpuCycleDuration() {
        return 200;
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters,
                                Object onFunctionStartResult) throws ExecutionException {
        InventoryBinding.InventoryWithSlots inventory =
                FluidFunctionParamValidationUtil.validateFluidInventoryBinding(line, computer,
                parameters, "fluidInventoryBinding", methodName, null);

        Map<String, Variable> result = new HashMap<>();

        List<Variable> inventoryResult = getInventory(inventory);
        List<Variable> inventoryCopyResult = getInventory(inventory);

        result.put("inventory", new Variable(inventoryResult));

        result.put("condition", new Variable(
                new InventoryCondition() {
                    @Override
                    public boolean checkRelease() {
                        try {
                            InventoryBinding.InventoryWithSlots inventory =
                                    FluidFunctionParamValidationUtil.validateFluidInventoryBinding(line, computer,
                                    parameters, "fluidInventoryBinding", methodName, null);
                            List<Variable> currentInventory = getInventory(inventory);
                            if (!currentInventory.equals(inventoryCopyResult)) {
                                release(null);
                                return true;
                            } else {
                                return false;
                            }
                        } catch (ExecutionException exp) {
                            releaseWithError(exp);
                            return true;
                        }
                    }

                    @Override
                    public void cancelCondition() {
                        releaseWithError(new ExecutionException(line, "Observed inventory has been removed."));
                    }

                    @Override
                    protected Runnable registerAwaitingCondition() throws ExecutionException {
                        if (!checkRelease()) {
                            fluidManipulatorConditionsRegister.addFluidInventoryChangeListener(inventory.inventory,
                                    this);
                            final InventoryCondition condition = this;
                            return new Runnable() {
                                @Override
                                public void run() {
                                    fluidManipulatorConditionsRegister.removeFluidInventoryChangeListener(inventory.inventory, condition);
                                }
                            };
                        } else {
                            return null;
                        }
                    }
                }));


        return result;
    }

    private List<Variable> getInventory(InventoryBinding.InventoryWithSlots inventory) {
        List<Variable> inventoryResult = new ArrayList<>();

        for (int slot : inventory.slots) {
            String fluidType = FluidUtils.getFluidAt(inventory.inventory, slot);
            float fluidAmount = FluidUtils.getFluidAmount(inventory.inventory, slot);

            Map<String, Variable> itemMap = new HashMap<>();

            itemMap.put("type", new Variable(fluidType));
            itemMap.put("volume", new Variable(fluidAmount));

            inventoryResult.add(new Variable(itemMap));
        }
        return inventoryResult;
    }
}

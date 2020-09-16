// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.fluidcomputerintegration.storage;

import org.terasology.modularcomputers.context.ComputerCallback;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.ExecutionException;
import org.terasology.modularcomputers.shadedlibs.com.gempukku.lang.Variable;
import org.terasology.modularcomputers.system.server.lang.AbstractModuleMethodExecutable;

import java.util.Map;

public class FluidStorageInventoryBindingMethod extends AbstractModuleMethodExecutable<Object> {
    private final boolean input;

    public FluidStorageInventoryBindingMethod(boolean input) {
        super(input ? "Creates the fluid input inventory binding for the Fluid internal storage." :
                        "Creates the fluid output inventory binding for the Fluid internal storage.",
                "FluidInventoryBinding",
                input ? "Returns fluid inventory binding allowing to insert fluids into this Fluid internal storage." :
                        "Returns fluid inventory binding allowing to extract fluids from this Fluid internal storage.");
        this.input = input;
    }

    @Override
    public int getCpuCycleDuration() {
        return 10;
    }

    @Override
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters,
                                Object onFunctionStartResult) throws ExecutionException {
        return new FluidInternalInventoryBindingCustomObject(input);
    }
}

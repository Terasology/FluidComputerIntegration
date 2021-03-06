/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.fluid.computer.module.storage;

import com.gempukku.lang.ExecutionException;
import com.gempukku.lang.Variable;
import org.terasology.computer.context.ComputerCallback;
import org.terasology.computer.system.server.lang.AbstractModuleMethodExecutable;

import java.util.Map;

public class FluidStorageInventoryBindingMethod extends AbstractModuleMethodExecutable<Object> {
    private boolean input;

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
    public Object onFunctionEnd(int line, ComputerCallback computer, Map<String, Variable> parameters, Object onFunctionStartResult) throws ExecutionException {
        return new FluidInternalInventoryBindingCustomObject(input);
    }
}

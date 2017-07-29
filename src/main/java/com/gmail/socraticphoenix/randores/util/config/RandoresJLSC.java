/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 socraticphoenix@gmail.com
 * Copyright (c) 2017 contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.gmail.socraticphoenix.randores.util.config;

import com.gmail.socraticphoenix.jlsc.registry.JLSCRegistry;
import com.gmail.socraticphoenix.jlsc.serialization.JLSCCollectionSerializer;
import com.gmail.socraticphoenix.randores.mod.component.CraftableComponent;
import com.gmail.socraticphoenix.randores.mod.component.ability.Ability;
import com.gmail.socraticphoenix.randores.mod.component.ability.abilities.PotionEffectAbility;
import com.gmail.socraticphoenix.randores.mod.component.property.MaterialProperty;
import com.gmail.socraticphoenix.randores.mod.component.property.properties.FlammableProperty;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RandoresJLSC {

    public static void registerDefaults() {
        JLSCRegistry.register(new JLSCColorProcessor());
        JLSCRegistry.register(new JLSCCollectionSerializer(ArrayList.class, MaterialProperty.class, false, ArrayList::new, "arrayList"));
        JLSCRegistry.register(new JLSCCollectionSerializer(ArrayList.class, Ability.class, false, ArrayList::new, "arrayList"));
        JLSCRegistry.register(new JLSCCollectionSerializer(ArrayList.class, CraftableComponent.class, false, ArrayList::new, "arrayList"));

        JLSCRegistry.registerAnnotated(PotionEffectAbility.class, false, "potionEffect");
        JLSCRegistry.registerAnnotated(FlammableProperty.class, false, "flammable");
        JLSCRegistry.registerAnnotated(CraftableComponent.class);
    }

    private Map<Class, RegistryWrapper> wrapperMap = new HashMap<>();

    @SubscribeEvent
    public void registryEvent(RegistryEvent.Register ev) {
        IForgeRegistry registry = ev.getRegistry();
        if(wrapperMap.containsKey(registry.getRegistrySuperType())) {
            wrapperMap.get(registry.getRegistrySuperType()).setRegistry(registry);
        } else {
            RegistryWrapper wrapper = new RegistryWrapper(registry);
            wrapperMap.put(registry.getRegistrySuperType(), wrapper);
            JLSCRegistry.register(new JLSCForgeRegistrableProcessor(registry.getRegistrySuperType(), this.cleanName(registry.getRegistrySuperType()), false, wrapper));
        }
    }

    private String cleanName(Class clazz) {
        String simple = clazz.getSimpleName();
        if(simple.equals("IRecipe")) {
            simple = simple.substring(1);
        }

        return simple.toLowerCase();
    }

}

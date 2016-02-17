/*
  Copyright 2015 - 2015 pac4j organization

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.millross.event.processor;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.millross.event.Event;
import com.millross.event.EventOne;
import com.millross.event.EventTwo;

/**
 * @author Jeremy Prime
 * @since 1.0.0
 */
public class EventProcessor {

    private final Map<Class, MethodHandle> methodHandles = new HashMap<>();

    public EventProcessor() {
        final Method[] declaredMethods = EventProcessor.class.getDeclaredMethods();
        final List<Method> methods = Arrays.asList(declaredMethods);
        methods.stream().filter(m -> m.getName().equals("handle"))
                .filter(m -> m.getParameterTypes()[0] != Event.class)
                .map(m -> new HandlerMethodMapping(m.getParameterTypes()[0], getMethodHandle(m)))
                .forEach(mapping -> methodHandles.put(mapping.clazz, mapping.methodHandle));
    }

    public void handle(final EventOne event) {
        event.sayHello();
    }

    public void handle(final EventTwo event) {
        event.sayHi();
    }

    public void handle(final Event event) {
        if (methodHandles.containsKey(event.getClass())) {
            try {
                methodHandles.get(event.getClass()).invoke(this, event);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            System.out.println("Handler not found for " + event.getClass().getCanonicalName());
        }
    }

    private static final MethodHandle getMethodHandle(final Method method) {
        try {
            return  MethodHandles.lookup().findVirtual(
                    EventProcessor.class,
                    method.getName(),
                    MethodType.methodType(void.class, method.getParameterTypes()[0]));
        } catch (NoSuchMethodException|IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static class HandlerMethodMapping<T extends Event> {

        private final Class<T> clazz;
        private final MethodHandle methodHandle;

        private HandlerMethodMapping(final Class<T> c, final MethodHandle method) {
            this.clazz = c;
            this.methodHandle = method;
        }
    }

}

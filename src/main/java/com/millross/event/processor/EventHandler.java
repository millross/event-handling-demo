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

import com.millross.event.Event;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EventHandler<T> {

    private final Map<Class, MethodHandle> methodHandles = new HashMap<>();

    protected EventHandler() {

        final Method[] declaredMethods = getGenericTypeClass().getDeclaredMethods();

        final List<Method> methods = Arrays.asList(declaredMethods);
        methods.stream().filter(m -> m.getName().equals("handle"))
                .filter(m -> m.getParameterTypes()[0] != Event.class)
                .map(m -> new HandlerMethodMapping(m.getParameterTypes()[0], getMethodHandle(m)))
                .forEach(mapping -> methodHandles.put(mapping.clazz, mapping.methodHandle));
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

    @SuppressWarnings("unchecked")
    private Class<T> getGenericTypeClass() {
        try {
            String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
            Class<?> clazz = Class.forName(className);
            return (Class<T>) clazz;
        } catch (Exception e) {
            throw new IllegalStateException("Class is not parametrized with generic type!!! Please use extends <> ");
        }
    }

    private final MethodHandle getMethodHandle(final Method method) {
        try {
            return  MethodHandles.lookup().findVirtual(
                    getGenericTypeClass(),
                    method.getName(),
                    MethodType.methodType(void.class, method.getParameterTypes()[0]));
        } catch (NoSuchMethodException|IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private class HandlerMethodMapping<T extends Event> {

        private final Class<T> clazz;
        private final MethodHandle methodHandle;

        private HandlerMethodMapping(final Class<T> c, final MethodHandle method) {
            this.clazz = c;
            this.methodHandle = method;
        }
    }
}

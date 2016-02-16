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
import com.millross.event.EventOne;
import com.millross.event.EventTwo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Jeremy Prime
 * @since 1.0.0
 */
public class EventProcessor {

    private Map<Class, Consumer<Event>> processors = new HashMap<>();

    public EventProcessor() {
        final Method[] declaredMethods = EventProcessor.class.getDeclaredMethods();
        final List<Method> methods = Arrays.asList(declaredMethods);
        final EventProcessor processor = this;
        methods.stream().filter(m -> m.getName().equals("handle"))
                .filter(m -> m.getParameterTypes()[0] != Event.class)
                .forEach(m -> processors.put(m.getParameterTypes()[0], (Consumer<Event>) event -> {
                    try {
                        m.invoke(processor, event);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }));
    }

    public void handle(final EventOne event) {
        event.sayHello();
    }

    public void handle(final EventTwo event) {
        event.sayHi();
    }

    public void handle(final Event event) {
        if (processors.containsKey(event.getClass())) {
            processors.get(event.getClass()).accept(event);
        } else {
            System.out.println("Handler not found for " + event.getClass().getCanonicalName());
        }
    }

}

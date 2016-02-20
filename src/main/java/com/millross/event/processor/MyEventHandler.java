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

import com.millross.event.EventOne;
import com.millross.event.EventTwo;

/**
 * @author Peter van der Merwe
 * @since 1.0.0
 */
public class MyEventHandler extends EventHandler<MyEventHandler> {

    public void handle(final EventOne event) {
        event.sayHello();
    }

    public void handle(final EventTwo event) {
        event.sayHi();
    }
}

package com.millross.event;

import com.millross.event.processor.EventProcessor;
import com.millross.event.processor.MyEventHandler;

/**
 * Quick demo of event handling via type safe approach
 *
 */
public class App {
    public static void main( String[] args ) {

        // Option 1
        //
        EventProcessor processor = new EventProcessor();
        final Event eventOne = new EventOne();
        final Event eventTwo = new EventTwo();

        processor.handle(eventTwo);
        processor.handle(eventOne);

        // Option 2 - use event handler with EventHandler superclass
        //
        MyEventHandler eventHandler = new MyEventHandler();
        eventHandler.handle(eventTwo);
        eventHandler.handle(eventOne);
    }
}

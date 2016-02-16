package com.millross.event;

import com.millross.event.processor.EventProcessor;

/**
 * Quick demo of event handling via type safe approach
 *
 */
public class App {
    public static void main( String[] args ) {

        EventProcessor processor = new EventProcessor();
        final Event eventOne = new EventOne();
        final Event eventTwo = new EventTwo();

        processor.handle(eventTwo);
        processor.handle(eventOne);
    }
}

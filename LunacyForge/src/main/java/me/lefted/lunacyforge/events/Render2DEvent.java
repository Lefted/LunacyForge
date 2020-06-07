package me.lefted.lunacyforge.events;

import com.darkmagician6.eventapi.events.Event;

public class Render2DEvent implements Event {

    // ATTRIBUTES
    private final float partialTicks;

    // CONSTRUCTOR
    public Render2DEvent(final float partialTicks) {
	this.partialTicks = partialTicks;
    }

    // METHODS
    public final float getPartialTicks() {
	return this.partialTicks;
    }
}

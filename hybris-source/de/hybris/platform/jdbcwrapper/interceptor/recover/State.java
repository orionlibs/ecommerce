package de.hybris.platform.jdbcwrapper.interceptor.recover;

import java.time.Duration;
import java.time.Instant;

final class State
{
    private final boolean closed;
    private final Instant instant;
    private final int closedIterations;


    private State(boolean closed, int closedIterations)
    {
        if(!closed && closedIterations > 0)
        {
            throw new IllegalArgumentException("Cannot create open state with positive number of 'closedIterations'.");
        }
        this.closed = closed;
        this.instant = Instant.now();
        this.closedIterations = this.closed ? closedIterations : 0;
    }


    public static State createClosed()
    {
        return new State(true, 0);
    }


    public static State createOpen()
    {
        return new State(false, 0);
    }


    public State createStillClosed()
    {
        if(!isClosed())
        {
            throw new IllegalStateException(String.format("Cannot change %s to the still closed state.", new Object[] {this}));
        }
        return new State(true, (this.closedIterations == Integer.MAX_VALUE) ? Integer.MAX_VALUE : (this.closedIterations + 1));
    }


    public boolean isOpen()
    {
        return !this.closed;
    }


    public boolean isClosed()
    {
        return this.closed;
    }


    public int getClosedIterations()
    {
        return this.closedIterations;
    }


    public Instant getInstant()
    {
        return this.instant;
    }


    public boolean hasBeenClosedForAtLeast(Duration d)
    {
        return (isClosed() && this.instant.plus(d).isBefore(Instant.now()));
    }


    public String toString()
    {
        return String.format("%s@%s", new Object[] {this.closed ? String.format("CLOSED(%d)", new Object[] {Integer.valueOf(this.closedIterations)}) : "OPEN", this.instant});
    }
}

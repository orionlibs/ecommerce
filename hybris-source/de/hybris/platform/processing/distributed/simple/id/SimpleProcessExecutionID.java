package de.hybris.platform.processing.distributed.simple.id;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class SimpleProcessExecutionID
{
    protected static final String DEFAULT_PREFIX = "TURN";
    protected static final Splitter SPLITTER = Splitter.on('_');
    protected final String prefix;
    protected final int turnNumber;


    protected SimpleProcessExecutionID(String prefix, int turnNumber)
    {
        this.prefix = Objects.<String>requireNonNull(prefix, "prefix is required");
        this.turnNumber = turnNumber;
    }


    public static SimpleProcessExecutionID firstTurn()
    {
        return new SimpleProcessExecutionID("TURN", 0);
    }


    public static SimpleProcessExecutionID fromString(String processStringID)
    {
        Objects.requireNonNull(processStringID, "processStringID is required");
        Iterable<String> result = SPLITTER.split(processStringID);
        Preconditions.checkState((Iterables.size(result) == 2), "Wrong processStringID pattern");
        String prefix = (String)Iterables.get(result, 0);
        String turnNumber = (String)Iterables.get(result, 1);
        Preconditions.checkState(StringUtils.isNumeric(turnNumber), "turnNumber for processStringID must be numeric String");
        return new SimpleProcessExecutionID(prefix, Integer.parseInt(turnNumber));
    }


    public SimpleProcessExecutionID getNexID()
    {
        return new SimpleProcessExecutionID(this.prefix, this.turnNumber + 1);
    }


    public String toString()
    {
        return this.prefix + "_" + this.prefix;
    }
}

package de.hybris.platform.impex.distributed.process;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.util.Locale;
import java.util.Objects;

class ProcessExecutionId
{
    private static final int FIRST_TURN = 0;
    private static final int FIRST_GROUP = 0;
    public static final ProcessExecutionId INITIAL = new ProcessExecutionId(0, 0);
    private final int turn;
    private final int group;
    private final String stringRepresentation;


    public static ProcessExecutionId from(String stringRepresentation)
    {
        Objects.requireNonNull(stringRepresentation, "stringRepresentation mustn't be null");
        int splitIdx = stringRepresentation.indexOf('_');
        Preconditions.checkArgument((splitIdx != -1), "stringRepresentation '%s' must contain delimiter '_'", stringRepresentation);
        String turnStr = stringRepresentation.substring(0, splitIdx);
        String groupStr = stringRepresentation.substring(splitIdx + 1, stringRepresentation.length());
        Integer turn = Ints.tryParse(turnStr);
        Preconditions.checkArgument((turn != null), "stringRepresentation '%s' doesn't contain turn information", stringRepresentation);
        Integer group = Ints.tryParse(groupStr);
        Preconditions.checkArgument((group != null), "stringRepresentation '%s' doesn't contain group information", stringRepresentation);
        return new ProcessExecutionId(turn.intValue(), group.intValue());
    }


    private ProcessExecutionId(int turn, int group)
    {
        this.turn = turn;
        this.group = group;
        this.stringRepresentation = String.format(Locale.ROOT, "%d_%d", new Object[] {Integer.valueOf(turn), Integer.valueOf(group)});
    }


    public int getTurn()
    {
        return this.turn;
    }


    public int getGroup()
    {
        return this.group;
    }


    public boolean isFirstTurn()
    {
        return (this.turn == 0);
    }


    public ProcessExecutionId nextTurn()
    {
        return new ProcessExecutionId(this.turn + 1, 0);
    }


    public ProcessExecutionId nextGroup()
    {
        return new ProcessExecutionId(this.turn, this.group + 1);
    }


    public ProcessExecutionId previousTurn()
    {
        Preconditions.checkState(!isFirstTurn(), "It's first turn");
        return new ProcessExecutionId(this.turn - 1, 0);
    }


    public String toString()
    {
        return this.stringRepresentation;
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(getClass() != obj.getClass())
        {
            return false;
        }
        ProcessExecutionId other = (ProcessExecutionId)obj;
        return (this.turn == other.turn && this.group == other.group);
    }


    public int hashCode()
    {
        return this.stringRepresentation.hashCode();
    }
}

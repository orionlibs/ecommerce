package de.hybris.bootstrap.ddl.tools;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class DMLStatement extends SqlStatement
{
    private final Collection<Object> params;
    private boolean stopIfEmpty;


    public DMLStatement(String statement)
    {
        this(statement, Collections.EMPTY_LIST);
    }


    public DMLStatement(String statement, Collection<Object> params)
    {
        super(statement);
        this.params = Objects.<Collection<Object>>requireNonNull(params);
    }


    public Object[] getParams()
    {
        return this.params.toArray();
    }


    public String toString()
    {
        FluentIterable<String> stringParams = FluentIterable.from(this.params).transform((Function)new Object(this));
        return getStatement() + " [" + getStatement() + "]";
    }


    public boolean isStopIfEmpty()
    {
        return this.stopIfEmpty;
    }


    public void setStopIfEmpty(boolean stopIfEmpty)
    {
        this.stopIfEmpty = stopIfEmpty;
    }
}

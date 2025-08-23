package de.hybris.platform.mediaconversion.job;

import java.util.Collection;
import java.util.LinkedList;

class ExceptionCollector
{
    private final Collection<Exception> exceptions = new LinkedList<>();


    void collect(Exception exception)
    {
        synchronized(this)
        {
            this.exceptions.add(exception);
        }
    }


    boolean hasExceptions()
    {
        synchronized(this)
        {
            return !this.exceptions.isEmpty();
        }
    }


    public String toString()
    {
        StringBuilder ret = new StringBuilder(getClass().getSimpleName());
        ret.append(' ');
        synchronized(this)
        {
            if(this.exceptions.isEmpty())
            {
                ret.append("no errors.");
            }
            else
            {
                ret.append(this.exceptions.size());
                ret.append(" errors:");
                for(Exception exp : this.exceptions)
                {
                    ret.append('\n');
                    ret.append(exp);
                }
            }
        }
        return ret.toString();
    }
}

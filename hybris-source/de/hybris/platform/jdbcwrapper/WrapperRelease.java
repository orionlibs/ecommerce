package de.hybris.platform.jdbcwrapper;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class WrapperRelease<T>
{
    private LinkedList<T> resourcesStack;
    private boolean releasing = false;


    protected void addResource(T resource)
    {
        if(!this.releasing)
        {
            if(this.resourcesStack == null)
            {
                this.resourcesStack = new LinkedList<>();
            }
            this.resourcesStack.add(resource);
        }
    }


    protected void removeResource(T resource)
    {
        if(!this.releasing)
        {
            if(this.resourcesStack != null)
            {
                this.resourcesStack.remove(resource);
            }
        }
    }


    protected void releaseResources() throws SQLException
    {
        if(this.resourcesStack != null)
        {
            try
            {
                this.releasing = true;
                for(Iterator<T> it = this.resourcesStack.descendingIterator(); it.hasNext(); )
                {
                    releaseResourceImpl(it.next());
                }
            }
            finally
            {
                this.resourcesStack = null;
                this.releasing = false;
            }
        }
    }


    protected abstract void releaseResourceImpl(T paramT) throws SQLException;


    protected abstract boolean isFlexibleSyntax(String paramString) throws SQLException;
}

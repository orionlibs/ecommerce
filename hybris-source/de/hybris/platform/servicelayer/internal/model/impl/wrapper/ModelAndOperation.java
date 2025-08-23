package de.hybris.platform.servicelayer.internal.model.impl.wrapper;

import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;

public class ModelAndOperation
{
    private final Object model;
    private final PersistenceOperation operation;


    public ModelAndOperation(Object model, PersistenceOperation operation)
    {
        this.model = model;
        this.operation = operation;
    }


    public Object getModel()
    {
        return this.model;
    }


    public PersistenceOperation getOperation()
    {
        return this.operation;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        ModelAndOperation that = (ModelAndOperation)o;
        if((this.model != null) ? !this.model.equals(that.model) : (that.model != null))
        {
            return false;
        }
        if(this.operation != that.operation)
        {
            return false;
        }
        return true;
    }


    public int hashCode()
    {
        int result = (this.model != null) ? this.model.hashCode() : 0;
        result = 31 * result + ((this.operation != null) ? this.operation.hashCode() : 0);
        return result;
    }
}

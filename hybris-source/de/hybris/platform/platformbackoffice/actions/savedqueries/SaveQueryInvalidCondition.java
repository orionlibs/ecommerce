package de.hybris.platform.platformbackoffice.actions.savedqueries;

import java.util.Objects;

public class SaveQueryInvalidCondition
{
    private final String qualifier;


    public SaveQueryInvalidCondition(String qualifier)
    {
        this.qualifier = qualifier;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public String toString()
    {
        return "SaveQueryInvalidCondition{qualifier='" + this.qualifier + "'}";
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
        SaveQueryInvalidCondition that = (SaveQueryInvalidCondition)o;
        return Objects.equals(this.qualifier, that.qualifier);
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.qualifier});
    }
}

package de.hybris.platform.cms2.namedquery;

import de.hybris.platform.cms2.enums.SortDirection;

public class Sort
{
    private String parameter;
    private SortDirection direction;


    public void setParameter(String parameter)
    {
        this.parameter = parameter;
    }


    public Sort withParameter(String parameter)
    {
        this.parameter = parameter;
        return this;
    }


    public String getParameter()
    {
        return this.parameter;
    }


    public void setDirection(SortDirection direction)
    {
        this.direction = direction;
    }


    public Sort withDirection(SortDirection direction)
    {
        this.direction = direction;
        return this;
    }


    public SortDirection getDirection()
    {
        return this.direction;
    }
}

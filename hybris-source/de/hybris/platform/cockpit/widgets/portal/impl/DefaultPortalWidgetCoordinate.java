package de.hybris.platform.cockpit.widgets.portal.impl;

import de.hybris.platform.cockpit.widgets.portal.PortalWidgetCoordinate;

public class DefaultPortalWidgetCoordinate implements PortalWidgetCoordinate
{
    private final int column;
    private final int row;


    public DefaultPortalWidgetCoordinate()
    {
        this(2147483647, 2147483647);
    }


    public DefaultPortalWidgetCoordinate(int column, int row)
    {
        this.column = column;
        this.row = row;
    }


    public int getColumn()
    {
        return this.column;
    }


    public int getRow()
    {
        return this.row;
    }


    public String toString()
    {
        return "[" + this.column + "," + this.row + "]";
    }
}

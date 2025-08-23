package de.hybris.platform.cockpit.util.comparators;

import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import java.util.Comparator;

public class ColumnNameComparator implements Comparator<ColumnDescriptor>
{
    public int compare(ColumnDescriptor desc1, ColumnDescriptor desc2)
    {
        if(desc1 == null && desc2 == null)
        {
            return 0;
        }
        if(desc1 == null)
        {
            return -1;
        }
        if(desc2 == null)
        {
            return 1;
        }
        return desc1.getName().compareToIgnoreCase(desc2.getName());
    }
}

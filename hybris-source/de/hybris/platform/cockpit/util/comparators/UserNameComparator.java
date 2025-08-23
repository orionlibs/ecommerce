package de.hybris.platform.cockpit.util.comparators;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Comparator;

public class UserNameComparator implements Comparator<TypedObject>
{
    public int compare(TypedObject name1, TypedObject name2)
    {
        String label1 = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject(name1);
        String label2 = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabelForTypedObject(name2);
        if(label1 == null && label2 == null)
        {
            return 0;
        }
        if(label1 == null)
        {
            return -1;
        }
        if(label2 == null)
        {
            return 1;
        }
        return label1.compareToIgnoreCase(label2);
    }
}

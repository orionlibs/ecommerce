package de.hybris.platform.admincockpit.util;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.util.Comparator;

public class ComposedTypeComparator implements Comparator<ComposedTypeModel>
{
    public int compare(ComposedTypeModel composedTypeModel1, ComposedTypeModel composedTypeModel2)
    {
        return composedTypeModel1.getCode().compareTo(composedTypeModel2.getCode());
    }
}

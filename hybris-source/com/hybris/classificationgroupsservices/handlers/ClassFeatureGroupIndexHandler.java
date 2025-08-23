package com.hybris.classificationgroupsservices.handlers;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;
import java.util.List;

public class ClassFeatureGroupIndexHandler extends AbstractDynamicAttributeHandler<Integer, ClassFeatureGroupModel>
{
    private static final int FIRST_INDEX_VALUE = 1;
    private static final int INDEX_NOT_PRESENT = -1;


    public Integer get(ClassFeatureGroupModel model)
    {
        ClassificationClassModel classificationClass = model.getClassificationClass();
        if(classificationClass == null)
        {
            return Integer.valueOf(-1);
        }
        List<ClassFeatureGroupModel> groups = classificationClass.getClassFeatureGroups();
        if(groups.indexOf(model) == -1)
        {
            return Integer.valueOf(-1);
        }
        return Integer.valueOf(groups.indexOf(model) + 1);
    }
}

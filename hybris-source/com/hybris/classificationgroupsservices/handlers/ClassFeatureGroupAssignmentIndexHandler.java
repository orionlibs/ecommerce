package com.hybris.classificationgroupsservices.handlers;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;
import java.util.List;

public class ClassFeatureGroupAssignmentIndexHandler extends AbstractDynamicAttributeHandler<Integer, ClassFeatureGroupAssignmentModel>
{
    private static final int FIRST_INDEX_VALUE = 1;
    private static final int INDEX_NOT_PRESENT = -1;


    public Integer get(ClassFeatureGroupAssignmentModel model)
    {
        if(model.getClassFeatureGroup() == null)
        {
            return Integer.valueOf(-1);
        }
        List<ClassFeatureGroupAssignmentModel> containers = model.getClassFeatureGroup().getClassFeatureGroupAssignments();
        return Integer.valueOf(calculateIndexValue(model, containers));
    }


    private int calculateIndexValue(ClassFeatureGroupAssignmentModel model, List<ClassFeatureGroupAssignmentModel> containers)
    {
        if(containers.indexOf(model) == -1)
        {
            return -1;
        }
        return containers.indexOf(model) + 1;
    }
}

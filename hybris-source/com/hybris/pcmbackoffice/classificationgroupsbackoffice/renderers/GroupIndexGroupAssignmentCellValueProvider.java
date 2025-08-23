package com.hybris.pcmbackoffice.classificationgroupsbackoffice.renderers;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import com.hybris.classificationgroupsservices.model.ClassFeatureGroupModel;

public class GroupIndexGroupAssignmentCellValueProvider implements GroupAssignmentCellValueProvider
{
    private static final int INDEX_NOT_FOUND = -1;


    public String apply(ClassFeatureGroupAssignmentModel model)
    {
        ClassFeatureGroupModel group = model.getClassFeatureGroup();
        if(group.getIndex().intValue() == -1)
        {
            return "";
        }
        return String.valueOf(group.getIndex());
    }
}

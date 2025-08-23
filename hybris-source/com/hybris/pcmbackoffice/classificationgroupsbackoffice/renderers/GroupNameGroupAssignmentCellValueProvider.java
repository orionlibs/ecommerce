package com.hybris.pcmbackoffice.classificationgroupsbackoffice.renderers;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import com.hybris.classificationgroupsservices.model.ClassFeatureGroupModel;

public class GroupNameGroupAssignmentCellValueProvider implements GroupAssignmentCellValueProvider
{
    public String apply(ClassFeatureGroupAssignmentModel model)
    {
        ClassFeatureGroupModel group = model.getClassFeatureGroup();
        if(group == null)
        {
            return "";
        }
        return (group.getName() != null) ? group.getName() : group.getCode();
    }
}

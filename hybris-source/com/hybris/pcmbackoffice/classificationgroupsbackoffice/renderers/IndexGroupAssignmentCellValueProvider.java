package com.hybris.pcmbackoffice.classificationgroupsbackoffice.renderers;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import com.hybris.classificationgroupsservices.model.ClassFeatureGroupModel;
import java.util.List;

public class IndexGroupAssignmentCellValueProvider implements GroupAssignmentCellValueProvider
{
    private static final int INDEX_NOT_FOUND = -1;


    public String apply(ClassFeatureGroupAssignmentModel model)
    {
        ClassFeatureGroupModel group = model.getClassFeatureGroup();
        if(group == null)
        {
            return "";
        }
        List<ClassFeatureGroupAssignmentModel> assignmentGroups = group.getClassFeatureGroupAssignments();
        int index = assignmentGroups.indexOf(model);
        if(index == -1)
        {
            return "";
        }
        return String.valueOf(index + 1);
    }
}

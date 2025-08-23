package com.hybris.pcmbackoffice.classificationgroupsbackoffice.renderers;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;

public class ClassFeatureGroupAssignmentIndexRenderer extends AbstractClassificationCellRenderer<ClassFeatureGroupAssignmentModel>
{
    protected String getValue(ClassFeatureGroupAssignmentModel featureGroupAssignment, WidgetInstanceManager widgetInstanceManager)
    {
        if(featureGroupAssignment.getIndex() == null || featureGroupAssignment.getIndex().intValue() == -1)
        {
            return "";
        }
        return String.valueOf(featureGroupAssignment.getIndex());
    }
}

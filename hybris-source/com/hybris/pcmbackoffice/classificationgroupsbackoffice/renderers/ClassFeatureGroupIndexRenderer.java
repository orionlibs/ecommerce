package com.hybris.pcmbackoffice.classificationgroupsbackoffice.renderers;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;

public class ClassFeatureGroupIndexRenderer extends AbstractClassificationCellRenderer<ClassFeatureGroupModel>
{
    protected String getValue(ClassFeatureGroupModel classFeatureGroup, WidgetInstanceManager widgetInstanceManager)
    {
        if(classFeatureGroup.getIndex() == null || classFeatureGroup.getIndex().intValue() == -1)
        {
            return "";
        }
        return String.valueOf(classFeatureGroup.getIndex());
    }
}

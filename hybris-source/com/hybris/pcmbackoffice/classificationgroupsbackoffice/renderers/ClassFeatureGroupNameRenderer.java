package com.hybris.pcmbackoffice.classificationgroupsbackoffice.renderers;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;

public class ClassFeatureGroupNameRenderer extends AbstractClassificationCellRenderer<ClassFeatureGroupModel>
{
    protected String getValue(ClassFeatureGroupModel classFeatureGroup, WidgetInstanceManager widgetInstanceManager)
    {
        return (classFeatureGroup.getName() != null) ? classFeatureGroup.getName() : classFeatureGroup.getCode();
    }
}

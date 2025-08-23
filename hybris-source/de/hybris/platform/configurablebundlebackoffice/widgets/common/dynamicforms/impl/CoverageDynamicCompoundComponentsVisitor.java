package de.hybris.platform.configurablebundlebackoffice.widgets.common.dynamicforms.impl;

import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.DynamicForms;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.dynamicforms.ComponentsVisitor;
import com.hybris.cockpitng.widgets.common.dynamicforms.impl.CompoundComponentsVisitor;
import de.hybris.platform.core.Registry;
import java.util.List;
import java.util.stream.Collectors;

public class CoverageDynamicCompoundComponentsVisitor extends CompoundComponentsVisitor
{
    public static List<ComponentsVisitor> getDynamicVisitors(DynamicForms dynamicForms)
    {
        return (List<ComponentsVisitor>)dynamicForms.getVisitor().stream().map(dv -> dv.getBeanId()).distinct().map(bean -> (ComponentsVisitor)Registry.getApplicationContext().getBean(bean, ComponentsVisitor.class))
                        .collect(Collectors.toList());
    }


    public void initialize(String typeCode, WidgetInstanceManager wim, DynamicForms dynamicForms)
    {
        setVisitors(getDynamicVisitors(dynamicForms));
        super.initialize(typeCode, wim, dynamicForms);
    }
}

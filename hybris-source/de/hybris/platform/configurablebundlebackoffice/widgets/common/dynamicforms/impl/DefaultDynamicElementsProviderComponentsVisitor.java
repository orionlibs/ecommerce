package de.hybris.platform.configurablebundlebackoffice.widgets.common.dynamicforms.impl;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.AbstractDynamicElement;
import com.hybris.cockpitng.widgets.common.dynamicforms.impl.visitors.AbstractComponentsVisitor;
import de.hybris.platform.configurablebundlebackoffice.widgets.common.dynamicforms.DynamicElementsProviderComponentsVisitor;
import java.util.List;
import java.util.stream.Collectors;
import org.zkoss.zk.ui.Component;

public class DefaultDynamicElementsProviderComponentsVisitor<C extends Component> extends AbstractComponentsVisitor<C, AbstractDynamicElement> implements DynamicElementsProviderComponentsVisitor
{
    public List<AbstractDynamicElement> getNonVisibleDynamicElements(Object target)
    {
        return (List<AbstractDynamicElement>)getDynamicElements().stream().filter(e -> !isVisible(e, target)).collect(Collectors.toList());
    }


    protected boolean canHandle(Component component)
    {
        return false;
    }


    protected String getComponentKey(C c)
    {
        return null;
    }


    protected List<AbstractDynamicElement> getDynamicElements()
    {
        List<AbstractDynamicElement> elements = Lists.newArrayList();
        elements.addAll(getDynamicForms().getAttribute());
        elements.addAll(getDynamicForms().getSection());
        elements.addAll(getDynamicForms().getTab());
        return elements;
    }


    protected void visitComponents(AbstractDynamicElement element, Object target, boolean initial)
    {
    }
}

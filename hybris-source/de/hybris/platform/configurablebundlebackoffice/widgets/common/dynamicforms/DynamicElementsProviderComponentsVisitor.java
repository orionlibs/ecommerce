package de.hybris.platform.configurablebundlebackoffice.widgets.common.dynamicforms;

import com.hybris.cockpitng.core.config.impl.jaxb.dynamicforms.AbstractDynamicElement;
import java.util.List;

public interface DynamicElementsProviderComponentsVisitor
{
    List<AbstractDynamicElement> getNonVisibleDynamicElements(Object paramObject);
}

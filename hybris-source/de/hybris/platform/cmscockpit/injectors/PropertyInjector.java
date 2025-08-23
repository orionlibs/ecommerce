package de.hybris.platform.cmscockpit.injectors;

import de.hybris.platform.cmscockpit.services.config.ContentEditorConfiguration;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import java.util.Map;
import org.zkoss.zk.ui.HtmlBasedComponent;

public interface PropertyInjector
{
    void injectProperty(TypedObject paramTypedObject, HtmlBasedComponent paramHtmlBasedComponent, ContentEditorConfiguration paramContentEditorConfiguration, ObjectValueContainer paramObjectValueContainer, Object paramObject, boolean paramBoolean1, boolean paramBoolean2,
                    Map<String, ? extends Object> paramMap);
}

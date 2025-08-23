package de.hybris.platform.cmscockpit.injectors;

import de.hybris.platform.cmscockpit.services.config.ContentEditorConfiguration;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.List;
import org.zkoss.zk.ui.HtmlBasedComponent;

public interface ReferenceInjector
{
    void injectReference(TypedObject paramTypedObject, HtmlBasedComponent paramHtmlBasedComponent, ContentEditorConfiguration paramContentEditorConfiguration, Object paramObject, boolean paramBoolean, List<HtmlBasedComponent> paramList);
}

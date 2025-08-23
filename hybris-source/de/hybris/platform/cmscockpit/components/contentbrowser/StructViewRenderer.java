package de.hybris.platform.cmscockpit.components.contentbrowser;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import org.zkoss.zk.ui.HtmlBasedComponent;

public interface StructViewRenderer
{
    void renderStructView(TypedObject paramTypedObject, HtmlBasedComponent paramHtmlBasedComponent) throws IllegalArgumentException;
}

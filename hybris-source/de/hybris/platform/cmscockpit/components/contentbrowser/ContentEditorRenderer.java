package de.hybris.platform.cmscockpit.components.contentbrowser;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import java.util.Map;
import org.zkoss.zk.ui.HtmlBasedComponent;

public interface ContentEditorRenderer
{
    void setAutoPersist(boolean paramBoolean);


    boolean isAutoPersist();


    void renderContentEditor(TypedObject paramTypedObject, String paramString, ObjectValueContainer paramObjectValueContainer, HtmlBasedComponent paramHtmlBasedComponent, Map<String, ? extends Object> paramMap) throws IllegalArgumentException;


    void renderContentEditor(TypedObject paramTypedObject, String paramString, ObjectValueContainer paramObjectValueContainer, HtmlBasedComponent paramHtmlBasedComponent, Map<String, ? extends Object> paramMap, boolean paramBoolean) throws IllegalArgumentException;
}

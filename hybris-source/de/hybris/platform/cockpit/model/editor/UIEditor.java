package de.hybris.platform.cockpit.model.editor;

import java.util.Map;
import org.zkoss.zk.ui.HtmlBasedComponent;

public interface UIEditor
{
    public static final String INITIAL_EDIT_STRING = "initialEditString";


    HtmlBasedComponent createViewComponent(Object paramObject, Map<String, ? extends Object> paramMap, EditorListener paramEditorListener);


    boolean isInline();


    String getEditorType();


    void setValue(Object paramObject);


    Object getValue();


    void setEditable(boolean paramBoolean);


    boolean isEditable();


    void setFocus(HtmlBasedComponent paramHtmlBasedComponent, boolean paramBoolean);


    boolean isOptional();


    void setOptional(boolean paramBoolean);
}

package de.hybris.platform.cockpit.model.meta;

import de.hybris.platform.cockpit.model.editor.UIEditor;

public interface PropertyEditorDescriptor
{
    public static final String DEFAULT = "default";
    public static final String SINGLE = "single";
    public static final String MULTI = "multi";
    public static final String RANGE = "range";


    String getLabel();


    String getPreviewImage();


    String getEditorType();


    PropertyEditorBean createValueBean(Object paramObject);


    UIEditor createUIEditor();


    UIEditor createUIEditor(String paramString);
}

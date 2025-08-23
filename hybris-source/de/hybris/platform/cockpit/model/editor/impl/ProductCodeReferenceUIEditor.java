package de.hybris.platform.cockpit.model.editor.impl;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.ReferenceUIEditor;
import de.hybris.platform.cockpit.model.referenceeditor.simple.impl.DefaultSimpleReferenceUIEditor;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Map;
import org.zkoss.zk.ui.HtmlBasedComponent;

public class ProductCodeReferenceUIEditor extends DefaultTextUIEditor
{
    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        ReferenceUIEditor refEditor = createEditor();
        HtmlBasedComponent viewComponent = refEditor.createViewComponent(null, parameters, (EditorListener)new Object(this, listener));
        return viewComponent;
    }


    protected ReferenceUIEditor createEditor()
    {
        DefaultSimpleReferenceUIEditor defaultSimpleReferenceUIEditor = new DefaultSimpleReferenceUIEditor();
        defaultSimpleReferenceUIEditor.setAllowCreate(Boolean.FALSE);
        defaultSimpleReferenceUIEditor.setEditable(isEditable());
        defaultSimpleReferenceUIEditor.setRootType(UISessionUtils.getCurrentSession().getTypeService().getObjectType("Product"));
        return (ReferenceUIEditor)defaultSimpleReferenceUIEditor;
    }
}

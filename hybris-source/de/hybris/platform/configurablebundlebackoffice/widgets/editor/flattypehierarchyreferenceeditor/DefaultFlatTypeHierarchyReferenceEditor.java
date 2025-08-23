package de.hybris.platform.configurablebundlebackoffice.widgets.editor.flattypehierarchyreferenceeditor;

import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorLayout;
import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorLogic;
import com.hybris.cockpitng.editor.defaultreferenceeditor.DefaultReferenceEditor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.engine.WidgetInstanceManager;

public class DefaultFlatTypeHierarchyReferenceEditor<T> extends DefaultReferenceEditor<T>
{
    public ReferenceEditorLayout<T> createReferenceLayout(EditorContext context)
    {
        FlatTypeHierarchyReferenceEditorLayout ret = new FlatTypeHierarchyReferenceEditorLayout((ReferenceEditorLogic)this, loadBaseConfiguration(getTypeCode(), (WidgetInstanceManager)context.getParameter("wim")));
        ret.setPlaceholderKey(getPlaceholderKey());
        return (ReferenceEditorLayout<T>)ret;
    }
}

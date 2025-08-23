package de.hybris.platform.customersupportbackoffice.editor;

import com.hybris.cockpitng.editor.defaultenum.DefaultEnumEditor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import org.zkoss.zk.ui.Component;

public class NonOptionalEnumEditor extends DefaultEnumEditor
{
    public void render(Component parent, EditorContext<Object> context, EditorListener<Object> listener)
    {
        context.setOptional(false);
        super.render(parent, context, listener);
    }
}

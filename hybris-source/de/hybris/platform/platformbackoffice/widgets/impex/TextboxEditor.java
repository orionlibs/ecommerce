package de.hybris.platform.platformbackoffice.widgets.impex;

import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Textbox;

public class TextboxEditor extends AbstractCockpitEditorRenderer<String>
{
    public void render(Component parent, EditorContext<String> context, EditorListener<String> listener)
    {
        Validate.notNull("All parameters are mandatory", new Object[] {parent, context, listener});
        Textbox textbox = new Textbox();
        textbox.setParent(parent);
        textbox.setWidth("100%");
        textbox.setHeight("100%");
        textbox.setMultiline(true);
        textbox.setReadonly(true);
        textbox.setText((String)context.getInitialValue());
    }
}

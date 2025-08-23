package de.hybris.platform.platformbackoffice.classification.editor;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import java.util.Objects;
import org.zkoss.zk.ui.Component;

public class UndefinedTypeEditor extends AbstractCockpitEditorRenderer<Object>
{
    public void render(Component parent, EditorContext<Object> context, EditorListener<Object> listener)
    {
        Editor editor = new Editor();
        editor.setReadableLocales(context.getReadableLocales());
        editor.setWritableLocales(context.getWritableLocales());
        editor.setType(String.class.getName());
        editor.setOrdered(context.isOrdered());
        editor.setValue(Objects.toString(context.getInitialValue(), ""));
        editor.setOptional(context.isOptional());
        editor.setWidgetInstanceManager((WidgetInstanceManager)context.getParameter("wim"));
        editor.addParameters(context.getParameters());
        editor.setReadOnly(true);
        editor.afterCompose();
        parent.appendChild((Component)editor);
    }
}

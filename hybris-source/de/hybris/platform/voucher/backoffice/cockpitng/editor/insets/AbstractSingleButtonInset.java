package de.hybris.platform.voucher.backoffice.cockpitng.editor.insets;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;

public abstract class AbstractSingleButtonInset<T> implements CockpitEditorRenderer<T>
{
    public static final String BUTTON_LABEL_PARAM = "buttonLabel";
    public static final String INSET_SCLASS = "single-button";
    public static final String BUTTON_SCLASS = "inset-button";


    protected abstract EventListener<Event> getInsetListener(Component paramComponent, EditorContext<T> paramEditorContext, EditorListener<T> paramEditorListener);


    public void render(Component parent, EditorContext<T> context, EditorListener<T> listener)
    {
        if(parent != null && context != null && listener != null)
        {
            Div insetContainer = new Div();
            insetContainer.setSclass("inset " + getSclass());
            insetContainer.setParent(parent);
            String insetLabel = "";
            Object insetLabelObject = context.getParameter("buttonLabel");
            if(insetLabelObject instanceof String)
            {
                insetLabel = context.getLabel((String)insetLabelObject);
            }
            Button button = new Button();
            button.setLabel(insetLabel);
            button.setSclass("inset-button");
            button.setParent((Component)insetContainer);
            button.addEventListener("onClick", getInsetListener(parent, context, listener));
            button.setDisabled(!isEnabled(context));
        }
    }


    protected boolean isEnabled(EditorContext<T> context)
    {
        return true;
    }


    protected String getSclass()
    {
        return "single-button";
    }


    protected Editor findAncestorEditor(Component component)
    {
        Component current = component;
        while(current != null && !(current instanceof Editor) && !(current instanceof com.hybris.cockpitng.core.Widget))
        {
            current = current.getParent();
        }
        if(current instanceof Editor)
        {
            return (Editor)current;
        }
        return null;
    }
}

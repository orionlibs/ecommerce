package de.hybris.platform.platformbackoffice.widgets.impex;

import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Spinner;

public class SpinnerEditor extends AbstractCockpitEditorRenderer<Integer>
{
    private static final String MIN_CONSTRAINT = "min 1";
    private static final Integer MIN_VALUE = Integer.valueOf(1);


    public void render(Component parent, EditorContext<Integer> context, EditorListener<Integer> listener)
    {
        Spinner spinnerView = new Spinner();
        Integer value = (Integer)context.getInitialValue();
        if(value == null)
        {
            value = MIN_VALUE;
        }
        spinnerView.setValue(value);
        spinnerView.addEventListener("onChange", event -> handleChangeEvent(listener, spinnerView));
        spinnerView.setConstraint("min 1");
        spinnerView.setParent(parent);
    }


    private static void handleChangeEvent(EditorListener<Integer> listener, Spinner editorView)
    {
        listener.onValueChanged(editorView.getValue());
    }
}

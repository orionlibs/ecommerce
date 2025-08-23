package de.hybris.platform.admincockpit.components.editorarea;

import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.impl.DefaultDecimalUIEditor;
import de.hybris.platform.cockpit.util.UITools;
import java.math.BigDecimal;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Decimalbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.impl.InputElement;

public class BigDecimalUIEditor extends DefaultDecimalUIEditor
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultDecimalUIEditor.class);


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        BigDecimal value = null;
        if(initialValue == null || initialValue instanceof BigDecimal)
        {
            value = (BigDecimal)initialValue;
        }
        else
        {
            LOG.error("Initial value not of type BigDecimal.");
        }
        if(isEditable())
        {
            Decimalbox decimalbox = new Decimalbox();
            decimalbox.setValue(value);
            UITools.applyTestID((Component)decimalbox, getTestId(parameters));
            return (HtmlBasedComponent)createViewComponentInternal((InputElement)decimalbox, listener, parameters);
        }
        Label editorView = new Label();
        editorView.setValue((value == null) ? "" : value.toPlainString());
        UITools.applyTestID((Component)editorView, "dummy_ed");
        return (HtmlBasedComponent)editorView;
    }


    public boolean isInline()
    {
        return true;
    }


    public String getEditorType()
    {
        return "Dummy";
    }
}

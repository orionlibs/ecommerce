package de.hybris.platform.platformbackoffice.editors.taxdiscountvalueeditor;

import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import de.hybris.platform.platformbackoffice.taxdiscountvalueparser.ParserException;
import de.hybris.platform.platformbackoffice.taxdiscountvalueparser.ValueParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Textbox;

public class TaxDiscountValueEditor extends AbstractComponentWidgetAdapterAware implements CockpitEditorRenderer<Object>
{
    public static final String TAX_VALUE_TYPE = "de.hybris.platform.util.TaxValue";
    public static final String TAX_VALUE_PARSER = "taxValueParser";
    public static final String DISCOUNT_VALUE_PARSER = "discountValueParser";
    private static final Logger LOG = LoggerFactory.getLogger(TaxDiscountValueEditor.class);


    public void render(Component component, EditorContext<Object> editorContext, EditorListener<Object> editorListener)
    {
        String valueType = editorContext.getValueType();
        String parserBeanName = "de.hybris.platform.util.TaxValue".equals(valueType) ? "taxValueParser" : "discountValueParser";
        ValueParser valueParser = (ValueParser)BackofficeSpringUtil.getBean(parserBeanName);
        Textbox textbox = new Textbox();
        textbox.setWidth("100%");
        String initialValueString = valueParser.render(editorContext.getInitialValue());
        textbox.setValue(initialValueString);
        textbox.addEventListener("onChanging", event -> validateValueAndNotifyListener(event.getValue(), valueParser, editorListener, textbox));
        textbox.addEventListener("onChange", event -> validateValueAndNotifyListener(textbox.getValue(), valueParser, editorListener, textbox));
        textbox.addEventListener("onOK", event -> {
            validateValueAndNotifyListener(textbox.getValue(), valueParser, editorListener, textbox);
            editorListener.onEditorEvent("enter_pressed");
        });
        textbox.addEventListener("onCancel", event -> {
            textbox.setValue(initialValueString);
            editorListener.onValueChanged(null);
        });
        textbox.setParent(component);
    }


    protected void validateValueAndNotifyListener(String value, ValueParser valueParser, EditorListener editorListener, Textbox textbox)
    {
        try
        {
            Object parsedValue = valueParser.parse(value);
            textbox.clearErrorMessage();
            editorListener.onValueChanged(parsedValue);
        }
        catch(ParserException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e.getMessage(), (Throwable)e);
            }
            editorListener.onValueChanged(null);
            throw new WrongValueException(textbox, e.getMessage());
        }
    }
}

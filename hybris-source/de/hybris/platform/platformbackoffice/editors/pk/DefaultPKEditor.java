package de.hybris.platform.platformbackoffice.editors.pk;

import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import de.hybris.platform.core.PK;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Textbox;

public class DefaultPKEditor extends AbstractCockpitEditorRenderer<Object>
{
    public static final String PK_EDITOR_SCLASS = "ye-onpremise-pk-editor";


    public void render(Component parent, EditorContext<Object> context, EditorListener<Object> listener)
    {
        if(parent == null || context == null)
        {
            throw new IllegalArgumentException("parent or context equals null");
        }
        Object value = context.getInitialValue();
        Textbox input = new Textbox();
        input.setPlaceholder(context.getLabel("pkeditor.placeholder"));
        input.setReadonly(!context.isEditable());
        input.setInstant(true);
        input.setSclass("ye-onpremise-pk-editor");
        if(value instanceof PK)
        {
            input.setText(((PK)value).getLongValueAsString());
        }
        else if(value != null)
        {
            input.setText(context.getLabel("wrong.editor.type", (Object[])new String[] {value
                            .getClass().getCanonicalName()}));
        }
        input.addEventListener("onChange", change -> listener.onValueChanged(tryParsePK(parent, input.getValue(), context)));
        parent.appendChild((Component)input);
    }


    protected PK tryParsePK(Component parent, String value, EditorContext<Object> context)
    {
        if(StringUtils.isBlank(value))
        {
            return null;
        }
        try
        {
            return PK.parse(value);
        }
        catch(de.hybris.platform.core.PK.PKException pkException)
        {
            throw new WrongValueException(parent, context.getLabel("pkeditor.error.invalid.value"), pkException);
        }
    }
}

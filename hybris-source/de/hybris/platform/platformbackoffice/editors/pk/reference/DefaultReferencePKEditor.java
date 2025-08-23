package de.hybris.platform.platformbackoffice.editors.pk.reference;

import com.hybris.cockpitng.common.EditorBuilder;
import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.type.BackofficeTypeUtils;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.WrongValueException;

public class DefaultReferencePKEditor extends AbstractCockpitEditorRenderer<Object>
{
    public static final String EDITOR_ID_ONPREMISE_PK = "com.hybris.cockpitng.editor.onpremise.pk";
    public static final String SCLASS_YE_DEFAULT_REFERENCE_CONTAINER = "ye-default-reference-container";
    public static final String SCLASS_YE_ONPREMISE_REFERENCE_PK_EDITOR = "ye-onpremise-reference-pk-editor";
    public static final String SCLASS_YE_REMOVE_REFERENCE = "ye-remove-reference ye-delete-btn";
    public static final String SCLASS_YE_INVALID_PK = "ye-invalid-pk";
    @Resource
    private ModelService modelService;
    @Resource
    private LabelService labelService;
    @Resource
    private BackofficeTypeUtils backofficeTypeUtils;


    public void render(Component parent, EditorContext<Object> context, EditorListener<Object> listener)
    {
        Validate.notNull("All arguments are mandatory", new Object[] {parent, context, listener});
        UITools.modifySClass((HtmlBasedComponent)parent, "ye-onpremise-reference-pk-editor", true);
        Object initialValue = context.getInitialValue();
        Editor editor = createNestedPKEditor(prepareNestedContext(context));
        ReferenceContainer referenceContainer = createReferenceContainer(initialValue, listener, editor);
        if(initialValue instanceof ItemModel)
        {
            editor.setVisible(false);
            referenceContainer.setVisible(true);
        }
        else
        {
            editor.setVisible(true);
            referenceContainer.setVisible(false);
        }
        editor.addEventListener("onValueChanged", event -> handleOnValueChanged(listener, editor, referenceContainer, event.getData(), context));
        parent.appendChild((Component)editor);
        parent.appendChild((Component)referenceContainer);
    }


    protected EditorContext<Object> prepareNestedContext(EditorContext<Object> context)
    {
        EditorContext<Object> editorContext = new EditorContext(context.getInitialValue(), context.getDefinition(), context.getParameters(), context.getLabels(), context.getReadableLocales(), context.getWritableLocales());
        editorContext.setValueType(ItemModel.class.getName());
        editorContext.setEditable(true);
        return editorContext;
    }


    protected void handleOnValueChanged(EditorListener<Object> listener, Editor editor, ReferenceContainer referenceContainer, Object data, EditorContext<Object> context)
    {
        if(data instanceof PK)
        {
            ItemModel model = loadItemByPK(editor, (PK)data, context);
            if(isOfRequiredType(model, context))
            {
                listener.onValueChanged(model);
                referenceContainer.setLabel(getLabelService().getObjectLabel(model));
                referenceContainer.setVisible(true);
                editor.setVisible(false);
                UITools.modifySClass((HtmlBasedComponent)editor, "ye-invalid-pk", false);
            }
            else
            {
                UITools.modifySClass((HtmlBasedComponent)editor, "ye-invalid-pk", true);
                throw new WrongValueException(editor, context.getLabel("item.with.given.pk.has.unsupported.type"));
            }
        }
        else if(data == null)
        {
            listener.onValueChanged(null);
            referenceContainer.setVisible(false);
            editor.setVisible(true);
        }
        else
        {
            throw new IllegalArgumentException("Passed data is on invalid type: " + data);
        }
    }


    protected boolean isOfRequiredType(ItemModel model, EditorContext<Object> context)
    {
        for(String type : getSupportedTypes(context))
        {
            if(getBackofficeTypeUtils().isAssignableFrom(type, model.getItemtype()))
            {
                return true;
            }
        }
        return false;
    }


    protected String[] getSupportedTypes(EditorContext<Object> context)
    {
        Validate.notNull("Context may not be null", new Object[] {context});
        String supportedTypes = (String)context.getParameterAs("supportedTypes");
        String[] result = StringUtils.split(supportedTypes, ',');
        if(result == null)
        {
            return new String[] {"Item"};
        }
        return result;
    }


    protected ItemModel loadItemByPK(Editor editor, PK pk, EditorContext<Object> context)
    {
        try
        {
            return (ItemModel)getModelService().get(pk);
        }
        catch(IllegalArgumentException iae)
        {
            UITools.modifySClass((HtmlBasedComponent)editor, "ye-invalid-pk", true);
            throw new WrongValueException(editor, context.getLabel("no.item.with.given.pk.error", new Object[] {pk
                            .toString()}), iae);
        }
    }


    protected Editor createNestedPKEditor(EditorContext<Object> context)
    {
        EditorBuilder builder = new EditorBuilder(context);
        builder.setReadOnly(false);
        builder.useEditor("com.hybris.cockpitng.editor.onpremise.pk");
        return builder.build();
    }


    protected ReferenceContainer createReferenceContainer(Object initialValue, EditorListener<Object> listener, Editor editor)
    {
        ReferenceContainer container = new ReferenceContainer();
        container.setLabel(getLabelService().getObjectLabel(initialValue));
        container.button.addEventListener("onClick", click -> {
            listener.onValueChanged(null);
            editor.setVisible(true);
            container.setVisible(false);
            editor.setValue(null);
        });
        return container;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    public LabelService getLabelService()
    {
        return this.labelService;
    }


    public BackofficeTypeUtils getBackofficeTypeUtils()
    {
        return this.backofficeTypeUtils;
    }
}

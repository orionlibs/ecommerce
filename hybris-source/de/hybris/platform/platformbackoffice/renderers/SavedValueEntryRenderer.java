package de.hybris.platform.platformbackoffice.renderers;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.labels.LabelUtils;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.editorarea.renderer.impl.AbstractEditorAreaComponentRenderer;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.hmc.model.SavedValueEntryModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;

public class SavedValueEntryRenderer<T> extends AbstractEditorAreaComponentRenderer<T, SavedValueEntryModel>
{
    private static final String MODEL_LOCALIZED_OLD_VALUE = "localizedOldValue";
    private static final String MODEL_LOCALIZED_NEW_VALUE = "localizedNewValue";
    private static final String QUALIFIER_OLD_VALUE = "oldValue";
    private static final String QUALIFIER_NEW_VALUE = "newValue";
    private static final String SAVED_VALUE_ENTRY_TYPECODE = "SavedValueEntry";
    protected TypeFacade typeFacade;
    protected CommonI18NService commonI18NService;


    protected TypeFacade getTypeFacade()
    {
        return this.typeFacade;
    }


    @Required
    public void setTypeFacade(TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    protected CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    public void render(Component component, T abstractSection, SavedValueEntryModel savedValueEntryModel, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        String modifiedAttribute = savedValueEntryModel.getModifiedAttribute();
        Editor editorOldValue = createEditor(widgetInstanceManager, savedValueEntryModel, modifiedAttribute, "oldValue");
        Editor editorNewValue = createEditor(widgetInstanceManager, savedValueEntryModel, modifiedAttribute, "newValue");
        Hlayout hlayout = new Hlayout();
        Div oldValueContainer = new Div();
        oldValueContainer.setSclass("yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-ed");
        if(!editorOldValue.isLocalized())
        {
            appendLabel(oldValueContainer, editorOldValue.getEditorLabel(), "oldValue");
        }
        oldValueContainer.appendChild((Component)editorOldValue);
        oldValueContainer.setParent((Component)hlayout);
        Div newValueContainer = new Div();
        newValueContainer.setSclass("yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-ed");
        if(!editorNewValue.isLocalized())
        {
            appendLabel(newValueContainer, editorNewValue.getEditorLabel(), "newValue");
        }
        newValueContainer.appendChild((Component)editorNewValue);
        newValueContainer.setParent((Component)hlayout);
        hlayout.setParent(component);
    }


    protected Editor createEditor(WidgetInstanceManager widgetInstanceManager, SavedValueEntryModel savedValueEntryModel, String modifiedAttributeName, String qualifier)
    {
        DataType dataType;
        ItemModel modifiedItem = savedValueEntryModel.getParent().getModifiedItem();
        String typeName = getTypeFacade().getType(modifiedItem);
        try
        {
            dataType = this.typeFacade.load(typeName);
        }
        catch(TypeNotFoundException exc)
        {
            throw new IllegalStateException(exc);
        }
        DataAttribute modifiedAttribute = getDataAttribute(modifiedAttributeName, dataType);
        if(modifiedAttribute == null)
        {
            return null;
        }
        Editor editor = new Editor();
        editor.setReadOnly(true);
        editor.setLocalized(modifiedAttribute.isLocalized());
        editor.setWidgetInstanceManager(widgetInstanceManager);
        String attributeLabel = getLabelService().getObjectLabel(resolveAttributePath(qualifier, "SavedValueEntry"));
        if(StringUtils.isBlank(attributeLabel))
        {
            attributeLabel = LabelUtils.getFallbackLabel(qualifier);
        }
        editor.setEditorLabel(attributeLabel);
        editor.setType(resolveEditorType(modifiedAttribute));
        editor.setOptional(!modifiedAttribute.isMandatory());
        YTestTools.modifyYTestId((Component)editor, "editor_" + qualifier);
        WidgetModel model = widgetInstanceManager.getModel();
        editor.setWritableLocales(getPermissionFacade().getWritableLocalesForInstance(modifiedItem));
        editor.setReadableLocales(getPermissionFacade().getReadableLocalesForInstance(modifiedItem));
        if(modifiedAttribute.isLocalized())
        {
            editor.addParameter("headerLabelTooltip", qualifier);
            editor.addParameter("localizedEditor.attributeDescription",
                            getLabelService().getObjectDescription(resolveAttributePath(qualifier, "SavedValueEntry")));
        }
        String referencedModelProperty = CURRENT_OBJECT + CURRENT_OBJECT;
        if(modifiedAttribute.isLocalized())
        {
            if(qualifier.equals("oldValue"))
            {
                model.put("localizedOldValue", convertLocalizedValue((Map<LanguageModel, Object>)model.getValue(referencedModelProperty, Map.class)));
                editor.setProperty("localizedOldValue");
            }
            else if(qualifier.equals("newValue"))
            {
                model.put("localizedNewValue", convertLocalizedValue((Map<LanguageModel, Object>)model.getValue(referencedModelProperty, Map.class)));
                editor.setProperty("localizedNewValue");
            }
        }
        else
        {
            editor.setProperty(referencedModelProperty);
        }
        editor.setOrdered(modifiedAttribute.isOrdered());
        editor.afterCompose();
        editor.setSclass("ye-default-editor-readonly");
        return editor;
    }


    protected DataAttribute getDataAttribute(String modifiedAttributeName, DataType dataType)
    {
        return dataType.getAttribute(modifiedAttributeName);
    }


    private Map<Locale, Object> convertLocalizedValue(Map<LanguageModel, Object> input)
    {
        if(input == null)
        {
            return null;
        }
        Map<Locale, Object> output = new HashMap<>();
        for(Map.Entry<LanguageModel, Object> entry : input.entrySet())
        {
            output.put(getCommonI18NService().getLocaleForLanguage(entry.getKey()), entry.getValue());
        }
        return output;
    }


    private void appendLabel(Div attributeContainer, String labelText, String tooltip)
    {
        Label label = new Label(labelText);
        label.setSclass("yw-editorarea-tabbox-tabpanels-tabpanel-groupbox-ed-label");
        if(StringUtils.isNotBlank(tooltip))
        {
            label.setTooltiptext(tooltip);
        }
        attributeContainer.appendChild((Component)label);
    }
}

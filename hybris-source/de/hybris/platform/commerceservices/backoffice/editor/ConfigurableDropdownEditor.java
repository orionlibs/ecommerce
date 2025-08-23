package de.hybris.platform.commerceservices.backoffice.editor;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import de.hybris.platform.commerceservices.backoffice.dropdownprovider.DropdownProvider;
import java.util.List;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

public class ConfigurableDropdownEditor implements CockpitEditorRenderer<Object>
{
    private static final Object EMPTY_OPTION = new Object();
    private static final String I18N_PREFIX = "i18n.";
    private static final String ATTR_PROVIDER = "availableValuesProvider";
    private static final String ATTR_AVAILABLE_VALUES = "availableValues";
    private static final String ATTR_PLACEHOLDER = "placeholderKey";
    private static final String EMPTY_LABEL = " ";


    public void render(Component parent, EditorContext<Object> context, EditorListener<Object> listener)
    {
        Combobox combobox = new Combobox();
        combobox.setDisabled(!context.isEditable());
        List<Object> data = Lists.newArrayList();
        String optionProviderId = ObjectUtils.toString(context.getParameter("availableValuesProvider"));
        String placeholderKey = ObjectUtils.toString(context.getParameter("placeholderKey"));
        DropdownProvider dropdownProvider = getProviderIfPossible(optionProviderId);
        if(dropdownProvider != null)
        {
            data.addAll(dropdownProvider.getAllValues());
        }
        else
        {
            String rawOptions = ObjectUtils.toString(context.getParameter("availableValues"));
            String[] optionArray = rawOptions.split(",");
            data.addAll(Lists.newArrayList((Object[])optionArray));
        }
        data.add(0, EMPTY_OPTION);
        ListModel comboModel = createComboModelWithSelection(data, context.getInitialValue(), dropdownProvider);
        combobox.setModel(comboModel);
        combobox.setItemRenderer(createComboRenderer(dropdownProvider, placeholderKey));
        combobox.addEventListener("onChange", createOnChangeHandler(listener));
        if(StringUtils.isNotBlank(placeholderKey))
        {
            combobox.setPlaceholder(Labels.getLabel(placeholderKey));
        }
        parent.appendChild((Component)combobox);
    }


    protected EventListener createOnChangeHandler(EditorListener editorListener)
    {
        return (EventListener)new Object(this, editorListener);
    }


    protected ComboitemRenderer createComboRenderer(DropdownProvider dropdownProvider, String placeholderKey)
    {
        return (ComboitemRenderer)new Object(this, placeholderKey, dropdownProvider);
    }


    protected ListModel createComboModelWithSelection(List<Object> data, Object initValue, DropdownProvider dropdownProvider)
    {
        ListModelList<Object> comboModel = new ListModelList(data);
        if(initValue != null)
        {
            List<Object> selectedObjects = Lists.newArrayList();
            if(dropdownProvider == null)
            {
                String toSelect = String.format("%s%s", new Object[] {"i18n.", initValue});
                selectedObjects.add(toSelect);
            }
            else
            {
                selectedObjects.add(initValue);
            }
            comboModel.setSelection(selectedObjects);
        }
        return (ListModel)comboModel;
    }


    protected DropdownProvider getProviderIfPossible(String optionProviderId)
    {
        return (DropdownProvider)BackofficeSpringUtil.getBean(optionProviderId);
    }
}

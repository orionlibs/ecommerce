package de.hybris.platform.adaptivesearchbackoffice.editors.configurabledropdown;

import com.google.common.base.Splitter;
import com.hybris.cockpitng.dataaccess.services.PropertyValueService;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import de.hybris.platform.adaptivesearchbackoffice.common.DataProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

public class ConfigurableDropdownEditor implements CockpitEditorRenderer<Object>
{
    protected static final String DATA_PROVIDER = "dataProvider";
    protected static final String DATA_PROVIDER_PARAMETERS = "dataProviderParameters";
    protected static final String PLACEHOLDER = "placeholderKey";
    protected static final String PROPERTY_VALUE_SERVICE_BEAN_ID = "propertyValueService";
    private static final Pattern SPEL_REGEXP = Pattern.compile("\\{((.*))\\}");


    public void render(Component parent, EditorContext<Object> context, EditorListener<Object> listener)
    {
        String placeholderKey = ObjectUtils.toString(context.getParameter("placeholderKey"));
        DataProvider dataProvider = createDataProvider(context);
        Map<String, Object> dataProviderParameters = createDataProviderParameters(context);
        Combobox combobox = new Combobox();
        combobox.setReadonly(true);
        combobox.setDisabled(!context.isEditable());
        combobox.setModel(createModel(dataProvider, dataProviderParameters, loadInitialValue(context)));
        combobox.setItemRenderer(createItemRenderer(dataProvider, dataProviderParameters));
        combobox.addEventListener("onChange", createOnChangeHandler(context, listener));
        if(StringUtils.isNotBlank(placeholderKey))
        {
            combobox.setPlaceholder(Labels.getLabel(placeholderKey));
        }
        parent.appendChild((Component)combobox);
    }


    protected DataProvider createDataProvider(EditorContext<Object> context)
    {
        String dataProvider = ObjectUtils.toString(context.getParameter("dataProvider"));
        return (DataProvider)BackofficeSpringUtil.getBean(dataProvider);
    }


    protected Map<String, Object> createDataProviderParameters(EditorContext<Object> context)
    {
        Map<String, Object> dataProviderParameters;
        String dataProviderParametersString = ObjectUtils.toString(context.getParameter("dataProviderParameters"));
        if(StringUtils.isNotBlank(dataProviderParametersString))
        {
            PropertyValueService propertyValueService = (PropertyValueService)BackofficeSpringUtil.getBean("propertyValueService");
            Map<String, String> parameters = Splitter.on(";").withKeyValueSeparator("=").split(dataProviderParametersString);
            dataProviderParameters = (Map<String, Object>)parameters.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> evaluate(context, (String)entry.getValue(), propertyValueService)));
        }
        else
        {
            dataProviderParameters = new HashMap<>();
        }
        dataProviderParameters.put("parentObject", context.getParameter("parentObject"));
        return dataProviderParameters;
    }


    protected Object evaluate(EditorContext<Object> context, String value, PropertyValueService propertyValueService)
    {
        Matcher matcher = SPEL_REGEXP.matcher(value);
        if(matcher.find())
        {
            Map<String, Object> evaluationContext = createEvaluationContext(context);
            String expression = matcher.group(1);
            return propertyValueService.readValue(evaluationContext, expression);
        }
        return value;
    }


    protected Map<String, Object> createEvaluationContext(EditorContext<Object> context)
    {
        Map<String, Object> evaluationContext = new HashMap<>();
        evaluationContext.put("parentObject", context.getParameter("parentObject"));
        return evaluationContext;
    }


    protected ListModel createModel(DataProvider dataProvider, Map<String, Object> dataProviderParameters, Object initialValue)
    {
        List<Object> data = new ArrayList();
        data.add((Object)null);
        data.addAll(dataProvider.getData(dataProviderParameters));
        ListModelList<Object> model = new ListModelList(data);
        if(initialValue != null)
        {
            Optional<Object> selectedItem = data.stream().filter(item -> Objects.equals(initialValue, dataProvider.getValue(item, dataProviderParameters))).findFirst();
            if(selectedItem.isPresent())
            {
                model.setSelection(Collections.singletonList(selectedItem.get()));
            }
        }
        return (ListModel)model;
    }


    protected ComboitemRenderer createItemRenderer(DataProvider dataProvider, Map<String, Object> dataProviderParameters)
    {
        return (item, data, index) -> {
            item.setValue(dataProvider.getValue(data, dataProviderParameters));
            item.setLabel(dataProvider.getLabel(data, dataProviderParameters));
        };
    }


    protected EventListener createOnChangeHandler(EditorContext<Object> context, EditorListener editorListener)
    {
        return event -> {
            Combobox combobox = (Combobox)event.getTarget();
            Comboitem selectedItem = combobox.getSelectedItem();
            if(selectedItem == null && CollectionUtils.isNotEmpty(combobox.getItems()))
            {
                combobox.setSelectedIndex(0);
            }
            else if(selectedItem != null)
            {
                editorListener.onValueChanged(selectedItem.getValue());
            }
        };
    }


    protected Object loadInitialValue(EditorContext<Object> context)
    {
        return context.getInitialValue();
    }
}

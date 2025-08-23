package de.hybris.platform.adaptivesearchbackoffice.editors.boostrulevalue;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.services.PropertyValueService;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.EditorRegistry;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import de.hybris.platform.adaptivesearch.AsException;
import de.hybris.platform.adaptivesearch.data.AsIndexPropertyData;
import de.hybris.platform.adaptivesearch.model.AsBoostRuleModel;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProviderFactory;
import de.hybris.platform.adaptivesearch.util.ObjectConverter;
import de.hybris.platform.adaptivesearchbackoffice.editors.EditorRuntimeException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;

public class BoostRuleValueEditor extends AbstractCockpitEditorRenderer<Object>
{
    private static final Logger LOG = Logger.getLogger(BoostRuleValueEditor.class);
    protected static final String INDEX_TYPE_PARAM = "indexType";
    protected static final String PROPERTY_VALUE_SERVICE_BEAN_ID = "propertyValueService";
    protected static final Pattern SPEL_REGEXP = Pattern.compile("\\{((.*))\\}");
    private EditorListener<Object> listener;
    @Resource
    private AsSearchProviderFactory asSearchProviderFactory;
    @Resource
    private EditorRegistry editorRegistry;


    public void render(Component parent, EditorContext<Object> context, EditorListener<Object> listener)
    {
        Object value;
        Validate.notNull("All parameters are mandatory", new Object[] {parent, context, listener});
        setListener(listener);
        Object parentObject = context.getParameter("parentObject");
        if(!(parentObject instanceof AsBoostRuleModel))
        {
            return;
        }
        AsBoostRuleModel boostRule = (AsBoostRuleModel)parentObject;
        String indexProperty = boostRule.getIndexProperty();
        WidgetInstanceManager widgetInstanceManager = getWidgetInstanceManager(context);
        String indexType = getIndexType(context, boostRule);
        AsSearchProvider searchProvider = this.asSearchProviderFactory.getSearchProvider();
        Optional<AsIndexPropertyData> indexPropertyData = searchProvider.getIndexPropertyForCode(indexType, indexProperty);
        if(!indexPropertyData.isPresent())
        {
            throw new EditorRuntimeException("Index property not found: " + indexProperty);
        }
        Class<?> indexPropertyType = ((AsIndexPropertyData)indexPropertyData.get()).getType();
        try
        {
            value = ObjectConverter.convert(context.getInitialValue(), indexPropertyType);
        }
        catch(AsException e)
        {
            LOG.error(e);
            value = null;
        }
        String defaultEditorCode = this.editorRegistry.getDefaultEditorCode(indexPropertyType.getName());
        Editor editor = new Editor();
        editor.setParent(parent);
        editor.setWidgetInstanceManager(widgetInstanceManager);
        editor.setInitialValue(value);
        editor.setDefaultEditor(defaultEditorCode);
        editor.setType(indexPropertyType.getName());
        editor.afterCompose();
        editor.addEventListener("onValueChanged", event -> updateValue(event.getData(), boostRule));
    }


    protected void updateValue(Object newValue, AsBoostRuleModel boostRule)
    {
        String value;
        try
        {
            value = (String)ObjectConverter.convert(newValue, String.class);
        }
        catch(AsException e)
        {
            LOG.error(e);
            value = null;
        }
        this.listener.onValueChanged(value);
    }


    protected String getIndexType(EditorContext<Object> context, AsBoostRuleModel boostRule)
    {
        String indexType = (String)context.getParameter("indexType");
        PropertyValueService propertyValueService = (PropertyValueService)BackofficeSpringUtil.getBean("propertyValueService");
        Matcher matcher = SPEL_REGEXP.matcher(indexType);
        if(matcher.find())
        {
            Map<String, Object> evaluationContext = new HashMap<>();
            evaluationContext.put("parentObject", boostRule);
            String expression = matcher.group(1);
            return (String)propertyValueService.readValue(evaluationContext, expression);
        }
        return indexType;
    }


    protected WidgetInstanceManager getWidgetInstanceManager(EditorContext<Object> context)
    {
        return (WidgetInstanceManager)context.getParameter("wim");
    }


    public EditorListener<Object> getListener()
    {
        return this.listener;
    }


    public void setListener(EditorListener<Object> listener)
    {
        this.listener = listener;
    }
}

package de.hybris.platform.adaptivesearchbackoffice.editors.extendedconfigurabledropdown;

import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import de.hybris.platform.adaptivesearchbackoffice.common.ReplaceableDataHandler;
import de.hybris.platform.adaptivesearchbackoffice.editors.EditorRuntimeException;
import de.hybris.platform.adaptivesearchbackoffice.editors.configurabledropdown.ConfigurableDropdownEditor;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;

public class ExtendedConfigurableDropdownEditor extends ConfigurableDropdownEditor
{
    protected static final String REPLACEABLE_DATA_HANDLER = "replaceableDataHandler";
    private ReplaceableDataHandler<Object> replaceableDataHandler;


    public void render(Component parent, EditorContext<Object> context, EditorListener<Object> listener)
    {
        setReplaceableDataHandler(createReplaceableDataHandler(context));
        context.setEditable(!getReplaceableDataHandler().isReplaceable(context));
        super.render(parent, context, listener);
    }


    protected Object loadInitialValue(EditorContext<Object> context)
    {
        return getReplaceableDataHandler().getValue(context);
    }


    protected ReplaceableDataHandler<Object> createReplaceableDataHandler(EditorContext<Object> context)
    {
        String dataProviderId = ObjectUtils.toString(context.getParameter("replaceableDataHandler"));
        if(StringUtils.isBlank(dataProviderId))
        {
            throw new EditorRuntimeException(String.format("%s parameter has not been configured for %s editor (%s)", new Object[] {"replaceableDataHandler", context.getDefinition().getName(), context.getEditorLabel()}));
        }
        return (ReplaceableDataHandler<Object>)BackofficeSpringUtil.getBean(dataProviderId);
    }


    protected ReplaceableDataHandler<Object> getReplaceableDataHandler()
    {
        return this.replaceableDataHandler;
    }


    @Required
    protected void setReplaceableDataHandler(ReplaceableDataHandler<Object> replaceableDataHandler)
    {
        this.replaceableDataHandler = replaceableDataHandler;
    }
}

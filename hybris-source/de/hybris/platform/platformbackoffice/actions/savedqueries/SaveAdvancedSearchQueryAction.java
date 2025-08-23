package de.hybris.platform.platformbackoffice.actions.savedqueries;

import com.google.common.collect.Maps;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.widgets.configurableflow.ConfigurableFlowContextParameterNames;
import de.hybris.platform.platformbackoffice.widgets.savedqueries.SaveAdvancedSearchQueryForm;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.Messagebox;

public class SaveAdvancedSearchQueryAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<AdvancedSearchData, Object>
{
    public static final String SOCKET_CTX_MAP = "contextMap";
    public static final String CTX_PROPERTY_ADV_SEARCH_DATA = "advancedSearchData";
    private static final Logger LOGGER = LoggerFactory.getLogger(SaveAdvancedSearchQueryAction.class);
    private static final String LABEL_SAVE_QUERY_INVALID = "savedqueries.conditions.invalid";
    private static final String LABEL_SAVE_QUERY_INVALID_QUALIFIER = "savedqueries.conditions.invalid.qualifiers";
    private static final String LABEL_SAVE_QUERY_TITLE = "savedqueries.title.label";
    @Resource
    private SaveQueryActionChecker saveQueryActionChecker;


    public ActionResult<Object> perform(ActionContext<AdvancedSearchData> ctx)
    {
        Map<String, Object> outputCtx = Maps.newHashMap();
        if(validateSaveQueryInput(ctx))
        {
            outputCtx.put("advancedSearchData", ctx.getData());
            outputCtx.put(ConfigurableFlowContextParameterNames.TYPE_CODE.getName(), SaveAdvancedSearchQueryForm.class.getName());
            sendOutput("contextMap", outputCtx);
        }
        return new ActionResult("success");
    }


    private boolean validateSaveQueryInput(ActionContext<AdvancedSearchData> ctx)
    {
        try
        {
            Collection<SaveQueryInvalidCondition> invalidConditions = this.saveQueryActionChecker.check((AdvancedSearchData)ctx.getData());
            if(CollectionUtils.isNotEmpty(invalidConditions))
            {
                String invalidQualifiers = invalidConditions.stream().map(SaveQueryInvalidCondition::getQualifier).collect(Collectors.joining(", ", "[", "]"));
                displayWarning(ctx.getLabel("savedqueries.conditions.invalid.qualifiers", new Object[] {invalidQualifiers}), ctx);
                return false;
            }
        }
        catch(TypeNotFoundException e)
        {
            LOGGER.error("Cannot save query. Reason: {}", e.getMessage());
            displayWarning(ctx.getLabel("savedqueries.conditions.invalid"), ctx);
            return false;
        }
        return true;
    }


    protected void displayWarning(String message, ActionContext<AdvancedSearchData> ctx)
    {
        Messagebox.show(message, ctx.getLabel("savedqueries.title.label"), 1, "z-messagebox-icon z-messagebox-exclamation");
    }


    public boolean canPerform(ActionContext<AdvancedSearchData> ctx)
    {
        return true;
    }


    public boolean needsConfirmation(ActionContext<AdvancedSearchData> ctx)
    {
        return false;
    }


    public String getConfirmationMessage(ActionContext<AdvancedSearchData> ctx)
    {
        return null;
    }
}

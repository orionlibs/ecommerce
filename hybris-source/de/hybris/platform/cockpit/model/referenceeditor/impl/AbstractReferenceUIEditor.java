package de.hybris.platform.cockpit.model.referenceeditor.impl;

import de.hybris.platform.cockpit.model.editor.ReferenceUIEditor;
import de.hybris.platform.cockpit.model.editor.impl.AbstractUIEditor;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Executions;

public abstract class AbstractReferenceUIEditor extends AbstractUIEditor implements ReferenceUIEditor
{
    private static final String DEFAULT_MAX_AUTOCOMPLETE_RESULT_SIZE = "default.max.autocomplete.result.size";
    private static final String MAX_AUTOCOMPLETE_SEARCH_RESULT = "maxAutocompleteSearchResult";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractReferenceUIEditor.class);
    private Boolean allowCreate = Boolean.TRUE;


    public Boolean isAllowCreate()
    {
        return this.allowCreate;
    }


    public void setAllowCreate(Boolean allowCreate)
    {
        this.allowCreate = allowCreate;
    }


    public String getEditorType()
    {
        return "REFERENCE";
    }


    protected Integer findMaxAutocompleteSearchResults(Map<String, ? extends Object> parameters)
    {
        Object object = parameters.get("maxAutocompleteSearchResult");
        String maxAutocompleteConfig = null;
        if(object instanceof String && StringUtils.isNotEmpty((String)object))
        {
            maxAutocompleteConfig = (String)object;
        }
        else
        {
            maxAutocompleteConfig = UITools.getCockpitParameter("default.max.autocomplete.result.size", Executions.getCurrent());
        }
        if(StringUtils.isNotBlank(maxAutocompleteConfig))
        {
            try
            {
                int res = Integer.parseInt(maxAutocompleteConfig);
                if(res > 0)
                {
                    return Integer.valueOf(res);
                }
            }
            catch(Exception e)
            {
                LOG.warn("could not parse autocomplete search result limit from value: [" + maxAutocompleteConfig + "]. Using default value instead.");
            }
        }
        return null;
    }


    protected Optional<Boolean> getBooleanParameter(String paramKey, Map<String, ? extends Object> parameters)
    {
        Object param = parameters.get(paramKey);
        if(param instanceof Boolean)
        {
            return Optional.of((Boolean)param);
        }
        if(param instanceof String)
        {
            return Optional.of(Boolean.valueOf((String)param));
        }
        return Optional.empty();
    }
}

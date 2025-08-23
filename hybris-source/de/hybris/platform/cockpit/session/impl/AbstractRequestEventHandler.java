package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.session.RequestEventHandler;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractRequestEventHandler implements RequestEventHandler
{
    protected static final String DELIMITER = "-";
    private String prefix = null;


    @Required
    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }


    public String getPrefix()
    {
        return this.prefix;
    }


    protected String getParameter(Map<String, String[]> params, String rawKey)
    {
        String param = null;
        String realKey = getPrefix() + "-" + getPrefix();
        if(params != null && params.containsKey(realKey) && params.get(realKey) != null)
        {
            String[] values = params.get(realKey);
            if(values.length > 0)
            {
                param = values[0];
            }
        }
        return (param == null) ? "" : param;
    }
}

package de.hybris.platform.servicelayer.event.events;

import de.hybris.platform.util.JspContext;
import java.io.Serializable;
import java.util.Map;

public class AfterInitializationStartEvent extends AbstractEvent
{
    private Map<String, String> params;
    private JspContext ctx;


    public AfterInitializationStartEvent()
    {
    }


    public AfterInitializationStartEvent(Serializable source)
    {
        super(source);
    }


    public void setParams(Map<String, String> params)
    {
        this.params = params;
    }


    public Map<String, String> getParams()
    {
        return this.params;
    }


    public void setCtx(JspContext ctx)
    {
        this.ctx = ctx;
    }


    public JspContext getCtx()
    {
        return this.ctx;
    }
}

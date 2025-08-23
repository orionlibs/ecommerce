package de.hybris.platform.deeplink.pojo;

import de.hybris.platform.deeplink.model.rules.DeeplinkUrlModel;
import java.io.Serializable;

public class DeeplinkUrlInfo implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Object contextObject;
    private DeeplinkUrlModel deeplinkUrl;


    public void setContextObject(Object contextObject)
    {
        this.contextObject = contextObject;
    }


    public Object getContextObject()
    {
        return this.contextObject;
    }


    public void setDeeplinkUrl(DeeplinkUrlModel deeplinkUrl)
    {
        this.deeplinkUrl = deeplinkUrl;
    }


    public DeeplinkUrlModel getDeeplinkUrl()
    {
        return this.deeplinkUrl;
    }
}

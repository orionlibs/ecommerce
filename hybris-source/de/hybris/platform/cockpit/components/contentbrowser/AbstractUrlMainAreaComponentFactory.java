package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.session.impl.UrlMainAreaComponentFactory;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;

public abstract class AbstractUrlMainAreaComponentFactory implements UrlMainAreaComponentFactory
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractUrlMainAreaComponentFactory.class);
    protected static final String UID_URL_MAPPING_BEAN_ID = "uid2UrlMap";
    private String uid = null;
    private String urlMappingBeanId = null;


    public String getUrlMappingBeanId()
    {
        if(StringUtils.isBlank(this.urlMappingBeanId))
        {
            LOG.info("No URL mapping bean specified. Using default one 'uid2UrlMap'.");
            this.urlMappingBeanId = "uid2UrlMap";
        }
        return this.urlMappingBeanId;
    }


    public void setUrlMappingBeanId(String mappingBeanId)
    {
        this.urlMappingBeanId = mappingBeanId;
    }


    public String getUid()
    {
        return this.uid;
    }


    public void setUid(String uid)
    {
        this.uid = uid;
    }


    public String getViewModeID()
    {
        return this.uid;
    }


    public String getButtonLabel()
    {
        return Labels.getLabel(this.uid, getConfiguredUrl());
    }


    public String getConfiguredUrl()
    {
        String retUrl = null;
        if(StringUtils.isNotBlank(this.uid))
        {
            try
            {
                Map urlMappings = (Map)SpringUtil.getBean("uid2UrlMap");
                if(urlMappings != null)
                {
                    Object urlKey = urlMappings.get(this.uid);
                    if(urlKey != null && StringUtils.isNotBlank(urlKey.toString()))
                    {
                        retUrl = urlKey.toString();
                    }
                }
            }
            catch(Exception e)
            {
                LOG.warn("Could not get mapped URL for UID. Reason: Could not resolve url mapping bean.", e);
            }
        }
        return retUrl;
    }
}

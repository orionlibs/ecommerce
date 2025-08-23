package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.contentbrowser.MainAreaComponentFactory;

public interface UrlMainAreaComponentFactory extends MainAreaComponentFactory
{
    void setUrlMappingBeanId(String paramString);


    String getUrlMappingBeanId();


    void setUid(String paramString);


    String getUid();


    String getConfiguredUrl();
}

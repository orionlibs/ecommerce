/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.modules;

import java.util.Collection;

/**
 * DTO used to transfer module information over webservice
 */
public class DefaultModuleInfo implements ModuleInfo
{
    private String id;
    private String widgetsExtension;
    private String widgetPackage;
    private String locationUrl;
    private String iconUrl;
    private String applicationContextUri;
    private Collection<String> parentModulesLocationUrls;


    @Override
    public String getWidgetsPackage()
    {
        return widgetPackage;
    }


    public void setWidgetsPackage(final String widgetPackage)
    {
        this.widgetPackage = widgetPackage;
    }


    @Override
    public String getWidgetsExtension()
    {
        return widgetsExtension;
    }


    public void setWidgetsExtension(final String widgetsExtension)
    {
        this.widgetsExtension = widgetsExtension;
    }


    @Override
    public String getId()
    {
        return id;
    }


    public void setId(final String id)
    {
        this.id = id;
    }


    @Override
    public String getLocationUrl()
    {
        return locationUrl;
    }


    public void setLocationUrl(final String locationUrl)
    {
        this.locationUrl = locationUrl;
    }


    @Override
    public String getIconUrl()
    {
        return iconUrl;
    }


    public void setIconUrl(final String iconUrl)
    {
        this.iconUrl = iconUrl;
    }


    @Override
    public String getApplicationContextUri()
    {
        return applicationContextUri;
    }


    public void setApplicationContextUri(final String applicationContextUri)
    {
        this.applicationContextUri = applicationContextUri;
    }


    @Override
    public Collection<String> getParentModulesLocationUrls()
    {
        return parentModulesLocationUrls;
    }


    public void setParentModulesLocationUrls(final Collection<String> parentModulesLocationUrls)
    {
        this.parentModulesLocationUrls = parentModulesLocationUrls;
    }
}

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.modules.server.ws.jaxb;

import com.hybris.cockpitng.core.modules.DefaultModuleInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * DTO used to transfer module information over webservice
 */
@XmlRootElement
public class CockpitModuleInfo extends DefaultModuleInfo
{
    @Override
    @XmlElement(name = "package")
    public String getWidgetsPackage()
    {
        return super.getWidgetsPackage();
    }


    @Override
    @XmlElement(name = "widget-extension")
    public String getWidgetsExtension()
    {
        return super.getWidgetsExtension();
    }


    @Override
    @XmlElement(name = "module-id")
    public String getId()
    {
        return super.getId();
    }


    @Override
    @XmlElement(name = "icon-url")
    public String getIconUrl()
    {
        return super.getIconUrl();
    }


    @Override
    @XmlTransient
    public String getApplicationContextUri()
    {
        return super.getApplicationContextUri();
    }
}

/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.occ.controllers;

import de.hybris.platform.cpq.productconfig.facades.AccessControlFacade;
import de.hybris.platform.cpq.productconfig.facades.data.AccessControlData;
import de.hybris.platform.cpq.productconfig.occ.ConfigurationEngineAccessWsDTO;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.util.YSanitizer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Web Services Controller to expose the functionality of the
 * {@link de.hybris.platform.cpq.productconfig.facades.AccessControlFacade}.
 */
@Controller
@Api(tags = "Product Configurator CPQ")
@RequestMapping(value = CpqproductconfigoccControllerConstants.BASE_SITE_USER_ID_PART)
public class ProductConfiguratorCPQAccessController
{
    private static final Logger LOG = Logger.getLogger(ProductConfiguratorCPQAccessController.class);
    @Resource(name = "cpqProductConfigAccessControlFacade")
    protected AccessControlFacade accessControlFacade;
    @Resource(name = "dataMapper")
    protected DataMapper dataMapper;


    @RequestMapping(value = "/access/"
                    + CpqproductconfigoccControllerConstants.CONFIGURATOR_TYPE_FOR_OCC_EXPOSURE, method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(nickname = "getAccessToConfigurationEngine", value = "Gets access data for using the configuration engine")
    @ApiBaseSiteIdAndUserIdParam
    public ConfigurationEngineAccessWsDTO getAccessToConfigurationEngine()
    {
        final AccessControlData accessControlData = getAccessControlFacade().performAccessControlForClient();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("getAccessToConfigurationEngine: '" + logParam("ownerId", accessControlData.getOwnerId()) + "'");
        }
        return getDataMapper().map(accessControlData, ConfigurationEngineAccessWsDTO.class);
    }


    protected static String logParam(final String paramName, final String paramValue)
    {
        return paramName + " = " + paramValue;
    }


    protected static String sanitize(final String input)
    {
        return YSanitizer.sanitize(input);
    }


    protected AccessControlFacade getAccessControlFacade()
    {
        return accessControlFacade;
    }


    protected DataMapper getDataMapper()
    {
        return dataMapper;
    }
}

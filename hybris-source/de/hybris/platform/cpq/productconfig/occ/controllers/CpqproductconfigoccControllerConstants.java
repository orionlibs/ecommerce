/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.occ.controllers;

/**
 * Constants for OCC controllers
 */
public class CpqproductconfigoccControllerConstants
{
    static final String CONFIGURATOR_TYPE_FOR_OCC_EXPOSURE = "cpqconfigurator";
    static final String BASE_SITE_ID_PART = "/{baseSiteId}";
    static final String USER_ID_PART = "/users/{userId}";
    static final String BASE_SITE_USER_ID_PART = BASE_SITE_ID_PART + USER_ID_PART;


    private CpqproductconfigoccControllerConstants()
    {
    }
}

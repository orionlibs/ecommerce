/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.cpq.productconfig.services.client;

/**
 * HTTP related Constants of {@link CpqClient}
 */
public class CpqClientConstants
{
    public static final String HTTP_PRODUCE_APPL_JSON = "application/json";
    public static final String HTTP_HEADER_CPQ_SESSION_ID = "x-cpq-session-id";
    public static final String HTTP_HEADER_COOKIELESS_SESSION = "x-cpq-disable-cookies";
    public static final String HTTP_HEADER_AUTHORIZATION = "Authorization";
    public static final String HTTP_PARAM_CONFIG_ID = "configurationId";
    public static final String HTTP_PARAM_OWNER_ID = "ownerId";
    public static final int HTTP_STATUS_NOT_FOUND = 404;
    public static final int HTTP_STATUS_NO_CONTENT = 204;
    public static final int HTTP_STATUS_CREATED = 201;
    public static final int HTTP_STATUS_OK = 200;
    public static final String NO_COOKIES = "";
    public static final String NO_CONTENT_TYPE_PROVIDED = "NO_CONTENT";


    private CpqClientConstants()
    {
        //empty to avoid instantiating this constant class
    }
}

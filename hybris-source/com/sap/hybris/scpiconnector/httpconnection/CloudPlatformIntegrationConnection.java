/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.hybris.scpiconnector.httpconnection;

import com.sap.hybris.scpiconnector.data.ResponseData;
import java.io.IOException;

/**
 *
 */
public interface CloudPlatformIntegrationConnection
{
    ResponseData sendPost(final String iflowKey, final Object body) throws IOException;
}

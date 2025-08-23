/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package com.hybris.datahub.core.services;

import com.hybris.datahub.core.dto.ItemImportTaskData;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.impex.ImpExResource;

/**
 * A service for creation of ImpEx resources
 */
public interface ImpExResourceFactory
{
    /**
     * Creates an import resource for the specified import context.
     *
     * @param ctx context of the import task, which contains content to import and any information relevant to the import
     * process.
     * @return a resource to load into the system.
     * @throws ImpExException if the context does not contain a valid ImpEx script.
     */
    ImpExResource createResource(ItemImportTaskData ctx) throws ImpExException;
}

/**
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.dao;

import com.hybris.merchandising.model.MerchSynchronizationModel;
import java.util.Optional;

/**
 * The {@link MerchSynchronizationModel} DAO.
 */
public interface MerchSynchronizationDao
{
    /**
     * Finds merch synchronization model by operation identifier
     *
     * @param operationId operation identifier
     * @return optional of merch synchronization model
     */
    Optional<MerchSynchronizationModel> findByOperationId(String operationId);
}

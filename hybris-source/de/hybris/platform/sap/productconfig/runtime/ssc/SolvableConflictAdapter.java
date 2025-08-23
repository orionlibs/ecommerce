/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.ssc;

import com.sap.custdev.projects.fbs.slc.cfg.IConfigSession;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.ssc.impl.SolvableConflictAdapterImpl;

/**
 * Reads conflicts from SSC and adapts them to our model representation of conflicts
 */
public interface SolvableConflictAdapter
{
    /**
     * Transfers the conflicts from SSC representation to model representation
     *
     * @param configSession
     * @param configId
     *           ID of desired configuration in configSession
     * @param configModel
     */
    void transferSolvableConflicts(IConfigSession configSession, String configId, ConfigModel configModel);


    /**
     * Retrieves the assumptionId for a cstic which is to be retracted
     *
     * @param csticname
     * @param configModel
     * @return Assumption ID
     * @deprecated since 21.08 - use {@link SolvableConflictAdapterImpl#getAssumptionId(CsticModel, ConfigModel)} instead
     */
    @Deprecated(since = "2108", forRemoval = true)
    String getAssumptionId(String csticname, ConfigModel configModel);


    /**
     * Retrieves the assumptionId for a cstic and value which is to be retracted
     *
     * @param csticModel
     * @param configModel
     * @return Assumption ID
     */
    default String getAssumptionId(final CsticModel csticModel, final ConfigModel configModel)
    {
        return getAssumptionId(csticModel.getName(), configModel);
    }
}

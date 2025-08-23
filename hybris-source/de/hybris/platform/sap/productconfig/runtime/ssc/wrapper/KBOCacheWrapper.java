/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.ssc.wrapper;

import com.sap.custdev.projects.fbs.slc.kbo.util.KBOCache;
import com.sap.sce.kbrt.kb_descriptor;
import com.sap.sxe.sys.SAPDate;

/*
 * Wrapper class for SSC API KBOCache as it is static and cannot be mocked in unit tests
 */
public class KBOCacheWrapper
{
    /**
     *
     * @param kbId
     * @param kbLogsys
     * @param kbName
     * @param kbVersion
     * @param kbDate
     * @param kbProfileName
     * @param kbBuild
     * @param productId
     * @param productType
     * @param sessionID
     * @return
     */
    public kb_descriptor get_kb_Desc_from_cache(final Integer kbId, final String kbLogsys, final String kbName,
                    final String kbVersion, final SAPDate kbDate, final String kbProfileName, final String kbBuild, final String productId,
                    final String productType, final String sessionID)
    {
        return KBOCache.get_kb_Desc_from_cache(kbId, kbLogsys, kbName, kbVersion, kbDate, kbProfileName, kbBuild, productId,
                        productType, sessionID);
    }
}

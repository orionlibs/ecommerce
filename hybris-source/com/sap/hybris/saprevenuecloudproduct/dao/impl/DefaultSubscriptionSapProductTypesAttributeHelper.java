/*
 * [y] hybris Platform
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.sap.hybris.saprevenuecloudproduct.dao.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.sapmodel.daos.SapProductTypesAttributeHelper;
import de.hybris.platform.sap.sapmodel.enums.SAPProductType;
import java.util.HashSet;
import java.util.Set;

public class DefaultSubscriptionSapProductTypesAttributeHelper implements SapProductTypesAttributeHelper
{
    @Override
    public Set<SAPProductType> getSapProductTypes(ProductModel product)
    {
        Set<SAPProductType> result = new HashSet<>();
        if(product.getSubscriptionCode() != null)
        {
            result.add(SAPProductType.SUBSCRIPTION);
        }
        return result;
    }
}
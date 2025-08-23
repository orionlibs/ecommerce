/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.integration.service.impl;

import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.sap.productconfig.services.intf.ClassificationAttributeDescriptionAccess;

/**
 * Implementation of {@link ClassificationAttributeDescriptionAccess} which will be used when SAP Integration is present.
 */
public class ClassificationAttributeDescriptionAccessImpl implements ClassificationAttributeDescriptionAccess
{
    @Override
    public String getDescription(ClassificationAttributeModel classificationAttribute)
    {
        return classificationAttribute == null ? null : classificationAttribute.getDescription();
    }
}

/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapmodel.daos;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.sapmodel.enums.SAPProductType;
import java.util.Set;

/**
 * Interface to provide access to module specific determination of SAP product types,
 * abstracting internal attributes of the module.
 */
public interface SapProductTypesAttributeHelper
{
    /**
     * Get Set of ProductTypes for a given product
     * @param product the ProductModel
     * @return Returns Set<SAPProductType> set of types determined for a product by a module
     */
    public Set<SAPProductType> getSapProductTypes(ProductModel product);
}

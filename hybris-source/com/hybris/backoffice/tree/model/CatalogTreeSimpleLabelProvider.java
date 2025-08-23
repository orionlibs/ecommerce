/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.tree.model;

import java.util.Optional;

public interface CatalogTreeSimpleLabelProvider
{
    Optional<String> getNodeLabel(final Object parentNodeData, final Object nodeData);
}

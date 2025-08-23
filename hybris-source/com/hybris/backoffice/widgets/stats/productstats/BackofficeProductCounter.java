/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.stats.productstats;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import javax.annotation.Nonnull;

public interface BackofficeProductCounter
{
    long countProducts();


    long countProducts(@Nonnull final ArticleApprovalStatus status);
}

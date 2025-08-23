/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.formatters;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import java.math.BigDecimal;
import java.util.Locale;

public interface AmountFormatter
{
    public String formatAmount(final BigDecimal value, final CurrencyModel currency, final Locale locale);
}

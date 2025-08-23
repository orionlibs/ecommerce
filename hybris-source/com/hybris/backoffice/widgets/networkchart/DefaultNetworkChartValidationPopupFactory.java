/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.networkchart;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import java.util.Optional;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

/**
 * Default, empty popup factory. It will not present any violations.
 */
public class DefaultNetworkChartValidationPopupFactory implements NetworkChartValidationPopupFactory
{
    @Override
    public Optional<Window> createValidationPopup(final NetworkChartContext context, final Button saveButton,
                    final Runnable onConfirm)
    {
        return Optional.empty();
    }
}

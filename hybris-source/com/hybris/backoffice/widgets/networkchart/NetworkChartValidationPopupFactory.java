/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.networkchart;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import java.util.Optional;
import org.zkoss.zul.Button;
import org.zkoss.zul.Window;

/**
 * Creates Validation Pop Up for Network Chart component
 */
public interface NetworkChartValidationPopupFactory
{
    /**
     * @param context
     *           network chart context
     * @param saveButton
     *           save button
     * @param onConfirm
     *           action to be executed when confirm button is clicked
     * @return optional popup window - should be present when violations are found
     */
    Optional<Window> createValidationPopup(NetworkChartContext context, Button saveButton, Runnable onConfirm);
}

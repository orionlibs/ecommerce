/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.validation;

import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Vlayout;

/**
 *
 *
 */
public class DefaultValidationResultsListitemRenderer implements ListitemRenderer<ValidationInfo>
{
    private static final String SCLASS_RESULTS_CELL = "ye-validation-results-list-cell";
    private static final String SCLASS_RESULT_MESSAGE = "ye-validation-results-list-message";
    private static final String SCLASS_RESULT_ATTRIBUTE = "ye-validation-results-list-attribute";
    private final ValidatableContainer container;
    private final ValidationRenderer validationRenderer;


    public DefaultValidationResultsListitemRenderer(final ValidatableContainer container, final ValidationRenderer validationRenderer)
    {
        this.container = container;
        this.validationRenderer = validationRenderer;
    }


    @Override
    public void render(final Listitem item, final ValidationInfo validationInfo, final int i) throws Exception
    {
        final Listcell cell = new Listcell();
        UITools.modifySClass(cell, SCLASS_RESULTS_CELL, true);
        UITools.modifySClass(cell, validationRenderer.getSeverityStyleClass(validationInfo.getValidationSeverity()), true);
        item.appendChild(cell);
        final Object object = container.getCurrentObject(validationInfo.getInvalidPropertyPath());
        final String path = container.getCurrentObjectPath(validationInfo.getInvalidPropertyPath());
        final String attributeDescription;
        if(object != null && StringUtils.isNotBlank(path))
        {
            attributeDescription = validationRenderer.getLabel(object, path);
        }
        else
        {
            attributeDescription = path;
        }
        final Vlayout cellLayout = new Vlayout();
        cellLayout.setSpacing("auto");
        cellLayout.setParent(cell);
        final Label attrLabel = new Label(attributeDescription);
        UITools.modifySClass(attrLabel, SCLASS_RESULT_ATTRIBUTE, true);
        cellLayout.appendChild(attrLabel);
        final Label messageLabel = new Label(validationInfo.getValidationMessage());
        UITools.modifySClass(messageLabel, SCLASS_RESULT_MESSAGE, true);
        cellLayout.appendChild(messageLabel);
    }
}

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.bulkedit.renderer;

import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zhtml.Li;
import org.zkoss.zul.Label;

public class DefaultBulkEditValidationDetailsComponentFactory implements BulkEditValidationDetailsComponentFactory<Li>
{
    public static final String YW_BULKEDIT_VALIDATION_RESULT_DETAILS_CELL = "yw-bulkedit-validation-result-row-details-cell";
    public static final String YW_BULKEDIT_VALIDATION_RESULT_ATTRIBUTE_LABEL = YW_BULKEDIT_VALIDATION_RESULT_DETAILS_CELL + "-column-header-error";
    private LabelService labelService;


    @Override
    public Li createValidationDetails(final ValidationInfo validationMessage)
    {
        final Li li = new Li();
        li.setSclass(YW_BULKEDIT_VALIDATION_RESULT_DETAILS_CELL);
        final Label attributeLabel = new Label(getAttributeLabel(validationMessage));
        attributeLabel.setSclass(YW_BULKEDIT_VALIDATION_RESULT_ATTRIBUTE_LABEL);
        li.appendChild(attributeLabel);
        final Label validationMessageLabel = new Label(getMessageValue(validationMessage));
        li.appendChild(validationMessageLabel);
        return li;
    }


    protected String getAttributeLabel(final ValidationInfo validationInfo)
    {
        final String notLocalizedPath = ObjectValuePath.getNotLocalizedPath(validationInfo.getInvalidPropertyPath());
        final String attributeName = labelService.getObjectLabel(notLocalizedPath);
        final String locale = ObjectValuePath.getLocaleFromPath(validationInfo.getInvalidPropertyPath());
        final String attributeLabel = locale != null ? String.format("%s[%s]", attributeName, locale) : attributeName;
        return String.format("[%s]: ", attributeLabel);
    }


    protected String getMessageValue(final ValidationInfo validationMessage)
    {
        return validationMessage.getValidationMessage();
    }


    @Required
    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }
}

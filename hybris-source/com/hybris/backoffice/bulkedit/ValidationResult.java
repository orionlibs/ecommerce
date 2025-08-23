/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.bulkedit;

import com.hybris.cockpitng.validation.model.ValidationInfo;
import java.util.List;

public class ValidationResult
{
    private Object item;
    private List<ValidationInfo> validationInfos;


    public ValidationResult(final Object item, final List<ValidationInfo> validationInfos)
    {
        this.item = item;
        this.validationInfos = validationInfos;
    }


    public Object getItem()
    {
        return item;
    }


    public void setItem(final Object item)
    {
        this.item = item;
    }


    public List<ValidationInfo> getValidationInfos()
    {
        return validationInfos;
    }


    public void setValidationInfos(final List<ValidationInfo> validationInfos)
    {
        this.validationInfos = validationInfos;
    }
}

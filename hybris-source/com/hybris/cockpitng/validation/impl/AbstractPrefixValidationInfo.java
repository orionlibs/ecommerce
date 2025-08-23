/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.validation.impl;

import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationInfoWrapper;
import com.hybris.cockpitng.validation.model.ValidationSeverity;

/**
 * A wrapper class for {@link ValidationInfo} that changes its path by adding/removing a prefix.
 */
public abstract class AbstractPrefixValidationInfo extends DefaultValidationInfo implements ValidationInfoWrapper
{
    private final String prefix;
    private final ValidationInfo info;


    public AbstractPrefixValidationInfo(final String prefix, final ValidationInfo info)
    {
        this.prefix = prefix;
        this.info = info;
    }


    @Override
    public boolean isConfirmed()
    {
        return info.isConfirmed();
    }


    @Override
    public void setConfirmed(final boolean confirmed)
    {
        info.setConfirmed(confirmed);
    }


    @Override
    public ValidationSeverity getValidationSeverity()
    {
        return info.getValidationSeverity();
    }


    @Override
    public String getValidationMessage()
    {
        return info.getValidationMessage();
    }


    @Override
    public Object getInvalidValue()
    {
        return info.getInvalidValue();
    }


    public String getPrefix()
    {
        return prefix;
    }


    public ValidationInfo getInfo()
    {
        return info;
    }
}
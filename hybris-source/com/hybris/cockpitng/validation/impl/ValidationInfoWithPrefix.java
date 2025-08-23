/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.validation.impl;

import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import org.apache.commons.lang3.StringUtils;

/**
 * A wrapper class for {@link ValidationInfo} that changes its path by adding a prefix.
 */
public class ValidationInfoWithPrefix extends AbstractPrefixValidationInfo
{
    public ValidationInfoWithPrefix(final String prefix, final ValidationInfo info)
    {
        super(prefix, info);
    }


    @Override
    public String getInvalidPropertyPath()
    {
        if(StringUtils.isEmpty(getInfo().getInvalidPropertyPath()))
        {
            return getPrefix();
        }
        else
        {
            return ObjectValuePath.getPath(getPrefix(), getInfo().getInvalidPropertyPath());
        }
    }
}

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.validation.impl;

import com.hybris.cockpitng.validation.model.ValidationInfo;
import org.apache.commons.lang3.StringUtils;

/**
 * A wrapper class for {@link ValidationInfo} that changes its path by removing a prefix.
 */
public class ValidationInfoWithoutPrefix extends AbstractPrefixValidationInfo
{
    public ValidationInfoWithoutPrefix(final String prefix, final ValidationInfo info)
    {
        super(prefix, info);
    }


    @Override
    public String getInvalidPropertyPath()
    {
        String result = getInfo().getInvalidPropertyPath();
        if(result.startsWith(getPrefix().concat(".")))
        {
            result = result.substring(getPrefix().length() + 1);
        }
        else if(StringUtils.equals(result, getPrefix()))
        {
            result = StringUtils.EMPTY;
        }
        return result;
    }
}

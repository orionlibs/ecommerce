/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.validation.impl;

import com.hybris.cockpitng.validation.ValidationInfoFactory;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationResult;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * Factory wrapping validation info by adding prefix to its validation path
 */
public class ValidationInfoFactoryWithPrefix implements ValidationInfoFactory
{
    private final String prefix;


    public ValidationInfoFactoryWithPrefix(final String prefix)
    {
        this.prefix = prefix;
    }


    public String getPrefix()
    {
        return prefix;
    }


    @Override
    public ValidationInfo createValidationInfo(final ValidationInfo validationInfo)
    {
        if(validationInfo instanceof ValidationInfoWithPrefix
                        && StringUtils.equals(((ValidationInfoWithPrefix)validationInfo).getPrefix(), prefix))
        {
            return validationInfo;
        }
        else if((validationInfo instanceof ValidationInfoWithoutPrefix)
                        && StringUtils.equals(((ValidationInfoWithoutPrefix)validationInfo).getPrefix(), prefix))
        {
            return ((ValidationInfoWithoutPrefix)validationInfo).getInfo();
        }
        else
        {
            return new ValidationInfoWithPrefix(prefix, validationInfo);
        }
    }


    public static ValidationResult addPrefix(final ValidationResult validationResult, final String prefix)
    {
        return new ValidationResult(addPrefix(validationResult.getAll(), prefix));
    }


    public static List<ValidationInfo> addPrefix(final List<ValidationInfo> validationResult, final String prefix)
    {
        final ValidationInfoFactoryWithPrefix factory = new ValidationInfoFactoryWithPrefix(prefix);
        return validationResult.stream().map(factory::createValidationInfo).collect(Collectors.toList());
    }
}

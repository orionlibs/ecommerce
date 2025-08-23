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
 *
 *
 */
public class ValidationInfoFactoryWithoutPrefix implements ValidationInfoFactory
{
    private final String prefix;


    public ValidationInfoFactoryWithoutPrefix(final String prefix)
    {
        this.prefix = prefix;
    }


    @Override
    public ValidationInfo createValidationInfo(final ValidationInfo validationInfo)
    {
        if(validationInfo instanceof ValidationInfoWithoutPrefix
                        && StringUtils.equals(((ValidationInfoWithoutPrefix)validationInfo).getPrefix(), prefix))
        {
            return validationInfo;
        }
        else if((validationInfo instanceof ValidationInfoWithPrefix)
                        && StringUtils.equals(((ValidationInfoWithPrefix)validationInfo).getPrefix(), prefix))
        {
            return ((ValidationInfoWithPrefix)validationInfo).getInfo();
        }
        else
        {
            return new ValidationInfoWithoutPrefix(prefix, validationInfo);
        }
    }


    public static ValidationResult removePrefix(final ValidationResult validationResult, final String prefix)
    {
        return new ValidationResult(removePrefix(validationResult.getAll(), prefix));
    }


    public static List<ValidationInfo> removePrefix(final List<ValidationInfo> validationResult, final String prefix)
    {
        final ValidationInfoFactoryWithoutPrefix factory = new ValidationInfoFactoryWithoutPrefix(prefix);
        return validationResult.stream().map(factory::createValidationInfo).collect(Collectors.toList());
    }
}

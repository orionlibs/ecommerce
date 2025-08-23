/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.validation.model;

import com.hybris.cockpitng.validation.ValidationInfoFactory;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a narrowed validation result.
 */
public class ValidationResultSet
{
    private final ValidationResult validationResult;
    private final ValidationInfoFactory infoFactory;
    private final Stream<ValidationInfo> resultSet;


    protected ValidationResultSet(final ValidationResult validationResult, final ValidationInfoFactory infoFactory,
                    final Stream<ValidationInfo> resultSet)
    {
        this.validationResult = validationResult;
        this.infoFactory = infoFactory;
        this.resultSet = resultSet;
    }


    /**
     * Collects current narrowing result.
     *
     * @return list of validation info that meets conditions
     */
    public List<ValidationInfo> collect()
    {
        validationResult.checkDirty();
        return resultSet.map(infoFactory::createValidationInfo).collect(Collectors.toList());
    }


    /**
     * Collects current narrowing result and wraps it into new validation result.
     *
     * @return new, narrowed validation result
     */
    public ValidationResult wrap()
    {
        return new ValidationResult(infoFactory, validationResult.isDirty(), collect());
    }
}

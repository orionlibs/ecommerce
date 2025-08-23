/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editor.extendedmultireferenceeditor.state;

import com.hybris.cockpitng.core.model.ValueObserver;
import com.hybris.cockpitng.util.DesktopAware;
import com.hybris.cockpitng.validation.model.ValidationResult;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Keeps a state of a row in
 * {@link com.hybris.cockpitng.editor.extendedmultireferenceeditor.DefaultExtendedMultiReferenceEditor}.
 */
public class RowState<T> implements DesktopAware
{
    private final Set<String> modifiedProperties = new HashSet<>();
    private final T row;
    private final int rowIndex;
    private final ValidationResult validationResult;
    private boolean error;


    public RowState(final int rowIndex, final T row)
    {
        this.rowIndex = rowIndex;
        this.row = row;
        this.validationResult = new RowValidationResult();
    }


    public T getRow()
    {
        return row;
    }


    public boolean isRowModified()
    {
        return !getModifiedProperties().isEmpty();
    }


    public ValidationResult getValidationResult()
    {
        return validationResult;
    }


    public int getRowIndex()
    {
        return rowIndex;
    }


    public boolean hasError()
    {
        return error;
    }


    public void setError(final boolean error)
    {
        this.error = error;
    }


    public void setPropertyModified(final String qualifier)
    {
        getModifiedPropertiesMutable().add(qualifier);
    }


    public void resetPropertyModified(final String qualifier)
    {
        getModifiedPropertiesMutable().remove(qualifier);
    }


    public boolean isPropertyModified(final String qualifier)
    {
        return getModifiedPropertiesMutable().contains(qualifier);
    }


    public void resetModifiedFields()
    {
        getModifiedPropertiesMutable().clear();
    }


    protected Set<String> getModifiedPropertiesMutable()
    {
        return modifiedProperties;
    }


    public Set<String> getModifiedProperties()
    {
        return Collections.unmodifiableSet(modifiedProperties);
    }


    private static class RowValidationResult extends ValidationResult
    {
        public Set<ValueObserver> getObservers()
        {
            return getObservedValues().stream().map(this::getObservers)
                            .flatMap(os -> new HashSet<>(os).stream()).collect(Collectors.toSet());
        }
    }


    @Override
    public void afterDesktopChanged()
    {
        final ValidationResult result = getValidationResult();
        if(result instanceof RowValidationResult)
        {
            ((RowValidationResult)result).getObservers()
                            .forEach(observer -> getValidationResult().removeObserver(observer));
        }
    }
}

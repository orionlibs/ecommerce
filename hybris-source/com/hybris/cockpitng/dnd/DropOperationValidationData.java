/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dnd;

import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.apache.commons.collections.CollectionUtils;

public class DropOperationValidationData
{
    private final DropOperationData dropOperationData;
    private final Map<ValidationSeverity, List<ValidationInfo>> validationInfoMap;
    private final Collection<Consumer> ignoreValidationObservers = new ArrayList<>();
    private boolean ignoreValidation;


    public DropOperationValidationData(final DropOperationData dropOperationData,
                    final Map<ValidationSeverity, List<ValidationInfo>> infosBySeverity)
    {
        this.dropOperationData = dropOperationData;
        this.validationInfoMap = infosBySeverity;
        this.ignoreValidation = true;
    }


    public DropOperationData getDropOperationData()
    {
        return dropOperationData;
    }


    public List<ValidationInfo> getValidationInfo(final ValidationSeverity validationSeverity)
    {
        if(validationInfoMap.containsKey(validationSeverity))
        {
            return Collections.unmodifiableList(validationInfoMap.get(validationSeverity));
        }
        else
        {
            return Collections.emptyList();
        }
    }


    public void add(final ValidationSeverity validationSeverity, final List<ValidationInfo> validationInfo)
    {
        validationInfoMap.put(validationSeverity, validationInfo);
    }


    public boolean hasIssuesOfSeverity(final ValidationSeverity severity)
    {
        return CollectionUtils.isNotEmpty(validationInfoMap.get(severity));
    }


    public boolean isIgnoreValidation()
    {
        return ignoreValidation;
    }


    public void setIgnoreValidation(final boolean ignoreValidation, final Object source)
    {
        this.ignoreValidation = ignoreValidation;
        ignoreValidationObservers.forEach(consumer -> consumer.accept(source));
    }


    public void addIgnoreValidationChangedListener(final Consumer<Object> consumer)
    {
        ignoreValidationObservers.add(consumer);
    }


    public int getTotalViolations()
    {
        return validationInfoMap.entrySet().stream().mapToInt(entry -> CollectionUtils.size(entry.getValue())).sum();
    }
}

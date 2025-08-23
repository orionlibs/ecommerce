/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.object.dataquality.impl;

import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.dataquality.DataQualityCalculationService;
import com.hybris.cockpitng.dataquality.model.DataQuality;
import com.hybris.cockpitng.dataquality.model.DataQualityProperty;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.validation.coverage.CoverageCalculationService;
import de.hybris.platform.validation.coverage.CoverageInfo;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default Backoffice implementation of coverage calculation service. This implementation of the
 * {@link DataQualityCalculationService} uses
 * {@link de.hybris.platform.validation.coverage.strategies.CoverageCalculationStrategyRegistry} to lookup matching
 * strategy and redirect the calculation to it.
 */
public class BackofficeDataQualityCalculationService implements DataQualityCalculationService
{
    private static final Logger LOG = LoggerFactory.getLogger(BackofficeDataQualityCalculationService.class);
    private CoverageCalculationService coverageCalculationService;
    private TypeFacade typeFacade;


    @Override
    public Optional<DataQuality> calculate(final Object object, final String domainId)
    {
        if(getCoverageCalculationService() != null && object instanceof ItemModel)
        {
            return calculate(object, ((ItemModel)object).getItemtype(), domainId);
        }
        return Optional.empty();
    }


    @Override
    public Optional<DataQuality> calculate(final Object object, final String templateCode, final String domainId)
    {
        if(object instanceof ItemModel)
        {
            final CoverageInfo coverageInfo = getCoverageCalculationService().calculate((ItemModel)object, templateCode, domainId);
            return convertToDataQuality(coverageInfo, domainId, ((ItemModel)object).getItemtype());
        }
        LOG.error("object {} is not an instance of the ItemModel", object);
        return Optional.empty();
    }


    protected Optional<DataQuality> convertToDataQuality(final CoverageInfo coverageInfo)
    {
        return convertToDataQuality(coverageInfo, null);
    }


    protected Optional<DataQuality> convertToDataQuality(final CoverageInfo coverageInfo, final String domainId)
    {
        return convertToDataQuality(coverageInfo, domainId, null);
    }


    protected Optional<DataQuality> convertToDataQuality(final CoverageInfo coverageInfo, final String domainId, final String typeName)
    {
        if(coverageInfo == null)
        {
            return Optional.empty();
        }
        final DataQuality dataQuality = new DataQuality();
        dataQuality.setDataQualityIndex(coverageInfo.getCoverageIndex());
        dataQuality.setDescription(coverageInfo.getCoverageDescription());
        dataQuality.setDataQualityProperties(convertToCoverageProperties(coverageInfo.getPropertyInfoMessages(), typeName));
        return Optional.of(dataQuality);
    }


    protected List<DataQualityProperty> convertToCoverageProperties(
                    final List<CoverageInfo.CoveragePropertyInfoMessage> propertyInfoMessages)
    {
        return convertToCoverageProperties(propertyInfoMessages, null);
    }


    protected List<DataQualityProperty> convertToCoverageProperties(
                    final List<CoverageInfo.CoveragePropertyInfoMessage> propertyInfoMessages, final String typeName)
    {
        return propertyInfoMessages.stream().filter(Objects::nonNull)
                        .map(property -> new DataQualityProperty(getPropertyQualifier(property, typeName), property.getMessage()))
                        .collect(Collectors.toList());
    }


    protected String getPropertyQualifier(final CoverageInfo.CoveragePropertyInfoMessage property)
    {
        return getPropertyQualifier(property, null);
    }


    protected String getPropertyQualifier(final CoverageInfo.CoveragePropertyInfoMessage property, final String typeName)
    {
        final ObjectValuePath propertyPath = ObjectValuePath.parse(property.getPropertyQualifier());
        final ObjectValuePath root = propertyPath.getRoot();
        try
        {
            getTypeFacade().load(StringUtils.isBlank(typeName) ? root.toString() : typeName);
            final String relativePath = propertyPath.getRelative(root).toString();
            final boolean isLocalized = propertyPath.getLocale() != null;
            return isLocalized ? String.format("%s[%s]", relativePath, propertyPath.getLocale()) : relativePath;
        }
        catch(final TypeNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Type not found", e);
            }
            return propertyPath.toString();
        }
    }


    @Required
    public void setCoverageCalculationService(final CoverageCalculationService coverageCalculationService)
    {
        this.coverageCalculationService = coverageCalculationService;
    }


    protected CoverageCalculationService getCoverageCalculationService()
    {
        return coverageCalculationService;
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    protected TypeFacade getTypeFacade()
    {
        return typeFacade;
    }
}

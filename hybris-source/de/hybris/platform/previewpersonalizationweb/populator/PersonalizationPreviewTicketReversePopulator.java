package de.hybris.platform.previewpersonalizationweb.populator;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationservices.data.CxVariationKey;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.personalizationservices.segment.CxSegmentService;
import de.hybris.platform.personalizationservices.variation.CxVariationService;
import de.hybris.platform.previewwebservices.dto.PreviewTicketWsDTO;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.webservicescommons.util.LocalViewExecutor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

public class PersonalizationPreviewTicketReversePopulator implements Populator<PreviewTicketWsDTO, PreviewDataModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(PersonalizationPreviewTicketReversePopulator.class);
    private CxVariationService cxVariationService;
    private CxSegmentService cxSegmentService;
    private CatalogVersionService catalogVersionService;
    private LocalViewExecutor localViewExecutor;
    private PersonalizationPreviewTicketSupport previewTicketSupport;


    public void populate(PreviewTicketWsDTO source, PreviewDataModel target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        this.localViewExecutor.executeWithAllCatalogs(() -> {
            setVariations(source, target);
            setSegments(source, target);
            return null;
        });
    }


    protected void setVariations(PreviewTicketWsDTO source, PreviewDataModel target)
    {
        if(CollectionUtils.isEmpty(source.getVariations()))
        {
            target.setVariations(null);
        }
        else
        {
            List<CxVariationModel> variations = new ArrayList<>();
            List<CxVariationKey> missing = new ArrayList<>();
            mapVariations(source, variations, missing);
            validateVariations(missing);
            target.setVariations(variations);
        }
    }


    protected void mapVariations(PreviewTicketWsDTO source, List<CxVariationModel> variations, List<CxVariationKey> missing)
    {
        Set<String> supportedCatalogs = this.previewTicketSupport.getAllSupportedCatalogs(source);
        Objects.requireNonNull(variations);
        source.getVariations().stream().filter(key -> this.previewTicketSupport.invalidVariationFilter(key, supportedCatalogs)).map(variationKey -> {
            Optional<CxVariationModel> variation = getVariation(variationKey);
            if(variation.isEmpty())
            {
                missing.add(variationKey);
            }
            return variation;
        }).filter(Optional::isPresent).map(Optional::get).forEach(variations::add);
    }


    private Optional<CxVariationModel> getVariation(CxVariationKey variationKey)
    {
        Collection<CxVariationKey> keys = Collections.singletonList(variationKey);
        return findCxCatalogVersion(variationKey.getCatalog(), variationKey.getCatalogVersion())
                        .map(cvm -> this.cxVariationService.getVariations(keys, cvm))
                        .filter(c -> !c.isEmpty())
                        .map(c -> (CxVariationModel)c.iterator().next());
    }


    protected Optional<CatalogVersionModel> findCxCatalogVersion(String catalog, String catalogVersion)
    {
        if(catalog != null && catalogVersion != null)
        {
            try
            {
                return Optional.of(this.catalogVersionService.getCatalogVersion(catalog, catalogVersion));
            }
            catch(UnknownIdentifierException | de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException | IllegalArgumentException ex)
            {
                LOG.debug("Error getting CatalogVersionModel", ex);
            }
        }
        return Optional.empty();
    }


    protected void validateVariations(List<CxVariationKey> missing)
    {
        if(!missing.isEmpty())
        {
            throw new ConversionException(
                            String.format("No variations found for provided list of codes (%s)", new Object[] {createErrorMessageForVariationKeys(missing)}));
        }
    }


    protected String createErrorMessageForVariationKeys(Collection<CxVariationKey> keys)
    {
        Objects.requireNonNull(this.previewTicketSupport);
        return keys.stream().map(this.previewTicketSupport::variationKeyToString).collect(Collectors.joining(","));
    }


    protected void setSegments(PreviewTicketWsDTO source, PreviewDataModel target)
    {
        if(CollectionUtils.isEmpty(source.getSegments()))
        {
            target.setSegments(null);
        }
        else
        {
            Collection<CxSegmentModel> existingSegments = this.cxSegmentService.getSegmentsForCodes(source.getSegments());
            validateSegments(source.getSegments(), existingSegments);
            target.setSegments(existingSegments);
        }
    }


    protected void validateSegments(Collection<String> inputSegmentsCode, Collection<CxSegmentModel> existingSegments)
    {
        if(CollectionUtils.isEmpty(existingSegments))
        {
            throw new ConversionException(
                            String.format("No segments found for provided list of codes (%s)", new Object[] {createErrorMessageForCodes(inputSegmentsCode)}));
        }
        if(inputSegmentsCode.size() != existingSegments.size())
        {
            Collection<String> existingSegmentCodes = (Collection<String>)existingSegments.stream().map(s -> s.getCode()).collect(Collectors.toSet());
            Collection<String> wrongSegmentCodes = (Collection<String>)inputSegmentsCode.stream().filter(code -> !existingSegmentCodes.contains(code)).collect(Collectors.toSet());
            throw new ConversionException(
                            String.format("No segments found for provided list of codes (%s)", new Object[] {createErrorMessageForCodes(wrongSegmentCodes)}));
        }
    }


    protected String createErrorMessageForCodes(Collection<String> codes)
    {
        return codes.stream().map(c -> "[" + c + "]").collect(Collectors.joining(","));
    }


    public CxVariationService getCxVariationService()
    {
        return this.cxVariationService;
    }


    @Required
    public void setCxVariationService(CxVariationService cxVariationService)
    {
        this.cxVariationService = cxVariationService;
    }


    public CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    public CxSegmentService getCxSegmentService()
    {
        return this.cxSegmentService;
    }


    @Required
    public void setCxSegmentService(CxSegmentService cxSegmentService)
    {
        this.cxSegmentService = cxSegmentService;
    }


    @Required
    public void setLocalViewExecutor(LocalViewExecutor localViewExecutor)
    {
        this.localViewExecutor = localViewExecutor;
    }


    public LocalViewExecutor getLocalViewExecutor()
    {
        return this.localViewExecutor;
    }


    @Required
    public void setPersonalizationPreviewTicketSupport(PersonalizationPreviewTicketSupport previewTicketSupport)
    {
        this.previewTicketSupport = previewTicketSupport;
    }


    public PersonalizationPreviewTicketSupport getPersonalizationPreviewTicketSupport()
    {
        return this.previewTicketSupport;
    }
}

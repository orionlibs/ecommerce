package de.hybris.platform.previewpersonalizationweb.populator;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.personalizationservices.data.CxVariationKey;
import de.hybris.platform.previewwebservices.dto.PreviewTicketWsDTO;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class PersonalizationPreviewTicketSupport
{
    private static final Logger LOG = LoggerFactory.getLogger(PersonalizationPreviewTicketSupport.class);
    private CatalogVersionService catalogVersionService;


    public Set<String> getAllSupportedCatalogs(PreviewTicketWsDTO source)
    {
        return (Set<String>)source.getCatalogVersions().stream()
                        .map(dto -> findCatalogVersion(dto.getCatalog(), dto.getCatalogVersion()))
                        .filter(Optional::isPresent)
                        .map(Optional::get).flatMap(cv -> expandWithParents(cv).stream())
                        .collect(Collectors.toSet());
    }


    public static List<String> expandWithParents(CatalogVersionModel catalogVersion)
    {
        List<String> result = new ArrayList<>();
        result.add(catalogVersion.getCatalog().getId());
        if(catalogVersion.getCatalog() instanceof ContentCatalogModel)
        {
            ContentCatalogModel current = (ContentCatalogModel)catalogVersion.getCatalog();
            while(current.getSuperCatalog() != null)
            {
                current = current.getSuperCatalog();
                result.add(current.getId());
            }
        }
        return result;
    }


    protected Optional<CatalogVersionModel> findCatalogVersion(String catalog, String catalogVersion)
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


    public boolean invalidVariationFilter(CxVariationKey key, Set<String> supportedCatalogs)
    {
        if(supportedCatalogs.contains(key.getCatalog()))
        {
            return true;
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Dropping variation for different catalog than preview ticket {}", variationKeyToString(key));
        }
        return false;
    }


    public String variationKeyToString(CxVariationKey key)
    {
        return "[" + key.getCustomizationCode() + " , " + key.getVariationCode() + " , " + key.getCatalog() + " , " + key
                        .getCatalogVersion() + "]";
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    public CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }
}

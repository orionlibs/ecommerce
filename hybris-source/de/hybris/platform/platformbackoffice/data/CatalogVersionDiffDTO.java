package de.hybris.platform.platformbackoffice.data;

import de.hybris.platform.platformbackoffice.services.catalogversion.CatalogVersionCompareService;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

public class CatalogVersionDiffDTO implements Serializable
{
    private final Collection<CatalogVersionCompareService.CatalogVersionComparison> possibleComparisons;


    public CatalogVersionDiffDTO(Collection<CatalogVersionCompareService.CatalogVersionComparison> possibleComparisons)
    {
        Objects.requireNonNull(possibleComparisons, "possibleComparisons mustn't be null");
        this.possibleComparisons = possibleComparisons;
    }


    public Collection<CatalogVersionCompareService.CatalogVersionComparison> getPossibleComparisons()
    {
        return this.possibleComparisons;
    }
}

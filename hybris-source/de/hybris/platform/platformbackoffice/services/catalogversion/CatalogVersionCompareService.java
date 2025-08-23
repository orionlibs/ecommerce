package de.hybris.platform.platformbackoffice.services.catalogversion;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.ViewTypeModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class CatalogVersionCompareService
{
    private static final Logger LOG = LoggerFactory.getLogger(CatalogVersionCompareService.class.getName());
    private String viewName = "ModifiedCatalogItemsView";
    private ModelService modelService;
    private TypeService typeService;
    private FlexibleSearchService flexibleSearchService;


    public boolean canBeCompared(CatalogVersionModel version)
    {
        Objects.requireNonNull(version, "version musn't be null");
        return !getPossibleComparisons(version).isEmpty();
    }


    public Collection<CatalogVersionComparison> getPossibleComparisons(CatalogVersionModel version)
    {
        Objects.requireNonNull(version, "version musn't be null");
        String query = "select {PK} from {SyncItemJob} where {sourceVersion}=?cv or {targetVersion}=?cv";
        FlexibleSearchQuery fxQuery = new FlexibleSearchQuery("select {PK} from {SyncItemJob} where {sourceVersion}=?cv or {targetVersion}=?cv", (Map)ImmutableMap.of("cv", version));
        fxQuery.setResultClassList((List)ImmutableList.of(SyncItemJobModel.class));
        List<SyncItemJobModel> searchResult = this.flexibleSearchService.search(fxQuery).getResult();
        return (Collection<CatalogVersionComparison>)searchResult.stream().map(job -> new CatalogVersionComparison(job.getPk())).collect(Collectors.toList());
    }


    public List<CatalogVersionDifference> findDifferences(CatalogVersionComparison comparison)
    {
        Objects.requireNonNull(comparison, "comparison musn't be null");
        SyncItemJobModel syncItemJob = (SyncItemJobModel)this.modelService.get(comparison.getSyncItemJobPK());
        FlexibleSearchQuery fxQuery = new FlexibleSearchQuery(findViewType().getQuery(), (Map)ImmutableMap.of("job", syncItemJob));
        fxQuery.setResultClassList((List)ImmutableList.of(PK.class, PK.class, Date.class, Date.class, String.class));
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("Searching for catalog version difference with query '%s'", new Object[] {fxQuery.getQuery()}));
        }
        SearchResult<List<Object>> fxResult = this.flexibleSearchService.search(fxQuery);
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("Catalog version difference found: %s", new Object[] {Integer.valueOf(fxResult.getTotalCount())}));
        }
        return (List<CatalogVersionDifference>)fxResult.getResult().stream().map(row -> newDifferenceFrom(row)).collect(Collectors.toList());
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    public void setViewName(String viewName)
    {
        this.viewName = viewName;
    }


    private ViewTypeModel findViewType()
    {
        try
        {
            return (ViewTypeModel)this.typeService.getTypeForCode(this.viewName);
        }
        catch(ClassCastException e)
        {
            throw new IllegalArgumentException("View type '" + this.viewName + "' doesnt belong to a view type", e);
        }
        catch(UnknownIdentifierException e)
        {
            throw new IllegalArgumentException("No wView type '" + this.viewName + "' exists", e);
        }
    }


    private CatalogVersionDifference newDifferenceFrom(List<Object> row)
    {
        PK sourceItemPK = (PK)row.get(0);
        PK targetItemPK = (PK)row.get(1);
        Date sourceModificationTime = (Date)row.get(2);
        Date lastSyncTime = (Date)row.get(3);
        String typeName = (String)row.get(4);
        if(typeName != null)
        {
            typeName = typeName.trim();
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("Catalog version difference record sourcePK: %s, targetPK: %s, sourceModTS: %s, lastSync: %s, typeName: %s", new Object[] {sourceItemPK, targetItemPK, sourceModificationTime, lastSyncTime, typeName}));
        }
        return new CatalogVersionDifference(sourceItemPK, targetItemPK, sourceModificationTime, lastSyncTime, typeName);
    }
}

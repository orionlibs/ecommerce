package de.hybris.platform.personalizationfacades;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationfacades.exceptions.AlreadyExistsException;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;
import de.hybris.platform.tx.Transaction;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import rx.functions.Action0;

public abstract class AbstractBaseFacade
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractBaseFacade.class);
    private ModelService modelService;
    private CatalogVersionService catalogVersionService;
    public static final SearchResult EMPTY_SEARCH_RESULT = (SearchResult)new SearchResultImpl(Collections.emptyList(), 0, 0, 0);


    protected void validateCode(String cxTypeName, String code)
    {
        if(StringUtils.isEmpty(code))
        {
            throw new IllegalArgumentException(cxTypeName + " code can't be empty");
        }
    }


    protected void validateName(String cxTypeName, String name)
    {
        if(StringUtils.isEmpty(name))
        {
            throw new IllegalArgumentException(cxTypeName + " name can't be empty");
        }
    }


    protected void throwAlreadyExists(String name, String code)
    {
        throw new AlreadyExistsException(name + " with code " + name + " already exists.");
    }


    protected UnknownIdentifierException createUnknownIdentifierException(String name, String code)
    {
        return new UnknownIdentifierException(name + " with code " + name + " does not exists");
    }


    protected CatalogVersionModel getCatalogVersion(String catalogId, String catalogVersionId)
    {
        validateCatalogIdentifiers(catalogId, catalogVersionId);
        try
        {
            return this.catalogVersionService.getCatalogVersion(catalogId, catalogVersionId);
        }
        catch(AmbiguousIdentifierException | UnknownIdentifierException | IllegalArgumentException e)
        {
            throw new UnknownIdentifierException("Catalog version not found (catalogId : " + catalogId + " catalogVersionId : " + catalogVersionId, e);
        }
    }


    protected <T> SearchPageData<T> getEmptySearchData()
    {
        SearchPageData<T> result = new SearchPageData();
        result.setPagination(new PaginationData());
        result.setResults(Collections.emptyList());
        result.setSorts(Collections.emptyList());
        return result;
    }


    protected <T> SearchPageData<T> getSearchDataForSingleElement()
    {
        SearchPageData<T> result = getEmptySearchData();
        result.getPagination().setPageSize(1);
        return result;
    }


    protected <I, R> SearchPageData<R> convertSearchPage(SearchPageData<I> input, Function<List<I>, List<R>> mapper)
    {
        List<R> resultList = mapper.apply(input.getResults());
        SearchPageData<R> result = new SearchPageData();
        result.setPagination(input.getPagination());
        result.setSorts(input.getSorts());
        result.setResults(resultList);
        return result;
    }


    protected void validateCatalogIdentifiers(String catalogId, String catalogVersionId)
    {
        Assert.notNull(catalogId, "Invalid catalog identifier");
        Assert.notNull(catalogVersionId, "Invalid catalog version identifier");
    }


    protected <ITEM> ITEM executeInTransaction(Supplier<ITEM> transactionBody)
    {
        return executeInTransaction(transactionBody, () -> {
        });
    }


    protected <ITEM> ITEM executeInTransaction(Supplier<ITEM> transactionBody, Action0 rollbackAction)
    {
        ITEM item = null;
        Transaction tx = Transaction.current();
        tx.begin();
        try
        {
            item = transactionBody.get();
            tx.commit();
        }
        catch(RuntimeException e)
        {
            rollbackTransaction(tx);
            rollbackAction.call();
            throw e;
        }
        return item;
    }


    protected void rollbackTransaction(Transaction tx)
    {
        try
        {
            tx.rollback();
        }
        catch(RuntimeException e)
        {
            LOG.error("Rollback transaction failed", e);
        }
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
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

package de.hybris.platform.europe1.dynamic;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.jalo.PDTRowsQueryBuilder;
import de.hybris.platform.europe1.model.PDTRowModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public abstract class PdtRowEurope1AttributeHandler<T extends PDTRowModel, MODEL extends ProductModel> implements DynamicAttributeHandler<Collection<T>, MODEL>
{
    protected TypeService typeService;
    protected FlexibleSearchService flexibleSearchService;
    protected SessionService sessionService;


    public Collection<T> get(MODEL model)
    {
        Collection<T> ownAndOtherPdtRowModels = addWildCardsPdtRows((ProductModel)model, getOwnPdtRowModels((ProductModel)model));
        if(!useFastAlg() && !CollectionUtils.isEmpty(ownAndOtherPdtRowModels))
        {
            return sortPdtRows(ownAndOtherPdtRowModels);
        }
        return ownAndOtherPdtRowModels;
    }


    public void set(MODEL model, Collection<T> value)
    {
        Collection<T> own = filteroutWildcardsPdtRows((ProductModel)model, value);
        setOwnPdtRowModels((ProductModel)model, own);
    }


    protected Collection<T> addWildCardsPdtRows(ProductModel product, Collection<T> ownPdtRows)
    {
        List<T> list = new ArrayList<>(CollectionUtils.emptyIfNull(ownPdtRows));
        list.addAll(getWildcardsPdtRows(product));
        return list;
    }


    protected Collection<T> filteroutWildcardsPdtRows(ProductModel product, Collection<T> pdtRows)
    {
        if(pdtRows == null)
        {
            return Collections.EMPTY_LIST;
        }
        return (Collection<T>)pdtRows.stream().filter(pdt -> product.equals(pdt.getProduct())).collect(Collectors.toList());
    }


    private Collection<T> getWildcardsPdtRows(ProductModel product)
    {
        PK productGroupPk = getProductGroupPK(product);
        String productId = extractProductId(product);
        PDTRowsQueryBuilder builder = PDTRowsQueryBuilder.defaultBuilder(getType());
        PDTRowsQueryBuilder.QueryWithParams queryAndParams = builder.withAnyProduct().withProductId(productId).withProductGroup(productGroupPk).build();
        SearchResult<T> searchResult = this.flexibleSearchService.search(queryAndParams.getQuery(), queryAndParams.getParams());
        return searchResult.getResult();
    }


    private String extractProductId(ProductModel product)
    {
        String idFromContext = (String)this.sessionService.getAttribute("productId");
        if(idFromContext != null)
        {
            return idFromContext;
        }
        return product.getCode();
    }


    protected Collection<T> sortPdtRows(Collection<T> pdtRows)
    {
        List<T> list = new ArrayList<>(pdtRows);
        Collections.sort(list, getPdtRowComparator());
        return list;
    }


    protected boolean useFastAlg()
    {
        return Boolean.TRUE.equals(this.sessionService.getAttribute("use.fast.algorithms"));
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


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    protected abstract PK getProductGroupPK(ProductModel paramProductModel);


    protected abstract Collection<T> getOwnPdtRowModels(ProductModel paramProductModel);


    protected abstract void setOwnPdtRowModels(ProductModel paramProductModel, Collection<T> paramCollection);


    protected abstract Comparator<T> getPdtRowComparator();


    protected abstract String getType();
}

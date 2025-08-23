package de.hybris.platform.hac.performance.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class SlFlexibleSearch extends AbstractPerformanceTest
{
    private FlexibleSearchService flexibleSearchService;


    public void executeBlock()
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {PK} FROM {PRODUCT}");
        query.setStart(0);
        query.setCount(200);
        query.setNeedTotal(false);
        query.setFailOnUnknownFields(true);
        SearchResult<ProductModel> search = this.flexibleSearchService.search(query);
        List<ProductModel> result = search.getResult();
        for(ProductModel productModel : result)
        {
            productModel.getCode();
        }
    }


    public String getTestName()
    {
        return "ServiceLayer FlexibleSearch - 200 Products";
    }


    public void cleanup()
    {
    }


    public void prepare()
    {
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}

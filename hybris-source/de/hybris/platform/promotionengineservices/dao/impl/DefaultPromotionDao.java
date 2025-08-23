package de.hybris.platform.promotionengineservices.dao.impl;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.promotionengineservices.dao.PromotionDao;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotions.model.AbstractPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.HashMap;
import java.util.Map;

public class DefaultPromotionDao extends AbstractItemDao implements PromotionDao
{
    public AbstractPromotionModel findPromotionByCode(String code)
    {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        String query = "SELECT {" + Item.PK + "} FROM   {AbstractPromotion} WHERE  {code} = ?code";
        SearchResult<RuleBasedPromotionModel> searchResult = getFlexibleSearchService().search(query, params);
        if(searchResult.getCount() != 0)
        {
            return searchResult.getResult().get(0);
        }
        return null;
    }


    public PromotionGroupModel findPromotionGroupByCode(String identifier)
    {
        Map<String, String> params = new HashMap<>();
        params.put("identifier", identifier);
        String query = "SELECT {" + Item.PK + "} FROM {PromotionGroup} WHERE  {Identifier} = ?identifier";
        SearchResult<PromotionGroupModel> searchResult = getFlexibleSearchService().search(query, params);
        if(searchResult.getCount() != 0)
        {
            return searchResult.getResult().get(0);
        }
        return null;
    }


    public PromotionGroupModel findDefaultPromotionGroup()
    {
        return findPromotionGroupByCode("default");
    }
}

package de.hybris.platform.europe1.dynamic;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.enums.UserDiscountGroup;
import de.hybris.platform.europe1.model.GlobalDiscountRowModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class UserEurope1DiscountsAttributeHandler implements DynamicAttributeHandler<Collection<GlobalDiscountRowModel>, UserModel>
{
    private TypeService typeService;
    private FlexibleSearchService flexibleSearchService;


    public Collection<GlobalDiscountRowModel> get(UserModel model)
    {
        Collection<GlobalDiscountRowModel> own = model.getOwnEurope1Discounts();
        return addWildCardDiscounts(own, model.getEurope1PriceFactory_UDG());
    }


    private Collection<GlobalDiscountRowModel> addWildCardDiscounts(Collection<GlobalDiscountRowModel> own, UserDiscountGroup userDiscountGroup)
    {
        List<GlobalDiscountRowModel> list = Lists.newArrayList(CollectionUtils.emptyIfNull(own));
        list.addAll(getWildcardGlobalDiscountRows(userDiscountGroup));
        return list;
    }


    public void set(UserModel model, Collection<GlobalDiscountRowModel> globalDiscountRowModels)
    {
        Collection<GlobalDiscountRowModel> own = filteroutWildcards(globalDiscountRowModels, model
                        .getEurope1PriceFactory_UDG());
        model.setOwnEurope1Discounts(own);
    }


    private Collection<GlobalDiscountRowModel> filteroutWildcards(Collection<GlobalDiscountRowModel> allGlobalDiscountRows, UserDiscountGroup userDiscountGroup)
    {
        return CollectionUtils.removeAll(allGlobalDiscountRows, getWildcardGlobalDiscountRows(userDiscountGroup));
    }


    private Collection<GlobalDiscountRowModel> getWildcardGlobalDiscountRows(UserDiscountGroup userGroup)
    {
        String LEFT_JOIN = " LEFT JOIN ";
        String select = "SELECT {dr:pk}, CASE WHEN {dr:startTime} IS NULL THEN 2 ELSE 1 END as drOrd FROM {" + this.typeService.getComposedTypeForClass(GlobalDiscountRowModel.class).getCode() + " AS dr JOIN " + this.typeService.getComposedTypeForClass(DiscountModel.class).getCode()
                        + " AS disc ON {dr:discount}={disc:pk}  LEFT JOIN " + this.typeService.getComposedTypeForClass(CurrencyModel.class).getCode() + " AS curr ON {dr:currency}={curr:pk} } WHERE ";
        StringBuilder query = new StringBuilder();
        Map<String, Object> values = Maps.newHashMap();
        appendWildcardAndUserGroupConditions(query, "dr", userGroup, values, "ug");
        query.append(" ORDER BY ");
        query.append("{disc:").append("code").append("} ASC");
        query.append(",drOrd ASC");
        query.append(", CASE WHEN {").append("currency").append("} IS NULL THEN 0 ELSE 1 END ASC");
        query.append(",{curr:").append("isocode").append("} ASC");
        SearchResult<GlobalDiscountRowModel> searchResult = this.flexibleSearchService.search(select + select, values);
        return searchResult.getResult();
    }


    private void appendWildcardAndUserGroupConditions(StringBuilder query, String alias, UserDiscountGroup userGroup, Map<String, Object> values, String ugConst)
    {
        query.append("{").append(alias).append(":").append("userMatchQualifier").append("} IN ( ?anyU ");
        values.put("anyU", PK.NULL_PK);
        if(userGroup != null)
        {
            query.append(",?ug ");
            values.put("ug", userGroup);
        }
        query.append(")");
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
}

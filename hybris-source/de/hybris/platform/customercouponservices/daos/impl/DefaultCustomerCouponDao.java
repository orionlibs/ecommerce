package de.hybris.platform.customercouponservices.daos.impl;

import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customercouponservices.daos.CustomerCouponDao;
import de.hybris.platform.customercouponservices.model.CustomerCouponModel;
import de.hybris.platform.promotionengineservices.model.CatForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.ProductForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchParameter;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchService;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class DefaultCustomerCouponDao extends DefaultGenericDao<CustomerCouponModel> implements CustomerCouponDao
{
    private static final String CUSTOMERCOUPON_CUSTOMER_RELATION = "CustomerCoupon2Customer";
    private static final String CUSTOMER = "customer";
    private static final String CUSTOMERUID = "customerUid";
    private static final String COUPON_ACTIVE = "active";
    private static final String CUSTOMER_COUPON_ASSIGNABLE = "assignable";
    private static final String SEARCH_PROMOTION_RULE_QUERY = "select {pr.pk} from {PromotionSourceRule as pr}, {RuleStatus as rs} where {pr.status} = {rs.pk} and {rs.code} = '" + RuleStatus.PUBLISHED
                    .getCode() + "' and {pr.code} = ?code";
    private static final String FIND_PROMOTION_RULE_FOR_PRODUCT = "SELECT {r.pk} as pr FROM {PromotionSourceRule as r}, {ProductForPromotionSourceRule as rel}, {RuleStatus as rs} WHERE {r.status} = {rs.pk} and {rs.code} = '" + RuleStatus.PUBLISHED
                    .getCode() + "' AND {rel.rule} = {r.pk} AND {rel.productCode} =?productCode";
    private static final String FIND_EXCLUDED_PROMOTION_RULE_FOR_PRODUCT = "SELECT {r.pk} as pr FROM {PromotionSourceRule as r}, {ExcludedProductForRule as rel}, {RuleStatus as rs} WHERE {r.status} = {rs.pk} and {rs.code} = '" + RuleStatus.PUBLISHED
                    .getCode() + "' AND {rel.rule} = {r.pk} AND {rel.productCode} =?productCode";
    private static final String FIND_PRODUCT_FOR_PROMOTION_RULE = "SELECT {rel.pk} as pr FROM {PromotionSourceRule as r}, {ProductForPromotionSourceRule as rel}, {RuleStatus as rs} WHERE {r.status} = {rs.pk} and {rs.code} = '" + RuleStatus.PUBLISHED
                    .getCode() + "' AND {rel.rule} = {r.pk} AND {r.code} = ?code";
    private static final String FIND_PROMOTION_RULE_FOR_CATEGORY = "SELECT {r.pk} as pr FROM {PromotionSourceRule as r}, {CatForPromotionSourceRule as rel}, {RuleStatus as rs} WHERE {r.status} = {rs.pk} AND {rs.code} = '" + RuleStatus.PUBLISHED
                    .getCode() + "' AND {rel.rule} = {r.pk} AND {rel.categoryCode} = ?categoryCode";
    private static final String FIND_EXCLUDED_PROMOTION_RULE_FOR_CATEGORY = "SELECT {r.pk} as pr FROM {PromotionSourceRule as r}, {ExcludedCatForRule as rel}, {RuleStatus as rs} WHERE {r.status} = {rs.pk} AND {rs.code} = '" + RuleStatus.PUBLISHED
                    .getCode() + "' AND {rel.rule} = {r.pk} AND {rel.categoryCode} = ?categoryCode";
    private static final String FIND_CATEGORY_FOR_PROMOTION_RULE = "SELECT {rel.pk} as pr FROM {PromotionSourceRule as r}, {CatForPromotionSourceRule as rel}, {RuleStatus as rs} WHERE {r.status} = {rs.pk} AND {rs.code} = '" + RuleStatus.PUBLISHED
                    .getCode() + "' AND {rel.rule} = {r.pk} AND {r.code} = ?code";
    private static final String FIND_CUSTOMER_COUPON_FOR_PROMOTION_RULE = "SELECT {cc.pk} as pr FROM {PromotionSourceRule as r}, {CustomerCouponForPromotionSourceRule as rel},{CustomerCoupon as cc}, {RuleStatus as rs} WHERE {r.status} = {rs.pk} AND {rs.code} = '" + RuleStatus.PUBLISHED
                    .getCode() + "' AND {rel.rule} = {r.pk} AND {rel.customerCouponCode} = {cc.couponId} AND {r.code} = ?code";
    private static final String SEARCH_COUPON_QUERY = "select {cp:pk} from {CustomerCoupon as cp left join CustomerCoupon2Customer as ccr on {ccr:source} = {cp:pk} left join Customer as cst on {cst:pk} = {ccr:target}} where {active} = ?active and {endDate} > ?now and {cst:pk} = ?customer ";
    private static final String SEARCH_EFFECTIVE_COUPON_QUERY = "select {cp.pk} from {CustomerCoupon as cp left join CustomerCoupon2Customer as ccr on {ccr:source} = {cp:pk} left join Customer as cst on {cst.pk} = {ccr:target} } where {active} = ?active and {endDate} > ?now and {startDate } < ?now and {cst.uid} = ?customerUid";
    private static final String CHECK_EFFECTIVE_COUPON_QUERY = "select {cp.pk} from {CustomerCoupon as cp left join CustomerCoupon2Customer as ccr on {ccr:source} = {cp:pk} left join Customer as cst on {cst.pk} = {ccr:target} } where {active} = ?active and {endDate} > ?now and {startDate } < ?now and {cst.uid} = ?customerUid and {couponId } = ?couponCode";
    private static final String CHECK_ASSIGNED_COUPON_QUERY = "select {cp.pk} from {CustomerCoupon as cp left join CustomerCoupon2Customer as ccr on {ccr:source} = {cp:pk} left join Customer as cst on {cst.pk} = {ccr:target} } where {active} = ?active and {cst.uid} = ?customerUid and {couponId } = ?couponCode";
    private static final String FIND_ASSIGNABLE_COUPONS_QUERY = "SELECT {C:pk} FROM {CustomerCoupon AS C} WHERE {C:assignable} = ?assignable AND {C:startDate} IS NOT NULL AND {C:endDate} IS NOT NULL AND {C:endDate} >= ?now AND {C:active} = ?active AND {C:pk} NOT IN ({{SELECT {CC:pk} FROM {CustomerCoupon AS CC JOIN CUSTOMERCOUPON2CUSTOMER AS R ON {CC:pk} = {R:source} JOIN Customer AS U ON {R:target} = {U:pk}} WHERE {U:pk} = ?customer}}) ";
    private static final String FIND_ASSIGNED_COUPONS_BY_CUSTOMER = "SELECT {C:pk} FROM {CustomerCoupon AS C LEFT JOIN CustomerCoupon2Customer AS R ON {C:pk} = {R:source} LEFT JOIN Customer AS U ON {U:pk} = {R:target}} WHERE {U:pk} = ?customer AND {C:endDate} >= ?now ";
    private static final String BY_STARTDATE_ASC = "ORDER BY {startDate} ASC";
    private static final String BY_STARTDATE_DESC = "ORDER BY {startDate} DESC";
    private static final String BY_ENDDATE_ASC = "ORDER BY {endDate} ASC";
    private static final String BY_ENDDATE_DESC = "ORDER BY {endDate} DESC";
    private PagedFlexibleSearchService pagedFlexibleSearchService;
    private PaginatedFlexibleSearchService paginatedFlexibleSearchService;
    private Map<String, String> customerCouponSortCodeToQueryAlias;


    public DefaultCustomerCouponDao()
    {
        super("CustomerCoupon");
    }


    public Optional<PromotionSourceRuleModel> findPromotionSourceRuleByCode(String code)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(SEARCH_PROMOTION_RULE_QUERY);
        query.addQueryParameter("code", code);
        List<PromotionSourceRuleModel> result = getFlexibleSearchService().search(query).getResult();
        if(CollectionUtils.isNotEmpty(result))
        {
            return Optional.of(result.get(0));
        }
        return Optional.empty();
    }


    public List<PromotionSourceRuleModel> findPromotionSourceRuleByProduct(String productCode)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_PROMOTION_RULE_FOR_PRODUCT);
        query.addQueryParameter("productCode", productCode);
        return getFlexibleSearchService().search(query).getResult();
    }


    public List<PromotionSourceRuleModel> findExclPromotionSourceRuleByProduct(String productCode)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_EXCLUDED_PROMOTION_RULE_FOR_PRODUCT);
        query.addQueryParameter("productCode", productCode);
        return getFlexibleSearchService().search(query).getResult();
    }


    public List<PromotionSourceRuleModel> findPromotionSourceRuleByCategory(String categoryCode)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_PROMOTION_RULE_FOR_CATEGORY);
        query.addQueryParameter("categoryCode", categoryCode);
        return getFlexibleSearchService().search(query).getResult();
    }


    public List<PromotionSourceRuleModel> findExclPromotionSourceRuleByCategory(String categoryCode)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_EXCLUDED_PROMOTION_RULE_FOR_CATEGORY);
        query.addQueryParameter("categoryCode", categoryCode);
        return getFlexibleSearchService().search(query).getResult();
    }


    public List<CustomerCouponModel> findCustomerCouponByPromotionSourceRule(String code)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_CUSTOMER_COUPON_FOR_PROMOTION_RULE);
        query.addQueryParameter("code", code);
        return getFlexibleSearchService().search(query).getResult();
    }


    public List<ProductForPromotionSourceRuleModel> findProductForPromotionSourceRuleByPromotion(String code)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_PRODUCT_FOR_PROMOTION_RULE);
        query.addQueryParameter("code", code);
        return getFlexibleSearchService().search(query).getResult();
    }


    public List<CatForPromotionSourceRuleModel> findCategoryForPromotionSourceRuleByPromotion(String code)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_CATEGORY_FOR_PROMOTION_RULE);
        query.addQueryParameter("code", code);
        return getFlexibleSearchService().search(query).getResult();
    }


    public SearchPageData<CustomerCouponModel> findCustomerCouponsByCustomer(CustomerModel customer, PageableData pageableData)
    {
        List<SortQueryData> sortQueries = Arrays.asList(new SortQueryData[] {createSortQueryData("byStartDateAsc",
                        "select {cp:pk} from {CustomerCoupon as cp left join CustomerCoupon2Customer as ccr on {ccr:source} = {cp:pk} left join Customer as cst on {cst:pk} = {ccr:target}} where {active} = ?active and {endDate} > ?now and {cst:pk} = ?customer ORDER BY {startDate} ASC"),
                        createSortQueryData("byStartDateDesc",
                                        "select {cp:pk} from {CustomerCoupon as cp left join CustomerCoupon2Customer as ccr on {ccr:source} = {cp:pk} left join Customer as cst on {cst:pk} = {ccr:target}} where {active} = ?active and {endDate} > ?now and {cst:pk} = ?customer ORDER BY {startDate} DESC"),
                        createSortQueryData("byEndDateAsc",
                                        "select {cp:pk} from {CustomerCoupon as cp left join CustomerCoupon2Customer as ccr on {ccr:source} = {cp:pk} left join Customer as cst on {cst:pk} = {ccr:target}} where {active} = ?active and {endDate} > ?now and {cst:pk} = ?customer ORDER BY {endDate} ASC"),
                        createSortQueryData("byEndDateDesc",
                                        "select {cp:pk} from {CustomerCoupon as cp left join CustomerCoupon2Customer as ccr on {ccr:source} = {cp:pk} left join Customer as cst on {cst:pk} = {ccr:target}} where {active} = ?active and {endDate} > ?now and {cst:pk} = ?customer ORDER BY {endDate} DESC")});
        Map<String, Object> params = new HashMap<>(0);
        params.put("active", Boolean.TRUE);
        params.put("now", new Date());
        params.put("customer", customer);
        return getPagedFlexibleSearchService().search(sortQueries, "byEndDateAsc", params, pageableData);
    }


    protected SortQueryData createSortQueryData(String sortCode, String query)
    {
        SortQueryData result = new SortQueryData();
        result.setSortCode(sortCode);
        result.setQuery(query);
        return result;
    }


    public List<CustomerCouponModel> findEffectiveCustomerCouponsByCustomer(CustomerModel customer)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(
                        "select {cp.pk} from {CustomerCoupon as cp left join CustomerCoupon2Customer as ccr on {ccr:source} = {cp:pk} left join Customer as cst on {cst.pk} = {ccr:target} } where {active} = ?active and {endDate} > ?now and {startDate } < ?now and {cst.uid} = ?customerUid");
        query.addQueryParameter("active", Boolean.TRUE);
        query.addQueryParameter("now", new Date());
        query.addQueryParameter("customerUid", customer.getUid());
        return getFlexibleSearchService().search(query).getResult();
    }


    public boolean checkCustomerCouponAvailableForCustomer(String couponCode, CustomerModel customer)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(
                        "select {cp.pk} from {CustomerCoupon as cp left join CustomerCoupon2Customer as ccr on {ccr:source} = {cp:pk} left join Customer as cst on {cst.pk} = {ccr:target} } where {active} = ?active and {endDate} > ?now and {startDate } < ?now and {cst.uid} = ?customerUid and {couponId } = ?couponCode");
        query.addQueryParameter("active", Boolean.TRUE);
        query.addQueryParameter("now", new Date());
        query.addQueryParameter("customerUid", customer.getUid());
        query.addQueryParameter("couponCode", couponCode);
        int resultCount = getFlexibleSearchService().search(query).getTotalCount();
        return Boolean.valueOf((resultCount > 0)).booleanValue();
    }


    public int countAssignedCouponForCustomer(String couponCode, CustomerModel customer)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(
                        "select {cp.pk} from {CustomerCoupon as cp left join CustomerCoupon2Customer as ccr on {ccr:source} = {cp:pk} left join Customer as cst on {cst.pk} = {ccr:target} } where {active} = ?active and {cst.uid} = ?customerUid and {couponId } = ?couponCode");
        query.addQueryParameter("active", Boolean.TRUE);
        query.addQueryParameter("customerUid", customer.getUid());
        query.addQueryParameter("couponCode", couponCode);
        return getFlexibleSearchService().search(query).getTotalCount();
    }


    public List<CustomerCouponModel> findAssignableCoupons(CustomerModel customer, String text)
    {
        return findAssignmentCoupons(customer, text,
                        "SELECT {C:pk} FROM {CustomerCoupon AS C} WHERE {C:assignable} = ?assignable AND {C:startDate} IS NOT NULL AND {C:endDate} IS NOT NULL AND {C:endDate} >= ?now AND {C:active} = ?active AND {C:pk} NOT IN ({{SELECT {CC:pk} FROM {CustomerCoupon AS CC JOIN CUSTOMERCOUPON2CUSTOMER AS R ON {CC:pk} = {R:source} JOIN Customer AS U ON {R:target} = {U:pk}} WHERE {U:pk} = ?customer}}) ",
                        buildParamsMapForAssignableCoupons(customer));
    }


    public List<CustomerCouponModel> findAssignedCouponsByCustomer(CustomerModel customer, String text)
    {
        return findAssignmentCoupons(customer, text, "SELECT {C:pk} FROM {CustomerCoupon AS C LEFT JOIN CustomerCoupon2Customer AS R ON {C:pk} = {R:source} LEFT JOIN Customer AS U ON {U:pk} = {R:target}} WHERE {U:pk} = ?customer AND {C:endDate} >= ?now ",
                        buildQueryParamsForAssignedCoupons(customer));
    }


    protected Map<String, Object> buildParamsMapForAssignableCoupons(CustomerModel customer)
    {
        Map<String, Object> params = new HashMap<>(0);
        params.put("assignable", Boolean.TRUE);
        params.put("active", Boolean.TRUE);
        params.put("now", Calendar.getInstance().getTime());
        params.put("customer", customer);
        return params;
    }


    protected Map<String, Object> buildQueryParamsForAssignedCoupons(CustomerModel customer)
    {
        Map<String, Object> params = new HashMap<>(0);
        params.put("now", Calendar.getInstance().getTime());
        params.put("customer", customer);
        return params;
    }


    protected List<CustomerCouponModel> findAssignmentCoupons(CustomerModel customer, String text, String query, Map<String, Object> params)
    {
        StringBuilder fql = new StringBuilder(query);
        if(StringUtils.isNotBlank(text))
        {
            fql.append("AND (LOWER({C:");
            fql.append("couponId");
            fql.append("}) LIKE ?text OR LOWER({C:");
            fql.append("name");
            fql.append("}) LIKE ?text)");
            params.put("text", "%" + text.trim().toLowerCase(Locale.ROOT) + "%");
        }
        return getFlexibleSearchService().search(fql.toString(), params).getResult();
    }


    public SearchPageData<CustomerCouponModel> findPaginatedCouponsByCustomer(CustomerModel customer, SearchPageData searchPageData)
    {
        PaginatedFlexibleSearchParameter parameter = new PaginatedFlexibleSearchParameter();
        parameter.setSearchPageData(searchPageData);
        FlexibleSearchQuery query = new FlexibleSearchQuery("select {cp:pk} from {CustomerCoupon as cp left join CustomerCoupon2Customer as ccr on {ccr:source} = {cp:pk} left join Customer as cst on {cst:pk} = {ccr:target}} where {active} = ?active and {endDate} > ?now and {cst:pk} = ?customer ");
        query.addQueryParameter("active", Boolean.TRUE);
        query.addQueryParameter("now", Calendar.getInstance().getTime());
        query.addQueryParameter("customer", customer);
        parameter.setFlexibleSearchQuery(query);
        parameter.setSortCodeToQueryAlias(getCustomerCouponSortCodeToQueryAlias());
        return getPaginatedFlexibleSearchService().search(parameter);
    }


    protected PagedFlexibleSearchService getPagedFlexibleSearchService()
    {
        return this.pagedFlexibleSearchService;
    }


    public void setPagedFlexibleSearchService(PagedFlexibleSearchService pagedFlexibleSearchService)
    {
        this.pagedFlexibleSearchService = pagedFlexibleSearchService;
    }


    protected PaginatedFlexibleSearchService getPaginatedFlexibleSearchService()
    {
        return this.paginatedFlexibleSearchService;
    }


    public void setPaginatedFlexibleSearchService(PaginatedFlexibleSearchService paginatedFlexibleSearchService)
    {
        this.paginatedFlexibleSearchService = paginatedFlexibleSearchService;
    }


    protected Map<String, String> getCustomerCouponSortCodeToQueryAlias()
    {
        return this.customerCouponSortCodeToQueryAlias;
    }


    public void setCustomerCouponSortCodeToQueryAlias(Map<String, String> customerCouponSortCodeToQueryAlias)
    {
        this.customerCouponSortCodeToQueryAlias = customerCouponSortCodeToQueryAlias;
    }
}

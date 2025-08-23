package de.hybris.platform.couponservices.dao.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.couponservices.dao.CouponRedemptionDao;
import de.hybris.platform.couponservices.model.CouponRedemptionModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultCouponRedemptionDao extends DefaultGenericDao<CouponRedemptionModel> implements CouponRedemptionDao
{
    private static final String COUPONCODENOTNULL = "String couponCode cannot be null";
    protected static final String COUNT_COUPONREDEMPTIONS_BY_CODE = "SELECT COUNT({couponCode}) FROM {CouponRedemption}  WHERE {couponCode} = ?couponCode";


    public DefaultCouponRedemptionDao()
    {
        super("CouponRedemption");
    }


    public List<CouponRedemptionModel> findCouponRedemptionsByCode(String couponCode)
    {
        ServicesUtil.validateParameterNotNull(couponCode, "String couponCode cannot be null");
        Map<String, Object> params = new HashMap<>();
        params.put("couponCode", couponCode);
        return find(params);
    }


    public List<CouponRedemptionModel> findCouponRedemptionsByCodeAndOrder(String couponCode, AbstractOrderModel abstractOrder)
    {
        ServicesUtil.validateParameterNotNull(couponCode, "String couponCode cannot be null");
        Map<String, Object> params = new HashMap<>();
        params.put("couponCode", couponCode);
        params.put("order", abstractOrder);
        return find(params);
    }


    public List<CouponRedemptionModel> findCouponRedemptionsByCodeAndUser(String couponCode, UserModel user)
    {
        ServicesUtil.validateParameterNotNull(couponCode, "String couponCode cannot be null");
        Map<String, Object> params = new HashMap<>();
        params.put("couponCode", couponCode);
        params.put("user", user);
        return find(params);
    }


    public List<CouponRedemptionModel> findCouponRedemptionsByCodeOrderAndUser(String couponCode, AbstractOrderModel abstractOrder, UserModel user)
    {
        ServicesUtil.validateParameterNotNull(couponCode, "String couponCode cannot be null");
        Map<String, Object> params = new HashMap<>();
        params.put("couponCode", couponCode);
        params.put("order", abstractOrder);
        params.put("user", user);
        return find(params);
    }


    public Integer findCouponRedemptionsByCouponCode(String couponCode)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("couponCode", couponCode);
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT COUNT({couponCode}) FROM {CouponRedemption}  WHERE {couponCode} = ?couponCode", params);
        query.setResultClassList(Collections.singletonList(Integer.class));
        SearchResult<Integer> searchResult = getFlexibleSearchService().search(query);
        return searchResult.getResult().get(0);
    }
}

package de.hybris.platform.europe1.jalo;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import de.hybris.platform.core.PK;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

class DefaultPDTRowsQueryBuilder implements PDTRowsQueryBuilder
{
    public static final String USE_LEGACY_PRODUCTID_QUERY_STRATEGY = "europe1.use.legacy.productid.query.strategy";
    private final String type;
    private boolean anyProduct;
    private PK productPk;
    private PK productGroupPk;
    private String productId;
    private boolean anyUser;
    private PK userPk;
    private PK userGroupPk;
    private final List<String> returnAttributes;
    private final List<Class> returnAttributeTypes;


    public DefaultPDTRowsQueryBuilder(String type)
    {
        this(type, (Map<String, Class>)ImmutableMap.of("PK", PK.class));
    }


    protected DefaultPDTRowsQueryBuilder(String type, Map<String, Class<?>> attributes)
    {
        this.type = Objects.<String>requireNonNull(type);
        this.returnAttributes = Lists.newArrayList(((Map)Objects.<Map>requireNonNull(attributes)).keySet());
        Objects.requireNonNull(attributes);
        this.returnAttributeTypes = (List<Class>)this.returnAttributes.stream().map(attributes::get).collect(Collectors.toList());
    }


    public PDTRowsQueryBuilder withAnyProduct()
    {
        this.anyProduct = true;
        return this;
    }


    public PDTRowsQueryBuilder withProduct(PK productPk)
    {
        this.productPk = productPk;
        return this;
    }


    public PDTRowsQueryBuilder withProductGroup(PK productGroupPk)
    {
        this.productGroupPk = productGroupPk;
        return this;
    }


    public PDTRowsQueryBuilder withProductId(String productId)
    {
        this.productId = productId;
        return this;
    }


    public PDTRowsQueryBuilder withAnyUser()
    {
        this.anyUser = true;
        return this;
    }


    public PDTRowsQueryBuilder withUser(PK userPk)
    {
        this.userPk = userPk;
        return this;
    }


    public PDTRowsQueryBuilder withUserGroup(PK userGroupPk)
    {
        this.userGroupPk = userGroupPk;
        return this;
    }


    public PDTRowsQueryBuilder.QueryWithParams build()
    {
        if(Config.getBoolean("europe1.use.legacy.productid.query.strategy", false))
        {
            return buildLegacy();
        }
        StringBuilder query = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> productParams = getProductRelatedParameters();
        Map<String, Object> userParams = getUserRelatedParameters();
        boolean addPricesByProductId = (this.productId != null);
        boolean matchByProduct = (!productParams.isEmpty() || addPricesByProductId);
        boolean matchByUser = !userParams.isEmpty();
        String columns = this.returnAttributes.stream().map(s -> "{" + s + "} AS col_" + s).collect(Collectors.joining(","));
        if(!matchByProduct && !matchByUser)
        {
            return new PDTRowsQueryBuilder.QueryWithParams(generateSelect(columns), Collections.emptyMap(), this.returnAttributeTypes);
        }
        List<String> productConditions = new ArrayList<>();
        List<String> userConditions = new ArrayList<>();
        List<String> byProductIdConditions = new ArrayList<>();
        if(addPricesByProductId)
        {
            byProductIdConditions.add(String.format("{%s}=?matchByProductId", new Object[] {"productMatchQualifier"}));
            byProductIdConditions.add(String.format("{%s}=?%s", new Object[] {"productId", "productId"}));
            params.put("matchByProductId", Long.valueOf(Europe1PriceFactory.MATCH_BY_PRODUCT_ID));
            params.put("productId", this.productId);
        }
        query.append(generateSelect(columns)).append(" where ");
        if(!productParams.isEmpty())
        {
            if(addPricesByProductId)
            {
                query.append("( ");
            }
            productConditions.add(createInCondition(productParams, "productMatchQualifier"));
            query.append(joinWithAnd(productConditions));
            if(addPricesByProductId)
            {
                query.append(" or ( ");
                query.append(joinWithAnd(byProductIdConditions));
                query.append(" ))");
            }
            params.putAll(productParams);
        }
        else if(addPricesByProductId)
        {
            query.append(joinWithAnd(byProductIdConditions));
        }
        if(matchByUser)
        {
            if(matchByProduct)
            {
                query.append(" and ");
            }
            userConditions.add(createInCondition(userParams, "userMatchQualifier"));
            params.putAll(userParams);
            query.append(joinWithAnd(userConditions));
        }
        return new PDTRowsQueryBuilder.QueryWithParams(query.toString(), Collections.unmodifiableMap(params), this.returnAttributeTypes);
    }


    private static String joinWithAnd(List<String> userConditions)
    {
        return userConditions.stream().collect(Collectors.joining(" and "));
    }


    private String generateSelect(String columns)
    {
        return "select " + columns + " from {" + this.type + "}";
    }


    private static String createInCondition(Map<String, Object> userParams, String columnName)
    {
        return String.format("{%s} in %s", new Object[] {columnName, userParams
                        .keySet().stream().collect(Collectors.joining(", ?", "(?", ")"))});
    }


    private Map<String, Object> getProductRelatedParameters()
    {
        ImmutableMap.Builder<String, Object> params = ImmutableMap.builder();
        if(this.anyProduct)
        {
            params.put("anyProduct", Long.valueOf(Europe1PriceFactory.MATCH_ANY));
        }
        if(this.productPk != null)
        {
            params.put("product", this.productPk.getLong());
        }
        if(this.productGroupPk != null)
        {
            params.put("productGroup", this.productGroupPk.getLong());
        }
        return (Map<String, Object>)params.build();
    }


    private Map<String, Object> getUserRelatedParameters()
    {
        ImmutableMap.Builder<String, Object> params = ImmutableMap.builder();
        if(this.anyUser)
        {
            params.put("anyUser", Long.valueOf(Europe1PriceFactory.MATCH_ANY));
        }
        if(this.userPk != null)
        {
            params.put("user", this.userPk.getLong());
        }
        if(this.userGroupPk != null)
        {
            params.put("userGroup", this.userGroupPk.getLong());
        }
        return (Map<String, Object>)params.build();
    }


    private PDTRowsQueryBuilder.QueryWithParams buildLegacy()
    {
        StringBuilder resultQuery, query = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> productParams = getProductRelatedParameters();
        Map<String, Object> userParams = getUserRelatedParameters();
        boolean addPricesByProductId = (this.productId != null);
        boolean isUnion = false;
        boolean matchByProduct = !productParams.isEmpty();
        boolean matchByUser = !userParams.isEmpty();
        String columns = this.returnAttributes.stream().map(s -> "{" + s + "} AS col_" + s).collect(Collectors.joining(","));
        if(!matchByProduct && !matchByUser && !addPricesByProductId)
        {
            return new PDTRowsQueryBuilder.QueryWithParams("select " + columns + " from {" + this.type + "}", Collections.emptyMap(), this.returnAttributeTypes);
        }
        if(matchByProduct || matchByUser)
        {
            List<String> basicConditions = new ArrayList<>();
            query.append("select ").append(columns).append(" from {").append(this.type).append("} where ");
            if(matchByProduct)
            {
                basicConditions.add(createInCondition(productParams, "productMatchQualifier"));
                params.putAll(productParams);
            }
            if(matchByUser)
            {
                basicConditions.add(createInCondition(userParams, "userMatchQualifier"));
                params.putAll(userParams);
            }
            query.append(basicConditions.stream().collect(Collectors.joining(" and ")));
        }
        if(addPricesByProductId)
        {
            if(matchByProduct || matchByUser)
            {
                query.append("}} UNION {{");
                isUnion = true;
            }
            List<String> byProductIdConditions = new ArrayList<>();
            query.append("select ").append(columns).append(" from {").append(this.type).append("} where ");
            byProductIdConditions.add(String.format("{%s}=?matchByProductId", new Object[] {"productMatchQualifier"}));
            byProductIdConditions.add(String.format("{%s}=?%s", new Object[] {"productId", "productId"}));
            params.put("matchByProductId", Long.valueOf(Europe1PriceFactory.MATCH_BY_PRODUCT_ID));
            params.put("productId", this.productId);
            if(matchByUser)
            {
                byProductIdConditions.add(createInCondition(userParams, "userMatchQualifier"));
            }
            query.append(byProductIdConditions.stream().collect(Collectors.joining(" and ")));
        }
        if(isUnion)
        {
            String unionColumns = this.returnAttributes.stream().map(s -> "x.col_" + s).collect(Collectors.joining(","));
            resultQuery = (new StringBuilder("select ")).append(unionColumns).append(" from ({{").append(query).append("}}) x");
        }
        else
        {
            resultQuery = query;
        }
        return new PDTRowsQueryBuilder.QueryWithParams(resultQuery.toString(), Collections.unmodifiableMap(params), this.returnAttributeTypes);
    }
}

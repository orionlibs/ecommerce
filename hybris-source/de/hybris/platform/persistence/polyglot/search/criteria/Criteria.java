package de.hybris.platform.persistence.polyglot.search.criteria;

import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;
import de.hybris.platform.persistence.polyglot.model.TypeId;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.map.CaseInsensitiveMap;

public final class Criteria
{
    private final TypeId typeId;
    private final List<TypeId> subtypeIds;
    private final Condition condition;
    private final OrderBy orderBy;
    private final List<SingleAttributeKey> requestedKeys;
    private final Map<String, Object> params;
    private final boolean needTotal;
    private final boolean typeExclusive;
    private final int start;
    private final int count;


    Criteria(CriteriaBuilder builder)
    {
        this.typeId = builder.getTypeId();
        this.subtypeIds = builder.getSubtypeIds();
        this.condition = builder.getCondition();
        this.orderBy = builder.getOrderBy();
        this.params = (Map<String, Object>)new CaseInsensitiveMap(builder.getParams());
        this.requestedKeys = builder.getRequestedKeys();
        this.needTotal = builder.isNeedTotal();
        this.start = builder.getStart();
        this.count = builder.getCount();
        this.typeExclusive = builder.isTypeExclusive();
        validateParametersForCriteria();
    }


    public static CriteriaBuilder builder(TypeId typeId, Collection<TypeId> subtypeIds)
    {
        return new CriteriaBuilder(typeId, subtypeIds);
    }


    public static CriteriaBuilder newBuilderFromTemplate(Criteria template)
    {
        return (new CriteriaBuilder(template.getTypeId(), template.getSubtypeIds())).withCondition(template.getCondition())
                        .withRequestedKeys(template.getRequestedKeys())
                        .withParameters(template.getParams())
                        .withStart(template.getStart())
                        .withCount(template.getCount())
                        .withNeedTotal(template.isNeedTotal())
                        .withOrderBy(template.getOrderBy());
    }


    public Identity getTypeIdentity()
    {
        return this.typeId.getIdentity();
    }


    public Set<Identity> getTypeIdentitySet()
    {
        HashSet<Identity> result = new HashSet<>(this.subtypeIds.size() + 1);
        result.add(getTypeIdentity());
        this.subtypeIds.forEach(id -> result.add(id.getIdentity()));
        return result;
    }


    public TypeId getTypeId()
    {
        return this.typeId;
    }


    public boolean isTypeExclusive()
    {
        return this.typeExclusive;
    }


    public List<TypeId> getSubtypeIds()
    {
        return this.subtypeIds;
    }


    public boolean hasCondition()
    {
        return !(this.condition instanceof Conditions.EmptyCondition);
    }


    public Condition getCondition()
    {
        return this.condition;
    }


    public boolean hasOrderBy()
    {
        return (this.orderBy.getElementCount() > 0);
    }


    public OrderBy getOrderBy()
    {
        return this.orderBy;
    }


    public Map<String, Object> getParams()
    {
        return this.params;
    }


    public List<SingleAttributeKey> getRequestedKeys()
    {
        return this.requestedKeys;
    }


    public boolean isNeedTotal()
    {
        return this.needTotal;
    }


    public int getStart()
    {
        return this.start;
    }


    public int getCount()
    {
        return this.count;
    }


    private void validateParametersForCriteria()
    {
        QueryParamsValidatorVisitor validator = new QueryParamsValidatorVisitor(getParams().keySet());
        getCondition().visit((ConditionVisitor)validator);
        if(!validator.getNonexistentParamNames().isEmpty())
        {
            throw new PolyglotCriteriaValidationException("missing values for " + validator.getNonexistentParamNames());
        }
    }
}

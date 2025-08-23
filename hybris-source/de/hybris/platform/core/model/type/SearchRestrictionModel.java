package de.hybris.platform.core.model.type;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SearchRestrictionModel extends TypeManagerManagedModel
{
    public static final String _TYPECODE = "SearchRestriction";
    public static final String _PRINCIPAL2SEARCHRESTRICTIONRELATION = "Principal2SearchRestrictionRelation";
    public static final String CODE = "code";
    public static final String ACTIVE = "active";
    public static final String PRINCIPAL = "principal";
    public static final String QUERY = "query";
    public static final String RESTRICTEDTYPE = "restrictedType";


    public SearchRestrictionModel()
    {
    }


    public SearchRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SearchRestrictionModel(String _code, Boolean _generate, PrincipalModel _principal, String _query, ComposedTypeModel _restrictedType)
    {
        setCode(_code);
        setGenerate(_generate);
        setPrincipal(_principal);
        setQuery(_query);
        setRestrictedType(_restrictedType);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SearchRestrictionModel(String _code, Boolean _generate, ItemModel _owner, PrincipalModel _principal, String _query, ComposedTypeModel _restrictedType)
    {
        setCode(_code);
        setGenerate(_generate);
        setOwner(_owner);
        setPrincipal(_principal);
        setQuery(_query);
        setRestrictedType(_restrictedType);
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public Boolean getActive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("active");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "principal", type = Accessor.Type.GETTER)
    public PrincipalModel getPrincipal()
    {
        return (PrincipalModel)getPersistenceContext().getPropertyValue("principal");
    }


    @Accessor(qualifier = "query", type = Accessor.Type.GETTER)
    public String getQuery()
    {
        return (String)getPersistenceContext().getPropertyValue("query");
    }


    @Accessor(qualifier = "restrictedType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getRestrictedType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("restrictedType");
    }


    @Accessor(qualifier = "active", type = Accessor.Type.SETTER)
    public void setActive(Boolean value)
    {
        getPersistenceContext().setPropertyValue("active", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "principal", type = Accessor.Type.SETTER)
    public void setPrincipal(PrincipalModel value)
    {
        getPersistenceContext().setPropertyValue("principal", value);
    }


    @Accessor(qualifier = "query", type = Accessor.Type.SETTER)
    public void setQuery(String value)
    {
        getPersistenceContext().setPropertyValue("query", value);
    }


    @Accessor(qualifier = "restrictedType", type = Accessor.Type.SETTER)
    public void setRestrictedType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("restrictedType", value);
    }
}

package de.hybris.platform.advancedsavedquery.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.advancedsavedquery.enums.AdvancedQueryComparatorEnum;
import de.hybris.platform.advancedsavedquery.enums.EmptyParamEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class AbstractAdvancedSavedQuerySearchParameterModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractAdvancedSavedQuerySearchParameter";
    public static final String _WHEREPART2SEARCHPARAMETERRELATION = "WherePart2SearchParameterRelation";
    public static final String COMPARATOR = "comparator";
    public static final String EMPTYHANDLING = "emptyHandling";
    public static final String VALUETYPE = "valueType";
    public static final String SEARCHPARAMETERNAME = "searchParameterName";
    public static final String JOINALIAS = "joinAlias";
    public static final String NAME = "name";
    public static final String LOWER = "lower";
    public static final String WHEREPART = "wherePart";


    public AbstractAdvancedSavedQuerySearchParameterModel()
    {
    }


    public AbstractAdvancedSavedQuerySearchParameterModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractAdvancedSavedQuerySearchParameterModel(String _searchParameterName, WherePartModel _wherePart)
    {
        setSearchParameterName(_searchParameterName);
        setWherePart(_wherePart);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractAdvancedSavedQuerySearchParameterModel(ItemModel _owner, String _searchParameterName, WherePartModel _wherePart)
    {
        setOwner(_owner);
        setSearchParameterName(_searchParameterName);
        setWherePart(_wherePart);
    }


    @Accessor(qualifier = "comparator", type = Accessor.Type.GETTER)
    public AdvancedQueryComparatorEnum getComparator()
    {
        return (AdvancedQueryComparatorEnum)getPersistenceContext().getPropertyValue("comparator");
    }


    @Accessor(qualifier = "emptyHandling", type = Accessor.Type.GETTER)
    public EmptyParamEnum getEmptyHandling()
    {
        return (EmptyParamEnum)getPersistenceContext().getPropertyValue("emptyHandling");
    }


    @Accessor(qualifier = "joinAlias", type = Accessor.Type.GETTER)
    public String getJoinAlias()
    {
        return (String)getPersistenceContext().getPropertyValue("joinAlias");
    }


    @Accessor(qualifier = "lower", type = Accessor.Type.GETTER)
    public Boolean getLower()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("lower");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "searchParameterName", type = Accessor.Type.GETTER)
    public String getSearchParameterName()
    {
        return (String)getPersistenceContext().getPropertyValue("searchParameterName");
    }


    @Accessor(qualifier = "valueType", type = Accessor.Type.GETTER)
    public TypeModel getValueType()
    {
        return (TypeModel)getPersistenceContext().getPropertyValue("valueType");
    }


    @Accessor(qualifier = "wherePart", type = Accessor.Type.GETTER)
    public WherePartModel getWherePart()
    {
        return (WherePartModel)getPersistenceContext().getPropertyValue("wherePart");
    }


    @Accessor(qualifier = "comparator", type = Accessor.Type.SETTER)
    public void setComparator(AdvancedQueryComparatorEnum value)
    {
        getPersistenceContext().setPropertyValue("comparator", value);
    }


    @Accessor(qualifier = "emptyHandling", type = Accessor.Type.SETTER)
    public void setEmptyHandling(EmptyParamEnum value)
    {
        getPersistenceContext().setPropertyValue("emptyHandling", value);
    }


    @Accessor(qualifier = "joinAlias", type = Accessor.Type.SETTER)
    public void setJoinAlias(String value)
    {
        getPersistenceContext().setPropertyValue("joinAlias", value);
    }


    @Accessor(qualifier = "lower", type = Accessor.Type.SETTER)
    public void setLower(Boolean value)
    {
        getPersistenceContext().setPropertyValue("lower", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "searchParameterName", type = Accessor.Type.SETTER)
    public void setSearchParameterName(String value)
    {
        getPersistenceContext().setPropertyValue("searchParameterName", value);
    }


    @Accessor(qualifier = "valueType", type = Accessor.Type.SETTER)
    public void setValueType(TypeModel value)
    {
        getPersistenceContext().setPropertyValue("valueType", value);
    }


    @Accessor(qualifier = "wherePart", type = Accessor.Type.SETTER)
    public void setWherePart(WherePartModel value)
    {
        getPersistenceContext().setPropertyValue("wherePart", value);
    }
}

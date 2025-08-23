package de.hybris.platform.impex.jalo.imp;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Address;
import java.util.Collection;
import java.util.Map;

public class AmbiguousItemException extends ImpExException
{
    private final ComposedType targetType;
    private final Collection<Item> items;
    private final String query;
    private final Map<String, Object> searchValues;


    public AmbiguousItemException(ComposedType targetType, Collection<Item> items, String query, Map<String, Object> searchValues)
    {
        super(null, "More than one item of type " + targetType
                        .getCode() + " found for unique qualifiers " + searchValues + (
                        TypeManager.getInstance().getComposedType(Address.class).isAssignableFrom((Type)targetType) ?
                                        ". Please try adding the attribute \"duplicate[unique=true,default=false]\" to your Address header." :
                                        ""), 0);
        this.targetType = targetType;
        this.items = items;
        this.query = query;
        this.searchValues = searchValues;
    }


    public Collection<Item> getItems()
    {
        return this.items;
    }


    public String getQuery()
    {
        return this.query;
    }


    public Map<String, Object> getSearchValues()
    {
        return this.searchValues;
    }


    public ComposedType getTargetType()
    {
        return this.targetType;
    }
}

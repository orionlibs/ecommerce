package de.hybris.platform.impex;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Map;

public interface ImpExImportCUDHandler
{
    void delete(Item paramItem, ValueLine paramValueLine) throws ConsistencyCheckException;


    void update(Item paramItem, Map<String, Object> paramMap, ValueLine paramValueLine) throws ImpExException;


    Item create(ComposedType paramComposedType, Map<String, Object> paramMap, ValueLine paramValueLine) throws ImpExException;
}

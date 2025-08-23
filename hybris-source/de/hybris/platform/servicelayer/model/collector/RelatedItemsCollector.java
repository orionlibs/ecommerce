package de.hybris.platform.servicelayer.model.collector;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.visitor.ItemVisitor;
import java.util.List;
import java.util.Map;

public interface RelatedItemsCollector
{
    public static final String MAX_RECURSION_DEPTH = "maxRecursionDepth";


    <T extends ItemModel> List<ItemModel> collect(T paramT, Map<String, Object> paramMap);


    <T extends ItemModel> List<ItemModel> collect(T paramT, ItemVisitor<T> paramItemVisitor, Map<String, Object> paramMap);
}

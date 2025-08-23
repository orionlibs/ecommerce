package de.hybris.platform.servicelayer.model.visitor;

import de.hybris.platform.core.model.ItemModel;
import java.util.List;
import java.util.Map;

public interface ItemVisitor<T extends ItemModel>
{
    List<ItemModel> visit(T paramT, List<ItemModel> paramList, Map<String, Object> paramMap);
}

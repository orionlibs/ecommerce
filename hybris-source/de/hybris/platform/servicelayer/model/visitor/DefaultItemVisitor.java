package de.hybris.platform.servicelayer.model.visitor;

import de.hybris.platform.core.model.ItemModel;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DefaultItemVisitor implements ItemVisitor<ItemModel>
{
    public List<ItemModel> visit(ItemModel theSource, List<ItemModel> parents, Map<String, Object> ctx)
    {
        return Collections.emptyList();
    }
}

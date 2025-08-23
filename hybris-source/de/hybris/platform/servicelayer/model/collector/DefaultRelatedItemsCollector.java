package de.hybris.platform.servicelayer.model.collector;

import com.google.common.collect.Lists;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.visitor.ItemVisitor;
import de.hybris.platform.servicelayer.model.visitor.ItemVisitorRegistry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRelatedItemsCollector implements RelatedItemsCollector
{
    private ItemVisitorRegistry itemVisitorRegistry;


    public <T extends ItemModel> List<ItemModel> collect(T theSource, Map<String, Object> ctx)
    {
        List<ItemModel> ret = new ArrayList<>();
        collectInternal((ItemModel)theSource, ret, ctx, 0);
        return ret;
    }


    public <T extends ItemModel> List<ItemModel> collect(T theSource, ItemVisitor<T> theStrategy, Map<String, Object> ctx)
    {
        List<ItemModel> collected = Lists.newArrayList();
        if(theSource != null)
        {
            collected.add((ItemModel)theSource);
            for(ItemModel itemModel : theStrategy.visit((ItemModel)theSource, collected, ctx))
            {
                collectInternal(itemModel, collected, ctx, 1);
            }
        }
        return collected;
    }


    protected void collectInternal(ItemModel theSource, List<ItemModel> collected, Map<String, Object> ctx, int currentDepth)
    {
        if(theSource == null || collected.contains(theSource) || currentDepth > extractMaxRecursionDepth(ctx))
        {
            return;
        }
        ItemVisitor<ItemModel> theStrategy = this.itemVisitorRegistry.getVisitorByTypeCode(theSource.getItemtype());
        List<ItemModel> parents = Collections.unmodifiableList(Lists.newArrayList(collected));
        collected.add(theSource);
        currentDepth++;
        for(ItemModel theChild : theStrategy.visit(theSource, parents, ctx))
        {
            collectInternal(theChild, collected, ctx, currentDepth);
        }
    }


    protected int extractMaxRecursionDepth(Map<String, Object> ctx)
    {
        return NumberUtils.toInt(ObjectUtils.toString(ctx.get("maxRecursionDepth")), 10);
    }


    @Required
    public void setItemVisitorRegistry(ItemVisitorRegistry itemVisitorRegistry)
    {
        this.itemVisitorRegistry = itemVisitorRegistry;
    }
}

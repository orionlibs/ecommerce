package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class CachedPromotionResultModel extends PromotionResultModel
{
    public static final String _TYPECODE = "CachedPromotionResult";
    public static final String CACHEDACTIONS = "cachedActions";
    public static final String CACHEDCONSUMEDENTRIES = "cachedConsumedEntries";


    public CachedPromotionResultModel()
    {
    }


    public CachedPromotionResultModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CachedPromotionResultModel(AbstractOrderModel _order)
    {
        setOrder(_order);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CachedPromotionResultModel(AbstractOrderModel _order, ItemModel _owner)
    {
        setOrder(_order);
        setOwner(_owner);
    }


    @Accessor(qualifier = "cachedActions", type = Accessor.Type.GETTER)
    public Collection<AbstractPromotionActionModel> getCachedActions()
    {
        return (Collection<AbstractPromotionActionModel>)getPersistenceContext().getPropertyValue("cachedActions");
    }


    @Accessor(qualifier = "cachedConsumedEntries", type = Accessor.Type.GETTER)
    public Collection<CachedPromotionOrderEntryConsumedModel> getCachedConsumedEntries()
    {
        return (Collection<CachedPromotionOrderEntryConsumedModel>)getPersistenceContext().getPropertyValue("cachedConsumedEntries");
    }


    @Accessor(qualifier = "cachedActions", type = Accessor.Type.SETTER)
    public void setCachedActions(Collection<AbstractPromotionActionModel> value)
    {
        getPersistenceContext().setPropertyValue("cachedActions", value);
    }


    @Accessor(qualifier = "cachedConsumedEntries", type = Accessor.Type.SETTER)
    public void setCachedConsumedEntries(Collection<CachedPromotionOrderEntryConsumedModel> value)
    {
        getPersistenceContext().setPropertyValue("cachedConsumedEntries", value);
    }
}

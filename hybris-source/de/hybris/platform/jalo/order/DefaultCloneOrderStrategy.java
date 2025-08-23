package de.hybris.platform.jalo.order;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.ItemCloneCreator;
import de.hybris.platform.jalo.type.TypeManager;

public class DefaultCloneOrderStrategy implements CloneOrderStrategy<Order>
{
    private final ItemCloneCreator cloneCreator = new ItemCloneCreator();


    public Order clone(ComposedType _orderType, ComposedType _entryType, AbstractOrder original, OrderManager manager)
    {
        ComposedType orderType = (_orderType != null) ? _orderType : ((original instanceof Order) ? original.getComposedType() : TypeManager.getInstance().getComposedType(Order.class));
        ComposedType entryType = (_entryType != null) ? _entryType : ((original instanceof Order) ? TypeManager.getInstance().getComposedType(original.getAbstractOrderEntryTypeCode()) : TypeManager.getInstance().getComposedType(OrderEntry.class));
        Object object = new Object(this, entryType);
        object.addCopyUntypedPropsFor((Item)original);
        object.addCopyUntypedPropsFor(original.getAllEntries());
        try
        {
            Order ret = (Order)this.cloneCreator.copy(orderType, (Item)original, (ItemCloneCreator.CopyContext)object);
            OrderCloneHelper.postProcess(original, (AbstractOrder)ret);
            return ret;
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
    }
}

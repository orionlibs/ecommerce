package de.hybris.platform.jalo.order;

import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.ItemCloneCreator;
import de.hybris.platform.jalo.type.TypeManager;

public class DefaultCloneCartStrategy implements CloneCartStrategy<Cart>
{
    private final ItemCloneCreator cloneCreator = new ItemCloneCreator();


    public Cart clone(ComposedType _type, ComposedType _entryType, AbstractOrder original, String code, OrderManager manager) throws ConsistencyCheckException
    {
        ComposedType cartType = (_type != null) ? _type : ((original instanceof Cart) ? original.getComposedType() : TypeManager.getInstance().getComposedType(Cart.class));
        ComposedType entryType = (_entryType != null) ? _entryType : ((original instanceof Cart) ? TypeManager.getInstance().getComposedType(original.getAbstractOrderEntryTypeCode()) : TypeManager.getInstance().getComposedType(CartEntry.class));
        Object object = new Object(this, entryType);
        object.addPreset((Item)original, "code", manager.adjustOrderCode(code));
        object.addCopyUntypedPropsFor((Item)original);
        object.addCopyUntypedPropsFor(original.getAllEntries());
        try
        {
            Cart ret = (Cart)this.cloneCreator.copy(cartType, (Item)original, (ItemCloneCreator.CopyContext)object);
            OrderCloneHelper.postProcess(original, (AbstractOrder)ret);
            return ret;
        }
        catch(JaloBusinessException e)
        {
            if(e instanceof ConsistencyCheckException)
            {
                throw (ConsistencyCheckException)e;
            }
            throw new JaloSystemException(e);
        }
    }
}

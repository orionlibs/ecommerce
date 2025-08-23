package de.hybris.platform.servicelayer.model.strategies;

import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import java.io.ObjectStreamException;

public class SerializationStrategyLeanImpl implements SerializationStrategy
{
    public Object readResolve(AbstractItemModel aim) throws ObjectStreamException
    {
        return aim;
    }


    public Object writeReplace(AbstractItemModel aim) throws ObjectStreamException
    {
        ItemModelContext context = ModelContextUtils.getItemModelContext(aim);
        if(context.isDirty() || context.isNew() || context.isRemoved())
        {
            throw new IllegalStateException("model " + aim + " cannot be serialized due to being modified, new or removed");
        }
        return new LeanItemModelHandler(context.getPK(), context.getTenantId());
    }
}

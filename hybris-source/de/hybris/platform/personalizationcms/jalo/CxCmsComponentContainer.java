package de.hybris.platform.personalizationcms.jalo;

import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collections;
import java.util.List;

public class CxCmsComponentContainer extends GeneratedCxCmsComponentContainer
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item item = super.createItem(ctx, type, allAttributes);
        return item;
    }


    public List<SimpleCMSComponent> getCurrentCMSComponents(SessionContext arg0)
    {
        SimpleCMSComponent c = getDefaultCmsComponent();
        if(c != null)
        {
            return Collections.singletonList(c);
        }
        return Collections.emptyList();
    }
}

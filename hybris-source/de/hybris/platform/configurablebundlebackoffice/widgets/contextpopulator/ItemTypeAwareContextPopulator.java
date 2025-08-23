package de.hybris.platform.configurablebundlebackoffice.widgets.contextpopulator;

import com.hybris.backoffice.widgets.contextpopulator.DefaultContextPopulator;
import de.hybris.platform.core.model.ItemModel;
import java.util.Map;

public class ItemTypeAwareContextPopulator extends DefaultContextPopulator
{
    private static final String ITEM_TYPE = "itemType";


    public Map<String, Object> populate(Object data)
    {
        Map<String, Object> context = super.populate(data);
        if(data instanceof ItemModel)
        {
            context.put("itemType", ((ItemModel)data).getItemtype());
        }
        return context;
    }
}

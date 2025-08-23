package de.hybris.platform.processengine.model;

import de.hybris.platform.core.model.AbstractDynamicContentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class DynamicProcessDefinitionModel extends AbstractDynamicContentModel
{
    public static final String _TYPECODE = "DynamicProcessDefinition";


    public DynamicProcessDefinitionModel()
    {
    }


    public DynamicProcessDefinitionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DynamicProcessDefinitionModel(String _code, String _content)
    {
        setCode(_code);
        setContent(_content);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DynamicProcessDefinitionModel(String _code, String _content, ItemModel _owner)
    {
        setCode(_code);
        setContent(_content);
        setOwner(_owner);
    }
}

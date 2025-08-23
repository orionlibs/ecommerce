package de.hybris.platform.processengine.model;

import de.hybris.platform.core.model.AbstractDynamicContentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class BackofficeThemeConfigModel extends AbstractDynamicContentModel
{
    public static final String _TYPECODE = "BackofficeThemeConfig";


    public BackofficeThemeConfigModel()
    {
    }


    public BackofficeThemeConfigModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BackofficeThemeConfigModel(String _code, String _content)
    {
        setCode(_code);
        setContent(_content);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BackofficeThemeConfigModel(String _code, String _content, ItemModel _owner)
    {
        setCode(_code);
        setContent(_content);
        setOwner(_owner);
    }
}

package de.hybris.platform.cms2.model.contents.components;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class CMSParagraphComponentModel extends SimpleCMSComponentModel
{
    public static final String _TYPECODE = "CMSParagraphComponent";
    public static final String CONTENT = "content";


    public CMSParagraphComponentModel()
    {
    }


    public CMSParagraphComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSParagraphComponentModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSParagraphComponentModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "content", type = Accessor.Type.GETTER)
    public String getContent()
    {
        return getContent(null);
    }


    @Accessor(qualifier = "content", type = Accessor.Type.GETTER)
    public String getContent(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("content", loc);
    }


    @Accessor(qualifier = "content", type = Accessor.Type.SETTER)
    public void setContent(String value)
    {
        setContent(value, null);
    }


    @Accessor(qualifier = "content", type = Accessor.Type.SETTER)
    public void setContent(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("content", loc, value);
    }
}

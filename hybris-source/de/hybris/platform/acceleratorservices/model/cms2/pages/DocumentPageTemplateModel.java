package de.hybris.platform.acceleratorservices.model.cms2.pages;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class DocumentPageTemplateModel extends PageTemplateModel
{
    public static final String _TYPECODE = "DocumentPageTemplate";
    public static final String HTMLTEMPLATE = "htmlTemplate";


    public DocumentPageTemplateModel()
    {
    }


    public DocumentPageTemplateModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DocumentPageTemplateModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DocumentPageTemplateModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "htmlTemplate", type = Accessor.Type.GETTER)
    public RendererTemplateModel getHtmlTemplate()
    {
        return (RendererTemplateModel)getPersistenceContext().getPropertyValue("htmlTemplate");
    }


    @Accessor(qualifier = "htmlTemplate", type = Accessor.Type.SETTER)
    public void setHtmlTemplate(RendererTemplateModel value)
    {
        getPersistenceContext().setPropertyValue("htmlTemplate", value);
    }
}

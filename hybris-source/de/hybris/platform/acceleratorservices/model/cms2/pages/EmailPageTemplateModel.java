package de.hybris.platform.acceleratorservices.model.cms2.pages;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.commons.model.renderer.RendererTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class EmailPageTemplateModel extends DocumentPageTemplateModel
{
    public static final String _TYPECODE = "EmailPageTemplate";
    public static final String SUBJECT = "subject";


    public EmailPageTemplateModel()
    {
    }


    public EmailPageTemplateModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EmailPageTemplateModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EmailPageTemplateModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "subject", type = Accessor.Type.GETTER)
    public RendererTemplateModel getSubject()
    {
        return (RendererTemplateModel)getPersistenceContext().getPropertyValue("subject");
    }


    @Accessor(qualifier = "subject", type = Accessor.Type.SETTER)
    public void setSubject(RendererTemplateModel value)
    {
        getPersistenceContext().setPropertyValue("subject", value);
    }
}

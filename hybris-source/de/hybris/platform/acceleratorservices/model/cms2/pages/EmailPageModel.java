package de.hybris.platform.acceleratorservices.model.cms2.pages;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class EmailPageModel extends DocumentPageModel
{
    public static final String _TYPECODE = "EmailPage";
    public static final String FROMEMAIL = "fromEmail";
    public static final String FROMNAME = "fromName";


    public EmailPageModel()
    {
    }


    public EmailPageModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EmailPageModel(CatalogVersionModel _catalogVersion, PageTemplateModel _masterTemplate, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setMasterTemplate(_masterTemplate);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EmailPageModel(CatalogVersionModel _catalogVersion, PageTemplateModel _masterTemplate, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setMasterTemplate(_masterTemplate);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "fromEmail", type = Accessor.Type.GETTER)
    public String getFromEmail()
    {
        return getFromEmail(null);
    }


    @Accessor(qualifier = "fromEmail", type = Accessor.Type.GETTER)
    public String getFromEmail(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("fromEmail", loc);
    }


    @Accessor(qualifier = "fromName", type = Accessor.Type.GETTER)
    public String getFromName()
    {
        return getFromName(null);
    }


    @Accessor(qualifier = "fromName", type = Accessor.Type.GETTER)
    public String getFromName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("fromName", loc);
    }


    @Accessor(qualifier = "fromEmail", type = Accessor.Type.SETTER)
    public void setFromEmail(String value)
    {
        setFromEmail(value, null);
    }


    @Accessor(qualifier = "fromEmail", type = Accessor.Type.SETTER)
    public void setFromEmail(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("fromEmail", loc, value);
    }


    @Accessor(qualifier = "fromName", type = Accessor.Type.SETTER)
    public void setFromName(String value)
    {
        setFromName(value, null);
    }


    @Accessor(qualifier = "fromName", type = Accessor.Type.SETTER)
    public void setFromName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("fromName", loc, value);
    }
}

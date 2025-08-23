package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class PreviewTicketModel extends ItemModel
{
    public static final String _TYPECODE = "PreviewTicket";
    public static final String PREVIEWCATALOGVERSION = "previewCatalogVersion";
    public static final String VALIDTO = "validTo";
    public static final String CREATEDBY = "createdBy";
    public static final String TICKETCODE = "ticketCode";


    public PreviewTicketModel()
    {
    }


    public PreviewTicketModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PreviewTicketModel(UserModel _createdBy, CatalogVersionModel _previewCatalogVersion, Date _validTo)
    {
        setCreatedBy(_createdBy);
        setPreviewCatalogVersion(_previewCatalogVersion);
        setValidTo(_validTo);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PreviewTicketModel(UserModel _createdBy, ItemModel _owner, CatalogVersionModel _previewCatalogVersion, Date _validTo)
    {
        setCreatedBy(_createdBy);
        setOwner(_owner);
        setPreviewCatalogVersion(_previewCatalogVersion);
        setValidTo(_validTo);
    }


    @Accessor(qualifier = "createdBy", type = Accessor.Type.GETTER)
    public UserModel getCreatedBy()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("createdBy");
    }


    @Accessor(qualifier = "previewCatalogVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getPreviewCatalogVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("previewCatalogVersion");
    }


    @Accessor(qualifier = "ticketCode", type = Accessor.Type.GETTER)
    public String getTicketCode()
    {
        return (String)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "ticketCode");
    }


    @Accessor(qualifier = "validTo", type = Accessor.Type.GETTER)
    public Date getValidTo()
    {
        return (Date)getPersistenceContext().getPropertyValue("validTo");
    }


    @Accessor(qualifier = "createdBy", type = Accessor.Type.SETTER)
    public void setCreatedBy(UserModel value)
    {
        getPersistenceContext().setPropertyValue("createdBy", value);
    }


    @Accessor(qualifier = "previewCatalogVersion", type = Accessor.Type.SETTER)
    public void setPreviewCatalogVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("previewCatalogVersion", value);
    }


    @Accessor(qualifier = "validTo", type = Accessor.Type.SETTER)
    public void setValidTo(Date value)
    {
        getPersistenceContext().setPropertyValue("validTo", value);
    }
}

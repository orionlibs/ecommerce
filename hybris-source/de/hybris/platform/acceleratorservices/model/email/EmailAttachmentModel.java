package de.hybris.platform.acceleratorservices.model.email;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class EmailAttachmentModel extends MediaModel
{
    public static final String _TYPECODE = "EmailAttachment";
    public static final String _EMAILMESSAGE2EMAILATTACHMENTSREL = "EmailMessage2EmailAttachmentsRel";
    public static final String MESSAGE = "message";


    public EmailAttachmentModel()
    {
    }


    public EmailAttachmentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EmailAttachmentModel(CatalogVersionModel _catalogVersion, String _code)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public EmailAttachmentModel(CatalogVersionModel _catalogVersion, String _code, ItemModel _owner)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "message", type = Accessor.Type.GETTER)
    public EmailMessageModel getMessage()
    {
        return (EmailMessageModel)getPersistenceContext().getPropertyValue("message");
    }


    @Accessor(qualifier = "message", type = Accessor.Type.SETTER)
    public void setMessage(EmailMessageModel value)
    {
        getPersistenceContext().setPropertyValue("message", value);
    }
}

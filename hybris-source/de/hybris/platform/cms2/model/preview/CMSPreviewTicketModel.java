package de.hybris.platform.cms2.model.preview;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CMSPreviewTicketModel extends ItemModel
{
    public static final String _TYPECODE = "CMSPreviewTicket";
    public static final String ID = "id";
    public static final String PREVIEWDATA = "previewData";


    public CMSPreviewTicketModel()
    {
    }


    public CMSPreviewTicketModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSPreviewTicketModel(String _id)
    {
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSPreviewTicketModel(String _id, ItemModel _owner)
    {
        setId(_id);
        setOwner(_owner);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
    }


    @Accessor(qualifier = "previewData", type = Accessor.Type.GETTER)
    public PreviewDataModel getPreviewData()
    {
        return (PreviewDataModel)getPersistenceContext().getPropertyValue("previewData");
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
    }


    @Accessor(qualifier = "previewData", type = Accessor.Type.SETTER)
    public void setPreviewData(PreviewDataModel value)
    {
        getPersistenceContext().setPropertyValue("previewData", value);
    }
}

package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.enums.AsnStatus;
import de.hybris.platform.warehousing.model.AdvancedShippingNoticeEntryModel;
import de.hybris.platform.warehousing.model.AdvancedShippingNoticeModel;
import java.util.Date;
import java.util.List;

public class AsnModelBuilder
{
    private final AdvancedShippingNoticeModel model = new AdvancedShippingNoticeModel();


    private AdvancedShippingNoticeModel getModel()
    {
        return this.model;
    }


    public static AsnModelBuilder aModel()
    {
        return new AsnModelBuilder();
    }


    public AdvancedShippingNoticeModel build()
    {
        return getModel();
    }


    public AsnModelBuilder withExternalId(String externalId)
    {
        getModel().setExternalId(externalId);
        return this;
    }


    public AsnModelBuilder withInternalId(String internalId)
    {
        getModel().setInternalId(internalId);
        return this;
    }


    public AsnModelBuilder withStatus(AsnStatus status)
    {
        getModel().setStatus(status);
        return this;
    }


    public AsnModelBuilder withReleaseDate(Date date)
    {
        getModel().setReleaseDate(date);
        return this;
    }


    public AsnModelBuilder withWarehouse(WarehouseModel warehouse)
    {
        getModel().setWarehouse(warehouse);
        return this;
    }


    public AsnModelBuilder withPoS(PointOfServiceModel pos)
    {
        getModel().setPointOfService(pos);
        return this;
    }


    public AsnModelBuilder withComments(List<CommentModel> comments)
    {
        getModel().setComments(comments);
        return this;
    }


    public AsnModelBuilder withAsnEntries(List<AdvancedShippingNoticeEntryModel> asnEntries)
    {
        getModel().setAsnEntries(asnEntries);
        return this;
    }
}

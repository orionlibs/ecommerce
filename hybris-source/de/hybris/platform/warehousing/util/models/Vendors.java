package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.ordersplitting.model.VendorModel;
import de.hybris.platform.warehousing.util.builder.VendorModelBuilder;
import de.hybris.platform.warehousing.util.dao.WarehousingDao;
import org.springframework.beans.factory.annotation.Required;

public class Vendors extends AbstractItems<VendorModel>
{
    public static final String CODE_HYBRIS = "hybris";
    private WarehousingDao<VendorModel> vendorDao;


    public VendorModel Hybris()
    {
        return (VendorModel)getOrSaveAndReturn(() -> (VendorModel)getVendorDao().getByCode("hybris"), () -> VendorModelBuilder.aModel().withCode("hybris").build());
    }


    public WarehousingDao<VendorModel> getVendorDao()
    {
        return this.vendorDao;
    }


    @Required
    public void setVendorDao(WarehousingDao<VendorModel> vendorDao)
    {
        this.vendorDao = vendorDao;
    }
}

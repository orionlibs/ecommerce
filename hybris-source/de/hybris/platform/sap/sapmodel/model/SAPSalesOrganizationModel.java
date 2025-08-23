package de.hybris.platform.sap.sapmodel.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SAPSalesOrganizationModel extends ItemModel
{
    public static final String _TYPECODE = "SAPSalesOrganization";
    public static final String SALESORGANIZATION = "salesOrganization";
    public static final String DISTRIBUTIONCHANNEL = "distributionChannel";
    public static final String DIVISION = "division";


    public SAPSalesOrganizationModel()
    {
    }


    public SAPSalesOrganizationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPSalesOrganizationModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "distributionChannel", type = Accessor.Type.GETTER)
    public String getDistributionChannel()
    {
        return (String)getPersistenceContext().getPropertyValue("distributionChannel");
    }


    @Accessor(qualifier = "division", type = Accessor.Type.GETTER)
    public String getDivision()
    {
        return (String)getPersistenceContext().getPropertyValue("division");
    }


    @Accessor(qualifier = "salesOrganization", type = Accessor.Type.GETTER)
    public String getSalesOrganization()
    {
        return (String)getPersistenceContext().getPropertyValue("salesOrganization");
    }


    @Accessor(qualifier = "distributionChannel", type = Accessor.Type.SETTER)
    public void setDistributionChannel(String value)
    {
        getPersistenceContext().setPropertyValue("distributionChannel", value);
    }


    @Accessor(qualifier = "division", type = Accessor.Type.SETTER)
    public void setDivision(String value)
    {
        getPersistenceContext().setPropertyValue("division", value);
    }


    @Accessor(qualifier = "salesOrganization", type = Accessor.Type.SETTER)
    public void setSalesOrganization(String value)
    {
        getPersistenceContext().setPropertyValue("salesOrganization", value);
    }
}

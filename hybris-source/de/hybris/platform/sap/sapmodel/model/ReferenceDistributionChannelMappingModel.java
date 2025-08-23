package de.hybris.platform.sap.sapmodel.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class ReferenceDistributionChannelMappingModel extends ItemModel
{
    public static final String _TYPECODE = "ReferenceDistributionChannelMapping";
    public static final String SALESORGANIZATION = "salesOrganization";
    public static final String DISTCHANNEL = "distChannel";
    public static final String DISTCHANNELNAME = "distChannelName";
    public static final String REFDISTCHANNELCONDITIONS = "refDistChannelConditions";
    public static final String REFDISTCHANNELCONDITIONSNAME = "refDistChannelConditionsName";
    public static final String REFDISTCHANNELCUSTMAT = "refDistChannelCustMat";
    public static final String REFDISTCHANNELCUSTMATNAME = "refDistChannelCustMatName";


    public ReferenceDistributionChannelMappingModel()
    {
    }


    public ReferenceDistributionChannelMappingModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReferenceDistributionChannelMappingModel(String _distChannel, String _refDistChannelConditions, String _refDistChannelCustMat, String _salesOrganization)
    {
        setDistChannel(_distChannel);
        setRefDistChannelConditions(_refDistChannelConditions);
        setRefDistChannelCustMat(_refDistChannelCustMat);
        setSalesOrganization(_salesOrganization);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ReferenceDistributionChannelMappingModel(String _distChannel, ItemModel _owner, String _refDistChannelConditions, String _refDistChannelCustMat, String _salesOrganization)
    {
        setDistChannel(_distChannel);
        setOwner(_owner);
        setRefDistChannelConditions(_refDistChannelConditions);
        setRefDistChannelCustMat(_refDistChannelCustMat);
        setSalesOrganization(_salesOrganization);
    }


    @Accessor(qualifier = "distChannel", type = Accessor.Type.GETTER)
    public String getDistChannel()
    {
        return (String)getPersistenceContext().getPropertyValue("distChannel");
    }


    @Accessor(qualifier = "distChannelName", type = Accessor.Type.GETTER)
    public String getDistChannelName()
    {
        return getDistChannelName(null);
    }


    @Accessor(qualifier = "distChannelName", type = Accessor.Type.GETTER)
    public String getDistChannelName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("distChannelName", loc);
    }


    @Accessor(qualifier = "refDistChannelConditions", type = Accessor.Type.GETTER)
    public String getRefDistChannelConditions()
    {
        return (String)getPersistenceContext().getPropertyValue("refDistChannelConditions");
    }


    @Accessor(qualifier = "refDistChannelConditionsName", type = Accessor.Type.GETTER)
    public String getRefDistChannelConditionsName()
    {
        return getRefDistChannelConditionsName(null);
    }


    @Accessor(qualifier = "refDistChannelConditionsName", type = Accessor.Type.GETTER)
    public String getRefDistChannelConditionsName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("refDistChannelConditionsName", loc);
    }


    @Accessor(qualifier = "refDistChannelCustMat", type = Accessor.Type.GETTER)
    public String getRefDistChannelCustMat()
    {
        return (String)getPersistenceContext().getPropertyValue("refDistChannelCustMat");
    }


    @Accessor(qualifier = "refDistChannelCustMatName", type = Accessor.Type.GETTER)
    public String getRefDistChannelCustMatName()
    {
        return getRefDistChannelCustMatName(null);
    }


    @Accessor(qualifier = "refDistChannelCustMatName", type = Accessor.Type.GETTER)
    public String getRefDistChannelCustMatName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("refDistChannelCustMatName", loc);
    }


    @Accessor(qualifier = "salesOrganization", type = Accessor.Type.GETTER)
    public String getSalesOrganization()
    {
        return (String)getPersistenceContext().getPropertyValue("salesOrganization");
    }


    @Accessor(qualifier = "distChannel", type = Accessor.Type.SETTER)
    public void setDistChannel(String value)
    {
        getPersistenceContext().setPropertyValue("distChannel", value);
    }


    @Accessor(qualifier = "distChannelName", type = Accessor.Type.SETTER)
    public void setDistChannelName(String value)
    {
        setDistChannelName(value, null);
    }


    @Accessor(qualifier = "distChannelName", type = Accessor.Type.SETTER)
    public void setDistChannelName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("distChannelName", loc, value);
    }


    @Accessor(qualifier = "refDistChannelConditions", type = Accessor.Type.SETTER)
    public void setRefDistChannelConditions(String value)
    {
        getPersistenceContext().setPropertyValue("refDistChannelConditions", value);
    }


    @Accessor(qualifier = "refDistChannelConditionsName", type = Accessor.Type.SETTER)
    public void setRefDistChannelConditionsName(String value)
    {
        setRefDistChannelConditionsName(value, null);
    }


    @Accessor(qualifier = "refDistChannelConditionsName", type = Accessor.Type.SETTER)
    public void setRefDistChannelConditionsName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("refDistChannelConditionsName", loc, value);
    }


    @Accessor(qualifier = "refDistChannelCustMat", type = Accessor.Type.SETTER)
    public void setRefDistChannelCustMat(String value)
    {
        getPersistenceContext().setPropertyValue("refDistChannelCustMat", value);
    }


    @Accessor(qualifier = "refDistChannelCustMatName", type = Accessor.Type.SETTER)
    public void setRefDistChannelCustMatName(String value)
    {
        setRefDistChannelCustMatName(value, null);
    }


    @Accessor(qualifier = "refDistChannelCustMatName", type = Accessor.Type.SETTER)
    public void setRefDistChannelCustMatName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("refDistChannelCustMatName", loc, value);
    }


    @Accessor(qualifier = "salesOrganization", type = Accessor.Type.SETTER)
    public void setSalesOrganization(String value)
    {
        getPersistenceContext().setPropertyValue("salesOrganization", value);
    }
}

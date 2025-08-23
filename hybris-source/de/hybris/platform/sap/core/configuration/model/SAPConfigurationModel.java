package de.hybris.platform.sap.core.configuration.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.sap.sapmodel.model.SAPDeliveryModeModel;
import de.hybris.platform.sap.sapmodel.model.SAPPaymentModeModel;
import de.hybris.platform.sap.sapmodel.model.SAPPlantLogSysOrgModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;
import java.util.Collection;
import java.util.Set;

public class SAPConfigurationModel extends ItemModel
{
    public static final String _TYPECODE = "SAPConfiguration";
    public static final String _SAPCONFIGURATIONFORBASESTORE = "SAPConfigurationForBaseStore";
    public static final String CORE_NAME = "core_name";
    public static final String BASESTORES = "baseStores";
    public static final String SAPRFCDESTINATION = "SAPRFCDestination";
    public static final String SAPCOMMON_REFERENCECUSTOMER = "sapcommon_referenceCustomer";
    public static final String SAPCOMMON_TRANSACTIONTYPE = "sapcommon_transactionType";
    public static final String SAPCOMMON_SALESORGANIZATION = "sapcommon_salesOrganization";
    public static final String SAPCOMMON_DISTRIBUTIONCHANNEL = "sapcommon_distributionChannel";
    public static final String SAPCOMMON_DIVISION = "sapcommon_division";
    public static final String SAPDELIVERYMODES = "sapDeliveryModes";
    public static final String SAPPAYMENTMODES = "sapPaymentModes";
    public static final String SAPPLANTLOGSYSORG = "sapPlantLogSysOrg";
    public static final String REPLICATEREGISTEREDB2BUSER = "replicateregisteredb2buser";
    public static final String SAPORDEREXCHANGE_ITEMPRICECONDITIONTYPE = "saporderexchange_itemPriceConditionType";
    public static final String SAPORDEREXCHANGE_PAYMENTCOSTCONDITIONTYPE = "saporderexchange_paymentCostConditionType";
    public static final String SAPORDEREXCHANGE_DELIVERYCOSTCONDITIONTYPE = "saporderexchange_deliveryCostConditionType";


    public SAPConfigurationModel()
    {
    }


    public SAPConfigurationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPConfigurationModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "baseStores", type = Accessor.Type.GETTER)
    public Collection<BaseStoreModel> getBaseStores()
    {
        return (Collection<BaseStoreModel>)getPersistenceContext().getPropertyValue("baseStores");
    }


    @Accessor(qualifier = "core_name", type = Accessor.Type.GETTER)
    public String getCore_name()
    {
        return (String)getPersistenceContext().getPropertyValue("core_name");
    }


    @Accessor(qualifier = "replicateregisteredb2buser", type = Accessor.Type.GETTER)
    public Boolean getReplicateregisteredb2buser()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("replicateregisteredb2buser");
    }


    @Accessor(qualifier = "sapcommon_distributionChannel", type = Accessor.Type.GETTER)
    public String getSapcommon_distributionChannel()
    {
        return (String)getPersistenceContext().getPropertyValue("sapcommon_distributionChannel");
    }


    @Accessor(qualifier = "sapcommon_division", type = Accessor.Type.GETTER)
    public String getSapcommon_division()
    {
        return (String)getPersistenceContext().getPropertyValue("sapcommon_division");
    }


    @Accessor(qualifier = "sapcommon_referenceCustomer", type = Accessor.Type.GETTER)
    public String getSapcommon_referenceCustomer()
    {
        return (String)getPersistenceContext().getPropertyValue("sapcommon_referenceCustomer");
    }


    @Accessor(qualifier = "sapcommon_salesOrganization", type = Accessor.Type.GETTER)
    public String getSapcommon_salesOrganization()
    {
        return (String)getPersistenceContext().getPropertyValue("sapcommon_salesOrganization");
    }


    @Accessor(qualifier = "sapcommon_transactionType", type = Accessor.Type.GETTER)
    public String getSapcommon_transactionType()
    {
        return (String)getPersistenceContext().getPropertyValue("sapcommon_transactionType");
    }


    @Accessor(qualifier = "sapDeliveryModes", type = Accessor.Type.GETTER)
    public Set<SAPDeliveryModeModel> getSapDeliveryModes()
    {
        return (Set<SAPDeliveryModeModel>)getPersistenceContext().getPropertyValue("sapDeliveryModes");
    }


    @Accessor(qualifier = "saporderexchange_deliveryCostConditionType", type = Accessor.Type.GETTER)
    public String getSaporderexchange_deliveryCostConditionType()
    {
        return (String)getPersistenceContext().getPropertyValue("saporderexchange_deliveryCostConditionType");
    }


    @Accessor(qualifier = "saporderexchange_itemPriceConditionType", type = Accessor.Type.GETTER)
    public String getSaporderexchange_itemPriceConditionType()
    {
        return (String)getPersistenceContext().getPropertyValue("saporderexchange_itemPriceConditionType");
    }


    @Accessor(qualifier = "saporderexchange_paymentCostConditionType", type = Accessor.Type.GETTER)
    public String getSaporderexchange_paymentCostConditionType()
    {
        return (String)getPersistenceContext().getPropertyValue("saporderexchange_paymentCostConditionType");
    }


    @Accessor(qualifier = "sapPaymentModes", type = Accessor.Type.GETTER)
    public Set<SAPPaymentModeModel> getSapPaymentModes()
    {
        return (Set<SAPPaymentModeModel>)getPersistenceContext().getPropertyValue("sapPaymentModes");
    }


    @Accessor(qualifier = "sapPlantLogSysOrg", type = Accessor.Type.GETTER)
    public Set<SAPPlantLogSysOrgModel> getSapPlantLogSysOrg()
    {
        return (Set<SAPPlantLogSysOrgModel>)getPersistenceContext().getPropertyValue("sapPlantLogSysOrg");
    }


    @Accessor(qualifier = "SAPRFCDestination", type = Accessor.Type.GETTER)
    public SAPRFCDestinationModel getSAPRFCDestination()
    {
        return (SAPRFCDestinationModel)getPersistenceContext().getPropertyValue("SAPRFCDestination");
    }


    @Accessor(qualifier = "baseStores", type = Accessor.Type.SETTER)
    public void setBaseStores(Collection<BaseStoreModel> value)
    {
        getPersistenceContext().setPropertyValue("baseStores", value);
    }


    @Accessor(qualifier = "core_name", type = Accessor.Type.SETTER)
    public void setCore_name(String value)
    {
        getPersistenceContext().setPropertyValue("core_name", value);
    }


    @Accessor(qualifier = "replicateregisteredb2buser", type = Accessor.Type.SETTER)
    public void setReplicateregisteredb2buser(Boolean value)
    {
        getPersistenceContext().setPropertyValue("replicateregisteredb2buser", value);
    }


    @Accessor(qualifier = "sapcommon_distributionChannel", type = Accessor.Type.SETTER)
    public void setSapcommon_distributionChannel(String value)
    {
        getPersistenceContext().setPropertyValue("sapcommon_distributionChannel", value);
    }


    @Accessor(qualifier = "sapcommon_division", type = Accessor.Type.SETTER)
    public void setSapcommon_division(String value)
    {
        getPersistenceContext().setPropertyValue("sapcommon_division", value);
    }


    @Accessor(qualifier = "sapcommon_referenceCustomer", type = Accessor.Type.SETTER)
    public void setSapcommon_referenceCustomer(String value)
    {
        getPersistenceContext().setPropertyValue("sapcommon_referenceCustomer", value);
    }


    @Accessor(qualifier = "sapcommon_salesOrganization", type = Accessor.Type.SETTER)
    public void setSapcommon_salesOrganization(String value)
    {
        getPersistenceContext().setPropertyValue("sapcommon_salesOrganization", value);
    }


    @Accessor(qualifier = "sapcommon_transactionType", type = Accessor.Type.SETTER)
    public void setSapcommon_transactionType(String value)
    {
        getPersistenceContext().setPropertyValue("sapcommon_transactionType", value);
    }


    @Accessor(qualifier = "sapDeliveryModes", type = Accessor.Type.SETTER)
    public void setSapDeliveryModes(Set<SAPDeliveryModeModel> value)
    {
        getPersistenceContext().setPropertyValue("sapDeliveryModes", value);
    }


    @Accessor(qualifier = "saporderexchange_deliveryCostConditionType", type = Accessor.Type.SETTER)
    public void setSaporderexchange_deliveryCostConditionType(String value)
    {
        getPersistenceContext().setPropertyValue("saporderexchange_deliveryCostConditionType", value);
    }


    @Accessor(qualifier = "saporderexchange_itemPriceConditionType", type = Accessor.Type.SETTER)
    public void setSaporderexchange_itemPriceConditionType(String value)
    {
        getPersistenceContext().setPropertyValue("saporderexchange_itemPriceConditionType", value);
    }


    @Accessor(qualifier = "saporderexchange_paymentCostConditionType", type = Accessor.Type.SETTER)
    public void setSaporderexchange_paymentCostConditionType(String value)
    {
        getPersistenceContext().setPropertyValue("saporderexchange_paymentCostConditionType", value);
    }


    @Accessor(qualifier = "sapPaymentModes", type = Accessor.Type.SETTER)
    public void setSapPaymentModes(Set<SAPPaymentModeModel> value)
    {
        getPersistenceContext().setPropertyValue("sapPaymentModes", value);
    }


    @Accessor(qualifier = "sapPlantLogSysOrg", type = Accessor.Type.SETTER)
    public void setSapPlantLogSysOrg(Set<SAPPlantLogSysOrgModel> value)
    {
        getPersistenceContext().setPropertyValue("sapPlantLogSysOrg", value);
    }


    @Accessor(qualifier = "SAPRFCDestination", type = Accessor.Type.SETTER)
    public void setSAPRFCDestination(SAPRFCDestinationModel value)
    {
        getPersistenceContext().setPropertyValue("SAPRFCDestination", value);
    }
}

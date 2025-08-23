package de.hybris.platform.sap.sapmodel.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.sap.core.configuration.model.SAPGlobalConfigurationModel;
import de.hybris.platform.sap.core.configuration.model.SAPHTTPDestinationModel;
import de.hybris.platform.sap.sapmodel.enums.SapSystemType;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SAPLogicalSystemModel extends ItemModel
{
    public static final String _TYPECODE = "SAPLogicalSystem";
    public static final String _SAPGLOBALCONFIG2LOGSYSTEM = "SAPGlobalConfig2LogSystem";
    public static final String SAPLOGICALSYSTEMNAME = "sapLogicalSystemName";
    public static final String SAPHTTPDESTINATION = "sapHTTPDestination";
    public static final String SAPSYSTEMTYPE = "sapSystemType";
    public static final String SENDERNAME = "senderName";
    public static final String SENDERPORT = "senderPort";
    public static final String DEFAULTLOGICALSYSTEM = "defaultLogicalSystem";
    public static final String SAPGLOBALCONFIGURATION = "sapGlobalConfiguration";


    public SAPLogicalSystemModel()
    {
    }


    public SAPLogicalSystemModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPLogicalSystemModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "sapGlobalConfiguration", type = Accessor.Type.GETTER)
    public SAPGlobalConfigurationModel getSapGlobalConfiguration()
    {
        return (SAPGlobalConfigurationModel)getPersistenceContext().getPropertyValue("sapGlobalConfiguration");
    }


    @Accessor(qualifier = "sapHTTPDestination", type = Accessor.Type.GETTER)
    public SAPHTTPDestinationModel getSapHTTPDestination()
    {
        return (SAPHTTPDestinationModel)getPersistenceContext().getPropertyValue("sapHTTPDestination");
    }


    @Accessor(qualifier = "sapLogicalSystemName", type = Accessor.Type.GETTER)
    public String getSapLogicalSystemName()
    {
        return (String)getPersistenceContext().getPropertyValue("sapLogicalSystemName");
    }


    @Accessor(qualifier = "sapSystemType", type = Accessor.Type.GETTER)
    public SapSystemType getSapSystemType()
    {
        return (SapSystemType)getPersistenceContext().getPropertyValue("sapSystemType");
    }


    @Accessor(qualifier = "senderName", type = Accessor.Type.GETTER)
    public String getSenderName()
    {
        return (String)getPersistenceContext().getPropertyValue("senderName");
    }


    @Accessor(qualifier = "senderPort", type = Accessor.Type.GETTER)
    public String getSenderPort()
    {
        return (String)getPersistenceContext().getPropertyValue("senderPort");
    }


    @Accessor(qualifier = "defaultLogicalSystem", type = Accessor.Type.GETTER)
    public boolean isDefaultLogicalSystem()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("defaultLogicalSystem"));
    }


    @Accessor(qualifier = "defaultLogicalSystem", type = Accessor.Type.SETTER)
    public void setDefaultLogicalSystem(boolean value)
    {
        getPersistenceContext().setPropertyValue("defaultLogicalSystem", toObject(value));
    }


    @Accessor(qualifier = "sapGlobalConfiguration", type = Accessor.Type.SETTER)
    public void setSapGlobalConfiguration(SAPGlobalConfigurationModel value)
    {
        getPersistenceContext().setPropertyValue("sapGlobalConfiguration", value);
    }


    @Accessor(qualifier = "sapHTTPDestination", type = Accessor.Type.SETTER)
    public void setSapHTTPDestination(SAPHTTPDestinationModel value)
    {
        getPersistenceContext().setPropertyValue("sapHTTPDestination", value);
    }


    @Accessor(qualifier = "sapLogicalSystemName", type = Accessor.Type.SETTER)
    public void setSapLogicalSystemName(String value)
    {
        getPersistenceContext().setPropertyValue("sapLogicalSystemName", value);
    }


    @Accessor(qualifier = "sapSystemType", type = Accessor.Type.SETTER)
    public void setSapSystemType(SapSystemType value)
    {
        getPersistenceContext().setPropertyValue("sapSystemType", value);
    }


    @Accessor(qualifier = "senderName", type = Accessor.Type.SETTER)
    public void setSenderName(String value)
    {
        getPersistenceContext().setPropertyValue("senderName", value);
    }


    @Accessor(qualifier = "senderPort", type = Accessor.Type.SETTER)
    public void setSenderPort(String value)
    {
        getPersistenceContext().setPropertyValue("senderPort", value);
    }
}

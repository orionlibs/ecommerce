package de.hybris.platform.sap.core.configuration.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.sap.core.configuration.enums.BackendType;
import de.hybris.platform.sap.core.configuration.enums.JCoCPICTrace;
import de.hybris.platform.sap.core.configuration.enums.JCoTraceLevel;
import de.hybris.platform.sap.core.configuration.enums.SncQoP;
import de.hybris.platform.sap.core.model.RFCDestinationAttributeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class SAPRFCDestinationModel extends ItemModel
{
    public static final String _TYPECODE = "SAPRFCDestination";
    public static final String _JCODESTINATIONFORSAPCONFIGURATION = "JCODestinationForSAPConfiguration";
    public static final String RFCDESTINATIONNAME = "rfcDestinationName";
    public static final String OFFLINEMODE = "offlineMode";
    public static final String SID = "sid";
    public static final String CLIENT = "client";
    public static final String MESSAGESERVER = "messageServer";
    public static final String TARGETHOST = "targetHost";
    public static final String CONNECTIONTYPE = "connectionType";
    public static final String GROUP = "group";
    public static final String INSTANCE = "instance";
    public static final String USERID = "userid";
    public static final String PASSWORD = "password";
    public static final String POOLEDCONNECTIONMODE = "pooledConnectionMode";
    public static final String MAXCONNECTIONS = "maxConnections";
    public static final String POOLSIZE = "poolSize";
    public static final String MAXWAITTIME = "maxWaitTime";
    public static final String JCOMSSERV = "jcoMsServ";
    public static final String JCOSAPROUTER = "jcoSAPRouter";
    public static final String JCOTRACELEVEL = "jcoTraceLevel";
    public static final String JCOTRACEPATH = "jcoTracePath";
    public static final String JCORFCTRACE = "jcoRFCTrace";
    public static final String JCOCPICTRACE = "jcoCPICTrace";
    public static final String JCOCLIENTDELTA = "jcoClientDelta";
    public static final String BACKENDTYPE = "backendType";
    public static final String SNCMODE = "sncMode";
    public static final String SNCQOP = "sncQoP";
    public static final String SNCPARTNERNAME = "sncPartnerName";
    public static final String SAPCONFIGURATIONS = "SAPConfigurations";
    public static final String ADDITIONALATTRIBUTES = "AdditionalAttributes";


    public SAPRFCDestinationModel()
    {
    }


    public SAPRFCDestinationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPRFCDestinationModel(String _client, String _password, String _rfcDestinationName, String _userid)
    {
        setClient(_client);
        setPassword(_password);
        setRfcDestinationName(_rfcDestinationName);
        setUserid(_userid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPRFCDestinationModel(String _client, ItemModel _owner, String _password, String _rfcDestinationName, String _userid)
    {
        setClient(_client);
        setOwner(_owner);
        setPassword(_password);
        setRfcDestinationName(_rfcDestinationName);
        setUserid(_userid);
    }


    @Accessor(qualifier = "AdditionalAttributes", type = Accessor.Type.GETTER)
    public Collection<RFCDestinationAttributeModel> getAdditionalAttributes()
    {
        return (Collection<RFCDestinationAttributeModel>)getPersistenceContext().getPropertyValue("AdditionalAttributes");
    }


    @Accessor(qualifier = "backendType", type = Accessor.Type.GETTER)
    public BackendType getBackendType()
    {
        return (BackendType)getPersistenceContext().getPropertyValue("backendType");
    }


    @Accessor(qualifier = "client", type = Accessor.Type.GETTER)
    public String getClient()
    {
        return (String)getPersistenceContext().getPropertyValue("client");
    }


    @Accessor(qualifier = "connectionType", type = Accessor.Type.GETTER)
    public Boolean getConnectionType()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("connectionType");
    }


    @Accessor(qualifier = "group", type = Accessor.Type.GETTER)
    public String getGroup()
    {
        return (String)getPersistenceContext().getPropertyValue("group");
    }


    @Accessor(qualifier = "instance", type = Accessor.Type.GETTER)
    public String getInstance()
    {
        return (String)getPersistenceContext().getPropertyValue("instance");
    }


    @Accessor(qualifier = "jcoClientDelta", type = Accessor.Type.GETTER)
    public Boolean getJcoClientDelta()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("jcoClientDelta");
    }


    @Accessor(qualifier = "jcoCPICTrace", type = Accessor.Type.GETTER)
    public JCoCPICTrace getJcoCPICTrace()
    {
        return (JCoCPICTrace)getPersistenceContext().getPropertyValue("jcoCPICTrace");
    }


    @Accessor(qualifier = "jcoMsServ", type = Accessor.Type.GETTER)
    public String getJcoMsServ()
    {
        return (String)getPersistenceContext().getPropertyValue("jcoMsServ");
    }


    @Accessor(qualifier = "jcoRFCTrace", type = Accessor.Type.GETTER)
    public Boolean getJcoRFCTrace()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("jcoRFCTrace");
    }


    @Accessor(qualifier = "jcoSAPRouter", type = Accessor.Type.GETTER)
    public String getJcoSAPRouter()
    {
        return (String)getPersistenceContext().getPropertyValue("jcoSAPRouter");
    }


    @Accessor(qualifier = "jcoTraceLevel", type = Accessor.Type.GETTER)
    public JCoTraceLevel getJcoTraceLevel()
    {
        return (JCoTraceLevel)getPersistenceContext().getPropertyValue("jcoTraceLevel");
    }


    @Accessor(qualifier = "jcoTracePath", type = Accessor.Type.GETTER)
    public String getJcoTracePath()
    {
        return (String)getPersistenceContext().getPropertyValue("jcoTracePath");
    }


    @Accessor(qualifier = "maxConnections", type = Accessor.Type.GETTER)
    public String getMaxConnections()
    {
        return (String)getPersistenceContext().getPropertyValue("maxConnections");
    }


    @Accessor(qualifier = "maxWaitTime", type = Accessor.Type.GETTER)
    public String getMaxWaitTime()
    {
        return (String)getPersistenceContext().getPropertyValue("maxWaitTime");
    }


    @Accessor(qualifier = "messageServer", type = Accessor.Type.GETTER)
    public String getMessageServer()
    {
        return (String)getPersistenceContext().getPropertyValue("messageServer");
    }


    @Accessor(qualifier = "offlineMode", type = Accessor.Type.GETTER)
    public Boolean getOfflineMode()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("offlineMode");
    }


    @Accessor(qualifier = "password", type = Accessor.Type.GETTER)
    public String getPassword()
    {
        return (String)getPersistenceContext().getPropertyValue("password");
    }


    @Accessor(qualifier = "pooledConnectionMode", type = Accessor.Type.GETTER)
    public Boolean getPooledConnectionMode()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("pooledConnectionMode");
    }


    @Accessor(qualifier = "poolSize", type = Accessor.Type.GETTER)
    public String getPoolSize()
    {
        return (String)getPersistenceContext().getPropertyValue("poolSize");
    }


    @Accessor(qualifier = "rfcDestinationName", type = Accessor.Type.GETTER)
    public String getRfcDestinationName()
    {
        return (String)getPersistenceContext().getPropertyValue("rfcDestinationName");
    }


    @Accessor(qualifier = "SAPConfigurations", type = Accessor.Type.GETTER)
    public Collection<SAPConfigurationModel> getSAPConfigurations()
    {
        return (Collection<SAPConfigurationModel>)getPersistenceContext().getPropertyValue("SAPConfigurations");
    }


    @Accessor(qualifier = "sid", type = Accessor.Type.GETTER)
    public String getSid()
    {
        return (String)getPersistenceContext().getPropertyValue("sid");
    }


    @Accessor(qualifier = "sncMode", type = Accessor.Type.GETTER)
    public Boolean getSncMode()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("sncMode");
    }


    @Accessor(qualifier = "sncPartnerName", type = Accessor.Type.GETTER)
    public String getSncPartnerName()
    {
        return (String)getPersistenceContext().getPropertyValue("sncPartnerName");
    }


    @Accessor(qualifier = "sncQoP", type = Accessor.Type.GETTER)
    public SncQoP getSncQoP()
    {
        return (SncQoP)getPersistenceContext().getPropertyValue("sncQoP");
    }


    @Accessor(qualifier = "targetHost", type = Accessor.Type.GETTER)
    public String getTargetHost()
    {
        return (String)getPersistenceContext().getPropertyValue("targetHost");
    }


    @Accessor(qualifier = "userid", type = Accessor.Type.GETTER)
    public String getUserid()
    {
        return (String)getPersistenceContext().getPropertyValue("userid");
    }


    @Accessor(qualifier = "AdditionalAttributes", type = Accessor.Type.SETTER)
    public void setAdditionalAttributes(Collection<RFCDestinationAttributeModel> value)
    {
        getPersistenceContext().setPropertyValue("AdditionalAttributes", value);
    }


    @Accessor(qualifier = "backendType", type = Accessor.Type.SETTER)
    public void setBackendType(BackendType value)
    {
        getPersistenceContext().setPropertyValue("backendType", value);
    }


    @Accessor(qualifier = "client", type = Accessor.Type.SETTER)
    public void setClient(String value)
    {
        getPersistenceContext().setPropertyValue("client", value);
    }


    @Accessor(qualifier = "connectionType", type = Accessor.Type.SETTER)
    public void setConnectionType(Boolean value)
    {
        getPersistenceContext().setPropertyValue("connectionType", value);
    }


    @Accessor(qualifier = "group", type = Accessor.Type.SETTER)
    public void setGroup(String value)
    {
        getPersistenceContext().setPropertyValue("group", value);
    }


    @Accessor(qualifier = "instance", type = Accessor.Type.SETTER)
    public void setInstance(String value)
    {
        getPersistenceContext().setPropertyValue("instance", value);
    }


    @Accessor(qualifier = "jcoClientDelta", type = Accessor.Type.SETTER)
    public void setJcoClientDelta(Boolean value)
    {
        getPersistenceContext().setPropertyValue("jcoClientDelta", value);
    }


    @Accessor(qualifier = "jcoCPICTrace", type = Accessor.Type.SETTER)
    public void setJcoCPICTrace(JCoCPICTrace value)
    {
        getPersistenceContext().setPropertyValue("jcoCPICTrace", value);
    }


    @Accessor(qualifier = "jcoMsServ", type = Accessor.Type.SETTER)
    public void setJcoMsServ(String value)
    {
        getPersistenceContext().setPropertyValue("jcoMsServ", value);
    }


    @Accessor(qualifier = "jcoRFCTrace", type = Accessor.Type.SETTER)
    public void setJcoRFCTrace(Boolean value)
    {
        getPersistenceContext().setPropertyValue("jcoRFCTrace", value);
    }


    @Accessor(qualifier = "jcoSAPRouter", type = Accessor.Type.SETTER)
    public void setJcoSAPRouter(String value)
    {
        getPersistenceContext().setPropertyValue("jcoSAPRouter", value);
    }


    @Accessor(qualifier = "jcoTraceLevel", type = Accessor.Type.SETTER)
    public void setJcoTraceLevel(JCoTraceLevel value)
    {
        getPersistenceContext().setPropertyValue("jcoTraceLevel", value);
    }


    @Accessor(qualifier = "jcoTracePath", type = Accessor.Type.SETTER)
    public void setJcoTracePath(String value)
    {
        getPersistenceContext().setPropertyValue("jcoTracePath", value);
    }


    @Accessor(qualifier = "maxConnections", type = Accessor.Type.SETTER)
    public void setMaxConnections(String value)
    {
        getPersistenceContext().setPropertyValue("maxConnections", value);
    }


    @Accessor(qualifier = "maxWaitTime", type = Accessor.Type.SETTER)
    public void setMaxWaitTime(String value)
    {
        getPersistenceContext().setPropertyValue("maxWaitTime", value);
    }


    @Accessor(qualifier = "messageServer", type = Accessor.Type.SETTER)
    public void setMessageServer(String value)
    {
        getPersistenceContext().setPropertyValue("messageServer", value);
    }


    @Accessor(qualifier = "offlineMode", type = Accessor.Type.SETTER)
    public void setOfflineMode(Boolean value)
    {
        getPersistenceContext().setPropertyValue("offlineMode", value);
    }


    @Accessor(qualifier = "password", type = Accessor.Type.SETTER)
    public void setPassword(String value)
    {
        getPersistenceContext().setPropertyValue("password", value);
    }


    @Accessor(qualifier = "pooledConnectionMode", type = Accessor.Type.SETTER)
    public void setPooledConnectionMode(Boolean value)
    {
        getPersistenceContext().setPropertyValue("pooledConnectionMode", value);
    }


    @Accessor(qualifier = "poolSize", type = Accessor.Type.SETTER)
    public void setPoolSize(String value)
    {
        getPersistenceContext().setPropertyValue("poolSize", value);
    }


    @Accessor(qualifier = "rfcDestinationName", type = Accessor.Type.SETTER)
    public void setRfcDestinationName(String value)
    {
        getPersistenceContext().setPropertyValue("rfcDestinationName", value);
    }


    @Accessor(qualifier = "SAPConfigurations", type = Accessor.Type.SETTER)
    public void setSAPConfigurations(Collection<SAPConfigurationModel> value)
    {
        getPersistenceContext().setPropertyValue("SAPConfigurations", value);
    }


    @Accessor(qualifier = "sid", type = Accessor.Type.SETTER)
    public void setSid(String value)
    {
        getPersistenceContext().setPropertyValue("sid", value);
    }


    @Accessor(qualifier = "sncMode", type = Accessor.Type.SETTER)
    public void setSncMode(Boolean value)
    {
        getPersistenceContext().setPropertyValue("sncMode", value);
    }


    @Accessor(qualifier = "sncPartnerName", type = Accessor.Type.SETTER)
    public void setSncPartnerName(String value)
    {
        getPersistenceContext().setPropertyValue("sncPartnerName", value);
    }


    @Accessor(qualifier = "sncQoP", type = Accessor.Type.SETTER)
    public void setSncQoP(SncQoP value)
    {
        getPersistenceContext().setPropertyValue("sncQoP", value);
    }


    @Accessor(qualifier = "targetHost", type = Accessor.Type.SETTER)
    public void setTargetHost(String value)
    {
        getPersistenceContext().setPropertyValue("targetHost", value);
    }


    @Accessor(qualifier = "userid", type = Accessor.Type.SETTER)
    public void setUserid(String value)
    {
        getPersistenceContext().setPropertyValue("userid", value);
    }
}

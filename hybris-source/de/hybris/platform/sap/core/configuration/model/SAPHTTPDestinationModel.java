package de.hybris.platform.sap.core.configuration.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.sap.core.configuration.enums.HTTPAuthenticationType;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SAPHTTPDestinationModel extends ItemModel
{
    public static final String _TYPECODE = "SAPHTTPDestination";
    public static final String HTTPDESTINATIONNAME = "httpDestinationName";
    public static final String TARGETURL = "targetURL";
    public static final String AUTHENTICATIONTYPE = "authenticationType";
    public static final String USERID = "userid";
    public static final String PASSWORD = "password";


    public SAPHTTPDestinationModel()
    {
    }


    public SAPHTTPDestinationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SAPHTTPDestinationModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "authenticationType", type = Accessor.Type.GETTER)
    public HTTPAuthenticationType getAuthenticationType()
    {
        return (HTTPAuthenticationType)getPersistenceContext().getPropertyValue("authenticationType");
    }


    @Accessor(qualifier = "httpDestinationName", type = Accessor.Type.GETTER)
    public String getHttpDestinationName()
    {
        return (String)getPersistenceContext().getPropertyValue("httpDestinationName");
    }


    @Accessor(qualifier = "password", type = Accessor.Type.GETTER)
    public String getPassword()
    {
        return (String)getPersistenceContext().getPropertyValue("password");
    }


    @Accessor(qualifier = "targetURL", type = Accessor.Type.GETTER)
    public String getTargetURL()
    {
        return (String)getPersistenceContext().getPropertyValue("targetURL");
    }


    @Accessor(qualifier = "userid", type = Accessor.Type.GETTER)
    public String getUserid()
    {
        return (String)getPersistenceContext().getPropertyValue("userid");
    }


    @Accessor(qualifier = "authenticationType", type = Accessor.Type.SETTER)
    public void setAuthenticationType(HTTPAuthenticationType value)
    {
        getPersistenceContext().setPropertyValue("authenticationType", value);
    }


    @Accessor(qualifier = "httpDestinationName", type = Accessor.Type.SETTER)
    public void setHttpDestinationName(String value)
    {
        getPersistenceContext().setPropertyValue("httpDestinationName", value);
    }


    @Accessor(qualifier = "password", type = Accessor.Type.SETTER)
    public void setPassword(String value)
    {
        getPersistenceContext().setPropertyValue("password", value);
    }


    @Accessor(qualifier = "targetURL", type = Accessor.Type.SETTER)
    public void setTargetURL(String value)
    {
        getPersistenceContext().setPropertyValue("targetURL", value);
    }


    @Accessor(qualifier = "userid", type = Accessor.Type.SETTER)
    public void setUserid(String value)
    {
        getPersistenceContext().setPropertyValue("userid", value);
    }
}

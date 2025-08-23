package de.hybris.platform.core.model.web;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class StoredHttpSessionModel extends ItemModel
{
    public static final String _TYPECODE = "StoredHttpSession";
    public static final String SESSIONID = "sessionId";
    public static final String CLUSTERID = "clusterId";
    public static final String EXTENSION = "extension";
    public static final String CONTEXTROOT = "contextRoot";
    public static final String SERIALIZEDSESSION = "serializedSession";


    public StoredHttpSessionModel()
    {
    }


    public StoredHttpSessionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public StoredHttpSessionModel(Integer _clusterId, String _sessionId)
    {
        setClusterId(_clusterId);
        setSessionId(_sessionId);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public StoredHttpSessionModel(Integer _clusterId, String _contextRoot, String _extension, ItemModel _owner, String _sessionId)
    {
        setClusterId(_clusterId);
        setContextRoot(_contextRoot);
        setExtension(_extension);
        setOwner(_owner);
        setSessionId(_sessionId);
    }


    @Accessor(qualifier = "clusterId", type = Accessor.Type.GETTER)
    public Integer getClusterId()
    {
        return (Integer)getPersistenceContext().getPropertyValue("clusterId");
    }


    @Accessor(qualifier = "contextRoot", type = Accessor.Type.GETTER)
    public String getContextRoot()
    {
        return (String)getPersistenceContext().getPropertyValue("contextRoot");
    }


    @Accessor(qualifier = "extension", type = Accessor.Type.GETTER)
    public String getExtension()
    {
        return (String)getPersistenceContext().getPropertyValue("extension");
    }


    @Accessor(qualifier = "serializedSession", type = Accessor.Type.GETTER)
    public Object getSerializedSession()
    {
        return getPersistenceContext().getPropertyValue("serializedSession");
    }


    @Accessor(qualifier = "sessionId", type = Accessor.Type.GETTER)
    public String getSessionId()
    {
        return (String)getPersistenceContext().getPropertyValue("sessionId");
    }


    @Accessor(qualifier = "clusterId", type = Accessor.Type.SETTER)
    public void setClusterId(Integer value)
    {
        getPersistenceContext().setPropertyValue("clusterId", value);
    }


    @Accessor(qualifier = "contextRoot", type = Accessor.Type.SETTER)
    public void setContextRoot(String value)
    {
        getPersistenceContext().setPropertyValue("contextRoot", value);
    }


    @Accessor(qualifier = "extension", type = Accessor.Type.SETTER)
    public void setExtension(String value)
    {
        getPersistenceContext().setPropertyValue("extension", value);
    }


    @Accessor(qualifier = "serializedSession", type = Accessor.Type.SETTER)
    public void setSerializedSession(Object value)
    {
        getPersistenceContext().setPropertyValue("serializedSession", value);
    }


    @Accessor(qualifier = "sessionId", type = Accessor.Type.SETTER)
    public void setSessionId(String value)
    {
        getPersistenceContext().setPropertyValue("sessionId", value);
    }
}

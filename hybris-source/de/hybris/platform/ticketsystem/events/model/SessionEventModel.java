package de.hybris.platform.ticketsystem.events.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.List;

public class SessionEventModel extends ItemModel
{
    public static final String _TYPECODE = "SessionEvent";
    public static final String EVENTTIME = "eventTime";
    public static final String AGENT = "agent";
    public static final String SESSIONID = "sessionID";
    public static final String BASESITE = "baseSite";
    public static final String GROUPS = "groups";


    public SessionEventModel()
    {
    }


    public SessionEventModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SessionEventModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "agent", type = Accessor.Type.GETTER)
    public EmployeeModel getAgent()
    {
        return (EmployeeModel)getPersistenceContext().getPropertyValue("agent");
    }


    @Accessor(qualifier = "baseSite", type = Accessor.Type.GETTER)
    public BaseSiteModel getBaseSite()
    {
        return (BaseSiteModel)getPersistenceContext().getPropertyValue("baseSite");
    }


    @Accessor(qualifier = "eventTime", type = Accessor.Type.GETTER)
    public Date getEventTime()
    {
        return (Date)getPersistenceContext().getPropertyValue("eventTime");
    }


    @Accessor(qualifier = "groups", type = Accessor.Type.GETTER)
    public List<PrincipalGroupModel> getGroups()
    {
        return (List<PrincipalGroupModel>)getPersistenceContext().getPropertyValue("groups");
    }


    @Accessor(qualifier = "sessionID", type = Accessor.Type.GETTER)
    public String getSessionID()
    {
        return (String)getPersistenceContext().getPropertyValue("sessionID");
    }


    @Accessor(qualifier = "agent", type = Accessor.Type.SETTER)
    public void setAgent(EmployeeModel value)
    {
        getPersistenceContext().setPropertyValue("agent", value);
    }


    @Accessor(qualifier = "baseSite", type = Accessor.Type.SETTER)
    public void setBaseSite(BaseSiteModel value)
    {
        getPersistenceContext().setPropertyValue("baseSite", value);
    }


    @Accessor(qualifier = "eventTime", type = Accessor.Type.SETTER)
    public void setEventTime(Date value)
    {
        getPersistenceContext().setPropertyValue("eventTime", value);
    }


    @Accessor(qualifier = "groups", type = Accessor.Type.SETTER)
    public void setGroups(List<PrincipalGroupModel> value)
    {
        getPersistenceContext().setPropertyValue("groups", value);
    }


    @Accessor(qualifier = "sessionID", type = Accessor.Type.SETTER)
    public void setSessionID(String value)
    {
        getPersistenceContext().setPropertyValue("sessionID", value);
    }
}

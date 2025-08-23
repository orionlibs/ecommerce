package de.hybris.platform.core.model.user;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AbstractUserAuditModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractUserAudit";
    public static final String UID = "uid";
    public static final String USERPK = "userPK";
    public static final String CHANGINGUSER = "changingUser";
    public static final String CHANGINGAPPLICATION = "changingApplication";
    public static final String IPADDRESS = "ipAddress";


    public AbstractUserAuditModel()
    {
    }


    public AbstractUserAuditModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractUserAuditModel(String _uid, Long _userPK)
    {
        setUid(_uid);
        setUserPK(_userPK);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractUserAuditModel(String _ipAddress, ItemModel _owner, String _uid, Long _userPK)
    {
        setIpAddress(_ipAddress);
        setOwner(_owner);
        setUid(_uid);
        setUserPK(_userPK);
    }


    @Accessor(qualifier = "changingApplication", type = Accessor.Type.GETTER)
    public String getChangingApplication()
    {
        return (String)getPersistenceContext().getPropertyValue("changingApplication");
    }


    @Accessor(qualifier = "changingUser", type = Accessor.Type.GETTER)
    public String getChangingUser()
    {
        return (String)getPersistenceContext().getPropertyValue("changingUser");
    }


    @Accessor(qualifier = "ipAddress", type = Accessor.Type.GETTER)
    public String getIpAddress()
    {
        return (String)getPersistenceContext().getPropertyValue("ipAddress");
    }


    @Accessor(qualifier = "uid", type = Accessor.Type.GETTER)
    public String getUid()
    {
        return (String)getPersistenceContext().getPropertyValue("uid");
    }


    @Accessor(qualifier = "userPK", type = Accessor.Type.GETTER)
    public Long getUserPK()
    {
        return (Long)getPersistenceContext().getPropertyValue("userPK");
    }


    @Accessor(qualifier = "changingApplication", type = Accessor.Type.SETTER)
    public void setChangingApplication(String value)
    {
        getPersistenceContext().setPropertyValue("changingApplication", value);
    }


    @Accessor(qualifier = "changingUser", type = Accessor.Type.SETTER)
    public void setChangingUser(String value)
    {
        getPersistenceContext().setPropertyValue("changingUser", value);
    }


    @Accessor(qualifier = "ipAddress", type = Accessor.Type.SETTER)
    public void setIpAddress(String value)
    {
        getPersistenceContext().setPropertyValue("ipAddress", value);
    }


    @Accessor(qualifier = "uid", type = Accessor.Type.SETTER)
    public void setUid(String value)
    {
        getPersistenceContext().setPropertyValue("uid", value);
    }


    @Accessor(qualifier = "userPK", type = Accessor.Type.SETTER)
    public void setUserPK(Long value)
    {
        getPersistenceContext().setPropertyValue("userPK", value);
    }
}

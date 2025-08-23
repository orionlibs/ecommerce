package de.hybris.platform.core.model.user;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class BruteForceLoginDisabledAuditModel extends AbstractUserAuditModel
{
    public static final String _TYPECODE = "BruteForceLoginDisabledAudit";
    public static final String FAILEDLOGINS = "failedLogins";


    public BruteForceLoginDisabledAuditModel()
    {
    }


    public BruteForceLoginDisabledAuditModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BruteForceLoginDisabledAuditModel(Integer _failedLogins, String _uid, Long _userPK)
    {
        setFailedLogins(_failedLogins);
        setUid(_uid);
        setUserPK(_userPK);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BruteForceLoginDisabledAuditModel(Integer _failedLogins, String _ipAddress, ItemModel _owner, String _uid, Long _userPK)
    {
        setFailedLogins(_failedLogins);
        setIpAddress(_ipAddress);
        setOwner(_owner);
        setUid(_uid);
        setUserPK(_userPK);
    }


    @Accessor(qualifier = "failedLogins", type = Accessor.Type.GETTER)
    public Integer getFailedLogins()
    {
        return (Integer)getPersistenceContext().getPropertyValue("failedLogins");
    }


    @Accessor(qualifier = "failedLogins", type = Accessor.Type.SETTER)
    public void setFailedLogins(Integer value)
    {
        getPersistenceContext().setPropertyValue("failedLogins", value);
    }
}

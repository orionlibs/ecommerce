package de.hybris.platform.core.model.user;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class BruteForceOAuthDisabledAuditModel extends AbstractUserAuditModel
{
    public static final String _TYPECODE = "BruteForceOAuthDisabledAudit";
    public static final String FAILEDOAUTHAUTHORIZATIONS = "failedOAuthAuthorizations";


    public BruteForceOAuthDisabledAuditModel()
    {
    }


    public BruteForceOAuthDisabledAuditModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BruteForceOAuthDisabledAuditModel(Integer _failedOAuthAuthorizations, String _uid, Long _userPK)
    {
        setFailedOAuthAuthorizations(_failedOAuthAuthorizations);
        setUid(_uid);
        setUserPK(_userPK);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BruteForceOAuthDisabledAuditModel(Integer _failedOAuthAuthorizations, String _ipAddress, ItemModel _owner, String _uid, Long _userPK)
    {
        setFailedOAuthAuthorizations(_failedOAuthAuthorizations);
        setIpAddress(_ipAddress);
        setOwner(_owner);
        setUid(_uid);
        setUserPK(_userPK);
    }


    @Accessor(qualifier = "failedOAuthAuthorizations", type = Accessor.Type.GETTER)
    public Integer getFailedOAuthAuthorizations()
    {
        return (Integer)getPersistenceContext().getPropertyValue("failedOAuthAuthorizations");
    }


    @Accessor(qualifier = "failedOAuthAuthorizations", type = Accessor.Type.SETTER)
    public void setFailedOAuthAuthorizations(Integer value)
    {
        getPersistenceContext().setPropertyValue("failedOAuthAuthorizations", value);
    }
}

package de.hybris.platform.core.model.user;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class UserPasswordChangeAuditModel extends AbstractUserAuditModel
{
    public static final String _TYPECODE = "UserPasswordChangeAudit";
    public static final String ENCODEDPASSWORD = "encodedPassword";
    public static final String PASSWORDENCODING = "passwordEncoding";


    public UserPasswordChangeAuditModel()
    {
    }


    public UserPasswordChangeAuditModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public UserPasswordChangeAuditModel(String _encodedPassword, String _passwordEncoding, String _uid, Long _userPK)
    {
        setEncodedPassword(_encodedPassword);
        setPasswordEncoding(_passwordEncoding);
        setUid(_uid);
        setUserPK(_userPK);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public UserPasswordChangeAuditModel(String _encodedPassword, String _ipAddress, ItemModel _owner, String _passwordEncoding, String _uid, Long _userPK)
    {
        setEncodedPassword(_encodedPassword);
        setIpAddress(_ipAddress);
        setOwner(_owner);
        setPasswordEncoding(_passwordEncoding);
        setUid(_uid);
        setUserPK(_userPK);
    }


    @Accessor(qualifier = "encodedPassword", type = Accessor.Type.GETTER)
    public String getEncodedPassword()
    {
        return (String)getPersistenceContext().getPropertyValue("encodedPassword");
    }


    @Accessor(qualifier = "passwordEncoding", type = Accessor.Type.GETTER)
    public String getPasswordEncoding()
    {
        return (String)getPersistenceContext().getPropertyValue("passwordEncoding");
    }


    @Accessor(qualifier = "encodedPassword", type = Accessor.Type.SETTER)
    public void setEncodedPassword(String value)
    {
        getPersistenceContext().setPropertyValue("encodedPassword", value);
    }


    @Accessor(qualifier = "passwordEncoding", type = Accessor.Type.SETTER)
    public void setPasswordEncoding(String value)
    {
        getPersistenceContext().setPropertyValue("passwordEncoding", value);
    }
}

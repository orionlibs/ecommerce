package de.hybris.platform.core.model.initialization;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SystemSetupAuditModel extends ItemModel
{
    public static final String _TYPECODE = "SystemSetupAudit";
    public static final String HASH = "hash";
    public static final String EXTENSIONNAME = "extensionName";
    public static final String REQUIRED = "required";
    public static final String PATCH = "patch";
    public static final String USER = "user";
    public static final String NAME = "name";
    public static final String CLASSNAME = "className";
    public static final String METHODNAME = "methodName";
    public static final String DESCRIPTION = "description";


    public SystemSetupAuditModel()
    {
    }


    public SystemSetupAuditModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SystemSetupAuditModel(String _className, String _extensionName, String _hash, String _methodName, String _name, boolean _patch, boolean _required, UserModel _user)
    {
        setClassName(_className);
        setExtensionName(_extensionName);
        setHash(_hash);
        setMethodName(_methodName);
        setName(_name);
        setPatch(_patch);
        setRequired(_required);
        setUser(_user);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SystemSetupAuditModel(String _className, String _extensionName, String _hash, String _methodName, String _name, ItemModel _owner, boolean _patch, boolean _required, UserModel _user)
    {
        setClassName(_className);
        setExtensionName(_extensionName);
        setHash(_hash);
        setMethodName(_methodName);
        setName(_name);
        setOwner(_owner);
        setPatch(_patch);
        setRequired(_required);
        setUser(_user);
    }


    @Accessor(qualifier = "className", type = Accessor.Type.GETTER)
    public String getClassName()
    {
        return (String)getPersistenceContext().getPropertyValue("className");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return (String)getPersistenceContext().getPropertyValue("description");
    }


    @Accessor(qualifier = "extensionName", type = Accessor.Type.GETTER)
    public String getExtensionName()
    {
        return (String)getPersistenceContext().getPropertyValue("extensionName");
    }


    @Accessor(qualifier = "hash", type = Accessor.Type.GETTER)
    public String getHash()
    {
        return (String)getPersistenceContext().getPropertyValue("hash");
    }


    @Accessor(qualifier = "methodName", type = Accessor.Type.GETTER)
    public String getMethodName()
    {
        return (String)getPersistenceContext().getPropertyValue("methodName");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "user", type = Accessor.Type.GETTER)
    public UserModel getUser()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("user");
    }


    @Accessor(qualifier = "patch", type = Accessor.Type.GETTER)
    public boolean isPatch()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("patch"));
    }


    @Accessor(qualifier = "required", type = Accessor.Type.GETTER)
    public boolean isRequired()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("required"));
    }


    @Accessor(qualifier = "className", type = Accessor.Type.SETTER)
    public void setClassName(String value)
    {
        getPersistenceContext().setPropertyValue("className", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        getPersistenceContext().setPropertyValue("description", value);
    }


    @Accessor(qualifier = "extensionName", type = Accessor.Type.SETTER)
    public void setExtensionName(String value)
    {
        getPersistenceContext().setPropertyValue("extensionName", value);
    }


    @Accessor(qualifier = "hash", type = Accessor.Type.SETTER)
    public void setHash(String value)
    {
        getPersistenceContext().setPropertyValue("hash", value);
    }


    @Accessor(qualifier = "methodName", type = Accessor.Type.SETTER)
    public void setMethodName(String value)
    {
        getPersistenceContext().setPropertyValue("methodName", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "patch", type = Accessor.Type.SETTER)
    public void setPatch(boolean value)
    {
        getPersistenceContext().setPropertyValue("patch", toObject(value));
    }


    @Accessor(qualifier = "required", type = Accessor.Type.SETTER)
    public void setRequired(boolean value)
    {
        getPersistenceContext().setPropertyValue("required", toObject(value));
    }


    @Accessor(qualifier = "user", type = Accessor.Type.SETTER)
    public void setUser(UserModel value)
    {
        getPersistenceContext().setPropertyValue("user", value);
    }
}

package de.hybris.platform.core.model.user;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class BruteForceLoginAttemptsModel extends ItemModel
{
    public static final String _TYPECODE = "BruteForceLoginAttempts";
    public static final String UID = "uid";
    public static final String ATTEMPTS = "attempts";


    public BruteForceLoginAttemptsModel()
    {
    }


    public BruteForceLoginAttemptsModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BruteForceLoginAttemptsModel(Integer _attempts, String _uid)
    {
        setAttempts(_attempts);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public BruteForceLoginAttemptsModel(Integer _attempts, ItemModel _owner, String _uid)
    {
        setAttempts(_attempts);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "attempts", type = Accessor.Type.GETTER)
    public Integer getAttempts()
    {
        return (Integer)getPersistenceContext().getPropertyValue("attempts");
    }


    @Accessor(qualifier = "uid", type = Accessor.Type.GETTER)
    public String getUid()
    {
        return (String)getPersistenceContext().getPropertyValue("uid");
    }


    @Accessor(qualifier = "attempts", type = Accessor.Type.SETTER)
    public void setAttempts(Integer value)
    {
        getPersistenceContext().setPropertyValue("attempts", value);
    }


    @Accessor(qualifier = "uid", type = Accessor.Type.SETTER)
    public void setUid(String value)
    {
        getPersistenceContext().setPropertyValue("uid", value);
    }
}

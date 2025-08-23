package de.hybris.platform.core.model.security;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;
import java.util.Set;

public class PrincipalGroupModel extends PrincipalModel
{
    public static final String _TYPECODE = "PrincipalGroup";
    public static final String _PRINCIPALGROUPRELATION = "PrincipalGroupRelation";
    public static final String LOCNAME = "locName";
    public static final String MAXBRUTEFORCELOGINATTEMPTS = "maxBruteForceLoginAttempts";
    public static final String MEMBERS = "members";


    public PrincipalGroupModel()
    {
    }


    public PrincipalGroupModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PrincipalGroupModel(String _uid)
    {
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PrincipalGroupModel(ItemModel _owner, String _uid)
    {
        setOwner(_owner);
        setUid(_uid);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public String getLocname()
    {
        return getLocName();
    }


    @Deprecated(since = "ages", forRemoval = true)
    public String getLocname(Locale loc)
    {
        return getLocName(loc);
    }


    @Accessor(qualifier = "locName", type = Accessor.Type.GETTER)
    public String getLocName()
    {
        return getLocName(null);
    }


    @Accessor(qualifier = "locName", type = Accessor.Type.GETTER)
    public String getLocName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("locName", loc);
    }


    @Accessor(qualifier = "maxBruteForceLoginAttempts", type = Accessor.Type.GETTER)
    public Integer getMaxBruteForceLoginAttempts()
    {
        return (Integer)getPersistenceContext().getPropertyValue("maxBruteForceLoginAttempts");
    }


    @Accessor(qualifier = "members", type = Accessor.Type.GETTER)
    public Set<PrincipalModel> getMembers()
    {
        return (Set<PrincipalModel>)getPersistenceContext().getPropertyValue("members");
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void setLocname(String value)
    {
        setLocName(value);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void setLocname(String value, Locale loc)
    {
        setLocName(value, loc);
    }


    @Accessor(qualifier = "locName", type = Accessor.Type.SETTER)
    public void setLocName(String value)
    {
        setLocName(value, null);
    }


    @Accessor(qualifier = "locName", type = Accessor.Type.SETTER)
    public void setLocName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("locName", loc, value);
    }


    @Accessor(qualifier = "maxBruteForceLoginAttempts", type = Accessor.Type.SETTER)
    public void setMaxBruteForceLoginAttempts(Integer value)
    {
        getPersistenceContext().setPropertyValue("maxBruteForceLoginAttempts", value);
    }


    @Accessor(qualifier = "members", type = Accessor.Type.SETTER)
    public void setMembers(Set<PrincipalModel> value)
    {
        getPersistenceContext().setPropertyValue("members", value);
    }
}

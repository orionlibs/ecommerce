package de.hybris.platform.hmc.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;

public class UserProfileModel extends ItemModel
{
    public static final String _TYPECODE = "UserProfile";
    public static final String READABLELANGUAGES = "readableLanguages";
    public static final String WRITABLELANGUAGES = "writableLanguages";
    public static final String ALLREADABLELANGUAGES = "allReadableLanguages";
    public static final String ALLWRITABLELANGUAGES = "allWritableLanguages";
    public static final String EXPANDINITIAL = "expandInitial";


    public UserProfileModel()
    {
    }


    public UserProfileModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public UserProfileModel(PrincipalModel _owner)
    {
        setOwner((ItemModel)_owner);
    }


    @Accessor(qualifier = "allReadableLanguages", type = Accessor.Type.GETTER)
    public Collection<LanguageModel> getAllReadableLanguages()
    {
        return (Collection<LanguageModel>)getPersistenceContext().getPropertyValue("allReadableLanguages");
    }


    @Accessor(qualifier = "allWritableLanguages", type = Accessor.Type.GETTER)
    public Collection<LanguageModel> getAllWritableLanguages()
    {
        return (Collection<LanguageModel>)getPersistenceContext().getPropertyValue("allWritableLanguages");
    }


    @Accessor(qualifier = "expandInitial", type = Accessor.Type.GETTER)
    public Boolean getExpandInitial()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("expandInitial");
    }


    @Accessor(qualifier = "owner", type = Accessor.Type.GETTER)
    public PrincipalModel getOwner()
    {
        return (PrincipalModel)super.getOwner();
    }


    @Accessor(qualifier = "readableLanguages", type = Accessor.Type.GETTER)
    public List<LanguageModel> getReadableLanguages()
    {
        return (List<LanguageModel>)getPersistenceContext().getPropertyValue("readableLanguages");
    }


    @Accessor(qualifier = "writableLanguages", type = Accessor.Type.GETTER)
    public List<LanguageModel> getWritableLanguages()
    {
        return (List<LanguageModel>)getPersistenceContext().getPropertyValue("writableLanguages");
    }


    @Accessor(qualifier = "expandInitial", type = Accessor.Type.SETTER)
    public void setExpandInitial(Boolean value)
    {
        getPersistenceContext().setPropertyValue("expandInitial", value);
    }


    @Accessor(qualifier = "owner", type = Accessor.Type.SETTER)
    public void setOwner(ItemModel value)
    {
        if(value == null || value instanceof PrincipalModel)
        {
            super.setOwner(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.core.model.security.PrincipalModel");
        }
    }


    @Accessor(qualifier = "readableLanguages", type = Accessor.Type.SETTER)
    public void setReadableLanguages(List<LanguageModel> value)
    {
        getPersistenceContext().setPropertyValue("readableLanguages", value);
    }


    @Accessor(qualifier = "writableLanguages", type = Accessor.Type.SETTER)
    public void setWritableLanguages(List<LanguageModel> value)
    {
        getPersistenceContext().setPropertyValue("writableLanguages", value);
    }
}

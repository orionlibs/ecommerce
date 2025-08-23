package de.hybris.platform.productcockpit.model.favorites;

import de.hybris.platform.cockpit.model.collection.impl.ObjectCollectionImpl;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.productcockpit.model.favorites.impl.FavoriteCategory;
import java.util.Date;

public class FavoriteCategoryImpl implements FavoriteCategory
{
    private String label;
    private String description;
    private PK pk;
    private String type;
    private UserModel user;


    public String getDescription()
    {
        return this.description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public FavoriteCategoryImpl(String label, PK pk)
    {
        this.label = label;
    }


    public boolean equals(Object obj)
    {
        return (obj instanceof ObjectCollectionImpl && ((ObjectCollectionImpl)obj).getPK().equals(this.pk));
    }


    public int hashCode()
    {
        return this.pk.hashCode();
    }


    public void setUser(UserModel user)
    {
        this.user = user;
    }


    public UserModel getUser()
    {
        return this.user;
    }


    public String getType()
    {
        return this.type;
    }


    public PK getPK()
    {
        return this.pk;
    }


    public Date getCreationTime()
    {
        return null;
    }


    public Date getModificationTime()
    {
        return null;
    }


    public void setLabel(String label)
    {
        this.label = label;
    }


    public String getLabel()
    {
        return this.label;
    }
}

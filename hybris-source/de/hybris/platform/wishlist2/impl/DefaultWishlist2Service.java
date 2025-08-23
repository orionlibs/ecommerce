package de.hybris.platform.wishlist2.impl;

import de.hybris.platform.core.Constants;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.wishlist2.Wishlist2Service;
import de.hybris.platform.wishlist2.enums.Wishlist2EntryPriority;
import de.hybris.platform.wishlist2.impl.daos.Wishlist2Dao;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class DefaultWishlist2Service implements Wishlist2Service
{
    @Deprecated(since = "4.1")
    protected Wishlist2Dao wishlistDao;
    @Deprecated(since = "4.1")
    protected boolean saveAnonymousWishlists;
    private ModelService modelService;


    protected UserModel getCurrentUser()
    {
        return (UserModel)getModelService().get(JaloSession.getCurrentSession().getUser());
    }


    protected boolean saveWishlist(UserModel user)
    {
        if(user == null)
        {
            return false;
        }
        boolean anonymous = Constants.USER.ANONYMOUS_CUSTOMER.equals(user.getUid());
        return (!anonymous || (anonymous && this.saveAnonymousWishlists));
    }


    protected boolean saveWishlist(Wishlist2Model wishlist)
    {
        UserModel user = wishlist.getUser();
        return saveWishlist(user);
    }


    public Wishlist2Model getDefaultWishlist(UserModel user)
    {
        return this.wishlistDao.findDefaultWishlist(user);
    }


    public Wishlist2Model getDefaultWishlist()
    {
        return getDefaultWishlist(getCurrentUser());
    }


    public List<Wishlist2Model> getWishlists(UserModel user)
    {
        return this.wishlistDao.findAllWishlists(user);
    }


    public List<Wishlist2Model> getWishlists()
    {
        return getWishlists(getCurrentUser());
    }


    public Wishlist2EntryModel getWishlistEntryForProduct(ProductModel product, Wishlist2Model wishlist)
    {
        ServicesUtil.validateParameterNotNull(product, "Parameter 'product' was null.");
        ServicesUtil.validateParameterNotNull(wishlist, "Parameter 'wishlist' was null.");
        List<Wishlist2EntryModel> entries = this.wishlistDao.findWishlistEntryByProduct(product, wishlist);
        if(entries.isEmpty())
        {
            throw new UnknownIdentifierException("Wishlist entry with product [" + product
                            .getCode() + "] in wishlist [" + wishlist.getName() + " ] not found.");
        }
        if(entries.size() > 1)
        {
            throw new AmbiguousIdentifierException("Wishlist entry with product [" + product.getCode() + "] in wishlist [" + wishlist
                            .getName() + "] is not unique, " + entries.size() + " entries found.");
        }
        return entries.iterator().next();
    }


    public void removeWishlistEntryForProduct(ProductModel product, Wishlist2Model wishlist)
    {
        Wishlist2EntryModel entry = getWishlistEntryForProduct(product, wishlist);
        removeWishlistEntry(wishlist, entry);
    }


    public void addWishlistEntry(Wishlist2Model wishlist, Wishlist2EntryModel entry)
    {
        if(saveWishlist(wishlist))
        {
            getModelService().save(entry);
        }
        List<Wishlist2EntryModel> entries = new ArrayList<>(wishlist.getEntries());
        entries.add(entry);
        wishlist.setEntries(entries);
        if(saveWishlist(wishlist))
        {
            getModelService().save(wishlist);
        }
    }


    public void removeWishlistEntry(Wishlist2Model wishlist, Wishlist2EntryModel entry)
    {
        List<Wishlist2EntryModel> entries = new ArrayList<>(wishlist.getEntries());
        entries.remove(entry);
        wishlist.setEntries(entries);
        if(saveWishlist(wishlist))
        {
            getModelService().save(wishlist);
        }
    }


    public void addWishlistEntry(Wishlist2Model wishlist, ProductModel product, Integer desired, Wishlist2EntryPriority priority, String comment)
    {
        Wishlist2EntryModel entry = new Wishlist2EntryModel();
        entry.setProduct(product);
        entry.setDesired(desired);
        entry.setPriority(priority);
        entry.setComment(comment);
        entry.setAddedDate(new Date());
        addWishlistEntry(wishlist, entry);
    }


    public void addWishlistEntry(ProductModel product, Integer desired, Wishlist2EntryPriority priority, String comment)
    {
        Wishlist2Model wishlist = getDefaultWishlist();
        addWishlistEntry(wishlist, product, desired, priority, comment);
    }


    public void addWishlistEntry(UserModel user, ProductModel product, Integer desired, Wishlist2EntryPriority priority, String comment)
    {
        Wishlist2Model wishlist = getDefaultWishlist(user);
        addWishlistEntry(wishlist, product, desired, priority, comment);
    }


    public Wishlist2Model createDefaultWishlist(String name, String description)
    {
        return createDefaultWishlist(getCurrentUser(), name, description);
    }


    public Wishlist2Model createDefaultWishlist(UserModel user, String name, String description)
    {
        if(hasDefaultWishlist())
        {
            throw new SystemException("An default wishlist for the user <" + user.getName() + "> already exists");
        }
        return createWishlist(user, name, description, Boolean.TRUE);
    }


    public Wishlist2Model createWishlist(String name, String description)
    {
        return createWishlist(getCurrentUser(), name, description);
    }


    public Wishlist2Model createWishlist(UserModel user, String name, String description)
    {
        return createWishlist(user, name, description, Boolean.FALSE);
    }


    private Wishlist2Model createWishlist(UserModel user, String name, String description, Boolean defaultWL)
    {
        Wishlist2Model wishlist = new Wishlist2Model();
        wishlist.setName(name);
        wishlist.setDescription(description);
        wishlist.setDefault(defaultWL);
        wishlist.setUser(user);
        if(saveWishlist(user))
        {
            getModelService().save(wishlist);
        }
        return wishlist;
    }


    public boolean hasDefaultWishlist()
    {
        return hasDefaultWishlist(getCurrentUser());
    }


    public boolean hasDefaultWishlist(UserModel user)
    {
        return (this.wishlistDao.findDefaultWishlist(user) != null);
    }


    public void setWishlistDao(Wishlist2Dao wishlistDao)
    {
        this.wishlistDao = wishlistDao;
    }


    public void setSaveAnonymousWishlists(boolean saveAnonymousWishlists)
    {
        this.saveAnonymousWishlists = saveAnonymousWishlists;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}

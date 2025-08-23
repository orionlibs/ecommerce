package de.hybris.platform.wishlist2.impl.daos.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.wishlist2.impl.daos.Wishlist2Dao;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;
import java.util.List;
import org.apache.log4j.Logger;

public class DefaultWishlist2Dao extends AbstractItemDao implements Wishlist2Dao
{
    private static final Logger LOG = Logger.getLogger(DefaultWishlist2Dao.class.getName());


    public List<Wishlist2Model> findAllWishlists(UserModel user)
    {
        String query = "SELECT {pk} FROM {Wishlist2} WHERE {user} = ?user";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {Wishlist2} WHERE {user} = ?user");
        fQuery.addQueryParameter("user", user);
        SearchResult<Wishlist2Model> result = search(fQuery);
        return result.getResult();
    }


    public Wishlist2Model findDefaultWishlist(UserModel user)
    {
        String query = "SELECT {pk} FROM {Wishlist2} WHERE {user} = ?user AND {default} = ?trueValue";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {Wishlist2} WHERE {user} = ?user AND {default} = ?trueValue");
        fQuery.addQueryParameter("user", user);
        fQuery.addQueryParameter("trueValue", Boolean.TRUE);
        SearchResult<Wishlist2Model> result = search(fQuery);
        if(result.getCount() > 1)
        {
            LOG.warn("More than one default wishlist defined for user " + user.getName() + ". Returning first!");
        }
        if(result.getCount() > 0)
        {
            return result.getResult().iterator().next();
        }
        return null;
    }


    public List<Wishlist2EntryModel> findWishlistEntryByProduct(ProductModel product, Wishlist2Model wishlist)
    {
        StringBuilder query = new StringBuilder("SELECT {pk} FROM {Wishlist2Entry} WHERE {");
        query.append("product} = ?product AND {");
        query.append("wishlist} = ?wishlist");
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query.toString());
        fQuery.addQueryParameter("product", product);
        fQuery.addQueryParameter("wishlist", wishlist);
        SearchResult<Wishlist2EntryModel> result = search(fQuery);
        List<Wishlist2EntryModel> entries = result.getResult();
        return entries;
    }
}

package de.hybris.platform.wishlist2.impl.daos;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;
import java.util.List;

public interface Wishlist2Dao extends Dao
{
    List<Wishlist2Model> findAllWishlists(UserModel paramUserModel);


    Wishlist2Model findDefaultWishlist(UserModel paramUserModel);


    List<Wishlist2EntryModel> findWishlistEntryByProduct(ProductModel paramProductModel, Wishlist2Model paramWishlist2Model);
}

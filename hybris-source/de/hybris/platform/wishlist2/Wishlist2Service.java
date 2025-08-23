package de.hybris.platform.wishlist2;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.wishlist2.enums.Wishlist2EntryPriority;
import de.hybris.platform.wishlist2.model.Wishlist2EntryModel;
import de.hybris.platform.wishlist2.model.Wishlist2Model;
import java.util.List;

public interface Wishlist2Service
{
    List<Wishlist2Model> getWishlists(UserModel paramUserModel);


    List<Wishlist2Model> getWishlists();


    boolean hasDefaultWishlist();


    boolean hasDefaultWishlist(UserModel paramUserModel);


    Wishlist2Model createDefaultWishlist(String paramString1, String paramString2);


    Wishlist2Model createDefaultWishlist(UserModel paramUserModel, String paramString1, String paramString2);


    Wishlist2Model createWishlist(String paramString1, String paramString2);


    Wishlist2Model createWishlist(UserModel paramUserModel, String paramString1, String paramString2);


    Wishlist2Model getDefaultWishlist(UserModel paramUserModel);


    Wishlist2Model getDefaultWishlist();


    void addWishlistEntry(Wishlist2Model paramWishlist2Model, Wishlist2EntryModel paramWishlist2EntryModel);


    void addWishlistEntry(Wishlist2Model paramWishlist2Model, ProductModel paramProductModel, Integer paramInteger, Wishlist2EntryPriority paramWishlist2EntryPriority, String paramString);


    void addWishlistEntry(ProductModel paramProductModel, Integer paramInteger, Wishlist2EntryPriority paramWishlist2EntryPriority, String paramString);


    void addWishlistEntry(UserModel paramUserModel, ProductModel paramProductModel, Integer paramInteger, Wishlist2EntryPriority paramWishlist2EntryPriority, String paramString);


    void removeWishlistEntry(Wishlist2Model paramWishlist2Model, Wishlist2EntryModel paramWishlist2EntryModel);


    Wishlist2EntryModel getWishlistEntryForProduct(ProductModel paramProductModel, Wishlist2Model paramWishlist2Model);


    void removeWishlistEntryForProduct(ProductModel paramProductModel, Wishlist2Model paramWishlist2Model);
}

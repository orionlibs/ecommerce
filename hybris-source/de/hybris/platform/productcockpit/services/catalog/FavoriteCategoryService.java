package de.hybris.platform.productcockpit.services.catalog;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.productcockpit.model.favorites.impl.FavoriteCategory;
import java.util.List;

public interface FavoriteCategoryService
{
    List<FavoriteCategory> getFavoriteCategories(UserModel paramUserModel);


    FavoriteCategory createFavoriteCategory(String paramString, UserModel paramUserModel);


    void deleteFavoriteCategory(FavoriteCategory paramFavoriteCategory);


    void renameQuery(FavoriteCategory paramFavoriteCategory, String paramString);
}

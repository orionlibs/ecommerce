package de.hybris.platform.productcockpit.services.catalog.impl;

import de.hybris.platform.cockpit.jalo.CockpitFavoriteCategory;
import de.hybris.platform.cockpit.jalo.CockpitManager;
import de.hybris.platform.cockpit.services.impl.AbstractServiceImpl;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.productcockpit.model.favorites.FavoriteCategoryImpl;
import de.hybris.platform.productcockpit.model.favorites.impl.FavoriteCategory;
import de.hybris.platform.productcockpit.services.catalog.FavoriteCategoryService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FavoriteCategoryServiceImpl extends AbstractServiceImpl implements FavoriteCategoryService
{
    private static final Logger LOG = LoggerFactory.getLogger(FavoriteCategoryServiceImpl.class);


    public FavoriteCategory createFavoriteCategory(String label, UserModel user)
    {
        Map<String, Object> attr = new HashMap<>();
        attr.put("qualifier", null);
        attr.put("user", this.modelService.getSource(user));
        attr.put("label", label);
        CockpitFavoriteCategory coc = CockpitManager.getInstance().createCockpitFavoriteCategory(attr);
        return (FavoriteCategory)new FavoriteCategoryImpl(coc.getLabel(), coc.getPK());
    }


    public void deleteFavoriteCategory(FavoriteCategory favorite)
    {
        try
        {
            CockpitFavoriteCategory fav = unmapCollection(favorite);
            fav.remove();
        }
        catch(Exception e)
        {
            LOG.debug(e.getMessage(), e);
        }
    }


    protected CockpitFavoriteCategory unmapCollection(FavoriteCategory favorite)
    {
        CockpitFavoriteCategory ret = null;
        try
        {
            ret = (CockpitFavoriteCategory)JaloSession.getCurrentSession().getItem(favorite.getPK());
        }
        catch(JaloItemNotFoundException e)
        {
            LOG.debug(e.getMessage(), (Throwable)e);
        }
        return ret;
    }


    public List<FavoriteCategory> getFavoriteCategories(UserModel user)
    {
        List<FavoriteCategory> results = new ArrayList<>();
        for(CockpitFavoriteCategory favoriteCategory : CockpitManager.getInstance()
                        .getCockpitFavoriteCategories((User)this.modelService.getSource(user)))
        {
            results.add(new FavoriteCategoryImpl(favoriteCategory.getLabel(), favoriteCategory.getPK()));
        }
        return results;
    }


    public void renameQuery(FavoriteCategory favorite, String label)
    {
    }
}

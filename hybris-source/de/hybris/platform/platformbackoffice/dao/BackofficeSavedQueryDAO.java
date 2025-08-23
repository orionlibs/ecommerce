package de.hybris.platform.platformbackoffice.dao;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.platformbackoffice.model.BackofficeSavedQueryModel;
import java.util.List;

public interface BackofficeSavedQueryDAO
{
    List<BackofficeSavedQueryModel> findSavedQueries(UserModel paramUserModel);
}

package de.hybris.platform.cockpit.daos;

import de.hybris.platform.cockpit.model.CockpitUIComponentAccessRightModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

public interface CockpitUIComponentAccessRightDao extends Dao
{
    CockpitUIComponentAccessRightModel findCockpitUIComponentAccessRight(String paramString);
}

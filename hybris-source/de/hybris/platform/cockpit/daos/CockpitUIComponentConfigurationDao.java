package de.hybris.platform.cockpit.daos;

import de.hybris.platform.cockpit.jalo.CockpitUIComponentConfiguration;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.servicelayer.internal.dao.Dao;
import java.util.List;

public interface CockpitUIComponentConfigurationDao extends Dao
{
    CockpitUIComponentConfiguration findCockpitUIComponentConfiguration(String paramString1, String paramString2, String paramString3);


    List<CockpitUIComponentConfiguration> findCockpitUIComponentConfigurationsByPrincipal(Principal paramPrincipal);


    List<String> findRoleNamesByPrincipal(Principal paramPrincipal);
}

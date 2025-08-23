package de.hybris.platform.cockpit.daos;

import de.hybris.platform.cockpit.model.CockpitUIComponentConfigurationModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import java.util.List;

public interface CockpitConfigurationDao
{
    List<CockpitUIComponentConfigurationModel> findComponentConfigurationsByPrincipal(PrincipalModel paramPrincipalModel);


    List<CockpitUIComponentConfigurationModel> findDedicatedComponentConfigurationsByPrincipal(PrincipalModel paramPrincipalModel);


    List<CockpitUIComponentConfigurationModel> findComponentConfigurations(PrincipalModel paramPrincipalModel, String paramString1, String paramString2);


    List<String> findRoleNamesByPrincipal(PrincipalModel paramPrincipalModel);


    List<CockpitUIComponentConfigurationModel> findComponentConfigurationsByMedia(MediaModel paramMediaModel);
}

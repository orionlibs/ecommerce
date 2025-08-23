package de.hybris.platform.cockpit;

import de.hybris.platform.cockpit.model.CockpitUIComponentConfigurationModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import java.util.List;

public interface CockpitConfigurationService
{
    List<CockpitUIComponentConfigurationModel> getComponentConfigurationsForPrincipal(PrincipalModel paramPrincipalModel);


    List<CockpitUIComponentConfigurationModel> getDedicatedComponentConfigurationsForPrincipal(PrincipalModel paramPrincipalModel);


    void removeComponentConfigurations(List<CockpitUIComponentConfigurationModel> paramList);


    CockpitUIComponentConfigurationModel getComponentConfiguration(PrincipalModel paramPrincipalModel, String paramString1, String paramString2);


    List<String> getRoleNamesForPrincipal(PrincipalModel paramPrincipalModel);
}

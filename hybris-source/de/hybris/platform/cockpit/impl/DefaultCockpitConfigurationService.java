package de.hybris.platform.cockpit.impl;

import de.hybris.platform.cockpit.CockpitConfigurationService;
import de.hybris.platform.cockpit.daos.CockpitConfigurationDao;
import de.hybris.platform.cockpit.model.CockpitUIComponentConfigurationModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCockpitConfigurationService implements CockpitConfigurationService
{
    private CockpitConfigurationDao cockpitConfigurationDao;
    private ModelService modelService;


    public List<CockpitUIComponentConfigurationModel> getComponentConfigurationsForPrincipal(PrincipalModel principal)
    {
        ServicesUtil.validateParameterNotNull(principal, "Parameter 'principal' cannot be null");
        return getCockpitConfigurationDao().findComponentConfigurationsByPrincipal(principal);
    }


    public CockpitUIComponentConfigurationModel getComponentConfiguration(PrincipalModel principal, String objectTemplateCode, String code)
    {
        ServicesUtil.validateParameterNotNull(objectTemplateCode, "Parameter 'objectTemplateCode' cannot be null");
        ServicesUtil.validateParameterNotNull(code, "Parameter 'code' cannot be null");
        List<CockpitUIComponentConfigurationModel> componentConfigurationModels = getCockpitConfigurationDao().findComponentConfigurations(principal, objectTemplateCode, code);
        ServicesUtil.validateIfSingleResult(componentConfigurationModels, "No configuration found for arguments principal: " + principal + ", objectTemplateCode: " + objectTemplateCode + ", code: " + code,
                        "More than one configuration found for arguments principal: " + principal + ", objectTemplateCode: " + objectTemplateCode + ", code: " + code);
        return componentConfigurationModels.get(0);
    }


    public List<CockpitUIComponentConfigurationModel> getDedicatedComponentConfigurationsForPrincipal(PrincipalModel principal)
    {
        ServicesUtil.validateParameterNotNull(principal, "Parameter 'principal' cannot be null");
        return getCockpitConfigurationDao().findDedicatedComponentConfigurationsByPrincipal(principal);
    }


    public void removeComponentConfigurations(List<CockpitUIComponentConfigurationModel> configurations)
    {
        ServicesUtil.validateParameterNotNull(configurations, "Parameter 'configurations' cannot be null");
        for(CockpitUIComponentConfigurationModel cockpitUIComponentConfigurationModel : configurations)
        {
            MediaModel media = cockpitUIComponentConfigurationModel.getMedia();
            this.modelService.remove(cockpitUIComponentConfigurationModel);
            if(CollectionUtils.isEmpty(getCockpitConfigurationDao().findComponentConfigurationsByMedia(media)))
            {
                this.modelService.remove(media);
            }
        }
    }


    public List<String> getRoleNamesForPrincipal(PrincipalModel principal)
    {
        ServicesUtil.validateParameterNotNull(principal, "Parameter 'principal' cannot be null");
        return getCockpitConfigurationDao().findRoleNamesByPrincipal(principal);
    }


    protected CockpitConfigurationDao getCockpitConfigurationDao()
    {
        return this.cockpitConfigurationDao;
    }


    @Required
    public void setCockpitConfigurationDao(CockpitConfigurationDao cockpitConfigurationDao)
    {
        this.cockpitConfigurationDao = cockpitConfigurationDao;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}

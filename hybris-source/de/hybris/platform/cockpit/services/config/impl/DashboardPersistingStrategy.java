package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.daos.CockpitUIComponentConfigurationDao;
import de.hybris.platform.cockpit.jalo.CockpitUIComponentConfiguration;
import de.hybris.platform.cockpit.model.CockpitUIComponentConfigurationModel;
import de.hybris.platform.cockpit.model.CockpitUIConfigurationMediaModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.WidgetDashboardConfiguration;
import de.hybris.platform.cockpit.widgets.portal.PortalWidgetCoordinate;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DashboardPersistingStrategy extends DefaultConfigurationPersistingStrategy
{
    private ModelService modelService;
    private MediaService mediaService;
    private CockpitUIComponentConfigurationDao cockpitConfigurationDao;


    public void persistComponentConfiguration(UIComponentConfiguration configuration, UserModel user, ObjectTemplate objectTemplate, String code)
    {
        if(configuration instanceof WidgetDashboardConfiguration)
        {
            String xmlContent = createXmlContent((WidgetDashboardConfiguration)configuration);
            CockpitUIComponentConfiguration configItem = getCockpitUIComponentConfigurationDao().findCockpitUIComponentConfiguration(user.getUid(), "item", code);
            MediaModel media = null;
            CockpitUIComponentConfigurationModel configModel = null;
            if(configItem == null)
            {
                configModel = (CockpitUIComponentConfigurationModel)getModelService().create(CockpitUIComponentConfigurationModel.class);
                configModel.setCode(code);
                configModel.setFactoryBean("widgetDashboardConfigurationFactory");
                configModel.setObjectTemplateCode("item");
                configModel.setPrincipal((PrincipalModel)user);
            }
            else
            {
                configModel = (CockpitUIComponentConfigurationModel)this.modelService.get(configItem);
            }
            if(configModel.getMedia() == null)
            {
                media = (MediaModel)getModelService().create(CockpitUIConfigurationMediaModel.class);
                media.setCode(code + "_" + code + "_media");
                media.setCatalogVersion(null);
                getModelService().save(media);
                configModel.setMedia(media);
            }
            else
            {
                media = configModel.getMedia();
            }
            getMediaService().setDataForMedia(media, xmlContent.getBytes());
            getModelService().save(media);
            getModelService().save(configModel);
        }
    }


    protected String createXmlContent(WidgetDashboardConfiguration config)
    {
        String header = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n\t\t\n<widget-dashboard \txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n\txsi:noNamespaceSchemaLocation=\"../../../widget-dashboard.xsd\">";
        String footer = "\n</widget-dashboard>";
        StringBuilder contentBuilder = new StringBuilder();
        if(StringUtils.isNotBlank(config.getContainerLayoutID()))
        {
            contentBuilder.append("\n\t<containerlayout value=\"" + config.getContainerLayoutID() + "\"/>\n");
        }
        contentBuilder.append("\n\t<widgetpositions>");
        if(!config.getCoordinatesMap().isEmpty())
        {
            for(Map.Entry<String, PortalWidgetCoordinate> entry : (Iterable<Map.Entry<String, PortalWidgetCoordinate>>)config.getCoordinatesMap().entrySet())
            {
                contentBuilder.append("\n\t\t<position widgetID=\"" + (String)entry.getKey() + "\" column=\"" + ((PortalWidgetCoordinate)entry.getValue()).getColumn() + "\" row=\"" + ((PortalWidgetCoordinate)entry
                                .getValue()).getRow() + "\"/>");
            }
        }
        contentBuilder.append("\n\t</widgetpositions>\n");
        return "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n\t\t\n<widget-dashboard \txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n\txsi:noNamespaceSchemaLocation=\"../../../widget-dashboard.xsd\">" + contentBuilder.toString() + "\n</widget-dashboard>";
    }


    public Class getComponentClass()
    {
        return WidgetDashboardConfiguration.class;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    public MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setCockpitUIComponentConfigurationDao(CockpitUIComponentConfigurationDao cockpitConfigurationDao)
    {
        this.cockpitConfigurationDao = cockpitConfigurationDao;
    }


    public CockpitUIComponentConfigurationDao getCockpitUIComponentConfigurationDao()
    {
        return this.cockpitConfigurationDao;
    }
}

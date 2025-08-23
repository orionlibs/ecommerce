package com.hybris.hybrisdatasupplierbackoffice;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import de.hybris.datasupplier.services.genericdatasupplier.DataSupplierGenerationService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.localization.Localization;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ValidationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zul.Messagebox;

public class DataSupplierFlowActionHandler implements FlowActionHandler
{
    private static final String SBG = "sbg";
    private static final String EMS = "ems";
    private static final String DATAHUB = "dataHub";
    private static final String EMS_WEBAPP_LOCATION = "generic.datasupplier.ems.webapp.path";
    private static final String EMS_POM_LOCATION = "generic.datasupplier.ems.pom.path";
    private static final String SBG_WEBAPP_LOCATION = "generic.datasupplier.sbg.webapp.path";
    private static final String SBG_POM_LOCATION = "generic.datasupplier.sbg.pom.path";
    private static final String DATAHUB_WEBAPP_LOCATION = "generic.datasupplier.datahub.webapp.path";
    private static final String DATAHUB_LOCAL_PROPERTIES_PATH = "generic.datasupplier.datahub.properties";
    private static final String DATAHUB_WEBAPP_XML_PATH = "generic.datasupplier.datahub.webapp.xml";
    private static final String SAVE_PAYLOAD = "generic.datasupplier.sldreg.save.payload";
    private static final String SUPPLY_DELAY = "generic.datasupplier.supply.delay";
    private static final String SLDREG_LOCATION = "datasupplier.sldreg.exe.cmd";
    private static final String SLDREG_CONFIG_LOCATION = "datasupplier.sldreg.config.path";
    private static final String SYSTEM_ID = "com.sap.sup.admin.sldsupplier.SYSTEM_ID";
    private static final String DATAHUB_RADIO = "newConfig.dataHub";
    private static final String EMS_RADIO = "newConfig.ems";
    private static final String SBG_RADIO = "newConfig.sbg";
    private static final String SYSTEM_ID_TEXTBOX = "newConfig.systemId";
    private static final String DATAHUB_WEBAPP_LOCATION_TEXTBOX = "newConfig.datahubWebapp";
    private static final String DATAHUB_WEBAPP_XML_PATH_TEXTBOX = "newConfig.datahubWebappXml";
    private static final String DATAHUB_WEBAPP_LOCAL_PROPERTIES_PATH_TEXTBOX = "newConfig.datahubPropertiesFile";
    private static final String SBG_WEBAPP_LOCATION_TEXTBOX = "newConfig.sbgWebapp";
    private static final String SBG_POM_LOCATION_TEXTBOX = "newConfig.sbgPomFile";
    private static final String EMS_POM_LOCATION_TEXTBOX = "newConfig.emsPomFile";
    private static final String EMS_WEBAPP_LOCATION_TEXTBOX = "newConfig.emsWebapp";
    private static final String SAVE_PAYLOAD_TEXTBOX = "newConfig.savePayload";
    private static final String SUPPLY_DELAY_TEXTBOX = "newConfig.supplyDelay";
    private static final String SLDREG_LOCATION_TEXTBOX = "newConfig.sldregLocation";
    private static final String SLDREG_CONFIG_LOCATION_TEXTBOX = "newConfig.sldregConfigLocation";
    private static final String GENERATED_WAR_TEXTBOX = "newConfig.generatedWar";
    private static final String SYSTEM_ID_PATTERN = "^[A-Z\\d]{3}[A-Z\\d_]{0,5}$";
    private DataSupplierGenerationService generationService;
    private ModelService modelService;
    private MediaService mediaService;
    private static final Logger LOG = Logger.getLogger(DataSupplierFlowActionHandler.class);


    public void perform(CustomType customType, FlowActionHandlerAdapter adapter, Map<String, String> parameters)
    {
        WidgetModel model = adapter.getWidgetInstanceManager().getModel();
        try
        {
            checkSystemId(model);
            File generatedWar = generateWarFile(model);
            MediaModel mediaModel = createMedia(generatedWar);
            model.setValue("newConfig.generatedWar", mediaModel);
            adapter.next();
        }
        catch(ValidationException exc)
        {
            Messagebox.show(exc.getMessage(), "Error", 1, "z-messagebox-icon z-messagebox-error");
            LOG.error(exc);
        }
        catch(JaloBusinessException exc)
        {
            Messagebox.show(exc.getMessage(), "Error", 1, "z-messagebox-icon z-messagebox-error");
            LOG.error(exc);
        }
    }


    protected void checkSystemId(WidgetModel widgetModel)
    {
        String systemId = (String)widgetModel.getValue("newConfig.systemId", String.class);
        if(systemId != null)
        {
            Pattern systemIdPattern = Pattern.compile("^[A-Z\\d]{3}[A-Z\\d_]{0,5}$");
            Matcher matcher = systemIdPattern.matcher(systemId);
            if(!matcher.matches())
            {
                throw new ValidationException(Localization.getLocalizedString("genericdatasupplier.systemid.error"));
            }
        }
        else
        {
            throw new ValidationException(Localization.getLocalizedString("genericdatasupplier.systemid.error"));
        }
    }


    protected File generateWarFile(WidgetModel model)
    {
        try
        {
            List<String> applications = new ArrayList<>();
            Boolean datahub = (Boolean)model.getValue("newConfig.dataHub", Boolean.class);
            Properties additionalProperties = new Properties();
            if(Boolean.TRUE.equals(datahub))
            {
                applications.add("dataHub");
                putFieldValue(additionalProperties, "generic.datasupplier.datahub.properties", "newConfig.datahubPropertiesFile", model);
                putFieldValue(additionalProperties, "generic.datasupplier.datahub.webapp.path", "newConfig.datahubWebapp", model);
                putFieldValue(additionalProperties, "generic.datasupplier.datahub.webapp.xml", "newConfig.datahubWebappXml", model);
            }
            Boolean ems = (Boolean)model.getValue("newConfig.ems", Boolean.class);
            if(Boolean.TRUE.equals(ems))
            {
                applications.add("ems");
                putFieldValue(additionalProperties, "generic.datasupplier.ems.pom.path", "newConfig.emsPomFile", model);
                putFieldValue(additionalProperties, "generic.datasupplier.ems.webapp.path", "newConfig.emsWebapp", model);
            }
            Boolean sbg = (Boolean)model.getValue("newConfig.sbg", Boolean.class);
            if(Boolean.TRUE.equals(sbg))
            {
                applications.add("sbg");
                putFieldValue(additionalProperties, "generic.datasupplier.sbg.pom.path", "newConfig.sbgPomFile", model);
                putFieldValue(additionalProperties, "generic.datasupplier.sbg.webapp.path", "newConfig.sbgWebapp", model);
            }
            putFieldValue(additionalProperties, "datasupplier.sldreg.exe.cmd", "newConfig.sldregLocation", model);
            putFieldValue(additionalProperties, "datasupplier.sldreg.config.path", "newConfig.sldregConfigLocation", model);
            putFieldValue(additionalProperties, "generic.datasupplier.sldreg.save.payload", "newConfig.savePayload", model);
            putFieldValue(additionalProperties, "generic.datasupplier.supply.delay", "newConfig.supplyDelay", model);
            putFieldValue(additionalProperties, "com.sap.sup.admin.sldsupplier.SYSTEM_ID", "newConfig.systemId", model);
            return getGenerationService().generateDataSupplier(applications, additionalProperties);
        }
        catch(NullPointerException exc)
        {
            throw new ValidationException("The genericdatasupplier.war file can not be found");
        }
        catch(Exception exc)
        {
            throw new ValidationException(exc);
        }
    }


    protected MediaModel createMedia(File generatedWar) throws JaloBusinessException
    {
        Media media = MediaManager.getInstance().createMedia(generatedWar.getName());
        media.setFile(generatedWar);
        media.setRealFileName("genericdatasupplier.war");
        media.setMime("application/war");
        String mediaCode = media.getCode();
        return getMediaService().getMedia(mediaCode);
    }


    protected void putFieldValue(Properties additionalProperties, String key, String fieldName, WidgetModel model)
    {
        Object currentValue = model.getValue(fieldName, model.getValueType(fieldName));
        if(currentValue != null)
        {
            if(!currentValue.getClass().equals(String.class))
            {
                currentValue = currentValue.toString();
            }
            additionalProperties.put(key, currentValue);
        }
    }


    public DataSupplierGenerationService getGenerationService()
    {
        if(this.generationService == null)
        {
            this.generationService = (DataSupplierGenerationService)Registry.getApplicationContext().getBean("dataSupplierGenerationService", DataSupplierGenerationService.class);
        }
        return this.generationService;
    }


    protected MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}

package de.hybris.platform.cockpit.services.label.impl;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.BaseConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.services.label.ObjectLabelProvider;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import org.springframework.beans.factory.annotation.Required;

public class DefaultLabelService implements LabelService
{
    private TypeService typeService;
    private UIConfigurationService uiConfigurationService;
    private SessionService sessionService;
    private LanguageProvider languageProvider;


    public String getObjectTextLabel(TypedObject object)
    {
        return getObjectTextLabelForTypedObject(object);
    }


    public String getObjectTextLabelForTypedObject(TypedObject object)
    {
        Object labelObject = getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, object));
        return labelObject.toString();
    }


    public String getObjectDescription(TypedObject object)
    {
        return getObjectDescriptionForTypedObject(object);
    }


    public String getObjectDescriptionForTypedObject(TypedObject object)
    {
        Object descriptionObject = getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, object));
        return descriptionObject.toString();
    }


    public String getObjectIconPath(TypedObject object)
    {
        return getObjectIconPathForTypedObject(object);
    }


    public String getObjectIconPathForTypedObject(TypedObject object)
    {
        String langIso = getLanguageProvider().getLanguageIso();
        String iconPath = null;
        ObjectLabelProvider objectLabelProvider = getProvider(object);
        if(objectLabelProvider != null)
        {
            iconPath = objectLabelProvider.getIconPath(object, langIso);
            if(iconPath == null)
            {
                iconPath = objectLabelProvider.getIconPath(object);
            }
        }
        return iconPath;
    }


    private ObjectLabelProvider getProvider(TypedObject object)
    {
        ObjectTemplate objectTemplate = this.typeService.getObjectTemplate(object.getType().getCode());
        BaseConfiguration baseConfig = (BaseConfiguration)this.uiConfigurationService.getComponentConfiguration(objectTemplate, "base", BaseConfiguration.class);
        if(baseConfig != null)
        {
            return baseConfig.getObjectLabelProvider();
        }
        return null;
    }


    public void setUiConfigurationService(UIConfigurationService uiConfigurationService)
    {
        this.uiConfigurationService = uiConfigurationService;
    }


    public void setCockpitTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public void setLanguageProvider(LanguageProvider languageProvider)
    {
        this.languageProvider = languageProvider;
    }


    protected LanguageProvider getLanguageProvider()
    {
        if(this.languageProvider == null)
        {
            this.languageProvider = (LanguageProvider)new GlobalDataLanguageProvider();
        }
        return this.languageProvider;
    }
}

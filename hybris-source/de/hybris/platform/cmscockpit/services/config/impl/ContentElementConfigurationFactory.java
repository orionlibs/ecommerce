package de.hybris.platform.cmscockpit.services.config.impl;

import de.hybris.platform.cmscockpit.services.config.ContentElementConfiguration;
import de.hybris.platform.cmscockpit.services.config.ContentElementListConfiguration;
import de.hybris.platform.cmscockpit.services.config.jaxb.contentelement.ContentElement;
import de.hybris.platform.cmscockpit.services.config.jaxb.contentelement.ContentElements;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.impl.JAXBBasedUIComponentConfigurationFactory;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class ContentElementConfigurationFactory extends JAXBBasedUIComponentConfigurationFactory<ContentElementListConfiguration, ContentElements>
{
    private static final Logger LOG = Logger.getLogger(ContentElementConfigurationFactory.class);


    protected ContentElementListConfiguration createUIComponent(ObjectTemplate objectTemplate, ObjectTemplate originalObjectTemplate, ContentElements xmlContentElements)
    {
        Map<ObjectType, ContentElementConfiguration> elementsMap = createElementMap(xmlContentElements);
        return (ContentElementListConfiguration)new DefaultContentElementListConfiguration(elementsMap);
    }


    public UIComponentConfiguration createDefault(ObjectTemplate objectTemplate)
    {
        DefaultContentElementListConfiguration defConf = new DefaultContentElementListConfiguration(Collections.EMPTY_MAP);
        return (UIComponentConfiguration)defConf;
    }


    public Class getComponentClass()
    {
        return ContentElementListConfiguration.class;
    }


    protected Map<ObjectType, ContentElementConfiguration> createElementMap(ContentElements xmlContentElements)
    {
        HashMap<ObjectType, ContentElementConfiguration> elementMap = null;
        if(xmlContentElements != null)
        {
            elementMap = new HashMap<>();
            List<ContentElement> contentElements = xmlContentElements.getContentElement();
            if(contentElements != null)
            {
                for(ContentElement elem : contentElements)
                {
                    if(elem.getType() != null && elem.getType().length() > 0)
                    {
                        try
                        {
                            ObjectTemplate contentElementType = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(elem.getType());
                            DefaultContentElementConfiguration defaultContentElementConfiguration = new DefaultContentElementConfiguration(contentElementType.isDefaultTemplate() ? (ObjectType)contentElementType.getBaseType() : (ObjectType)contentElementType, elem.getName(), elem.getDescription(),
                                            elem.getImage(), elem.getRefImage(), elem.getImageSmall());
                            elementMap.put(contentElementType, defaultContentElementConfiguration);
                        }
                        catch(UnknownIdentifierException e)
                        {
                            LOG.debug("configuration for " + elem.getType() + " could not be loaded", (Throwable)e);
                        }
                    }
                }
            }
        }
        else
        {
            LOG.warn("No content elements object available.");
        }
        return elementMap;
    }
}

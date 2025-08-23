package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.daos.CockpitUIComponentConfigurationDao;
import de.hybris.platform.cockpit.jalo.CockpitUIComponentConfiguration;
import de.hybris.platform.cockpit.model.CockpitUIComponentConfigurationModel;
import de.hybris.platform.cockpit.model.CockpitUIConfigurationMediaModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.services.config.ConfigurationPersistingStrategy;
import de.hybris.platform.cockpit.services.config.ContextAwareUIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationException;
import de.hybris.platform.cockpit.services.security.UIAccessRightService;
import de.hybris.platform.cockpit.util.jaxb.JAXBContextCache;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;
import org.springframework.util.xml.TransformerUtils;
import org.xml.sax.SAXException;

public class DefaultConfigurationPersistingStrategy<CONFIG extends UIComponentConfiguration, JAXBCLASS> implements ConfigurationPersistingStrategy<CONFIG>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultConfigurationPersistingStrategy.class);
    private String componentClassName;
    private Resource schemaResource;
    private Class<JAXBCLASS> jaxbClass;
    private String configurationFactory;
    private ModelService modelService;
    private MediaService mediaService;
    private CockpitUIComponentConfigurationDao cockpitConfigurationDao;
    private UIAccessRightService uiAccessRightService;
    private JAXBContextCache jaxbContextCache;


    public JAXBCLASS updateJaxb(CONFIG configuration)
    {
        LOG.error("Not implemented. Please implement updateJaxb() in a subclass to convert/update " + configuration + ".");
        return null;
    }


    public void persistComponentConfiguration(CONFIG configuration, UserModel user, ObjectTemplate objectTemplate, String code)
    {
        if(checkPermissions(user))
        {
            JAXBCLASS jaxb = updateJaxb(configuration);
            LOG.debug("configuration " + jaxb + " is to be persisted");
            String rawXml = createXml(jaxb);
            try
            {
                TransformerFactory factory = TransformerFactory.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", null);
                factory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
                factory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet", "");
                Transformer transformer = factory.newTransformer();
                TransformerUtils.enableIndenting(transformer, 4);
                OutputStream ostream = new ByteArrayOutputStream();
                try
                {
                    StreamResult result = new StreamResult(ostream);
                    Source src = new StreamSource(new StringReader(rawXml));
                    transformer.transform(src, result);
                    storeConfigurationMedia((PrincipalModel)user, result.getOutputStream().toString(), code, objectTemplate.getCode(),
                                    getConfigurationFactory());
                }
                finally
                {
                    IOUtils.closeQuietly(ostream);
                }
            }
            catch(Exception e)
            {
                LOG.error("Could not persist configuration, reason: ", e);
            }
        }
    }


    protected boolean checkPermissions(UserModel user)
    {
        return this.uiAccessRightService.canWrite(user, "cockpit.personalizedconfiguration");
    }


    protected void storeConfigurationMedia(PrincipalModel principal, String xmlContent, String code, String objectTemplateCode, String factoryBeanId)
    {
        MediaModel media;
        CockpitUIComponentConfigurationModel configModel;
        CockpitUIComponentConfiguration configItem = getCockpitUIComponentConfigurationDao().findCockpitUIComponentConfiguration(principal.getUid(), objectTemplateCode, code);
        if(configItem == null)
        {
            configModel = (CockpitUIComponentConfigurationModel)getModelService().create(CockpitUIComponentConfigurationModel.class);
            configModel.setCode(code);
            configModel.setFactoryBean(factoryBeanId);
            configModel.setObjectTemplateCode(objectTemplateCode);
            configModel.setPrincipal(principal);
        }
        else
        {
            configModel = (CockpitUIComponentConfigurationModel)this.modelService.get(configItem);
        }
        if(configModel.getMedia() == null)
        {
            media = (MediaModel)getModelService().create(CockpitUIConfigurationMediaModel.class);
            String mediaCode = code + "_" + code + "_" + objectTemplateCode + "_media_personalized";
            try
            {
                MediaModel media2 = getMediaService().getMedia(mediaCode);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Found old unreferenced media in the system. this media will be removed.");
                }
                getModelService().remove(media2);
            }
            catch(UnknownIdentifierException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("No media with code '" + mediaCode + "' found, creating new one.");
                }
            }
            catch(AmbiguousIdentifierException e)
            {
                LOG.error("Could not proceed due to data inconsistency, ", (Throwable)e);
            }
            media.setCode(mediaCode);
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


    public Class<CONFIG> getComponentClass()
    {
        try
        {
            return (Class)Class.forName(this.componentClassName);
        }
        catch(ClassNotFoundException e)
        {
            LOG.error("Could not get class for name " + this.componentClassName + ", ignoring persisting strategy.");
            return null;
        }
    }


    public String createXml(JAXBCLASS root)
    {
        try
        {
            Schema schema;
            JAXBContext jctx = this.jaxbContextCache.resolveContext(this.jaxbClass);
            Marshaller marshaller = jctx.createMarshaller();
            try
            {
                schema = this.jaxbContextCache.resolveSchema(this.schemaResource.getURL());
            }
            catch(IOException e)
            {
                throw new UIConfigurationException(e.getMessage(), e);
            }
            marshaller.setSchema(schema);
            OutputStream ostream = new ByteArrayOutputStream();
            marshaller.marshal(root, ostream);
            return ostream.toString();
        }
        catch(JAXBException e)
        {
            Throwable throwable;
            JAXBException jAXBException1 = e;
            if(e.getLinkedException() != null)
            {
                throwable = e.getLinkedException();
            }
            throw new UIConfigurationException(throwable.getMessage(), throwable);
        }
        catch(SAXException e)
        {
            throw new UIConfigurationException(e.getMessage(), e);
        }
    }


    public void setJaxbClass(Class<JAXBCLASS> jaxbClass)
    {
        this.jaxbClass = jaxbClass;
    }


    public String getComponentClassName()
    {
        return this.componentClassName;
    }


    public void setComponentClassName(String componentClass)
    {
        this.componentClassName = componentClass;
    }


    public String getConfigurationFactory()
    {
        return this.configurationFactory;
    }


    public void setConfigurationFactory(String factory)
    {
        this.configurationFactory = factory;
    }


    protected JAXBCLASS getRootJaxbElement(CONFIG config)
    {
        JaxbBasedUIComponentConfigurationContext<JAXBCLASS> context = getContext(config);
        if(context != null)
        {
            return (JAXBCLASS)context.getRootJaxbElement();
        }
        return null;
    }


    protected Object getJaxbElement(CONFIG config, Object element)
    {
        JaxbBasedUIComponentConfigurationContext<JAXBCLASS> context = getContext(config);
        if(context != null)
        {
            return context.getJaxbElement(element);
        }
        return null;
    }


    private JaxbBasedUIComponentConfigurationContext<JAXBCLASS> getContext(CONFIG config)
    {
        if(config instanceof ContextAwareUIComponentConfiguration)
        {
            JaxbBasedUIComponentConfigurationContext<JAXBCLASS> context = (JaxbBasedUIComponentConfigurationContext<JAXBCLASS>)((ContextAwareUIComponentConfiguration)config).getContext();
            return context;
        }
        return null;
    }


    public void setSchemaResource(Resource schemaResource)
    {
        this.schemaResource = schemaResource;
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


    public MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    @Required
    public void setUiAccessRightService(UIAccessRightService uiAccessRightService)
    {
        this.uiAccessRightService = uiAccessRightService;
    }


    public CockpitUIComponentConfigurationDao getCockpitUIComponentConfigurationDao()
    {
        return this.cockpitConfigurationDao;
    }


    @Required
    public void setCockpitUIComponentConfigurationDao(CockpitUIComponentConfigurationDao cockpitConfigurationDao)
    {
        this.cockpitConfigurationDao = cockpitConfigurationDao;
    }


    @Required
    public void setJaxbContextCache(JAXBContextCache jaxbContextCache)
    {
        this.jaxbContextCache = jaxbContextCache;
    }
}

package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.config.BaseConfiguration;
import de.hybris.platform.cockpit.services.config.UIComponentConfiguration;
import de.hybris.platform.cockpit.services.config.UIComponentConfigurationFactory;
import de.hybris.platform.cockpit.services.config.UIConfigurationException;
import de.hybris.platform.cockpit.services.config.jaxb.ConfigLabel;
import de.hybris.platform.cockpit.services.meta.PropertyService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.search.SearchService;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.jaxb.JAXBContextCache;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.zkoss.util.Locales;
import org.zkoss.util.resource.Labels;

public abstract class JAXBBasedUIComponentConfigurationFactory<UICOMPONENTCONFIG extends UIComponentConfiguration, JAXBCLASS> implements UIComponentConfigurationFactory, BeanFactoryAware
{
    public static final String COCKPIT_CONFIG_LABEL_PREFIX = "cockpit.config.label.";
    private Class<JAXBCLASS> jaxbClass;
    private Resource schemaResource;
    protected TypeService typeService;
    protected SearchService searchService;
    protected BeanFactory beanFactory;
    private PropertyService propertyService;
    private CommonI18NService commonI18NService;
    private JAXBContextCache jaxbContextCache;


    public UICOMPONENTCONFIG create(ObjectTemplate objectTemplate, ObjectTemplate originalObjectTemplate, InputSource inputSource)
    {
        if(objectTemplate == null)
        {
            throw new IllegalArgumentException("Object template must not be null");
        }
        if(inputSource == null)
        {
            throw new IllegalArgumentException("Input source must not be null");
        }
        JAXBCLASS jaxbElement = null;
        try
        {
            Schema schema;
            Unmarshaller unmarshaller = this.jaxbContextCache.resolveContext(this.jaxbClass).createUnmarshaller();
            try
            {
                schema = this.jaxbContextCache.resolveSchema(this.schemaResource.getURL());
            }
            catch(IOException e)
            {
                throw new UIConfigurationException(e.getMessage(), e);
            }
            unmarshaller.setSchema(schema);
            jaxbElement = (JAXBCLASS)unmarshaller.unmarshal(inputSource);
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
        return createUIComponent(objectTemplate, originalObjectTemplate, jaxbElement);
    }


    protected Map<String, List<PropertyDescriptor>> getDefaultPropertyGroups(ObjectTemplate objectTemplate, BaseConfiguration baseConfiguration)
    {
        Map<String, List<PropertyDescriptor>> result = new LinkedHashMap<>();
        Set<PropertyDescriptor> generalProps = new LinkedHashSet<>();
        Set<PropertyDescriptor> additionalProps = new LinkedHashSet<>();
        generalProps.addAll(BaseFallbackConfigHelper.getBaseProperties(baseConfiguration));
        generalProps.addAll(BaseFallbackConfigHelper.getLabelProperties(baseConfiguration));
        generalProps.addAll(BaseFallbackConfigHelper.getSearchProperties(baseConfiguration, this.typeService));
        generalProps.addAll(TypeTools.getMandatoryAttributes((ObjectType)objectTemplate, false, this.propertyService));
        result.put("General", new ArrayList<>(generalProps));
        additionalProps.addAll(objectTemplate.getBaseType().getDeclaredPropertyDescriptors());
        additionalProps.addAll(BaseFallbackConfigHelper.getSortProperties(baseConfiguration, this.typeService));
        additionalProps.removeAll(generalProps);
        result.put("Additional", new ArrayList<>(additionalProps));
        return result;
    }


    protected List<PropertyDescriptor> getOtherProperties(Collection<PropertyDescriptor> props, ObjectTemplate template)
    {
        Set<PropertyDescriptor> allDescriptors = template.getPropertyDescriptors();
        List<PropertyDescriptor> other = new LinkedList<>();
        for(PropertyDescriptor desc : allDescriptors)
        {
            if(!props.contains(desc))
            {
                other.add(desc);
            }
        }
        return other;
    }


    protected Map<LanguageModel, String> createLabelForAllLanguages(String label, Set<LanguageModel> languages)
    {
        Map<LanguageModel, String> labels = new HashMap<>();
        for(LanguageModel language : languages)
        {
            labels.put(language, label);
            String locLabel = null;
            Locale oldLocale = Locales.setThreadLocal(this.commonI18NService
                            .getLocaleForLanguage(language));
            try
            {
                locLabel = Labels.getLabel("cockpit.config.label." + label);
            }
            finally
            {
                if(oldLocale != null)
                {
                    Locales.setThreadLocal(oldLocale);
                }
            }
            if(!StringUtils.isEmpty(locLabel))
            {
                labels.put(language, locLabel);
            }
        }
        return labels;
    }


    protected Map<LanguageModel, String> createLabelMap(List<? extends ConfigLabel> labels, Set<LanguageModel> languages)
    {
        String labelKey = null;
        Map<String, String> labelMap = new HashMap<>();
        for(ConfigLabel label : labels)
        {
            if(!StringUtils.isEmpty(label.getKey()))
            {
                labelKey = label.getKey();
                continue;
            }
            if(!StringUtils.isEmpty(label.getLang()))
            {
                labelMap.put(label.getLang(), label.getValue());
            }
        }
        return createLabelMap(labelKey, labelMap, languages);
    }


    protected Map<LanguageModel, String> createLabelMap(String localizationKey, Map<String, String> localizations, Set<LanguageModel> languages)
    {
        boolean hasKey = !StringUtils.isBlank(localizationKey);
        Map<LanguageModel, String> map = new HashMap<>();
        for(LanguageModel lang : languages)
        {
            String label = localizations.get(lang.getIsocode());
            if(hasKey && StringUtils.isEmpty(label))
            {
                Locale oldLocale = Locales.setThreadLocal(this.commonI18NService
                                .getLocaleForLanguage(lang));
                try
                {
                    label = Labels.getLabel(localizationKey);
                }
                finally
                {
                    if(oldLocale != null)
                    {
                        Locales.setThreadLocal(oldLocale);
                    }
                }
            }
            if(!StringUtils.isEmpty(label))
            {
                map.put(lang, label);
            }
        }
        return map;
    }


    protected abstract UICOMPONENTCONFIG createUIComponent(ObjectTemplate paramObjectTemplate1, ObjectTemplate paramObjectTemplate2, JAXBCLASS paramJAXBCLASS);


    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        this.beanFactory = beanFactory;
    }


    public void setJaxbClass(Class<JAXBCLASS> jaxbClass)
    {
        this.jaxbClass = jaxbClass;
    }


    public void setSchemaResource(Resource schemaResource)
    {
        this.schemaResource = schemaResource;
    }


    public void setCockpitTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public void setSearchService(SearchService searchService)
    {
        this.searchService = searchService;
    }


    @Required
    public void setPropertyService(PropertyService propertyService)
    {
        this.propertyService = propertyService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    @Required
    public void setJaxbContextCache(JAXBContextCache jaxbContextCache)
    {
        this.jaxbContextCache = jaxbContextCache;
    }
}

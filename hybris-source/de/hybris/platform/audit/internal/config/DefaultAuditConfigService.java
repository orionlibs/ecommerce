package de.hybris.platform.audit.internal.config;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.platform.core.model.audit.AuditReportConfigModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAuditConfigService implements AuditConfigService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAuditConfigService.class);
    private FlexibleSearchService flexibleSearchService;
    private ModelService modelService;
    private XMLAuditReportConfigReader xmlAuditReportConfigReader;
    private TypeService typeService;


    public AuditReportConfig getConfigForName(String configName)
    {
        Objects.requireNonNull(configName, "Name of the config is required");
        AuditReportConfigModel auditConfig = findAuditConfig(configName);
        return this.xmlAuditReportConfigReader.fromXml(auditConfig);
    }


    private AuditReportConfigModel findAuditConfig(String configName)
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {AuditReportConfig} WHERE {code}=?code AND {active}=?active");
        fQuery.addQueryParameter("code", configName);
        fQuery.addQueryParameter("active", Boolean.TRUE);
        return (AuditReportConfigModel)this.flexibleSearchService.searchUnique(fQuery);
    }


    String toXml(AuditReportConfig config)
    {
        Objects.requireNonNull(config, "config is required");
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] {AuditReportConfig.class});
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty("jaxb.encoding", "UTF-8");
            marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(config, stringWriter);
            return stringWriter.toString();
        }
        catch(JAXBException e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    public void storeConfigurations()
    {
        Map<String, AuditReportConfig> configurations = mergeConfigurationsForExtensions();
        configurations.values().forEach(this::storeConfiguration);
    }


    private AuditReportConfigModel getOrCreateConfig(String configName)
    {
        AuditReportConfigModel config;
        try
        {
            config = findAuditConfig(configName);
        }
        catch(Exception e)
        {
            LOG.debug("Couldn't find existing config item, creating new one", e);
            config = (AuditReportConfigModel)this.modelService.create(AuditReportConfigModel.class);
            config.setCode(configName);
            config.setActive(Boolean.TRUE);
        }
        return config;
    }


    private void storeConfiguration(AuditReportConfig config)
    {
        storeConfiguration(config.getName(), toXml(config));
    }


    public AuditReportConfigModel storeConfiguration(String configName, String content)
    {
        Objects.requireNonNull(configName, "configName is required");
        Objects.requireNonNull(content, "content is required");
        AuditReportConfigModel config = getOrCreateConfig(configName);
        config.setContent(content);
        this.modelService.save(config);
        return config;
    }


    private Map<String, AuditReportConfig> mergeConfigurationsForExtensions()
    {
        Map<String, AuditReportConfig> configurations = new HashMap<>();
        for(ExtensionInfo info : getAllExtensions())
        {
            Map<String, AuditReportConfig> collect = collectConfigurationsForExtension(info);
            for(Map.Entry<String, AuditReportConfig> entry : collect.entrySet())
            {
                AuditReportConfig configuration = entry.getValue();
                String confKey = configuration.getName();
                AuditReportConfig toMergeTo = configurations.get(confKey);
                if(toMergeTo == null)
                {
                    configurations.put(confKey, configuration);
                    continue;
                }
                AuditReportConfig merged = mergeConfigurations(toMergeTo, configuration);
                configurations.replace(confKey, merged);
            }
        }
        return configurations;
    }


    private Map<String, AuditReportConfig> collectConfigurationsForExtension(ExtensionInfo extensionInfo)
    {
        return (Map<String, AuditReportConfig>)extensionInfo.getGenericAuditXMLs().stream()
                        .map(configFile -> getAuditReportConfig(configFile))
                        .collect(Collectors.toMap(c -> extensionInfo.getName() + "-" + extensionInfo.getName(), Function.identity()));
    }


    private AuditReportConfig getAuditReportConfig(File configFile)
    {
        try
        {
            FileInputStream file = new FileInputStream(configFile);
            try
            {
                AuditReportConfig auditReportConfig = this.xmlAuditReportConfigReader.fromXml(file);
                file.close();
                return auditReportConfig;
            }
            catch(Throwable throwable)
            {
                try
                {
                    file.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    private static List<ExtensionInfo> getAllExtensions()
    {
        return ConfigUtil.getPlatformConfig(DefaultAuditConfigService.class).getExtensionInfosInBuildOrder();
    }


    AuditReportConfig mergeConfigurations(AuditReportConfig config1, AuditReportConfig config2)
    {
        AuditReportConfig.Builder merged = AuditReportConfig.builder().clone(config1);
        return merged.withTypesToCombine(config2.getAllTypes()).build();
    }


    public List<AuditReportConfig> getConfigsForRootType(String rootType)
    {
        Objects.requireNonNull(rootType, "rootType is required");
        List<AuditReportConfig> configurationsForRootType = new ArrayList<>();
        Set<String> allCodesForType = getAllSuperTypesUpperCaseCodesForType(rootType);
        for(AuditReportConfigModel configuration : findAllConfigurations())
        {
            AuditReportConfig auditReportConfig = this.xmlAuditReportConfigReader.fromXml(configuration);
            String rootTypeCode = auditReportConfig.getGivenRootType().getCode().toUpperCase();
            if(allCodesForType.contains(rootTypeCode))
            {
                configurationsForRootType.add(auditReportConfig);
            }
        }
        return configurationsForRootType;
    }


    private Set<String> getAllSuperTypesUpperCaseCodesForType(String typeCode)
    {
        List<ComposedTypeModel> types = new ArrayList<>();
        try
        {
            ComposedTypeModel compTypeModel = this.typeService.getComposedTypeForCode(typeCode);
            types.add(compTypeModel);
            types.addAll(compTypeModel.getAllSuperTypes());
        }
        catch(Exception e)
        {
            LOG.warn(e.getMessage(), e);
        }
        return (Set<String>)types.stream().map(c -> c.getCode().toUpperCase()).collect(Collectors.toSet());
    }


    private List<AuditReportConfigModel> findAllConfigurations()
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {pk} FROM {AuditReportConfig} WHERE {active}=?active");
        fQuery.addQueryParameter("active", Boolean.TRUE);
        return this.flexibleSearchService.search(fQuery).getResult();
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setXmlAuditReportConfigReader(XMLAuditReportConfigReader xmlAuditReportConfigReader)
    {
        this.xmlAuditReportConfigReader = xmlAuditReportConfigReader;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}

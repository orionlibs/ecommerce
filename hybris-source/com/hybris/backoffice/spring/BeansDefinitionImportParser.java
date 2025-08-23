package com.hybris.backoffice.spring;

import com.hybris.backoffice.constants.BackofficeModules;
import de.hybris.platform.spring.ctx.WebScopeTenantIgnoreDocReader;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class BeansDefinitionImportParser extends AbstractMultipleBeanParser
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BeansDefinitionImportParser.class);
    protected static final String ELEMENT_NAME = "import-modules";
    private static final String ATTRIBUTE_RESOURCES = "resources";


    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext)
    {
        try
        {
            Resource[] resources = getResources(element, parserContext);
            importResources(resources, parserContext);
        }
        catch(IOException ex)
        {
            throw new BeanDefinitionValidationException("Unable to import specified resources: " + element, ex);
        }
        return null;
    }


    protected Resource[] getResources(Element element, ParserContext parserContext) throws IOException
    {
        Optional<String> resourcesPattern = getResourcesPattern(element);
        Objects.requireNonNull(element);
        String pattern = resourcesPattern.orElseGet(element::getNodeValue);
        if(StringUtils.isNotBlank(pattern))
        {
            PathMatchingResourcePatternResolver pmrl = new PathMatchingResourcePatternResolver();
            return pmrl.getResources(pattern);
        }
        return (Resource[])BackofficeModules.getBackofficeModules().stream().map(BackofficeModules::getSpringDefinitionsFile)
                        .filter(File::exists).map(org.springframework.core.io.FileSystemResource::new).toArray(x$0 -> new Resource[x$0]);
    }


    protected Optional<String> getResourcesPattern(Element element)
    {
        if(element.hasAttributes())
        {
            Node attribute = element.getAttributes().getNamedItem("resources");
            if(attribute != null)
            {
                return Optional.of(attribute.getNodeValue());
            }
        }
        return Optional.empty();
    }


    protected void importResources(Resource[] resources, ParserContext parserContext) throws IOException
    {
        for(Resource resource : resources)
        {
            if(LOGGER.isInfoEnabled())
            {
                LOGGER.info("Importing additional bean definitions: {}", resource.getURI());
            }
            XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(parserContext.getRegistry());
            reader.setDocumentReaderClass(WebScopeTenantIgnoreDocReader.class);
            reader.setEnvironment(parserContext.getReaderContext().getEnvironment());
            int definitionsCount = reader.loadBeanDefinitions(resource);
            if(LOGGER.isDebugEnabled())
            {
                LOGGER.debug("Imported {} beans from {}", Integer.valueOf(definitionsCount), resource.getURI());
            }
        }
    }
}

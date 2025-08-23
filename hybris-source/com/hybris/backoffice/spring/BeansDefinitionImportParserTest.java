package com.hybris.backoffice.spring;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.util.Utilities;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.ProblemReporter;
import org.springframework.beans.factory.parsing.ReaderEventListener;
import org.springframework.beans.factory.parsing.SourceExtractor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.NamespaceHandlerResolver;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

@IntegrationTest
public class BeansDefinitionImportParserTest extends ServicelayerTest
{
    private static final String ATTRIBUTE_RESOURCES = "resources";
    private final BeansDefinitionImportParser parser = new BeansDefinitionImportParser();


    @Test
    public void shouldGetBeanDefinitionsForAllBackofficeModulesWhenNoPatternDefined() throws Exception
    {
        Element ELEMENT = (Element)Mockito.mock(Element.class);
        ParserContext PARSER_CONTEXT = new ParserContext((XmlReaderContext)Mockito.mock(XmlReaderContext.class), (BeanDefinitionParserDelegate)Mockito.mock(BeanDefinitionParserDelegate.class));
        List<File> BACKOFFICE_SPRING_MODULES = (List<File>)getBackofficeModules().stream().map(extension -> new File(extension.getItemsXML().getParent(), extension.getName() + "-backoffice-spring.xml")).filter(File::exists).collect(Collectors.toList());
        Resource[] resources = this.parser.getResources(ELEMENT, PARSER_CONTEXT);
        Assertions.assertThat((Object[])resources).hasSize(BACKOFFICE_SPRING_MODULES.size());
        for(Resource resource : resources)
        {
            Assertions.assertThat(BACKOFFICE_SPRING_MODULES).contains(new Object[] {resource.getFile()});
        }
    }


    @Test
    public void shouldGetMatchingBeanDefinitionsWhenPatternDefined() throws Exception
    {
        String PATTERN_RESOURCES = "backoffice-spring.xml";
        URL URL_RESOURCE = getClass().getResource("/backoffice-spring.xml");
        Element ELEMENT = (Element)Mockito.mock(Element.class);
        NamedNodeMap MAP_ATTRIBUTES = (NamedNodeMap)Mockito.mock(NamedNodeMap.class);
        Node NODE_RESOURCES = (Node)Mockito.mock(Node.class);
        Mockito.when(Boolean.valueOf(ELEMENT.hasAttributes())).thenReturn(Boolean.TRUE);
        Mockito.when(ELEMENT.getAttributes()).thenReturn(MAP_ATTRIBUTES);
        Mockito.when(MAP_ATTRIBUTES.getNamedItem((String)Matchers.eq("resources"))).thenReturn(NODE_RESOURCES);
        Mockito.when(NODE_RESOURCES.getNodeValue()).thenReturn("backoffice-spring.xml");
        ParserContext PARSER_CONTEXT = new ParserContext((XmlReaderContext)Mockito.mock(XmlReaderContext.class), (BeanDefinitionParserDelegate)Mockito.mock(BeanDefinitionParserDelegate.class));
        Resource[] resources = this.parser.getResources(ELEMENT, PARSER_CONTEXT);
        Assertions.assertThat((Object[])resources).hasSize(1);
        Assertions.assertThat(resources[0].getFile()).isEqualTo(FileUtils.toFile(URL_RESOURCE));
    }


    @Test
    public void shouldLoadAllBeansFromSpecifiedResources() throws Exception
    {
        ClassPathResource classPathResource = new ClassPathResource("/test/import-parser-spring.xml");
        BeanDefinitionRegistry BEAN_REGISTRY = (BeanDefinitionRegistry)Mockito.mock(BeanDefinitionRegistry.class);
        XmlBeanDefinitionReader READER = new XmlBeanDefinitionReader(BEAN_REGISTRY);
        XmlReaderContext READER_CONTEXT = new XmlReaderContext((Resource)Mockito.mock(Resource.class), (ProblemReporter)Mockito.mock(ProblemReporter.class), (ReaderEventListener)Mockito.mock(ReaderEventListener.class), (SourceExtractor)Mockito.mock(SourceExtractor.class), READER,
                        (NamespaceHandlerResolver)Mockito.mock(NamespaceHandlerResolver.class));
        ParserContext PARSER_CONTEXT = new ParserContext(READER_CONTEXT, (BeanDefinitionParserDelegate)Mockito.mock(BeanDefinitionParserDelegate.class));
        this.parser.importResources(new Resource[] {(Resource)classPathResource}, PARSER_CONTEXT);
        ArgumentCaptor<String> beanNames = ArgumentCaptor.forClass(String.class);
        ((BeanDefinitionRegistry)Mockito.verify(BEAN_REGISTRY, Mockito.times(2))).registerBeanDefinition((String)beanNames.capture(), (BeanDefinition)Matchers.any());
        Assertions.assertThat(beanNames.getAllValues()).containsExactly(new Object[] {"load-all-beans-from-specified-resources", "load-all-beans-from-specified-resources-2"});
    }


    private static List<ExtensionInfo> getBackofficeModules()
    {
        PlatformConfig PLATFORM_CONFIG = ConfigUtil.getPlatformConfig(Utilities.class);
        return (List<ExtensionInfo>)PLATFORM_CONFIG.getExtensionInfosInBuildOrder().stream()
                        .filter(ext -> Boolean.parseBoolean(ext.getMeta("backoffice-module"))).collect(Collectors.toList());
    }
}

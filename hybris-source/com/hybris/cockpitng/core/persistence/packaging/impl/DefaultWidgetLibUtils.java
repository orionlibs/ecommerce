/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.packaging.impl;

import static com.hybris.cockpitng.core.persistence.packaging.WidgetLibConstants.FILE_LIBRARY_INFO;
import static com.hybris.cockpitng.core.persistence.packaging.WidgetLibConstants.WIDGET_JAR_LIB_DIR;

import com.hybris.cockpitng.core.CockpitApplicationException;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetConnection;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.WidgetSocket;
import com.hybris.cockpitng.core.WidgetSocket.Multiplicity;
import com.hybris.cockpitng.core.WidgetSocket.SocketVisibility;
import com.hybris.cockpitng.core.impl.jaxb.Forward;
import com.hybris.cockpitng.core.impl.jaxb.Keywords;
import com.hybris.cockpitng.core.impl.jaxb.Socket;
import com.hybris.cockpitng.core.impl.jaxb.Sockets;
import com.hybris.cockpitng.core.persistence.impl.XMLWidgetPersistenceService;
import com.hybris.cockpitng.core.persistence.impl.jaxb.Widgets;
import com.hybris.cockpitng.core.persistence.packaging.SocketWrapper;
import com.hybris.cockpitng.core.persistence.packaging.WidgetJarLibInfo;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibUtils;
import com.hybris.cockpitng.core.spring.CockpitApplicationContext;
import com.hybris.cockpitng.core.spring.ModuleContentProvider;
import com.hybris.cockpitng.core.util.CockpitProperties;
import com.hybris.cockpitng.core.util.jaxb.JAXBContextFactory;
import com.hybris.cockpitng.util.zip.SafeZipEntry;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * Default implementation of {@link WidgetLibUtils}. For internal use only.
 */
public class DefaultWidgetLibUtils implements WidgetLibUtils, ApplicationContextAware
{
    public static final String COCKPITNG_WIDGETCLASSLOADER_RESOURCECACHE_ENABLED = "cockpitng.widgetclassloader.resourcecache.enabled";
    public static final String LIB_ALREADY_EXISTS_MSG = "A library assigned to this group ID is already registered.\n"
                    + "This library has to be deleted before a widget group with this group ID can be created.";
    private static final String WIDGET_GROUP_ROOT = "cockpitng/widgets/";
    private static final String DEFINITION_XML = "definition.xml";
    private static final String WIDGETS_XML = "widgets.xml";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWidgetLibUtils.class);
    private final Map<String, WidgetJarLibInfo> jarLibInfos = new ConcurrentHashMap<>();
    private CockpitApplicationContext applicationContext;
    private CockpitProperties cockpitProperties;
    private JAXBContextFactory jaxbContextFactory;
    private String rootDirectorySpring;
    private String rootDirectory;
    private String rootDirKey = "cockpitng.rootdir";


    private static String getComponentId(final Properties props)
    {
        final String type = getComponentType(props);
        return props.getProperty(type + "-id");
    }


    private static String getComponentType(final Properties props)
    {
        return props.getProperty("component-type", "widget");
    }


    private static boolean fillPropertiesFromXml(final InputStream stream, final Properties props) throws XPathExpressionException
    {
        final InputSource inputSource = new InputSource(stream);
        final String widgetDefPath = "/*";
        final XPath xpath = XPathFactory.newInstance().newXPath();
        final Node widgetDefNode = (Node)xpath.evaluate(widgetDefPath, inputSource, XPathConstants.NODE);
        if(widgetDefNode != null)
        {
            final String nodeName = widgetDefNode.getNodeName();
            if(nodeName.endsWith("-definition"))
            {
                final Node idAttr = widgetDefNode.getAttributes().getNamedItem("id");
                final String componentType = StringUtils.removeEnd(nodeName, "-definition");
                props.setProperty("component-type", componentType);
                if(idAttr != null)
                {
                    props.setProperty(componentType + "-id", idAttr.getNodeValue());
                }
                return true;
            }
        }
        return false;
    }


    /**
     * Replace variables in target dir string.
     *
     * @param dir
     * @return
     * @deprecated since 2105, not used any more
     */
    @Deprecated(since = "2105", forRemoval = true)
    public static String processDir(final String dir)
    {
        String ret = dir;
        if(dir.contains("${"))
        {
            if(dir.contains("${user.home}"))
            {
                ret = dir.replace("${user.home}", FileUtils.getUserDirectoryPath());
            }
            else if(dir.contains("${java.io.tmpdir}"))
            {
                ret = dir.replace("${java.io.tmpdir}", FileUtils.getTempDirectoryPath());
            }
            else
            {
                LOG.error("Could not resolve variable in path {}", dir);
            }
        }
        return ret;
    }


    @Override
    public Properties loadLibProps(final File libRoot)
    {
        final File libpropfile = new File(libRoot, FILE_LIBRARY_INFO);
        final Properties props = new Properties();
        try(final FileInputStream inputStream = new FileInputStream(libpropfile))
        {
            props.load(inputStream);
        }
        catch(final IOException e)
        {
            LOG.debug("Could not open file", e);
            return null;
        }
        return props;
    }


    @Override
    public void storeLibProps(final Properties props, final File libRoot)
    {
        final File libpropfile = new File(libRoot, FILE_LIBRARY_INFO);
        try(final OutputStream ostream = new FileOutputStream(libpropfile))
        {
            props.store(ostream, "This files was generated. Do not change anything since it will be overwritten.");
        }
        catch(final IOException e)
        {
            LOG.debug("Could not open file", e);
        }
    }


    @Override
    public List<WidgetJarLibInfo> loadAllWidgetJarLibInfos()
    {
        return loadAllWidgetJarLibInfos(getWidgetJarLibDir());
    }


    @Override
    public List<WidgetJarLibInfo> loadAllWidgetJarLibInfos(final File libRoot)
    {
        final List<WidgetJarLibInfo> ret = new ArrayList<>();
        final File[] listFiles = libRoot.listFiles((directory, fileName) -> fileName.endsWith(".jar"));
        if(listFiles == null)
        {
            return ret;
        }
        final Properties libProps = loadLibProps(libRoot);
        for(final File file : listFiles)
        {
            loadWidgetLibInfosFromFile(ret, libProps, file);
        }
        return orderedWidgetLibInfos(ret);
    }


    private List<WidgetJarLibInfo> orderedWidgetLibInfos(final List<WidgetJarLibInfo> widgetJarLibInfos)
    {
        final Map<WidgetJarLibInfo, String> widgetJarLibInfosToModuleNames = mapWidgetJarLibInfosToModuleNames(widgetJarLibInfos);
        return getOrderedWidgetJarLibInfos(widgetJarLibInfosToModuleNames);
    }


    private Map<WidgetJarLibInfo, String> mapWidgetJarLibInfosToModuleNames(final List<WidgetJarLibInfo> widgetJarLibInfos)
    {
        final Map<WidgetJarLibInfo, String> widgetJarLibInfosToModuleNames = new HashMap<>();
        for(final WidgetJarLibInfo widgetJarLibInfo : widgetJarLibInfos)
        {
            final Optional<String> moduleName = moduleNameFromWidgetJarLibInfo(widgetJarLibInfo);
            widgetJarLibInfosToModuleNames.put(widgetJarLibInfo, moduleName.orElse(null));
        }
        return widgetJarLibInfosToModuleNames;
    }


    private Optional<String> moduleNameFromWidgetJarLibInfo(final WidgetJarLibInfo widgetJarLibInfo)
    {
        Optional<URI> uri;
        try
        {
            uri = Optional.of(new URI(widgetJarLibInfo.getExternalLocation()));
        }
        catch(final URISyntaxException e)
        {
            uri = Optional.empty();
            LOG.error("{} has invalid external name. {}", widgetJarLibInfo, e);
        }
        return uri.isPresent() ? getApplicationContext().getModuleName(uri.get()) : Optional.empty();
    }


    private List<WidgetJarLibInfo> getOrderedWidgetJarLibInfos(final Map<WidgetJarLibInfo, String> widgetJarLibInfosToModuleNames)
    {
        final List<String> orderedLoadedModulesNames = getApplicationContext().getLoadedModulesNames();
        final List<WidgetJarLibInfo> ordered = new ArrayList<>();
        orderedLoadedModulesNames.forEach(orderedModuleName -> ordered
                        .addAll(getWidgetJarLibInfosForModuleName(widgetJarLibInfosToModuleNames, orderedModuleName)));
        return ordered;
    }


    private List<WidgetJarLibInfo> getWidgetJarLibInfosForModuleName(
                    final Map<WidgetJarLibInfo, String> widgetJarLibInfosToModuleNames, final String orderedModuleName)
    {
        return widgetJarLibInfosToModuleNames.entrySet().stream().filter(entry -> entry.getValue().equals(orderedModuleName))
                        .map(Entry::getKey).collect(Collectors.toList());
    }


    private void loadWidgetLibInfosFromFile(final List<WidgetJarLibInfo> ret, final Properties libProps, final File file)
    {
        try(final JarFile jarfile = new JarFile(file))
        {
            final ZipEntry mfEntry = jarfile.getEntry(DEFINITION_XML);
            if(mfEntry != null)
            {
                readInfoFromEntry(ret, libProps, file, jarfile, mfEntry, null);
            }
            // search in sub-folders
            final Enumeration<JarEntry> entries = jarfile.entries();
            while(entries.hasMoreElements())
            {
                final JarEntry nextElement = entries.nextElement();
                String prefix = null;
                if(nextElement.getName().endsWith("/" + DEFINITION_XML))
                {
                    prefix = nextElement.getName().substring(0, nextElement.getName().length() - DEFINITION_XML.length() - 1);
                }
                if(prefix == null)
                {
                    continue;
                }
                readInfoFromEntry(ret, libProps, file, jarfile, nextElement, prefix);
            }
        }
        catch(final IOException e)
        {
            LOG.error("error loading component definition from file '{}'", file.getName(), e);
        }
    }


    private void readInfoFromEntry(final List<WidgetJarLibInfo> ret, final Properties libProps, final File file,
                    final JarFile jarfile, final ZipEntry mfEntry, final String prefix) throws IOException
    {
        final WidgetJarLibInfo widgetJarLibInfo = getJarLibInfoFromXml(new SafeZipEntry(mfEntry), jarfile, file, prefix);
        if(widgetJarLibInfo != null)
        {
            if(libProps != null)
            {
                widgetJarLibInfo.setExternalLocation(libProps.getProperty(file.toString()));
            }
            ret.add(widgetJarLibInfo);
        }
    }


    private WidgetJarLibInfo getJarLibInfoFromXml(final ZipEntry entry, final JarFile jarfile, final File file,
                    final String prefix) throws IOException
    {
        final Properties props = new Properties();
        if(fillPropertiesFromXml(jarfile.getName() + ":" + entry.getName(), jarfile.getInputStream(entry), props))
        {
            return createLibInfo(props, file, prefix);
        }
        else
        {
            return null;
        }
    }


    private WidgetJarLibInfo createLibInfo(final Properties props, final File file, final String prefix)
    {
        final String idProperty = getComponentId(props);
        final WidgetJarLibInfo widgetJarLibInfo = new WidgetJarLibInfo(idProperty, props, file, prefix);
        if(idProperty != null)
        {
            jarLibInfos.put(idProperty, widgetJarLibInfo);
        }
        return widgetJarLibInfo;
    }


    @Override
    public boolean fillPropertiesFromXml(final String resourceName, final InputStream stream, final Properties props)
    {
        try
        {
            return fillPropertiesFromXml(stream, props);
        }
        catch(final XPathExpressionException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Exception occurred: ", e);
            }
        }
        catch(final Exception e)
        {
            LOG.error("Error loading component from " + resourceName + ".", e);
        }
        finally
        {
            IOUtils.closeQuietly(stream);
        }
        return false;
    }


    @Override
    public void uploadJarFromStream(final String name, final InputStream stream) throws IOException
    {
        final File widgetJarLibDir = getWidgetJarLibDir();
        if(!widgetJarLibDir.exists())
        {
            final boolean dirCreated = widgetJarLibDir.mkdirs();
            if(dirCreated)
            {
                LOG.info("Created directory: {}", widgetJarLibDir);
            }
        }
        final File archiveFile = new File(widgetJarLibDir + File.separator + name);
        if(!archiveFile.createNewFile())
        {
            throw new IOException("File already exists.");
        }
        try(final FileOutputStream fos = new FileOutputStream(archiveFile))
        {
            IOUtils.copy(stream, fos);
        }
    }


    @Override
    public void createComposedWidgetJarArchive(final WidgetDefinition widgetDefinition, final List<SocketWrapper> socketWrappers,
                    final XMLWidgetPersistenceService xmlWidgetPersistenceService) throws IOException
    {
        final String moduleName = widgetDefinition.getCode();
        if(getApplicationContext().getModuleInfo(moduleName).isPresent())
        {
            try
            {
                getApplicationContext().unregisterModule(moduleName);
            }
            catch(final CockpitApplicationException e)
            {
                throw new IOException(LIB_ALREADY_EXISTS_MSG, e);
            }
        }
        final Widget composedWidgetRoot = widgetDefinition.getComposedWidgetRoot();
        composedWidgetRoot.setId(widgetDefinition.getCode());
        final List<SocketWrapper> externalInputSockets = new ArrayList<>();
        final List<SocketWrapper> externalOutputSockets = new ArrayList<>();
        final List<SocketWrapper> internalInputSockets = new ArrayList<>();
        final List<SocketWrapper> internalOutputSockets = new ArrayList<>();
        final Map<String, String> forwardMap = new HashMap<>(widgetDefinition.getForwardMap());
        for(final SocketWrapper socketWrapper : socketWrappers)
        {
            if(socketWrapper.isInput())
            {
                externalInputSockets.add(socketWrapper);
                final SocketWrapper outputSocket = new SocketWrapper(socketWrapper.getWidget(), socketWrapper.getSocket(), false);
                outputSocket.setId(socketWrapper.getId() + "Forward");
                internalOutputSockets.add(outputSocket);
                forwardMap.put(socketWrapper.getId(), outputSocket.getId());
                final WidgetConnection widgetConnection = new WidgetConnection(composedWidgetRoot, socketWrapper.getWidget(),
                                socketWrapper.getSocket().getId(), outputSocket.getId());
                composedWidgetRoot.addOutConnection(widgetConnection);
                socketWrapper.getWidget().addInConnection(widgetConnection);
            }
            else
            // output socket
            {
                externalOutputSockets.add(socketWrapper);
                final SocketWrapper inputSocket = new SocketWrapper(socketWrapper.getWidget(), socketWrapper.getSocket(), false);
                inputSocket.setId(socketWrapper.getId() + "Forward");
                internalInputSockets.add(inputSocket);
                forwardMap.put(inputSocket.getId(), socketWrapper.getId());
                final WidgetConnection widgetConnection = new WidgetConnection(socketWrapper.getWidget(), composedWidgetRoot,
                                inputSocket.getId(), socketWrapper.getSocket().getId());
                composedWidgetRoot.addInConnection(widgetConnection);
                socketWrapper.getWidget().addOutConnection(widgetConnection);
            }
        }
        final com.hybris.cockpitng.core.impl.jaxb.WidgetDefinition jaxbDefinition = new com.hybris.cockpitng.core.impl.jaxb.WidgetDefinition();
        jaxbDefinition.setId(widgetDefinition.getCode());
        jaxbDefinition.setName(widgetDefinition.getCode());
        jaxbDefinition.setDescription(widgetDefinition.getDescription());
        jaxbDefinition.setVersion("1.0");
        final Keywords keywords = new Keywords();
        jaxbDefinition.setKeywords(keywords);
        keywords.getKeyword().add(widgetDefinition.getCategoryTag());
        final Sockets sockets = new Sockets();
        jaxbDefinition.setSockets(sockets);
        for(final SocketWrapper socketWrapper : externalInputSockets)
        {
            final WidgetSocket socket = socketWrapper.getSocket();
            final Socket newSocket = new Socket();
            sockets.getInput().add(newSocket);
            newSocket.setId(socketWrapper.getId());
            newSocket.setVisibility(getVisibility(socket.getVisibility()));
            newSocket.setType(socket.getDataType());
            newSocket.setMultiplicity(getMultiplicity(socket.getDataMultiplicity()));
        }
        for(final SocketWrapper socketWrapper : internalInputSockets)
        {
            final WidgetSocket socket = socketWrapper.getSocket();
            final Socket newSocket = new Socket();
            sockets.getInput().add(newSocket);
            newSocket.setId(socketWrapper.getId());
            newSocket.setVisibility(com.hybris.cockpitng.core.impl.jaxb.SocketVisibility.INVISIBLE);
            newSocket.setType(socket.getDataType());
            newSocket.setMultiplicity(getMultiplicity(socket.getDataMultiplicity()));
        }
        for(final SocketWrapper socketWrapper : externalOutputSockets)
        {
            final WidgetSocket socket = socketWrapper.getSocket();
            final Socket newSocket = new Socket();
            sockets.getOutput().add(newSocket);
            newSocket.setId(socketWrapper.getId());
            newSocket.setVisibility(getVisibility(socket.getVisibility()));
            newSocket.setType(socket.getDataType());
            newSocket.setMultiplicity(getMultiplicity(socket.getDataMultiplicity()));
        }
        for(final SocketWrapper socketWrapper : internalOutputSockets)
        {
            final WidgetSocket socket = socketWrapper.getSocket();
            final Socket newSocket = new Socket();
            sockets.getOutput().add(newSocket);
            newSocket.setId(socketWrapper.getId());
            newSocket.setVisibility(com.hybris.cockpitng.core.impl.jaxb.SocketVisibility.INVISIBLE);
            newSocket.setType(socket.getDataType());
            newSocket.setMultiplicity(getMultiplicity(socket.getDataMultiplicity()));
        }
        for(final Entry<String, String> forwardEntry : forwardMap.entrySet())
        {
            final Forward newForward = new Forward();
            sockets.getForward().add(newForward);
            newForward.setInput(forwardEntry.getKey());
            newForward.setOutput(forwardEntry.getValue());
        }
        final String definitionXmlContent = writeDefinitionXml(jaxbDefinition);
        if(definitionXmlContent == null)
        {
            return;
        }
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        xmlWidgetPersistenceService.storeWidgetTree(composedWidgetRoot, new Widgets(), baos);
        try
        {
            getApplicationContext().registerNewModule(widgetDefinition.getCode(),
                            new DefaultModuleContentProvider(moduleName, definitionXmlContent, baos));
        }
        catch(final Exception ex)
        {
            LOG.error("Error registering new module: " + widgetDefinition.getCode(), ex);
        }
    }


    private com.hybris.cockpitng.core.impl.jaxb.SocketVisibility getVisibility(final SocketVisibility visibility)
    {
        if(visibility == null)
        {
            return com.hybris.cockpitng.core.impl.jaxb.SocketVisibility.DEFAULT;
        }
        else
        {
            switch(visibility)
            {
                case EXTERNAL:
                    return com.hybris.cockpitng.core.impl.jaxb.SocketVisibility.EXTERNAL;
                case INTERNAL:
                    return com.hybris.cockpitng.core.impl.jaxb.SocketVisibility.INTERNAL;
                case HIDDEN:
                    return com.hybris.cockpitng.core.impl.jaxb.SocketVisibility.INVISIBLE;
                default:
                    return com.hybris.cockpitng.core.impl.jaxb.SocketVisibility.DEFAULT;
            }
        }
    }


    private com.hybris.cockpitng.core.impl.jaxb.SocketMultiplicity getMultiplicity(final Multiplicity multiplicity)
    {
        if(multiplicity == null)
        {
            return null;
        }
        else
        {
            switch(multiplicity)
            {
                case COLLECTION:
                    return com.hybris.cockpitng.core.impl.jaxb.SocketMultiplicity.COLLECTION;
                case LIST:
                    return com.hybris.cockpitng.core.impl.jaxb.SocketMultiplicity.LIST;
                case SET:
                    return com.hybris.cockpitng.core.impl.jaxb.SocketMultiplicity.SET;
                default:
                    return null;
            }
        }
    }


    private String writeDefinitionXml(final com.hybris.cockpitng.core.impl.jaxb.WidgetDefinition jaxbDefinition)
    {
        try(final StringWriter writer = new StringWriter())
        {
            final Marshaller marshaller = createMarshaller(com.hybris.cockpitng.core.impl.jaxb.WidgetDefinition.class);
            marshaller.marshal(jaxbDefinition, writer);
            return writer.toString();
        }
        catch(final JAXBException | IOException e)
        {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }


    private Marshaller createMarshaller(final Class<?>... classes) throws JAXBException
    {
        final Marshaller marshaller = getJAXBContext(classes).createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        return marshaller;
    }


    private JAXBContext getJAXBContext(final Class<?>... classes) throws JAXBException
    {
        if(this.jaxbContextFactory == null)
        {
            return JAXBContext.newInstance(classes);
        }
        else
        {
            return this.jaxbContextFactory.createContext(classes);
        }
    }


    @Override
    public WidgetJarLibInfo getWidgetJarLibInfo(final String widgetDefinitionCode)
    {
        if(widgetDefinitionCode == null)
        {
            return null;
        }
        WidgetJarLibInfo widgetJarLibInfo = jarLibInfos.get(widgetDefinitionCode);
        if(widgetJarLibInfo == null)
        {
            loadAllWidgetJarLibInfos();
            widgetJarLibInfo = jarLibInfos.get(widgetDefinitionCode);
        }
        return widgetJarLibInfo;
    }


    @Override
    public Collection<WidgetJarLibInfo> getAllJarLibInfos()
    {
        return Collections.unmodifiableCollection(jarLibInfos.values());
    }


    @Override
    public File getWidgetJarLibDir()
    {
        return new File(getRootDir(), WIDGET_JAR_LIB_DIR);
    }


    @Override
    public File getRootDir()
    {
        return WidgetLibHelper.getRootDir(getRootDirectory(), getDirProcessors());
    }


    public String getRootDirectory()
    {
        if(rootDirectory == null)
        {
            rootDirectory = cockpitProperties.getProperty(getRootDirKey());
            if(StringUtils.isBlank(rootDirectory))
            {
                rootDirectory = rootDirectorySpring;
            }
        }
        return rootDirectory;
    }


    public void setRootDirectory(final String rootDirectory)
    {
        this.rootDirectorySpring = rootDirectory;
    }


    protected String getRootDirKey()
    {
        return this.rootDirKey;
    }


    public void setRootDirKey(final String key)
    {
        this.rootDirKey = key;
    }


    @Required
    public void setCockpitProperties(final CockpitProperties cockpitProperties)
    {
        this.cockpitProperties = cockpitProperties;
    }


    public void setJaxbContextFactory(final JAXBContextFactory jaxbContextFactory)
    {
        this.jaxbContextFactory = jaxbContextFactory;
    }


    @Override
    public boolean isResourceCacheEnabled()
    {
        if(cockpitProperties != null)
        {
            final String property = cockpitProperties.getProperty(COCKPITNG_WIDGETCLASSLOADER_RESOURCECACHE_ENABLED);
            if(property != null)
            {
                return Boolean.parseBoolean(property);
            }
        }
        return true;
    }


    protected CockpitApplicationContext getApplicationContext()
    {
        return applicationContext;
    }


    @Override
    public void setApplicationContext(final ApplicationContext applicationContext)
    {
        this.applicationContext = CockpitApplicationContext.getCockpitApplicationContext(applicationContext);
    }


    protected static class DefaultModuleContentProvider implements ModuleContentProvider
    {
        private final String moduleName;
        private final String definitionXmlContent;
        private final ByteArrayOutputStream baos;


        public DefaultModuleContentProvider(final String moduleName, final String definitionXmlContent,
                        final ByteArrayOutputStream baos)
        {
            this.moduleName = moduleName;
            this.definitionXmlContent = definitionXmlContent;
            this.baos = baos;
        }


        @Override
        public OutputStream prepareStream(final OutputStream destination) throws IOException
        {
            return new JarOutputStream(destination, new Manifest());
        }


        @Override
        public void writeContent(final OutputStream destination) throws IOException
        {
            final JarOutputStream jar = (JarOutputStream)destination;
            final String[] moduleNameParts = moduleName.split("\\.");
            final String widgetName = moduleNameParts[moduleNameParts.length - 1];
            JarEntry jarAdd = new JarEntry(WIDGET_GROUP_ROOT + widgetName + IOUtils.DIR_SEPARATOR_UNIX + DEFINITION_XML);
            jarAdd.setTime(new Date().getTime());
            jar.putNextEntry(jarAdd);
            byte[] bytes = definitionXmlContent.getBytes(Charset.defaultCharset());
            destination.write(bytes, 0, bytes.length);
            // Add widgets.xml
            jarAdd = new JarEntry(WIDGET_GROUP_ROOT + widgetName + IOUtils.DIR_SEPARATOR_UNIX + WIDGETS_XML);
            jarAdd.setTime(new Date().getTime());
            jar.putNextEntry(jarAdd);
            bytes = baos.toByteArray();
            destination.write(bytes, 0, bytes.length);
        }


        @Override
        public void finalizeStream(final OutputStream destination)
        {
            // No operation required here
        }
    }
}

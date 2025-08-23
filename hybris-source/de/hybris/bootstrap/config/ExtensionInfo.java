package de.hybris.bootstrap.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ExtensionInfo
{
    private final PlatformConfig platformConfig;
    private final Collection<AbstractExtensionModule> extensionModules;
    private final Set<String> requiredExtensionNames;
    private final Set<String> implicitlyRequExtNames;
    private final boolean coreExtension;
    private final Map<String, String> metaMap;
    private String name;
    private String version;
    private String abstractClassPrefix;
    private String classPrefix;
    private String managerName;
    private String managerSuperclass;
    private boolean oldStyleExtension;
    private boolean requiredByAll;
    private boolean jaloLogicFree;
    private boolean lazyLoaded;
    private boolean excluded;
    private File extensionDirectory;
    private File configFile;
    private Boolean modifiedForGeneration;
    private static final String TAG_EXTENSIONINFO = "extensioninfo";
    private static final String TAG_EXTENSION = "extension";
    private static final String TAG_EXTENSION_ATTRIBUTE_XMLNS_XSI = "xmlns:xsi";
    private static final String TAG_EXTENSION_ATTRIBUTE_XSI_NONAMESPACESCHEMALOCATION = "xsi:noNamespaceSchemaLocation";
    private static final String TAG_EXTENSION_ATTRIBUTE_XMLNS_XSI_VALUE = "=\"http://www.w3.org/2001/XMLSchema-instance\"";
    private static final String TAG_EXTENSION_ATTRIBUTE_XSI_NONAMESPACESCHEMALOCATION_VALUE = "=\"extensioninfo.xsd\"";


    public ExtensionInfo(String contextRoot, HashMap<String, String> metaMap) throws RuntimeException
    {
        this.name = contextRoot;
        this.metaMap = metaMap;
        this.platformConfig = null;
        this.extensionModules = Collections.EMPTY_LIST;
        this.requiredExtensionNames = Collections.EMPTY_SET;
        this.implicitlyRequExtNames = Collections.EMPTY_SET;
        this.coreExtension = false;
    }


    public ExtensionInfo(PlatformConfig extensionsXML, File configFile, boolean isCoreExtension) throws RuntimeException
    {
        this.coreExtension = isCoreExtension;
        this.platformConfig = extensionsXML;
        this.extensionModules = new ArrayList<>();
        this.requiredExtensionNames = new HashSet<>();
        this.oldStyleExtension = false;
        this.implicitlyRequExtNames = new HashSet<>();
        this.metaMap = new HashMap<>(5, 0.75F);
        fillFromXMLFile(configFile);
    }


    public boolean equals(Object obj)
    {
        if(obj instanceof ExtensionInfo)
        {
            return ((ExtensionInfo)obj).getName().equalsIgnoreCase(getName());
        }
        return false;
    }


    public int hashCode()
    {
        return getName().hashCode();
    }


    private File getCheckFile()
    {
        File checkDir = new File(this.platformConfig.getSystemConfig().getTempDir(), "touch");
        if(!checkDir.exists())
        {
            checkDir.mkdir();
        }
        return new File(checkDir, this.name + "_itemstouch");
    }


    public void markUnmodifiedForCodeGeneration()
    {
        File checkFile = getCheckFile();
        try
        {
            FileUtils.touch(checkFile);
        }
        catch(IOException e)
        {
            System.err.println("Error while touching " + checkFile.getAbsolutePath() + ": " + e.getMessage());
        }
    }


    public boolean isDeprecated()
    {
        return "true".equalsIgnoreCase(getMeta("deprecated"));
    }


    public boolean isModifiedForCodeGeneration()
    {
        if(this.modifiedForGeneration == null)
        {
            File checkFile = getCheckFile();
            this.modifiedForGeneration = Boolean.valueOf((
                            isModified(getItemsXML(), checkFile) ||
                                            isModified(getBeansXML(), checkFile)));
        }
        return this.modifiedForGeneration.booleanValue();
    }


    private boolean isModified(File metaXML, File checkFile)
    {
        if(metaXML.exists())
        {
            return (!checkFile.exists() || checkFile.lastModified() < metaXML.lastModified());
        }
        return !checkFile.exists();
    }


    public File getItemsXML()
    {
        File itemsxml = new File(getResourcesDirectory(), getName() + "-items.xml");
        if(!itemsxml.exists())
        {
            itemsxml = new File(getResourcesDirectory(), "items.xml");
        }
        return itemsxml;
    }


    public File getBeansXML()
    {
        return new File(getResourcesDirectory(), getName() + "-beans.xml");
    }


    public Set<File> getGenericAuditXMLs()
    {
        File resourcesDirectory = getResourcesDirectory();
        if(!resourcesDirectory.exists())
        {
            return Collections.emptySet();
        }
        try
        {
            DirectoryStream<Path> dirStream = Files.newDirectoryStream(resourcesDirectory.toPath(),
                            getName() + "-*-audit.xml");
            try
            {
                Set<File> result = new HashSet<>();
                dirStream.forEach(p -> result.add(p.toFile()));
                Set<File> set1 = result;
                if(dirStream != null)
                {
                    dirStream.close();
                }
                return set1;
            }
            catch(Throwable throwable)
            {
                if(dirStream != null)
                {
                    try
                    {
                        dirStream.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    public File getResourcesDirectory()
    {
        return new File(getExtensionDirectory(), "resources");
    }


    public boolean isItemsXMLModifiedAfter(long lastModifiedTime)
    {
        File itemsxml = new File(new File(getExtensionDirectory(), "resources"), getName() + "-items.xml");
        if(!itemsxml.exists())
        {
            itemsxml = new File(new File(getExtensionDirectory(), "resources"), "items.xml");
        }
        return (itemsxml.exists() && itemsxml.lastModified() > lastModifiedTime);
    }


    public String getName()
    {
        return this.name;
    }


    private void setName(String name)
    {
        this.name = name;
    }


    public String getVersion()
    {
        return this.version;
    }


    public void setVersion(String version)
    {
        this.version = version;
    }


    public boolean isRequiredByAll()
    {
        return this.requiredByAll;
    }


    public boolean isJaloLogicFree()
    {
        return this.jaloLogicFree;
    }


    public boolean isCoreExtension()
    {
        return this.coreExtension;
    }


    public boolean isWebExtension()
    {
        return (getWebModule() != null);
    }


    public void setRequiredByAll(boolean required)
    {
        this.requiredByAll = required;
    }


    public void setJaloLogicFree(boolean jaloLogicFree)
    {
        this.jaloLogicFree = jaloLogicFree;
    }


    public String getAbstractClassPrefix()
    {
        return this.abstractClassPrefix;
    }


    private void setAbstractClassPrefix(String abstractClassPrefix)
    {
        this.abstractClassPrefix = abstractClassPrefix;
    }


    public boolean isOldStyleExtension()
    {
        return this.oldStyleExtension;
    }


    private void setOldStyleExtension(boolean oldstyle)
    {
        this.oldStyleExtension = oldstyle;
    }


    public String getClassPrefix()
    {
        if(this.classPrefix == null)
        {
            return getName().substring(0, 1).toUpperCase() + getName().substring(0, 1).toUpperCase();
        }
        return this.classPrefix;
    }


    private void setClassPrefix(String classPrefix)
    {
        this.classPrefix = classPrefix;
    }


    public String getManagerName()
    {
        return (this.managerName == null) ? (getClassPrefix() + "Manager") : this.managerName;
    }


    private void setManagerName(String managerName)
    {
        this.managerName = managerName;
    }


    public String getManagerSuperclass()
    {
        return (this.managerSuperclass == null) ? "de.hybris.platform.jalo.extension.Extension" : this.managerSuperclass;
    }


    private void setManagerSuperclass(String superclass)
    {
        this.managerSuperclass = superclass;
    }


    public File getExtensionDirectory()
    {
        return this.extensionDirectory;
    }


    private void setExtensionDirectory(File extensionDirectory)
    {
        this.extensionDirectory = extensionDirectory;
    }


    public File getConfigFile()
    {
        return this.configFile;
    }


    private void setConfigFile(File configFile)
    {
        this.configFile = configFile;
    }


    public Collection<AbstractExtensionModule> getModules()
    {
        return this.extensionModules;
    }


    private void addModule(AbstractExtensionModule extensionmodules)
    {
        this.extensionModules.add(extensionmodules);
        if(extensionmodules instanceof CoreExtensionModule)
        {
            if(!getName().equalsIgnoreCase("core"))
            {
                addRequiredExtensionName("core");
            }
        }
    }


    Set<String> getConfiguredDependingExtensionNames()
    {
        Set<String> ret = new LinkedHashSet<>();
        for(ExtensionInfo other : this.platformConfig.getExtensionInfosInBuildOrder())
        {
            if(!equals(other) && other.getConfiguredRequiredExtensionNames().contains(getName()))
            {
                ret.add(other.getName());
            }
        }
        return ret;
    }


    public Set<String> getAllRequiredExtensionNames()
    {
        Set<String> ret = new HashSet<>();
        ret.addAll(getConfiguredRequiredExtensionNames());
        ret.addAll(getImplicitRequiredExtensionNames());
        return ret;
    }


    public Set<String> getConfiguredRequiredExtensionNames()
    {
        return this.requiredExtensionNames;
    }


    public Set<String> getImplicitRequiredExtensionNames()
    {
        return this.implicitlyRequExtNames;
    }


    public void addRequiredExtensionName(String extensionname)
    {
        if(extensionname.equals(this.name))
        {
            throw new BootstrapConfigException("an extension cannot require itself!");
        }
        this.requiredExtensionNames.add(extensionname);
    }


    public void addImplicitlyRequiredExtensionNames(Set<String> extensionnames)
    {
        if(extensionnames.contains(this.name))
        {
            throw new BootstrapConfigException("an extension cannot require itself!");
        }
        if(!isExternalExtension())
        {
            this.implicitlyRequExtNames.addAll(extensionnames);
        }
    }


    public void removeRequiredExtensionName(String name)
    {
        this.requiredExtensionNames.remove(name);
        this.implicitlyRequExtNames.remove(name);
    }


    public CoreExtensionModule getCoreModule()
    {
        CoreExtensionModule module = null;
        for(AbstractExtensionModule info : getModules())
        {
            if(info instanceof CoreExtensionModule)
            {
                module = (CoreExtensionModule)info;
            }
        }
        return module;
    }


    public HMCExtensionModule getHMCModule()
    {
        HMCExtensionModule module = null;
        for(AbstractExtensionModule info : getModules())
        {
            if(info instanceof HMCExtensionModule)
            {
                module = (HMCExtensionModule)info;
            }
        }
        return module;
    }


    public WebExtensionModule getWebModule()
    {
        WebExtensionModule module = null;
        for(AbstractExtensionModule info : getModules())
        {
            if(info instanceof WebExtensionModule)
            {
                module = (WebExtensionModule)info;
            }
        }
        return module;
    }


    public Set<ExtensionInfo> getAllRequiredExtensionInfos()
    {
        HashSet<ExtensionInfo> result = new HashSet<>();
        HashSet<String> allNames = new HashSet<>();
        allNames.add(getName());
        Set<String> requiredNamesToProcess = getAllRequiredExtensionNames();
        while(!requiredNamesToProcess.isEmpty())
        {
            Set<String> nextRequiredNamesToProcess = new HashSet<>();
            for(String n : requiredNamesToProcess)
            {
                if(allNames.add(n))
                {
                    ExtensionInfo inf = this.platformConfig.getExtensionInfo(n);
                    result.add(inf);
                    nextRequiredNamesToProcess.addAll(inf.getAllRequiredExtensionNames());
                }
            }
            nextRequiredNamesToProcess.removeAll(allNames);
            requiredNamesToProcess = nextRequiredNamesToProcess;
        }
        return result;
    }


    public Set<ExtensionInfo> getAllConfiguredRequiredExtensionInfos()
    {
        HashSet<ExtensionInfo> result = new HashSet<>();
        HashSet<String> allNames = new HashSet<>();
        allNames.add(getName());
        Set<String> requiredNamesToProcess = getConfiguredRequiredExtensionNames();
        while(!requiredNamesToProcess.isEmpty())
        {
            Set<String> nextRequiredNamesToProcess = new HashSet<>();
            for(String n : requiredNamesToProcess)
            {
                if(allNames.add(n))
                {
                    ExtensionInfo inf = this.platformConfig.getExtensionInfo(n);
                    result.add(inf);
                    nextRequiredNamesToProcess.addAll(inf.getConfiguredRequiredExtensionNames());
                }
            }
            nextRequiredNamesToProcess.removeAll(allNames);
            requiredNamesToProcess = nextRequiredNamesToProcess;
        }
        return result;
    }


    public Set<ExtensionInfo> getRequiredExtensionInfos()
    {
        Set<ExtensionInfo> infos = new HashSet<>();
        for(String ext : getAllRequiredExtensionNames())
        {
            ExtensionInfo info = this.platformConfig.getExtensionInfo(ext);
            if(info == null)
            {
                throw new BootstrapConfigException("Extension " + getName() + ": 'Required' extension " + ext + " not found. Check if you have included all required extensions in your (local)extensions.xml or if this extension is really required.");
            }
            infos.add(info);
        }
        return infos;
    }


    public String getAllRequiredExtensionsString()
    {
        return toString(getAllRequiredExtensionInfos());
    }


    public String getAllConfiguredRequiredExtensionsString()
    {
        return toString(getAllConfiguredRequiredExtensionInfos());
    }


    protected String toString(Collection<ExtensionInfo> extensions)
    {
        StringBuilder sbuf = new StringBuilder();
        for(ExtensionInfo inf : extensions)
        {
            if(sbuf.length() > 0)
            {
                sbuf.append(",");
            }
            sbuf.append(inf.getName());
        }
        return sbuf.toString();
    }


    public String toString()
    {
        return this.name;
    }


    private synchronized void fillFromXMLFile(File configFile) throws BootstrapConfigException
    {
        setConfigFile(configFile);
        try
        {
            FileInputStream inputStream = new FileInputStream(configFile);
            try
            {
                setExtensionDirectory(configFile.getParentFile().getCanonicalFile());
                XPath xpath = XPathFactory.newInstance().newXPath();
                NodeList nodes = (NodeList)xpath.evaluate("/extensioninfo/extension", new InputSource(inputStream), XPathConstants.NODESET);
                Node extension_node = nodes.item(0);
                if(extension_node == null)
                {
                    throw new BootstrapConfigException("Did not found node <extension> in file ' " + configFile
                                    .getAbsolutePath() + " '! Aborting.");
                }
                if(nodes.getLength() > 1)
                {
                    throw new BootstrapConfigException("Found more than one <extension> node  in file ' " + configFile
                                    .getAbsolutePath() + " '! Aborting.");
                }
                Node extensioninfo_node = extension_node.getParentNode();
                if(extensioninfo_node.getAttributes().getNamedItem("xmlns:xsi") == null)
                {
                    System.out.println("WARNING! missing attribute ' xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ' in file '" + configFile
                                    .getAbsolutePath() + "'");
                }
                if(extensioninfo_node.getAttributes().getNamedItem("xsi:noNamespaceSchemaLocation") == null)
                {
                    System.out.println("WARNING! missing attribute ' xsi:noNamespaceSchemaLocation=\"extensioninfo.xsd\" ' in file '" + configFile
                                    .getAbsolutePath() + "'");
                }
                for(int i = 0; i < nodes.getLength(); i++)
                {
                    parseExtensionNode(nodes.item(i));
                }
                inputStream.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    inputStream.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        catch(FileNotFoundException e)
        {
            if((new File(this.platformConfig.getSystemConfig().getConfigDir(), "localextensions.xml")).exists())
            {
                throw new BootstrapConfigException("ExtensionInfo file '" + configFile.getAbsolutePath() + "' could not be found! Check the extensions paths specified in your '" + this.platformConfig
                                .getSystemConfig()
                                .getConfigDir()
                                .getAbsolutePath() + "/localextensions.xml' !", e);
            }
            throw new BootstrapConfigException("ExtensionInfo file '" + configFile.getAbsolutePath() + "' could not be found! Check the extensions paths specified in your '" + this.platformConfig
                            .getPlatformHome()
                            .getAbsolutePath() + "/extensions.xml' !", e);
        }
        catch(XPathExpressionException e)
        {
            throw new BootstrapConfigException("XPath Exception in file " + configFile.getAbsolutePath() + " : ", e);
        }
        catch(IOException e)
        {
            throw new BootstrapConfigException("file not found! ", e);
        }
    }


    private void parseExtensionNode(Node extension_node) throws IOException
    {
        NamedNodeMap nnm = extension_node.getAttributes();
        if(nnm == null)
        {
            throw new BootstrapConfigException("no attributes found in node <" + extension_node
                            .getNodeName() + "> in file " + this.configFile.getAbsolutePath());
        }
        String name = (nnm.getNamedItem("name") == null) ? null : nnm.getNamedItem("name").getNodeValue();
        String version = (nnm.getNamedItem("version") == null) ? null : nnm.getNamedItem("version").getNodeValue();
        if(name == null)
        {
            throw new BootstrapConfigException("no attribute 'name' found in node <" + extension_node.getNodeName() + "> in file " + this.configFile
                            .getAbsolutePath());
        }
        if(!Pattern.matches("^[A-Za-z]+[A-Za-z0-9]*$", name))
        {
            System.out.println("****************************************************");
            System.out.println("WARNING! There are not allowed characters in the extension name '" + name + "'. This violates the naming conventions and will be forbidden in the next major release.");
            System.out.println("****************************************************");
        }
        String abstractClassPrefix = (nnm.getNamedItem("abstractclassprefix") == null) ? null : nnm.getNamedItem("abstractclassprefix").getNodeValue();
        String classPrefix = (nnm.getNamedItem("classprefix") == null) ? null : nnm.getNamedItem("classprefix").getNodeValue();
        String managerName = (nnm.getNamedItem("managername") == null) ? null : nnm.getNamedItem("managername").getNodeValue();
        boolean isOldStyleExtension = (nnm.getNamedItem("isoldstyleextension") == null) ? false : Boolean.parseBoolean(nnm.getNamedItem("isoldstyleextension").getNodeValue());
        Node reqNode = nnm.getNamedItem("requiredbyall");
        boolean requiredByAll = (reqNode != null && Boolean.parseBoolean(reqNode.getNodeValue()));
        Node jaloLogicFreeNode = nnm.getNamedItem("jaloLogicFree");
        boolean jaloLogicFreeVal = (jaloLogicFreeNode != null && Boolean.parseBoolean(jaloLogicFreeNode.getNodeValue()));
        String managerSuperclass = (nnm.getNamedItem("managersuperclass") == null) ? null : nnm.getNamedItem("managersuperclass").getNodeValue();
        setName(name);
        setVersion(version);
        setAbstractClassPrefix(abstractClassPrefix);
        setClassPrefix(classPrefix);
        setManagerName(managerName);
        setManagerSuperclass(managerSuperclass);
        setOldStyleExtension(isOldStyleExtension);
        setRequiredByAll(requiredByAll);
        setJaloLogicFree(jaloLogicFreeVal);
        NodeList nodelist = extension_node.getChildNodes();
        for(int i = 0; i < nodelist.getLength(); i++)
        {
            Node node = nodelist.item(i);
            switch(null.$SwitchMap$de$hybris$bootstrap$config$ExtensionInfo$extensionchildnode[extensionchildnode.get(node.getNodeName()).ordinal()])
            {
                case 1:
                    parseRequiredExtensionsNode(node);
                    break;
                case 2:
                    parseCoreModuleNode(node);
                    break;
                case 3:
                    parseHMCModuleNode(node);
                    break;
                case 4:
                    parseWebModuleNode(node);
                    break;
                case 5:
                    parseMetaNode(node);
                    break;
            }
        }
    }


    private void parseRequiredExtensionsNode(Node requiresextension_node)
    {
        NamedNodeMap nnm = requiresextension_node.getAttributes();
        if(nnm == null)
        {
            throw new BootstrapConfigException("no attributes found in node " + requiresextension_node.getNodeName());
        }
        String name = (nnm.getNamedItem("name") == null) ? null : nnm.getNamedItem("name").getNodeValue();
        if(name == null)
        {
            throw new BootstrapConfigException("no attribute 'name' found in node <" + requiresextension_node.getNodeName() + "> in file " + this.configFile
                            .getAbsolutePath());
        }
        addRequiredExtensionName(name);
    }


    private void parseCoreModuleNode(Node coremodule_node) throws IOException
    {
        File baseDir = this.configFile.getParentFile().getCanonicalFile();
        String additionalclasspath = "";
        boolean sourceavailable = true;
        String packageroot = null;
        boolean generate = false;
        String manager = null;
        boolean autoPartOf = true;
        NamedNodeMap nnm = coremodule_node.getAttributes();
        if(nnm != null)
        {
            additionalclasspath = (nnm.getNamedItem("additionalclasspath") == null) ? "" : nnm.getNamedItem("additionalclasspath").getNodeValue();
            sourceavailable = (new File(baseDir, "src")).exists();
            packageroot = (nnm.getNamedItem("packageroot") == null) ? null : nnm.getNamedItem("packageroot").getNodeValue();
            if(packageroot == null)
            {
                throw new BootstrapConfigException("no attribute 'packageroot' found in node <" + coremodule_node.getNodeName() + "> in file " + this.configFile
                                .getAbsolutePath());
            }
            manager = (nnm.getNamedItem("manager") == null) ? null : nnm.getNamedItem("manager").getNodeValue();
            generate = (nnm.getNamedItem("generated") == null) ? false : Boolean.parseBoolean(nnm.getNamedItem("generated").getNodeValue());
            if(nnm.getNamedItem("java5") != null)
            {
                System.out.println(" ");
                System.out.println("***************");
                System.out.println("WARNING: The java5 flag at extensioninfo.xml is deprecated and has no effect anymore.");
                System.out.println("         Please remove it for extension " +
                                getName() + " from " + this.configFile.getAbsolutePath());
                System.out.println("***************");
                System.out.println(" ");
            }
            autoPartOf = (nnm.getNamedItem("generatePartOf") == null) ? true : Boolean.parseBoolean(nnm.getNamedItem("generatePartOf").getNodeValue());
        }
        CoreExtensionModule coreinfo = new CoreExtensionModule();
        coreinfo.setAdditionalClassPath(buildAbsoluteClassPath(additionalclasspath, baseDir));
        coreinfo.setSourceAvailable(sourceavailable);
        coreinfo.setPackageRoot(packageroot);
        coreinfo.setGenerate(generate);
        coreinfo.setManager(manager);
        coreinfo.setGeneratePartOf(autoPartOf);
        addModule((AbstractExtensionModule)coreinfo);
    }


    private void parseMetaNode(Node meta_node) throws IOException
    {
        String key = null;
        String value = null;
        NamedNodeMap nnm = meta_node.getAttributes();
        if(nnm != null)
        {
            key = (nnm.getNamedItem("key") == null) ? null : nnm.getNamedItem("key").getNodeValue();
            if(key == null)
            {
                throw new BootstrapConfigException("no attribute 'key' found in node <" + meta_node
                                .getNodeName() + "> in file " + this.configFile.getAbsolutePath());
            }
            value = (nnm.getNamedItem("value") == null) ? null : nnm.getNamedItem("value").getNodeValue();
            if(value == null)
            {
                throw new BootstrapConfigException("no attribute 'value' found in node <" + meta_node
                                .getNodeName() + "> in file " + this.configFile.getAbsolutePath());
            }
        }
        this.metaMap.put(key, value);
    }


    public String getMeta(String key)
    {
        return this.metaMap.get(key);
    }


    public boolean isExtgenTemplate()
    {
        return Boolean.valueOf(this.metaMap.get("extgen-template-extension")).booleanValue();
    }


    public boolean isModulegenTemplate()
    {
        return (getModulegenName() != null);
    }


    public String getModulegenName()
    {
        return this.metaMap.get("modulegen-name");
    }


    private void parseWebModuleNode(Node webmodule_node) throws IOException
    {
        File webDir = new File((new File(this.configFile.getParent())).getCanonicalPath(), "web");
        boolean sourceavailable = true;
        String webroot = null;
        String additionalclasspath = "";
        boolean jspcompile = false;
        NamedNodeMap nnm = webmodule_node.getAttributes();
        if(nnm != null)
        {
            webroot = (nnm.getNamedItem("webroot") == null) ? null : nnm.getNamedItem("webroot").getNodeValue();
            if(webroot == null)
            {
                throw new BootstrapConfigException("no attribute 'webroot' found in node <" + webmodule_node.getNodeName() + "> in file " + this.configFile
                                .getAbsolutePath());
            }
            sourceavailable = (new File(webDir, "src")).exists();
            additionalclasspath = (nnm.getNamedItem("additionalclasspath") == null) ? "" : nnm.getNamedItem("additionalclasspath").getNodeValue();
            jspcompile = (nnm.getNamedItem("jspcompile") == null) ? false : Boolean.parseBoolean(nnm.getNamedItem("jspcompile").getNodeValue());
        }
        WebExtensionModule webinfo = new WebExtensionModule();
        webinfo.setSourceAvailable(sourceavailable);
        webinfo.setWebRoot(webroot);
        webinfo.setAdditionalClassPath(buildAbsoluteClassPath(additionalclasspath, webDir));
        webinfo.setJspCompile(jspcompile);
        addModule((AbstractExtensionModule)webinfo);
    }


    private void parseHMCModuleNode(Node hmcmodule_node) throws IOException
    {
        File hmcDir = this.configFile.getParentFile();
        boolean sourceavailable = true;
        String additionalclasspath = "";
        String extensionclassname = "";
        NamedNodeMap nnm = hmcmodule_node.getAttributes();
        if(nnm != null)
        {
            sourceavailable = (new File(new File(hmcDir, "hmc"), "src")).exists();
            additionalclasspath = (nnm.getNamedItem("additionalclasspath") == null) ? "" : nnm.getNamedItem("additionalclasspath").getNodeValue();
            extensionclassname = (nnm.getNamedItem("extensionclassname") == null) ? "" : nnm.getNamedItem("extensionclassname").getNodeValue();
        }
        HMCExtensionModule hmcinfo = new HMCExtensionModule();
        hmcinfo.setAdditionalClassPath(buildAbsoluteClassPath(additionalclasspath, hmcDir));
        hmcinfo.setExtensionClassName(extensionclassname);
        hmcinfo.setSourceAvailable(sourceavailable);
        addModule((AbstractExtensionModule)hmcinfo);
    }


    private static String buildAbsoluteClassPath(String classPath, File baseDir)
    {
        StringTokenizer tockenizer = new StringTokenizer(classPath, ":");
        String newclasspath = "";
        String tok = "";
        while(tockenizer.hasMoreTokens())
        {
            if(tok.length() == 1)
            {
                tok = tok + ":" + tok;
            }
            else
            {
                tok = tockenizer.nextToken();
            }
            if(tok.length() > 1 && tok.charAt(0) != '$')
            {
                File clElementF;
                String clElement = tok;
                if((new File(clElement)).isAbsolute())
                {
                    clElementF = new File(clElement);
                }
                else
                {
                    clElementF = new File(baseDir, clElement);
                }
                try
                {
                    clElementF = clElementF.getCanonicalFile();
                }
                catch(IOException e)
                {
                    throw new BootstrapConfigException("error in getCanonicalFile", e);
                }
                newclasspath = newclasspath + newclasspath + ":";
                continue;
            }
            if(tok.charAt(0) == '$')
            {
                newclasspath = newclasspath + newclasspath + ":";
            }
        }
        if(newclasspath.endsWith(":"))
        {
            newclasspath = newclasspath.substring(0, newclasspath.length() - 1);
        }
        return newclasspath;
    }


    boolean isLazyLoaded()
    {
        return this.lazyLoaded;
    }


    void setLazyLoaded(boolean lazyLoaded)
    {
        this.lazyLoaded = lazyLoaded;
    }


    public boolean isExcluded()
    {
        return this.excluded;
    }


    public void setExcluded(boolean excluded)
    {
        this.excluded = excluded;
    }


    public Map<String, String> getMetaMap()
    {
        return this.metaMap;
    }


    public boolean isCorePlusExtension()
    {
        return Boolean.parseBoolean(this.metaMap.get("coreplus"));
    }


    public boolean isExternalExtension()
    {
        return this.metaMap.containsKey("war");
    }
}

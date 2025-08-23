package de.hybris.bootstrap.config;

import de.hybris.bootstrap.util.RequirementSolver;
import de.hybris.bootstrap.util.RequirementSolverException;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public final class PlatformConfig
{
    public static final String PLATFORM_EXTENSION_SCAN_DIRS = "platform.extensions.scan.dirs";
    public static final String PLATFORM_EXTENSIONS = "platform.extensions";
    public static final String PLATFORM_EXTENSIONS_SCAN_MAXDEPTH = "platform.extensions.scan.maxdepth";
    public static final String PLATFORM_EXTENSIONS_AUTOLOAD = "platform.extensions.autoload";
    public static final String PLATFORM_EXTENSIONS_HMC_RECURSION_LIMIT = "platform.extensions.hmc.recursion.limit";
    public static final String PLATFORM_EXTGEN_TEMPLATE_KEY = "extgen-template-extension";
    public static final String PLATFORM_MODULEGEN_NAME_KEY = "modulegen-name";
    public static final String XPATH_FACTORY = "com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl";
    public static final String DOCUMENTBUILDER_FACTORY = "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl";
    private static final String TAG_HYBRISCONFIG = "hybrisconfig";
    private static final String TAG_EXTENSIONS = "extensions";
    private static final String TAG_EXTENSION = "extension";
    private static final String TAG_WEBAPP = "webapp";
    private static final String TAG_PATH = "path";
    private static final String ATTRIBUTE_DIR = "dir";
    private static final String ATTRIBUTE_CONTEXTROOT = "contextroot";
    private static final String ATTRIBUTE_CONTEXT = "context";
    private static final String ATTRIBUTE_PATH = "path";
    private static final String ATTRIBUTE_DOCBASE = "docBase";
    private static final String ATTRIBUTE_AUTOLOAD = "autoload";
    private static final String ATTRIBUTE_DEPTH = "depth";
    private static final String EXTENSIONINFO = "extensioninfo.xml";
    private static final Pattern MAX_DEPTH_AT_PATH_PATTERN = Pattern.compile("(.*)\\[\\s*(\\d+)\\s*\\].*");
    private static final int DEFAULT_DIRECTORY_SCAN_DEPTH = 10;
    private final SystemConfig config;
    private boolean isAutoLoadPlatformExtensions = true;
    private final DependencyResolvedExtensions extensions;
    private static volatile PlatformConfig instance;
    private volatile Map<String, TenantInfo> tenantsInfos;


    public static PlatformConfig getInstance(SystemConfig config)
    {
        if(instance == null)
        {
            synchronized(PlatformConfig.class)
            {
                if(instance == null)
                {
                    instance = new PlatformConfig(config);
                }
            }
        }
        return instance;
    }


    private PlatformConfig(SystemConfig config)
    {
        this.config = config;
        ConfiguredExtensions confExtensions = collectConfiguredExtensions();
        addHMCDependenciesIfNecessary(confExtensions);
        this.extensions = processDependencies(confExtensions, 0, getHmcRecursionLimtFromSystemProperty(5));
        logDeprecationWarning();
    }


    private void logDeprecationWarning()
    {
        List<String> deprecatedOnes = new ArrayList<>();
        for(ExtensionInfo ext : this.extensions.list)
        {
            if(ext.isDeprecated())
            {
                deprecatedOnes.add(ext.getName());
            }
        }
        if(!deprecatedOnes.isEmpty())
        {
            Collections.sort(deprecatedOnes);
            System.err.println("---------------------------------------------------------------");
            System.err.println("Warning: you're using at least one deprecated extension!");
            System.err.println("Please note that they may not be available in future releases.");
            System.err.println();
            System.err.println("Deprecated extensions: " + deprecatedOnes);
            System.err.println("---------------------------------------------------------------");
        }
    }


    private File determineExtensionsFile()
    {
        File platformHome = getSystemConfig().getPlatformHome();
        File configDir = getSystemConfig().getConfigDir();
        File extensionsFile = new File(platformHome, "localextensions.xml");
        if(extensionsFile.exists() && !platformHome.equals(configDir))
        {
            throw new BootstrapConfigException("Please move your localextensions.xml to the hybris config folder at " + configDir);
        }
        extensionsFile = new File(configDir, "localextensions.xml");
        if(!extensionsFile.exists())
        {
            extensionsFile = new File(platformHome, "extensions.xml");
            if(!extensionsFile.exists())
            {
                throw new BootstrapConfigException("Neither " + configDir.getAbsolutePath() + "/localextensions.xml nor " + platformHome
                                .getAbsolutePath() + "/extensions.xml found!");
            }
        }
        return extensionsFile;
    }


    private boolean getAutoLoadPlatformExtensionsFromSystemProperty()
    {
        return !"false".equalsIgnoreCase(System.getProperty("platform.extensions.autoload"));
    }


    private int getScanMaxDepthFromSystemProperty(int def)
    {
        String s = System.getProperty("platform.extensions.scan.maxdepth");
        if(s != null)
        {
            try
            {
                return Integer.parseInt(s);
            }
            catch(NumberFormatException e)
            {
                System.err.println("Illegal extension scan max depth parameter '" + s + "'. Using default of " + def + ".");
            }
        }
        return def;
    }


    private int getHmcRecursionLimtFromSystemProperty(int def)
    {
        String str = System.getProperty("platform.extensions.hmc.recursion.limit");
        if(str != null)
        {
            try
            {
                return Integer.parseInt(str);
            }
            catch(NumberFormatException e)
            {
                System.err.println("Illegal extension hmc recursion limit parameter '" + str + "'. Using default of " + def + ".");
            }
        }
        return def;
    }


    private boolean isUsingExtensionsFromSystemProperty()
    {
        return (System.getProperty("platform.extensions") != null);
    }


    private List<String> getExtensionNamesFromSystemProperty()
    {
        List<String> ret = Collections.EMPTY_LIST;
        String extensionsProp = System.getProperty("platform.extensions");
        if(extensionsProp != null)
        {
            ret = new ArrayList<>();
            for(String s : extensionsProp.split("[,; ]"))
            {
                String name = s.trim();
                if(!name.isEmpty())
                {
                    ret.add(name);
                }
            }
        }
        return ret;
    }


    private List<ScanDir> getExtensionScanDirsFromSystemProperty(int defaultMaxDepth)
    {
        List<ScanDir> ret = Collections.EMPTY_LIST;
        String dirsProp = System.getProperty("platform.extensions.scan.dirs");
        File binDir = this.config.getBinDir();
        if(dirsProp != null)
        {
            ret = new ArrayList<>();
            for(String s : dirsProp.split("[,;]"))
            {
                String path = s.trim();
                if(!path.isEmpty())
                {
                    path = getSystemConfig().replaceProperties(path);
                    int maxDepth = defaultMaxDepth;
                    Matcher matcher = MAX_DEPTH_AT_PATH_PATTERN.matcher(path);
                    if(matcher.matches())
                    {
                        maxDepth = Integer.parseInt(matcher.group(2));
                        path = matcher.group(1).trim();
                    }
                    File dir = new File(path);
                    if(!dir.isAbsolute())
                    {
                        dir = new File(binDir, path);
                    }
                    if(!dir.exists() || !dir.isDirectory())
                    {
                        System.err.println("Scan dir '" + dir + "' doesn't exist or is no dir. Ignoring it.");
                    }
                    else
                    {
                        ret.add(new ScanDir(dir, maxDepth));
                    }
                }
            }
        }
        else
        {
            ret = Collections.singletonList(new ScanDir(binDir, defaultMaxDepth));
        }
        return ret;
    }


    private DependencyResolvedExtensions processDependencies(ConfiguredExtensions configuredExtensions, int recursionLevel, int recursionLimit)
    {
        Collection<ExtensionRequirementHolder> allRequirements = expandExtensionsToAllRequirments(configuredExtensions);
        try
        {
            List<ExtensionRequirementHolder> reqInDepenencyOrder = RequirementSolver.solve(allRequirements);
            assertNoExtensionsMissing(reqInDepenencyOrder);
            List<List<ExtensionRequirementHolder>> reqGroupsInDepenencyOrder = RequirementSolver.solveParallel(allRequirements);
            List<ExtensionInfo> extensionsInBuildOrder = toExtensions(reqInDepenencyOrder);
            List<List<ExtensionInfo>> extensionGroups = toExtensionGroups(reqGroupsInDepenencyOrder);
            ExtensionInfo hmcExt = configuredExtensions.get("hmc");
            boolean needSecondPass = false;
            if(extensionsInBuildOrder.contains(hmcExt))
            {
                needSecondPass |= lookupAndMakeExtensionExplicit("platformhmc", configuredExtensions, extensionsInBuildOrder);
                for(ExtensionInfo ext : extensionsInBuildOrder)
                {
                    if(!"hmc".equalsIgnoreCase(ext.getName()))
                    {
                        needSecondPass |= lookupAndMakeExtensionExplicit(ext.getName() + "hmc", configuredExtensions, extensionsInBuildOrder);
                    }
                }
            }
            if(needSecondPass && recursionLevel < recursionLimit)
            {
                return processDependencies(configuredExtensions, recursionLevel + 1, recursionLimit);
            }
            return new DependencyResolvedExtensions(extensionsInBuildOrder, extensionGroups);
        }
        catch(RequirementSolverException e1)
        {
            throw new BootstrapConfigException("Extension setup contains a cyclic definition. Check all your configured extensions in the localextensions.xml and the required extensions in all extensioninfo.xml. Current dependencies are " + allRequirements);
        }
    }


    private boolean lookupAndMakeExtensionExplicit(String extName, ConfiguredExtensions configuredExtensions, List<ExtensionInfo> extensionsInBuildOrder)
    {
        ExtensionInfo platformHMC = configuredExtensions.get(extName);
        if(platformHMC != null && !extensionsInBuildOrder.contains(platformHMC) && !platformHMC.isExcluded())
        {
            platformHMC.setLazyLoaded(false);
            return true;
        }
        return false;
    }


    public String getExtensionSetupInfo(String platformVersion)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("---------------------------------------------------------------\n");
        sb.append("--- Extensions in dependency order ( options: \n");
        sb.append("---  @deprecated: is deprecated, p: platform extension,*: auto-required \n");
        sb.append("---  ?: lazy-loaded, i: got items.xml, b: got beans.xml, c: got core module \n");
        sb.append("---  w: got web module ) \n");
        sb.append("---------------------------------------------------------------\n");
        for(ExtensionInfo info : getExtensionInfosInBuildOrder())
        {
            if(info.isExternalExtension())
            {
                continue;
            }
            if(info.isDeprecated())
            {
                sb.append("@deprecated ");
            }
            sb.append(info.getName());
            Set<String> requiredExtensionNames = new LinkedHashSet<>(info.getConfiguredRequiredExtensionNames());
            requiredExtensionNames.remove("core");
            if(!requiredExtensionNames.isEmpty())
            {
                sb.append("->").append(toDependencyCollectionString(requiredExtensionNames));
            }
            if(info.isLazyLoaded())
            {
                sb.append(" <-?-").append(toDependencyCollectionString(info.getConfiguredDependingExtensionNames()));
            }
            String version = info.getVersion();
            if(version == null || version.isEmpty())
            {
                version = platformVersion;
            }
            sb.append(' ').append(version);
            StringBuilder options = new StringBuilder();
            if(info.isCoreExtension())
            {
                options.append('p');
            }
            if(info.isRequiredByAll())
            {
                options.append('*');
            }
            if(info.isLazyLoaded())
            {
                options.append('?');
            }
            if(info.getCoreModule() != null)
            {
                options.append('c');
                if(info.getItemsXML().exists())
                {
                    options.append('i');
                }
                if(info.getBeansXML().exists())
                {
                    options.append('b');
                }
            }
            if(info.getWebModule() != null)
            {
                options.append('w');
            }
            if(info.getHMCModule() != null && getExtensionInfo("hmc") != null)
            {
                options.append('h');
            }
            if(info.isCorePlusExtension())
            {
                options.append('+');
            }
            if(options.length() > 0)
            {
                sb.append(" [").append(options).append(']');
            }
            if(!info.isCoreExtension())
            {
                sb.append(" path:").append(info.getExtensionDirectory());
            }
            sb.append('\n');
        }
        sb.append("---------------------------------------------------------------");
        return sb.toString();
    }


    private String toDependencyCollectionString(Collection<String> requiredExtensionNames)
    {
        StringBuilder sb = new StringBuilder();
        if(requiredExtensionNames.size() == 1)
        {
            sb.append(requiredExtensionNames.iterator().next());
        }
        else
        {
            sb.append('(');
            int i = 0;
            for(String req : requiredExtensionNames)
            {
                if(i++ > 0)
                {
                    sb.append(',');
                }
                sb.append(req);
            }
            sb.append(')');
        }
        return sb.toString();
    }


    private Collection<ExtensionRequirementHolder> expandExtensionsToAllRequirments(ConfiguredExtensions allKnownExtensions)
    {
        Map<String, ExtensionRequirementHolder> extensionToRequirementMap = new LinkedHashMap<>();
        Object object = new Object(this, extensionToRequirementMap, allKnownExtensions);
        Collection<ExtensionRequirementHolder> explicitExtensionsAsRequirements = createRequirementsFor(allKnownExtensions
                        .getExplicitConfiguredExtensions(), (ExtensionResolver)object);
        for(ExtensionRequirementHolder holder : explicitExtensionsAsRequirements)
        {
            extensionToRequirementMap.put(holder.extName, holder);
        }
        Collection<ExtensionRequirementHolder> allExtensionsIncludingDependencies = addRequirementsForMissingDependencies(explicitExtensionsAsRequirements);
        computeAndApplyRequiredByAllDependencies(allExtensionsIncludingDependencies);
        return allExtensionsIncludingDependencies;
    }


    private Collection<ExtensionRequirementHolder> createRequirementsFor(Collection<ExtensionInfo> extensions, ExtensionResolver resolver)
    {
        List<ExtensionRequirementHolder> explicitExtensionsAsRequirements = new ArrayList<>();
        for(ExtensionInfo e : extensions)
        {
            ExtensionRequirementHolder holder = new ExtensionRequirementHolder(e, resolver);
            explicitExtensionsAsRequirements.add(holder);
        }
        return explicitExtensionsAsRequirements;
    }


    private Collection<ExtensionRequirementHolder> addRequirementsForMissingDependencies(Collection<ExtensionRequirementHolder> explicitExtensionsAsRequirements)
    {
        Set<ExtensionRequirementHolder> allExtensionsIncludingDependencies = new TreeSet<>();
        Set<ExtensionRequirementHolder> toProcess = new LinkedHashSet<>(explicitExtensionsAsRequirements);
        do
        {
            Set<ExtensionRequirementHolder> nextToProcess = new HashSet<>();
            for(ExtensionRequirementHolder ext : toProcess)
            {
                if(allExtensionsIncludingDependencies.add(ext))
                {
                    nextToProcess.addAll(ext.getRequirements());
                }
            }
            toProcess = nextToProcess;
        }
        while(!toProcess.isEmpty());
        return allExtensionsIncludingDependencies;
    }


    private void computeAndApplyRequiredByAllDependencies(Collection<ExtensionRequirementHolder> allExtensionsIncludingDependencies)
    {
        Set<String> requiredByAllExtensionNames = new HashSet<>();
        for(ExtensionRequirementHolder r : allExtensionsIncludingDependencies)
        {
            if(!r.isMissing() && r.info.isRequiredByAll())
            {
                requiredByAllExtensionNames.add(r.extName);
            }
        }
        for(ExtensionRequirementHolder r : allExtensionsIncludingDependencies)
        {
            if(!r.isMissing() && !r.info.isCoreExtension())
            {
                addRequiredByAllTo(r, requiredByAllExtensionNames);
            }
        }
    }


    private void addRequiredByAllTo(ExtensionRequirementHolder r, Set<String> requiredByAllExtensionNames)
    {
        Set<String> myImplicitDependencies = requiredByAllExtensionNames;
        if(myImplicitDependencies.contains(r.info.getName()))
        {
            myImplicitDependencies = new HashSet<>(requiredByAllExtensionNames);
            myImplicitDependencies.remove(r.info.getName());
        }
        r.info.addImplicitlyRequiredExtensionNames(myImplicitDependencies);
        r.requirements = null;
    }


    private List<List<ExtensionInfo>> toExtensionGroups(List<List<ExtensionRequirementHolder>> reqList)
    {
        List<List<ExtensionInfo>> ret = new ArrayList<>(reqList.size());
        for(List<ExtensionRequirementHolder> r : reqList)
        {
            ret.add(toExtensions(r));
        }
        return ret;
    }


    private List<ExtensionInfo> toExtensions(List<ExtensionRequirementHolder> reqList)
    {
        List<ExtensionInfo> ret = new ArrayList<>(reqList.size());
        for(ExtensionRequirementHolder r : reqList)
        {
            ret.add(r.info);
        }
        return ret;
    }


    private void assertNoExtensionsMissing(List<ExtensionRequirementHolder> listInDepenencyOrder)
    {
        StringBuilder error = null;
        for(ExtensionRequirementHolder req : listInDepenencyOrder)
        {
            if(req.isMissing())
            {
                if(error == null)
                {
                    error = new StringBuilder("Missing required extensions: ");
                }
                else
                {
                    error.append(',');
                }
                error.append(req.extName).append("<-").append(asString(req.dependent));
            }
        }
        if(error != null)
        {
            error.append(". Please check your extension setup!");
            throw new BootstrapConfigException(error.toString());
        }
    }


    static String asString(Collection<ExtensionRequirementHolder> req)
    {
        StringBuilder sb = new StringBuilder();
        for(ExtensionRequirementHolder r : req)
        {
            if(sb.length() > 0)
            {
                sb.append(',');
            }
            sb.append(r.extName);
        }
        sb.insert(0, '[');
        sb.append(']');
        return sb.toString();
    }


    private Collection<ExtensionInfo> scanPlatformExtDirectory(boolean autoloadPlatformExtensions)
    {
        File rootExtDir = getRootExtDir();
        return scanForExtensions(rootExtDir, 1, true, autoloadPlatformExtensions);
    }


    private File getRootExtDir()
    {
        File rootExtDir = new File(getSystemConfig().getPlatformHome(), "ext");
        if(!rootExtDir.exists() || !rootExtDir.isDirectory())
        {
            throw new IllegalArgumentException("Can not find platform extension directory " + rootExtDir.getAbsolutePath());
        }
        return rootExtDir;
    }


    private Collection<ExtensionInfo> scanForExtensions(File rootDir, int maxDepth, boolean asCoreExtension, boolean autoloaded)
    {
        if(rootDir.exists() && rootDir.isDirectory() && !this.config.getPlatformHome().equals(rootDir))
        {
            Collection<ExtensionInfo> extensions;
            File configFile = new File(rootDir, "extensioninfo.xml");
            if(configFile.exists())
            {
                ExtensionInfo info = new ExtensionInfo(this, configFile, asCoreExtension);
                info.setLazyLoaded(!autoloaded);
                extensions = Collections.singletonList(info);
            }
            else if(maxDepth > 0)
            {
                extensions = new ArrayList<>();
                for(File f : rootDir.listFiles())
                {
                    if(f.isDirectory())
                    {
                        extensions.addAll(scanForExtensions(f, maxDepth - 1, asCoreExtension, autoloaded));
                    }
                }
            }
            else
            {
                extensions = Collections.EMPTY_LIST;
            }
            return extensions;
        }
        return Collections.EMPTY_LIST;
    }


    private static boolean isAutoLoad(XPath xpath, File configFile)
    {
        try
        {
            FileInputStream inputStream = new FileInputStream(configFile);
            try
            {
                InputSource inputSource = new InputSource(inputStream);
                String autoloadpath = "/hybrisconfig/extensions/@autoload";
                String autoloadvalue = (String)xpath.evaluate("/hybrisconfig/extensions/@autoload", inputSource, XPathConstants.STRING);
                if(autoloadvalue == null || autoloadvalue.isEmpty())
                {
                    boolean bool1 = true;
                    inputStream.close();
                    return bool1;
                }
                boolean bool = Boolean.parseBoolean(autoloadvalue);
                inputStream.close();
                return bool;
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
        catch(IOException ioe)
        {
            throw new BootstrapConfigException("IOException when scanning for the autoload mode", ioe);
        }
        catch(XPathExpressionException xpee)
        {
            throw new BootstrapConfigException("XPathExpressionException when scanning for the autoload mode", xpee);
        }
    }


    private static boolean isNotSameLocation(ExtensionInfo info1, ExtensionInfo info2)
    {
        try
        {
            return
                            !info1.getExtensionDirectory().getCanonicalPath().equalsIgnoreCase(info2.getExtensionDirectory().getCanonicalPath());
        }
        catch(IOException e)
        {
            return !info1.getExtensionDirectory().equals(info2.getExtensionDirectory());
        }
    }


    private static boolean isSameLocation(ExtensionInfo info1, ExtensionInfo info2)
    {
        return !isNotSameLocation(info1, info2);
    }


    private ConfiguredExtensions collectConfiguredExtensions()
    {
        if(isUsingExtensionsFromSystemProperty())
        {
            return collectConfiguredExtensionsFromSystemProperties();
        }
        return collectConfiguredExtensionsFromXMLConfig(determineExtensionsFile());
    }


    private ConfiguredExtensions collectConfiguredExtensionsFromSystemProperties()
    {
        List<String> extensionNames = getExtensionNamesFromSystemProperty();
        int defaultMaxDepth = getScanMaxDepthFromSystemProperty(10);
        List<ScanDir> extensionScanDirs = getExtensionScanDirsFromSystemProperty(defaultMaxDepth);
        boolean autoLoad = getAutoLoadPlatformExtensionsFromSystemProperty();
        System.out.println("Using extension configuration from system property: autoLoadPlatformExt=" + autoLoad + " names=" + extensionNames + " scanDirs=" + extensionScanDirs + " scanMaxDepth=" + defaultMaxDepth + "..");
        ConfiguredExtensions known = new ConfiguredExtensions();
        Collection<ExtensionInfo> allScannedExtensions = new ArrayList<>();
        Collection<ExtensionInfo> platformExtExtensions = collectPlatformExtExtensions(autoLoad);
        allScannedExtensions.addAll(platformExtExtensions);
        List<ExtensionInfo> scannedOtherExtensions = readAndScanExtensionDirs(extensionScanDirs);
        allScannedExtensions.addAll(scannedOtherExtensions);
        Collection<ExtensionInfo> configuredExtensions = matchConfiguredExtensions(extensionNames, allScannedExtensions, extensionScanDirs);
        known.addAll(platformExtExtensions, true);
        known.addAll(configuredExtensions, true);
        known.addAll(scannedOtherExtensions, false);
        known.addAll(getRequiredTemplateExtensions(known.extensions.values()), false);
        return known;
    }


    private Collection<ExtensionInfo> collectPlatformExtExtensions(boolean autoLoad)
    {
        Collection<ExtensionInfo> extensions = scanPlatformExtDirectory(autoLoad);
        setAutoLoadPlatformExtensions(autoLoad);
        if(autoLoad)
        {
            for(ExtensionInfo ext : extensions)
            {
                ext.setRequiredByAll(true);
            }
        }
        return extensions;
    }


    private ConfiguredExtensions collectConfiguredExtensionsFromXMLConfig(File configFile)
    {
        XPath xPath = XPathFactory.newInstance().newXPath();
        ConfiguredExtensions known = new ConfiguredExtensions();
        Collection<ExtensionInfo> allScannedExtensions = new ArrayList<>();
        boolean autoloadPlatformExtensions = isAutoLoad(xPath, configFile);
        Collection<ExtensionInfo> platformExtExtensions = collectPlatformExtExtensions(autoloadPlatformExtensions);
        allScannedExtensions.addAll(platformExtExtensions);
        List<ExtensionInfo> scannedOtherExtensions = readAndScanExtensionDirsFromXMLFile(xPath, configFile);
        allScannedExtensions.addAll(scannedOtherExtensions);
        known.addAll(platformExtExtensions, true);
        known.addAll(readConfiguredExtensionsFromXMLFile(xPath, configFile, allScannedExtensions), true);
        known.addAll(readWebappExtensions(xPath, configFile), true);
        known.addAll(scannedOtherExtensions, false);
        known.addAll(getRequiredTemplateExtensions(known.extensions.values()), false);
        return known;
    }


    private Collection<ExtensionInfo> getRequiredTemplateExtensions(Collection<ExtensionInfo> extensions)
    {
        if(isExtgenMode())
        {
            Collection<ExtensionInfo> templates = getExtgenTemplateExtensions(extensions);
            if(!templates.isEmpty())
            {
                for(ExtensionInfo extensionInfo : templates)
                {
                    extensionInfo.setLazyLoaded(false);
                    System.out.println(" - explicitly loading extgen template extension : " + extensionInfo.getName());
                }
                return templates;
            }
            System.err.println("No extgen templates found.");
        }
        if(isModulegenMode())
        {
            Collection<ExtensionInfo> templates = getModulegenTemplateExtensions(extensions);
            if(!templates.isEmpty())
            {
                for(ExtensionInfo extensionInfo : templates)
                {
                    extensionInfo.setLazyLoaded(false);
                    System.out.println(" - explicitly loading modulegen template extension : " + extensionInfo.getName());
                }
                return templates;
            }
            System.err.println("No modulegen templates found.");
        }
        return Collections.EMPTY_LIST;
    }


    private Collection<ExtensionInfo> matchConfiguredExtensions(List<String> extensionNames, Collection<ExtensionInfo> knownExtensions, List<ScanDir> scanDirs)
    {
        Collection<ExtensionInfo> extensions = new LinkedHashSet<>();
        for(String name : extensionNames)
        {
            if(isLoadAll(name))
            {
                System.out.println("Loading *all* scanned extensions..");
                for(ExtensionInfo e : knownExtensions)
                {
                    extensions.add(createExtensionForName(e.getName(), knownExtensions));
                }
                continue;
            }
            if(isExclude(name))
            {
                String realName = name.substring(1).trim();
                removeExcludedExtension(realName, knownExtensions, extensions);
                continue;
            }
            ExtensionInfo knowExt = createExtensionForName(name, knownExtensions);
            if(knowExt != null)
            {
                extensions.add(knowExt);
                continue;
            }
            throw new BootstrapConfigException("Extension '" + name + "' could not be found within " + scanDirs + "( found " + knowExt + ").");
        }
        return extensions;
    }


    public Collection<ExtensionInfo> getExtgenTemplateExtensions(Collection<ExtensionInfo> allExtensions)
    {
        Collection<ExtensionInfo> templates = new ArrayList<>();
        for(ExtensionInfo extensionInfo : allExtensions)
        {
            if(extensionInfo.isExtgenTemplate())
            {
                templates.add(extensionInfo);
            }
        }
        return templates;
    }


    public Collection<ExtensionInfo> getModulegenTemplateExtensions(Collection<ExtensionInfo> allExtensions)
    {
        Collection<ExtensionInfo> templates = new ArrayList<>();
        for(ExtensionInfo extensionInfo : allExtensions)
        {
            if(extensionInfo.isModulegenTemplate())
            {
                templates.add(extensionInfo);
            }
        }
        return templates;
    }


    public boolean isExtgenMode()
    {
        return Boolean.valueOf(System.getProperty("extgen.mode")).booleanValue();
    }


    public boolean isModulegenMode()
    {
        return Boolean.valueOf(System.getProperty("modulegen.mode")).booleanValue();
    }


    private void removeExcludedExtension(String name, Collection<ExtensionInfo> knownExtensions, Collection<ExtensionInfo> currentExtensions)
    {
        for(Iterator<ExtensionInfo> it = knownExtensions.iterator(); it.hasNext(); )
        {
            ExtensionInfo info = it.next();
            if(name.equalsIgnoreCase(info.getName()))
            {
                if(!isAutoLoadPlatformExtensions() || !info.isCoreExtension())
                {
                    System.out.println("removing excluded extension " + name + "..");
                    info.setExcluded(true);
                    info.setLazyLoaded(true);
                    currentExtensions.remove(info);
                    continue;
                }
                System.err.println("Cannot exclude platform/ext extension " + name + " with autoload='true' enabled since it will be always loaded! Please use <extensions autoload='false'> if you want to configure them explicitly.");
            }
        }
    }


    private boolean isLoadAll(String extensionName)
    {
        return "*".equals(extensionName);
    }


    private boolean isExclude(String extensionName)
    {
        return extensionName.startsWith("-");
    }


    private Collection<ExtensionInfo> readConfiguredExtensionsFromXMLFile(XPath xpath, File configFile, Collection<ExtensionInfo> knownExtensions)
    {
        try
        {
            FileInputStream inputStream = new FileInputStream(configFile);
            try
            {
                NodeList extensionNodes = (NodeList)xpath.evaluate("/hybrisconfig/extensions/extension", new InputSource(inputStream), XPathConstants.NODESET);
                List<ExtensionInfo> extensions = new ArrayList<>();
                for(int i = 0; i < extensionNodes.getLength(); i++)
                {
                    ExtensionInfo info = createExtensionInfoFromXML(extensionNodes.item(i), configFile, knownExtensions);
                    extensions.add(info);
                }
                List<ExtensionInfo> list1 = extensions;
                inputStream.close();
                return list1;
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
        catch(Exception e)
        {
            throw new RuntimeException("Error while evaluating file '" + configFile + "'. " + e.getMessage(), e);
        }
    }


    private Collection<ExtensionInfo> readWebappExtensions(XPath xpath, File configFile)
    {
        try
        {
            FileInputStream inputStream = new FileInputStream(configFile);
            try
            {
                NodeList webappNodes = (NodeList)xpath.evaluate("/hybrisconfig/extensions/webapp", new InputSource(inputStream), XPathConstants.NODESET);
                List<ExtensionInfo> extensions = new ArrayList<>();
                for(int i = 0; i < webappNodes.getLength(); i++)
                {
                    ExtensionInfo info = createExtensionForWebapp(webappNodes.item(i), configFile);
                    extensions.add(info);
                }
                List<ExtensionInfo> list1 = extensions;
                inputStream.close();
                return list1;
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
        catch(Exception e)
        {
            throw new RuntimeException("Error while evaluating file '" + configFile + "'. " + e.getMessage(), e);
        }
    }


    public static void closeQuietly(Closeable closeable)
    {
        try
        {
            if(closeable != null)
            {
                closeable.close();
            }
        }
        catch(IOException iOException)
        {
        }
    }


    private List<ExtensionInfo> readAndScanExtensionDirs(List<ScanDir> scanDirs)
    {
        List<ExtensionInfo> extensions = new ArrayList<>();
        for(ScanDir scanDir : scanDirs)
        {
            Collection<ExtensionInfo> scannedExtensions = scanForExtensions(scanDir.dir, scanDir.maxDepth, false, false);
            if(!scannedExtensions.isEmpty())
            {
                extensions.addAll(scannedExtensions);
            }
        }
        return extensions;
    }


    private List<ExtensionInfo> readAndScanExtensionDirsFromXMLFile(XPath xpath, File configFile)
    {
        try
        {
            FileInputStream inputStream = new FileInputStream(configFile);
            try
            {
                NodeList scanDirNodes = (NodeList)xpath.evaluate("/hybrisconfig/extensions/path", new InputSource(inputStream), XPathConstants.NODESET);
                List<ExtensionInfo> autoLoadExtensions = new ArrayList<>();
                List<ExtensionInfo> lazyLoadExtensions = new ArrayList<>();
                for(int i = 0; i < scanDirNodes.getLength(); i++)
                {
                    Node scanDirXMLNode = scanDirNodes.item(i);
                    String dirName = readDirAttribute(scanDirXMLNode);
                    if(dirName == null || dirName.isEmpty())
                    {
                        throw new BootstrapConfigException("Missing attribute 'dir' in the node <" + scanDirXMLNode
                                        .getLocalName() + "> in file " + configFile.getAbsolutePath());
                    }
                    File dir = new File(dirName);
                    if(dir.exists() && dir.isDirectory())
                    {
                        boolean autoLoaded = readAutoloadAttribute(scanDirXMLNode);
                        int maxDepth = readMaxDepthAttribute(scanDirXMLNode, 10);
                        Collection<ExtensionInfo> scannedExtensions = scanForExtensions(dir, maxDepth, false, autoLoaded);
                        if(!scannedExtensions.isEmpty())
                        {
                            if(autoLoaded)
                            {
                                autoLoadExtensions.addAll(scannedExtensions);
                            }
                            else
                            {
                                lazyLoadExtensions.addAll(scannedExtensions);
                            }
                        }
                    }
                    else
                    {
                        System.err.println("Cannot find extension scan dir " + dir + " - ignored.");
                    }
                }
                List<ExtensionInfo> mergedExtensions = new ArrayList<>(autoLoadExtensions);
                mergedExtensions.addAll(lazyLoadExtensions);
                List<ExtensionInfo> list1 = mergedExtensions;
                inputStream.close();
                return list1;
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
        catch(Exception e)
        {
            throw new RuntimeException("Error while evaluating file '" + configFile + "'. " + e.getMessage(), e);
        }
    }


    private boolean readAutoloadAttribute(Node extensionNode)
    {
        Node extension_node = extensionNode.getAttributes().getNamedItem("autoload");
        if(extension_node == null)
        {
            return false;
        }
        String stringValue = extension_node.getNodeValue();
        return Boolean.parseBoolean(stringValue);
    }


    private int readMaxDepthAttribute(Node extensionNode, int defaultValue)
    {
        Node depthNode = extensionNode.getAttributes().getNamedItem("depth");
        if(depthNode != null)
        {
            try
            {
                return Integer.parseInt(depthNode.getNodeValue());
            }
            catch(NumberFormatException e)
            {
                System.err.println("Invalid value depth='" + depthNode.getNodeValue() + "' in " + depthNode
                                .getOwnerDocument() + ". Using default of " + defaultValue + " instead.");
            }
        }
        return defaultValue;
    }


    private ExtensionInfo createExtensionInfoFromXML(Node extensionNode, File configFile, Collection<ExtensionInfo> knownExtensions)
    {
        String extensiondir = readDirAttribute(extensionNode);
        String name = readNameAttribute(extensionNode);
        if(extensiondir == null || extensiondir.length() <= 0)
        {
            if(name != null)
            {
                ExtensionInfo knowExt = createExtensionForName(name, knownExtensions);
                if(knowExt != null)
                {
                    return knowExt;
                }
                throw new BootstrapConfigException("Extension '" + name + "' doesn't specify a path and no scanned extension was matching the name. Please check file " + configFile
                                .getAbsolutePath() + ".");
            }
            throw new BootstrapConfigException("Missing attribute 'dir' in the node <" + extensionNode
                            .getLocalName() + "> in file " + configFile.getAbsolutePath());
        }
        return createExtensionForDir(getExtensionDir(extensiondir), knownExtensions);
    }


    private ExtensionInfo createExtensionForContext(Node extensionNode, String contextFile)
    {
        String path = readPathAttribute(extensionNode);
        String docBase = readDocBaseAttribute(extensionNode);
        if(path != null && path.startsWith("/"))
        {
            path = path.substring(1);
        }
        HashMap<String, String> metaMap = new HashMap<>(1, 0.75F);
        metaMap.put("war", docBase);
        return new ExtensionInfo(path, metaMap);
    }


    private ExtensionInfo createExtensionForWebapp(Node extensionNode, File configFile)
    {
        String contextRoot = readContextRootAttribute(extensionNode);
        String context = readContextAttribute(extensionNode);
        String path = readPathAttribute(extensionNode);
        if(context != null)
        {
            XPath xpath = XPathFactory.newInstance().newXPath();
            try
            {
                FileInputStream inputStream = new FileInputStream(context);
                try
                {
                    NodeList contextNodes = (NodeList)xpath.evaluate("/Context", new InputSource(inputStream), XPathConstants.NODESET);
                    int i = 0;
                    if(i < contextNodes.getLength())
                    {
                        ExtensionInfo extensionInfo = createExtensionForContext(contextNodes.item(i), context);
                        inputStream.close();
                        return extensionInfo;
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
            catch(Exception e)
            {
                throw new RuntimeException("Error while evaluating file '" + configFile + "'. " + e.getMessage(), e);
            }
        }
        HashMap<String, String> metaMap = new HashMap<>(1, 0.75F);
        metaMap.put("war", path);
        return new ExtensionInfo(contextRoot, metaMap);
    }


    private ExtensionInfo createExtensionForDir(File extensionDir, Collection<ExtensionInfo> known)
    {
        boolean isCoreExtension = false;
        if(extensionDir != null && extensionDir.isFile())
        {
            File dir = extensionDir.getParentFile();
            File[] dirs = getRootExtDir().listFiles();
            if(dirs != null)
            {
                isCoreExtension = Arrays.<File>asList(dirs).contains(dir);
            }
        }
        ExtensionInfo tmp = new ExtensionInfo(this, extensionDir, isCoreExtension);
        tmp.setLazyLoaded(false);
        String name = tmp.getName();
        for(ExtensionInfo i : known)
        {
            if(name.equalsIgnoreCase(i.getName()) && isSameLocation(tmp, i))
            {
                i.setLazyLoaded(false);
                return i;
            }
        }
        return tmp;
    }


    private ExtensionInfo createExtensionForName(String name, Collection<ExtensionInfo> known)
    {
        for(ExtensionInfo i : known)
        {
            if(name.equalsIgnoreCase(i.getName()))
            {
                i.setLazyLoaded(false);
                return i;
            }
        }
        return null;
    }


    private File getExtensionDir(String extensiondir)
    {
        File extension_configFile = new File(extensiondir, "extensioninfo.xml");
        if(!extension_configFile.isAbsolute())
        {
            extension_configFile = new File(getSystemConfig().getPlatformHome(), extension_configFile.getPath());
        }
        return extension_configFile;
    }


    private String readDirAttribute(Node extensionNode)
    {
        Node extension_node = extensionNode.getAttributes().getNamedItem("dir");
        if(extension_node == null)
        {
            return null;
        }
        return getSystemConfig().replaceProperties(extension_node.getNodeValue());
    }


    private String readContextRootAttribute(Node extensionNode)
    {
        Node extension_node = extensionNode.getAttributes().getNamedItem("contextroot");
        if(extension_node == null)
        {
            return null;
        }
        return getSystemConfig().replaceProperties(extension_node.getNodeValue());
    }


    private String readContextAttribute(Node extensionNode)
    {
        Node extension_node = extensionNode.getAttributes().getNamedItem("context");
        if(extension_node == null)
        {
            return null;
        }
        return getSystemConfig().replaceProperties(extension_node.getNodeValue());
    }


    private String readPathAttribute(Node extensionNode)
    {
        Node extension_node = extensionNode.getAttributes().getNamedItem("path");
        if(extension_node == null)
        {
            return null;
        }
        return getSystemConfig().replaceProperties(extension_node.getNodeValue());
    }


    private String readDocBaseAttribute(Node extensionNode)
    {
        Node extension_node = extensionNode.getAttributes().getNamedItem("docBase");
        if(extension_node == null)
        {
            return null;
        }
        return getSystemConfig().replaceProperties(extension_node.getNodeValue());
    }


    private String readNameAttribute(Node extensionNode)
    {
        Node extension_node = extensionNode.getAttributes().getNamedItem("name");
        return (extension_node == null) ? null : extension_node.getNodeValue();
    }


    private void addHMCDependenciesIfNecessary(ConfiguredExtensions configuredExtensions)
    {
        ExtensionInfo hmcExt = configuredExtensions.get("hmc");
        if(hmcExt != null)
        {
            for(Map.Entry<String, ExtensionInfo> e : (Iterable<Map.Entry<String, ExtensionInfo>>)configuredExtensions.extensions.entrySet())
            {
                ExtensionInfo ext = e.getValue();
                if(!"hmc".equalsIgnoreCase(e.getKey()) && ext.getHMCModule() != null)
                {
                    if(!hmcExt.isLazyLoaded())
                    {
                        ext.addRequiredExtensionName("hmc");
                    }
                    for(String dependency : createHmcModuleForDependenciesList(configuredExtensions, ext))
                    {
                        ext.addRequiredExtensionName(dependency);
                    }
                }
            }
        }
    }


    private List<String> createHmcModuleForDependenciesList(ConfiguredExtensions configuredExtensions, ExtensionInfo ext)
    {
        String hmcDependencyName = null;
        List<String> result = new ArrayList<>();
        for(String dependency : ext.getConfiguredRequiredExtensionNames())
        {
            hmcDependencyName = dependency + "hmc";
            if(!ext.getName().equals(hmcDependencyName) && configuredExtensions.get(hmcDependencyName) != null)
            {
                result.add(hmcDependencyName);
            }
        }
        return result;
    }


    public File getPlatformHome()
    {
        return getSystemConfig().getPlatformHome();
    }


    public SystemConfig getSystemConfig()
    {
        return this.config;
    }


    public List<List<ExtensionInfo>> getExtensionInfosInBuildOrderParallel()
    {
        return this.extensions.groups;
    }


    public List<ExtensionInfo> getExtensionInfosInBuildOrder()
    {
        return this.extensions.list;
    }


    public ExtensionInfo getExtensionInfo(String name)
    {
        return (ExtensionInfo)this.extensions.allExtensions.get(name);
    }


    public Map<String, TenantInfo> getTenantInfos()
    {
        if(this.tenantsInfos == null)
        {
            Map<String, TenantInfo> infos = (new TenantsInfoLoader(this)).getSlaveTenants();
            this.tenantsInfos = infos;
        }
        return this.tenantsInfos;
    }


    public String getTenantForWebroot(String webroot, String defaultTenantID)
    {
        for(Map.Entry<String, TenantInfo> entry : getTenantInfos().entrySet())
        {
            String tenantID = entry.getKey();
            TenantInfo tenantInfo = entry.getValue();
            if(tenantInfo.isWebrootOwner(webroot))
            {
                return tenantID;
            }
        }
        return defaultTenantID;
    }


    public Set<String> getAllPlatformExtensionNames()
    {
        return this.extensions.getCoreExtensionsNames();
    }


    public boolean isAutoLoadPlatformExtensions()
    {
        return this.isAutoLoadPlatformExtensions;
    }


    public void setAutoLoadPlatformExtensions(boolean isAutoLoadPlatformExtensions)
    {
        this.isAutoLoadPlatformExtensions = isAutoLoadPlatformExtensions;
    }
}

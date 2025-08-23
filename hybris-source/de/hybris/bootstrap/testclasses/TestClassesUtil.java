package de.hybris.bootstrap.testclasses;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import de.hybris.bootstrap.annotations.BugProofTest;
import de.hybris.bootstrap.annotations.DemoTest;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.bootstrap.annotations.ManualTest;
import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.config.ExtensionModule;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.bootstrap.config.TestClassesXmlSerializer;
import de.hybris.bootstrap.loader.PlatformInPlaceClassLoader;
import de.hybris.bootstrap.util.RegexUtil;
import groovy.lang.GroovyClassLoader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TestClassesUtil
{
    private static final String WEB_WEBROOT_WEB_INF_CLASSES = "/web/webroot/WEB-INF/classes";
    private final PlatformConfig platformConfig;
    private final ClassLoader classLoader;
    private boolean webRelated = false;
    private final Map<FILTER, Object> filters = new HashMap<>();
    private final Map<ExtensionModule, Set<String>> moduleClasspaths = new HashMap<>();
    private boolean logging = true;


    public static TestClassesUtil createBootstrapInstance(PlatformConfig platformConfig)
    {
        return new TestClassesUtil(platformConfig, asPlatformInPlaceClassloader(platformConfig, TestClassesUtil.class
                        .getClassLoader()));
    }


    public void setWebRelated(boolean webRelated)
    {
        this.webRelated = webRelated;
    }


    public static TestClassesUtil createIntegrationInstance(PlatformConfig platformConfig)
    {
        return new TestClassesUtil(platformConfig, TestClassesUtil.class.getClassLoader());
    }


    public static TestClassesUtil createInstance(PlatformConfig platformConfig, PlatformInPlaceClassLoader inPlaceClassLoader)
    {
        return new TestClassesUtil(platformConfig, (ClassLoader)inPlaceClassLoader);
    }


    TestClassesUtil(PlatformConfig platformConfig, ClassLoader cl)
    {
        this.platformConfig = platformConfig;
        this.classLoader = createTestClassLoader(platformConfig, cl);
    }


    public static ClassLoader createTestClassLoader(PlatformConfig platformConfig, ClassLoader parent)
    {
        PatchedForTestGroovyClassLoader patchedForTestGroovyClassLoader = new PatchedForTestGroovyClassLoader(parent);
        patchedForTestGroovyClassLoader.setShouldRecompile(Boolean.TRUE);
        addTestSrcFoldersToClassPath(platformConfig, (GroovyClassLoader)patchedForTestGroovyClassLoader);
        return (ClassLoader)patchedForTestGroovyClassLoader;
    }


    public static void addTestSrcFoldersToClassPath(PlatformConfig platformConfig, GroovyClassLoader ret)
    {
        for(ExtensionInfo ext : platformConfig.getExtensionInfosInBuildOrder())
        {
            if(ext.getCoreModule() != null && ext.getCoreModule().isSourceAvailable())
            {
                File testsrc = new File(ext.getExtensionDirectory(), "testsrc");
                if(testsrc.exists())
                {
                    try
                    {
                        ret.addURL(testsrc.toURI().toURL());
                    }
                    catch(MalformedURLException e)
                    {
                        throw new IllegalStateException(e);
                    }
                }
            }
        }
    }


    private static ClassLoader asPlatformInPlaceClassloader(PlatformConfig platformConfig, ClassLoader cl)
    {
        if(cl instanceof PlatformInPlaceClassLoader)
        {
            return cl;
        }
        return (ClassLoader)new Object(platformConfig, "", ClassLoader.getPlatformClassLoader(), false);
    }


    public ClassLoader getClassLoaderForTests()
    {
        return this.classLoader;
    }


    public Collection<Class> getAllTestClasses()
    {
        resetFilter();
        return scan();
    }


    public Collection<Class> getAllWebTestClasses()
    {
        resetFilter();
        return scan();
    }


    public Collection<Class> getAllUnitTestClasses()
    {
        resetFilter();
        addFilter(FILTER.FILTER_BY_ANNOTATION, UnitTest.class);
        return scan();
    }


    public Collection<Class> getAllIntegrationTestClasses()
    {
        resetFilter();
        addFilter(FILTER.FILTER_BY_ANNOTATION, IntegrationTest.class);
        return scan();
    }


    public Collection<Class> getAllDemoTestClasses()
    {
        resetFilter();
        addFilter(FILTER.FILTER_BY_ANNOTATION, DemoTest.class);
        return scan();
    }


    public Collection<Class> getAllPerformanceTestClasses()
    {
        resetFilter();
        addFilter(FILTER.FILTER_BY_ANNOTATION, PerformanceTest.class);
        return scan();
    }


    public Collection<Class> getAllManualTestClasses()
    {
        resetFilter();
        addFilter(FILTER.FILTER_BY_ANNOTATION, ManualTest.class);
        return scan();
    }


    public Collection<Class> getAllBugProofTestClasses()
    {
        resetFilter();
        addFilter(FILTER.FILTER_BY_ANNOTATION, BugProofTest.class);
        return scan();
    }


    public void addFilter(FILTER filter, Object filterValue)
    {
        Collection<Object> collection;
        if(!(filterValue instanceof Collection))
        {
            collection = new HashSet();
            collection.add(filterValue);
        }
        else
        {
            collection = (Collection<Object>)filterValue;
        }
        if(this.filters.containsKey(filter))
        {
            ((Collection<Object>)this.filters.get(filter)).addAll(collection);
        }
        else
        {
            this.filters.put(filter, collection);
        }
    }


    public void resetFilter()
    {
        this.webRelated = false;
        this.filters.clear();
    }


    public Collection<Class> getFilteredTestClasses()
    {
        Thread t = Thread.currentThread();
        ClassLoader threadClassLoader = t.getContextClassLoader();
        try
        {
            t.setContextClassLoader(null);
            return scan();
        }
        finally
        {
            t.setContextClassLoader(threadClassLoader);
        }
    }


    private List<Class> scan()
    {
        long startMilli = System.currentTimeMillis();
        List<Class<?>> allClasses = new ArrayList<>();
        if(this.filters.containsKey(FILTER.FILTER_BY_EXTENSION))
        {
            Set<String> extensions = (Set<String>)this.filters.get(FILTER.FILTER_BY_EXTENSION);
            if(extensions == null || extensions.isEmpty())
            {
                throw new RuntimeException("Extension Filter was set but empty or null!");
            }
            for(String extension : extensions)
            {
                ExtensionInfo extensionInfo = this.platformConfig.getExtensionInfo(extension);
                if(extensionInfo == null)
                {
                    throw new RuntimeException("Extension '" + extension + "' could not be found!");
                }
                allClasses.addAll((Collection)getAllModuleTestClassesForExtension(extensionInfo, ExtensionModule.values(this.webRelated)));
            }
        }
        else
        {
            for(ExtensionInfo extensionInfo : this.platformConfig.getExtensionInfosInBuildOrder())
            {
                allClasses.addAll((Collection)getAllModuleTestClassesForExtension(extensionInfo, ExtensionModule.values(this.webRelated)));
            }
        }
        List<Class<?>> filteredClasses = new ArrayList<>();
        for(Class<?> clazz : allClasses)
        {
            if(isAbstractClass(clazz))
            {
                continue;
            }
            if(!matchesPackageFilters(clazz))
            {
                continue;
            }
            boolean foundHybrisAnnotation = false;
            Annotation[] annotations = clazz.getAnnotations();
            if(annotations != null)
            {
                for(Annotation annotation : annotations)
                {
                    String annotationName = annotation.annotationType().getName();
                    if(annotationName.equals(IntegrationTest.class.getName()))
                    {
                        foundHybrisAnnotation = true;
                        if(!this.filters.containsKey(FILTER.FILTER_BY_ANNOTATION) || ((Collection)this.filters
                                        .get(FILTER.FILTER_BY_ANNOTATION)).contains(IntegrationTest.class))
                        {
                            if(!filteredClasses.contains(clazz))
                            {
                                filteredClasses.add(clazz);
                            }
                        }
                    }
                    if(annotationName.equals(DemoTest.class.getName()))
                    {
                        foundHybrisAnnotation = true;
                        if(!this.filters.containsKey(FILTER.FILTER_BY_ANNOTATION) || ((Collection)this.filters
                                        .get(FILTER.FILTER_BY_ANNOTATION)).contains(DemoTest.class))
                        {
                            if(!filteredClasses.contains(clazz))
                            {
                                filteredClasses.add(clazz);
                            }
                        }
                    }
                    if(annotationName.equals(UnitTest.class.getName()))
                    {
                        foundHybrisAnnotation = true;
                        if(!this.filters.containsKey(FILTER.FILTER_BY_ANNOTATION) || ((Collection)this.filters
                                        .get(FILTER.FILTER_BY_ANNOTATION)).contains(UnitTest.class))
                        {
                            if(!filteredClasses.contains(clazz))
                            {
                                filteredClasses.add(clazz);
                            }
                        }
                    }
                    if(annotationName.equals(PerformanceTest.class.getName()))
                    {
                        foundHybrisAnnotation = true;
                        if(!this.filters.containsKey(FILTER.FILTER_BY_ANNOTATION) || ((Collection)this.filters
                                        .get(FILTER.FILTER_BY_ANNOTATION)).contains(PerformanceTest.class))
                        {
                            int executions = 1;
                            Method[] methods = clazz.getDeclaredMethods();
                            for(Method method : methods)
                            {
                                if(method.getName().equals("executions"))
                                {
                                    try
                                    {
                                        executions = ((Integer)method.invoke(clazz, new Object[0])).intValue();
                                    }
                                    catch(Exception e)
                                    {
                                        executions = 1;
                                        break;
                                    }
                                }
                            }
                            for(int i = 0; i < executions; i++)
                            {
                                filteredClasses.add(clazz);
                            }
                        }
                    }
                    if(annotationName.equals(ManualTest.class.getName()))
                    {
                        foundHybrisAnnotation = true;
                        if(!this.filters.containsKey(FILTER.FILTER_BY_ANNOTATION) || ((Collection)this.filters
                                        .get(FILTER.FILTER_BY_ANNOTATION)).contains(ManualTest.class))
                        {
                            if(!filteredClasses.contains(clazz))
                            {
                                filteredClasses.add(clazz);
                            }
                        }
                    }
                    if(annotationName.equals(BugProofTest.class.getName()))
                    {
                        foundHybrisAnnotation = true;
                        if(this.filters.containsKey(FILTER.FILTER_BY_ANNOTATION) && ((Collection)this.filters
                                        .get(FILTER.FILTER_BY_ANNOTATION)).contains(BugProofTest.class))
                        {
                            if(!filteredClasses.contains(clazz))
                            {
                                filteredClasses.add(clazz);
                            }
                        }
                    }
                }
            }
            if(!foundHybrisAnnotation)
            {
                if(!this.filters.containsKey(FILTER.FILTER_BY_ANNOTATION) || ((Collection)this.filters
                                .get(FILTER.FILTER_BY_ANNOTATION)).contains(IntegrationTest.class))
                {
                    for(Method method : clazz.getMethods())
                    {
                        boolean foundJunitTestAnnotation = false;
                        for(Annotation anno : method.getAnnotations())
                        {
                            if("org.junit.Test".equals(anno.annotationType().getName()) && clazz
                                            .getAnnotation(BugProofTest.class) == null)
                            {
                                if(!filteredClasses.contains(clazz))
                                {
                                    filteredClasses.add(clazz);
                                }
                                foundJunitTestAnnotation = true;
                                break;
                            }
                        }
                        if(foundJunitTestAnnotation)
                        {
                            break;
                        }
                    }
                }
            }
        }
        if(this.filters.containsKey(FILTER.REPLACED))
        {
            Set<Class<?>> replaced = new HashSet<>();
            filteredClasses.forEach(aClass -> collectReplacedClasses(replaced, aClass));
            filteredClasses = (List<Class<?>>)filteredClasses.stream().filter(aClass -> !replaced.contains(aClass)).collect(Collectors.toList());
        }
        log("Found " + filteredClasses.size() + " filtered classes...");
        if(this.filters.containsKey(FILTER.SPLIT_FILTER))
        {
            HashSet dividerArray = (HashSet)this.filters.get(FILTER.SPLIT_FILTER);
            String divider = (String)dividerArray.toArray()[0];
            if(!splitByExecutionTime(divider, filteredClasses))
            {
                try
                {
                    if(divider.matches(".*-.*"))
                    {
                        String[] args = getDivider(divider);
                        int start = Integer.parseInt(args[0]);
                        int end = Integer.parseInt(args[1]);
                        if(start > end)
                        {
                            System.err.println("Splitfilter : Start cannot be greater than end!");
                        }
                        else
                        {
                            List<Class<?>> classesList = new ArrayList<>(filteredClasses);
                            Collections.sort(classesList, (Comparator<? super Class<?>>)new Object(this));
                            int[] startAndEnd = getStartAndEnd(classesList.size(), divider);
                            filteredClasses = new ArrayList<>();
                            for(int i = startAndEnd[0]; i <= startAndEnd[1]; i++)
                            {
                                filteredClasses.add(classesList.get(i));
                            }
                            log("JUnit Split Filter : Executing tests " + startAndEnd[0] + " to " + startAndEnd[1]);
                        }
                    }
                }
                catch(Exception e)
                {
                    System.err.println("Splitfilter : Could not use Split Filter with argument : " + divider);
                    e.printStackTrace();
                }
            }
        }
        log("Scanning finished in : " + System.currentTimeMillis() - startMilli + "ms, found " + filteredClasses.size() + " testclasse(s)!");
        return filteredClasses;
    }


    private boolean isAbstractClass(Class clazz)
    {
        return Modifier.isAbstract(clazz.getModifiers());
    }


    static Pattern SPLIT_BY_EXECUTION_TIME_PATTERN = Pattern.compile("(?<bucketNumber>\\d+)-(?<numberOfBuckets>\\d+)-byExecutionTime\\((?<inputFile>\\S+)\\)");


    private boolean splitByExecutionTime(String divider, List<Class<?>> classes)
    {
        if(divider == null)
        {
            return false;
        }
        Matcher matcher = SPLIT_BY_EXECUTION_TIME_PATTERN.matcher(divider);
        if(!matcher.matches())
        {
            return false;
        }
        int bucketNumber = Integer.parseUnsignedInt(matcher.group("bucketNumber"));
        int numberOfBuckets = Integer.parseUnsignedInt(matcher.group("numberOfBuckets"));
        if(bucketNumber == 0 || bucketNumber > numberOfBuckets)
        {
            System.err.println("Invalid divider '" + divider + "'");
            return false;
        }
        Path inputFilePath = Paths.get(matcher.group("inputFile"), new String[0]);
        if(!Files.isReadable(inputFilePath) || !Files.isRegularFile(inputFilePath, new java.nio.file.LinkOption[0]))
        {
            System.err.println("Couldn't access file '" + inputFilePath + "'");
            return false;
        }
        Properties hints = new Properties();
        try
        {
            Reader reader = Files.newBufferedReader(inputFilePath);
            try
            {
                hints.load(reader);
                if(reader != null)
                {
                    reader.close();
                }
            }
            catch(Throwable throwable)
            {
                if(reader != null)
                {
                    try
                    {
                        reader.close();
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
            System.err.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
        if(hints.isEmpty())
        {
            System.err.println("No hints found in file '" + inputFilePath + "'");
            return false;
        }
        ByExecutionTimeSplitter splitter = new ByExecutionTimeSplitter((Map)hints.entrySet().stream().collect(Collectors.toMap(e -> (String)e.getKey(), e -> Long.valueOf((String)e.getValue()))), new ArrayList<>(classes), numberOfBuckets);
        List<Class<?>> newClasses = splitter.getTestsFromBucket(bucketNumber);
        classes.clear();
        classes.addAll(newClasses);
        return true;
    }


    private void collectReplacedClasses(Set<Class<?>> replaced, Class aClass)
    {
        for(Annotation annotation : aClass.getAnnotations())
        {
            if(Proxy.isProxyClass(annotation.getClass()) && annotation
                            .annotationType().getName().equalsIgnoreCase(IntegrationTest.class.getName()))
            {
                try
                {
                    Method replaces = IntegrationTest.class.getMethod("replaces", new Class[0]);
                    InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
                    Class<?> result = (Class)invocationHandler.invoke(annotation, replaces, new Object[0]);
                    replaced.add(result);
                }
                catch(Throwable throwable)
                {
                    System.err.println("Replaced filter: error while replacing test classes.");
                    throwable.printStackTrace();
                }
            }
        }
    }


    public String getWebModuleClasspaths()
    {
        return getModuleClasspaths(new ExtensionModule[] {ExtensionModule.WEB});
    }


    public String getModuleClasspaths(ExtensionModule... modules)
    {
        Set<String> classpaths = new HashSet<>();
        for(ExtensionModule module : modules)
        {
            if(this.moduleClasspaths.containsKey(module))
            {
                Set<String> moduleClasspath = this.moduleClasspaths.get(module);
                if(moduleClasspath != null)
                {
                    classpaths.addAll(moduleClasspath);
                }
            }
        }
        if(classpaths.isEmpty())
        {
            return null;
        }
        return Joiner.on(',').skipNulls().join(classpaths);
    }


    private Set<Class> getAllModuleTestClassesForExtension(ExtensionInfo extensionInfo, ExtensionModule... modules)
    {
        Set<Class<?>> allTestClasses = new HashSet<>();
        for(ExtensionModule module : modules)
        {
            if(module.exists(extensionInfo))
            {
                TestClassesXmlSerializer classesXmlSerializer = getTestClassesXmlSerializer();
                Set<String> testNames = classesXmlSerializer.readModuleTests(extensionInfo, module, this.classLoader);
                if(module.isWebRelated())
                {
                    Set<String> webModuleClasspathNames = getExtensionModuleClasspathNames(module, extensionInfo);
                    adjustExtensionModuleClasspath(module, webModuleClasspathNames, getWebModuleLibDir(extensionInfo));
                    Iterable<URL> webModuleLibs = getWebModuleLibs(extensionInfo);
                    Iterable<URL> classpaths = getExtensionModuleClasspaths(webModuleClasspathNames);
                    URL[] urls = (URL[])Iterables.toArray(Iterables.concat(classpaths, webModuleLibs), URL.class);
                    URLClassLoader webClassLoader = new URLClassLoader(urls, this.classLoader);
                    allTestClasses.addAll((Collection)convertToTestClasses(testNames, webClassLoader));
                }
                else
                {
                    allTestClasses.addAll((Collection)convertToTestClasses(testNames, this.classLoader));
                }
            }
        }
        return allTestClasses;
    }


    private Set<String> getExtensionModuleClasspathNames(ExtensionModule extensionModule, ExtensionInfo extensionInfo)
    {
        Set<String> result = new HashSet<>(2);
        result.add((new File(extensionModule.getBasePath(extensionInfo), "testclasses")).getAbsolutePath());
        if(extensionModule.isWebRelated())
        {
            result.add("" + extensionInfo.getExtensionDirectory() + "/web/webroot/WEB-INF/classes");
        }
        Set<String> allRequiredExtensions = extensionInfo.getAllRequiredExtensionNames();
        if(extensionModule.requiresBackoffice() && dependesOnBackoffice(extensionInfo))
        {
            addWebLibrariesAndClasses(this.platformConfig.getExtensionInfo("backoffice"), result);
            addBackofficeJarIfExists(extensionInfo, result);
            for(ExtensionInfo info : extensionInfo.getAllRequiredExtensionInfos())
            {
                addBackofficeJarIfExists(info, result);
            }
        }
        for(String requiredWebExtension : extensionModule.getRequiredWebExtensions())
        {
            if(allRequiredExtensions.contains(requiredWebExtension))
            {
                ExtensionInfo webExtension = this.platformConfig.getExtensionInfo(requiredWebExtension);
                if(webExtension == null)
                {
                    System.err.println(
                                    String.format("Extension module %s of extension %s requires extension %s which was not found", new Object[] {extensionModule.name(), extensionInfo.getName(), requiredWebExtension}));
                    continue;
                }
                addWebLibrariesAndClasses(webExtension, result);
            }
        }
        return result;
    }


    private void addWebLibrariesAndClasses(ExtensionInfo extensionInfo, Set<String> result)
    {
        getWebModuleLibs(extensionInfo).forEach(u -> result.add(getPath(u)));
        result.add("" + extensionInfo.getExtensionDirectory() + "/web/testclasses");
        result.add("" + extensionInfo.getExtensionDirectory() + "/web/webroot/WEB-INF/classes");
    }


    private void addBackofficeJarIfExists(ExtensionInfo extensionInfo, Set<String> result)
    {
        File backofficeJar = new File(extensionInfo.getExtensionDirectory(), "resources/backoffice/" + extensionInfo.getName() + "_bof.jar");
        if(backofficeJar.exists())
        {
            result.add(backofficeJar.getAbsolutePath());
        }
    }


    private String getPath(URL u)
    {
        try
        {
            return Paths.get(u.toURI()).toString();
        }
        catch(URISyntaxException e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    private boolean dependesOnBackoffice(ExtensionInfo extensionInfo)
    {
        for(ExtensionInfo info : extensionInfo.getAllRequiredExtensionInfos())
        {
            if(info.getName().equalsIgnoreCase("backoffice"))
            {
                return true;
            }
        }
        return false;
    }


    private void adjustExtensionModuleClasspath(ExtensionModule module, Collection<String> moduleClasspathNames, String moduleLibDir)
    {
        Set<String> moduleClasspath;
        if(this.moduleClasspaths.containsKey(module))
        {
            moduleClasspath = this.moduleClasspaths.get(module);
        }
        else
        {
            moduleClasspath = new HashSet<>();
            this.moduleClasspaths.put(module, moduleClasspath);
        }
        moduleClasspath.addAll(moduleClasspathNames);
        moduleClasspath.add(moduleLibDir);
    }


    private Iterable<URL> getExtensionModuleClasspaths(Collection<String> moduleClasspathNames)
    {
        return Iterables.transform(moduleClasspathNames, (Function)new Object(this));
    }


    private Iterable<URL> getWebModuleLibs(ExtensionInfo extensionInfo)
    {
        List<URL> result = new ArrayList<>();
        String webModuleLibDir = getWebModuleLibDir(extensionInfo);
        File libDir = new File(webModuleLibDir);
        if(libDir.exists() && libDir.isDirectory())
        {
            for(File file : libDir.listFiles())
            {
                if(file.getPath().endsWith("jar"))
                {
                    try
                    {
                        result.add(file.toURI().toURL());
                    }
                    catch(MalformedURLException e)
                    {
                        throw new IllegalStateException(e.getMessage(), e);
                    }
                }
            }
        }
        return result;
    }


    private String getWebModuleLibDir(ExtensionInfo extensionInfo)
    {
        File extDir = extensionInfo.getExtensionDirectory();
        return "" + extDir + "/web/webroot/WEB-INF/lib";
    }


    private URL toURL(String path)
    {
        try
        {
            return Paths.get(path, new String[0]).toUri().toURL();
        }
        catch(MalformedURLException e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    private Set<Class> convertToTestClasses(Set<String> testNames, ClassLoader classLoader)
    {
        Set<Class<?>> classes = new LinkedHashSet<>();
        for(String classString : testNames)
        {
            Class<?> cl = getClassForName(classString, classLoader);
            if(cl != null && isTest(cl))
            {
                classes.add(cl);
            }
        }
        return classes;
    }


    TestClassesXmlSerializer getTestClassesXmlSerializer()
    {
        return new TestClassesXmlSerializer();
    }


    private boolean isTest(Class clazz)
    {
        for(Method method : clazz.getMethods())
        {
            for(Annotation annotation : method.getAnnotations())
            {
                if("org.junit.Test".equals(annotation.annotationType().getName()))
                {
                    return true;
                }
            }
        }
        return false;
    }


    private void checkForWrongUnitTestAnnotations(Class clazz)
    {
        if(clazz.getSuperclass().getName().startsWith("de.hybris.platform.testframework"))
        {
            throw new RuntimeException("You can't use the @UnitTest annotation if you extend a hybris junit test class! Use @IntegrationTest instead if you need a platform environment!\nTest :\t\t" + clazz
                            .getName() + "\nRunner :\t" + clazz.getSuperclass().getName());
        }
        for(Annotation annotation : clazz.getAnnotations())
        {
            if(annotation.annotationType().getName().equals("org.junit.runner.RunWith"))
            {
                try
                {
                    Class annotationClazz = (Class)annotation.annotationType().getMethod("value", new Class[0]).invoke(annotation, new Object[0]);
                    if(annotationClazz.getName().startsWith("de.hybris.platform.testframework"))
                    {
                        throw new RuntimeException("You can't use the @UnitTest annotation together with a hybris junit runner! Use @IntegrationTest instead if you need a platform environment!\nTest : \t" + clazz
                                        .getName() + "\nRunner :\t" + annotationClazz.getName());
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }


    int[] getStartAndEnd(int testCount, String divider)
    {
        String[] args = getDivider(divider);
        int start = Integer.parseInt(args[0]);
        int end = Integer.parseInt(args[1]);
        int testsPerRun = (int)Math.ceil(testCount / end);
        int[] reti = new int[2];
        reti[0] = (start - 1) * testsPerRun;
        reti[1] = reti[0] + testsPerRun - 1;
        if(reti[1] >= testCount)
        {
            reti[1] = testCount - 1;
        }
        return reti;
    }


    private String[] getDivider(String divider)
    {
        return divider.split("-");
    }


    public boolean matchesPackageFilters(Class clazz)
    {
        Collection packageFiltersPositive = (Collection)this.filters.get(FILTER.FILTER_BY_PACKAGE);
        Collection packageFiltersNegative = (Collection)this.filters.get(FILTER.FILTER_BY_PACKAGE_NEGATIVE);
        boolean foundInInclude = false;
        if(packageFiltersPositive != null && !packageFiltersPositive.isEmpty())
        {
            for(Object packageFilterObject : packageFiltersPositive)
            {
                String packageFilter = (String)packageFilterObject;
                if(clazz.getName().toLowerCase(Locale.getDefault())
                                .matches(RegexUtil.wildcardToRegex(packageFilter).toLowerCase(Locale.getDefault())))
                {
                    foundInInclude = true;
                    break;
                }
            }
        }
        else
        {
            foundInInclude = true;
        }
        if(foundInInclude)
        {
            if(packageFiltersNegative != null && !packageFiltersNegative.isEmpty())
            {
                for(Object packageFilterObject : packageFiltersNegative)
                {
                    String packageFilter = (String)packageFilterObject;
                    if(clazz.getName().toLowerCase(Locale.getDefault())
                                    .matches(RegexUtil.wildcardToRegex(packageFilter).toLowerCase(Locale.getDefault())))
                    {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }


    private Class getClassForName(String classPath, ClassLoader classLoader)
    {
        if(isGroovyTest(classPath))
        {
            return getClassFromClassLoader(classPath.substring(0, classPath.lastIndexOf(".groovy")), classLoader);
        }
        return getClassFromClassLoader(classPath, classLoader);
    }


    private Class getClassFromClassLoader(String classPath, ClassLoader cl)
    {
        try
        {
            return Class.forName(classPath, false, cl);
        }
        catch(ClassNotFoundException e)
        {
            log("Did not find class for " + classPath + " - Ignoring class.");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    private boolean isGroovyTest(String name)
    {
        return name.endsWith(".groovy");
    }


    private void log(String string)
    {
        if(this.logging)
        {
            System.out.println(string);
        }
    }


    public void setLogging(boolean logging)
    {
        this.logging = logging;
    }


    public boolean matchesPackageFilters(String string)
    {
        return matchesPackageFilters(getClassForName(string, this.classLoader));
    }
}

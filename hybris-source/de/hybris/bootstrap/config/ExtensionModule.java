package de.hybris.bootstrap.config;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public enum ExtensionModule
{
    STANDARD(".", false, "", ei -> Boolean.TRUE, new String[0]),
    WEB("web", true, "web", ExtensionModule::webModuleExists, new String[] {"backoffice"}),
    HAC("hac", true, "hac", ExtensionModule::hacModuleExists, new String[] {"hac"}),
    HMC("hac", true, "hac", ExtensionModule::hacModuleExists, new String[] {"hac"});


    static
    {
        HMC = new ExtensionModule("HMC", 2, "hmc", true, "hmc", ei -> Boolean.valueOf((ei.getHMCModule() != null)), new String[] {"hmc"});
    }

    private final String modulePath;
    private final Function<ExtensionInfo, Boolean> moduleExistenceCheck;
    private final boolean requiresWebLibraries;
    private final String testClassesFilePrefix;
    private final Set<String> requiredWebExtensions;


    ExtensionModule(String modulePath, boolean requiresWebLibraries, String testClassesFilePrefix, Function<ExtensionInfo, Boolean> moduleExistenceCheck, String... requiredWebExtensions)
    {
        this.modulePath = modulePath;
        this.requiresWebLibraries = requiresWebLibraries;
        this.testClassesFilePrefix = testClassesFilePrefix;
        this.moduleExistenceCheck = moduleExistenceCheck;
        this.requiredWebExtensions = new HashSet<>(Arrays.asList(requiredWebExtensions));
    }


    protected static boolean webModuleExists(ExtensionInfo extensionInfo)
    {
        return (shouldBeLoadedForWebtests(extensionInfo) && (extensionInfo.getWebModule() != null || containsBackofficeTests(extensionInfo)));
    }


    protected static boolean containsBackofficeTests(ExtensionInfo extensionInfo)
    {
        return (new File(new File(extensionInfo.getExtensionDirectory(), "backoffice"), "testsrc")).exists();
    }


    protected static boolean hacModuleExists(ExtensionInfo extensionInfo)
    {
        String metaHacModule = extensionInfo.getMeta("hac-module");
        return Boolean.parseBoolean(metaHacModule);
    }


    protected static boolean shouldBeLoadedForWebtests(ExtensionInfo extensionInfo)
    {
        String doNotLoadForWebTests = extensionInfo.getMeta("do-not-load-for-webtests");
        return !Boolean.parseBoolean(doNotLoadForWebTests);
    }


    public boolean exists(ExtensionInfo extensionInfo)
    {
        return ((Boolean)this.moduleExistenceCheck.apply(extensionInfo)).booleanValue();
    }


    public File getBasePath(ExtensionInfo extensionInfo)
    {
        return new File(extensionInfo.getExtensionDirectory(), this.modulePath);
    }


    public String getTestClassFileName()
    {
        return this.testClassesFilePrefix + "testclasses";
    }


    public boolean requiresBackoffice()
    {
        return this.requiredWebExtensions.contains("backoffice");
    }


    public Collection<String> getRequiredWebExtensions()
    {
        return Collections.unmodifiableSet(this.requiredWebExtensions);
    }


    public boolean isWebRelated()
    {
        return this.requiresWebLibraries;
    }
}

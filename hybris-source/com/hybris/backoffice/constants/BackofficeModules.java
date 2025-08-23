package com.hybris.backoffice.constants;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.platform.util.Utilities;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class BackofficeModules
{
    private static final PlatformConfig PLATFORM_CONFIG = ConfigUtil.getPlatformConfig(Utilities.class);
    private static final String SPRING_XML_SUFFIX = "spring.xml";
    private static final String WIDGETS_XML_SUFFIX = "widgets.xml";
    private static final String CONFIG_XML_SUFFIX = "config.xml";
    private static final String FILE_CONVENTION_SEPARATOR = "-";


    private BackofficeModules()
    {
        throw new AssertionError("Utilities class should not be instantiated");
    }


    public static List<ExtensionInfo> getBackofficeModules()
    {
        return (List<ExtensionInfo>)PLATFORM_CONFIG.getExtensionInfosInBuildOrder().stream()
                        .filter(ext -> Boolean.parseBoolean(ext.getMeta("backoffice-module")))
                        .collect(Collectors.toList());
    }


    public static List<String> getBackofficeModulesNames()
    {
        return (List<String>)getBackofficeModules().stream().map(ExtensionInfo::getName).collect(Collectors.toList());
    }


    public static Optional<ExtensionInfo> getBackofficeModule(String moduleName)
    {
        ExtensionInfo extensionInfo = PLATFORM_CONFIG.getExtensionInfo(moduleName);
        if(extensionInfo != null && Boolean.parseBoolean(extensionInfo.getMeta("backoffice-module")))
        {
            return Optional.of(extensionInfo);
        }
        return Optional.empty();
    }


    public static String getModuleFileName(String moduleName, String suffix)
    {
        StringBuilder fileName = (new StringBuilder(moduleName)).append("-").append("backoffice");
        if(!suffix.startsWith("-"))
        {
            fileName.append("-");
        }
        fileName.append(suffix);
        return fileName.toString();
    }


    public static File getModuleFile(ExtensionInfo extension, String suffix)
    {
        return new File(extension.getItemsXML().getParent(), getModuleFileName(extension.getName(), suffix));
    }


    public static File getSpringDefinitionsFile(ExtensionInfo extension)
    {
        return getModuleFile(extension, "spring.xml");
    }


    public static File getConfigXmlFile(ExtensionInfo extension)
    {
        return getModuleFile(extension, "config.xml");
    }


    public static File getWidgetsXmlFile(ExtensionInfo extension)
    {
        return getModuleFile(extension, "widgets.xml");
    }
}

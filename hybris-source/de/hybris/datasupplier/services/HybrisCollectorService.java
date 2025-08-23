package de.hybris.datasupplier.services;

public interface HybrisCollectorService
{
    String getClusterId();


    String isClusterModeEnabled();


    String getExtensionsString();


    String getPlatformHomeDirectory();


    String getDataDirectory();


    String getTempDirectory();


    String getLogDirectory();


    String getBinDirectory();


    String getConfigDirectory();


    String getInstallDirectory();


    String getServerLogDirectory();


    String getSystemHome();


    String getHybrisVersion();


    String getJavaVersion();


    String getTomcatLocalIpAddress();
}

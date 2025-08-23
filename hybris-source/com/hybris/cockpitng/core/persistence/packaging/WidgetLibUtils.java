/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.packaging;

import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.persistence.impl.XMLWidgetPersistenceService;
import com.hybris.cockpitng.core.persistence.packaging.impl.WidgetLibHelper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility methods dealing with cockpit widget arcives. For internal use only.
 */
public interface WidgetLibUtils
{
    void createComposedWidgetJarArchive(WidgetDefinition definition, List<SocketWrapper> socketWrapperList,
                    XMLWidgetPersistenceService xmlBasedWidgetPersistenceService) throws IOException;


    void uploadJarFromStream(String name, InputStream streamData) throws IOException;


    Collection<WidgetJarLibInfo> getAllJarLibInfos();


    default Collection<WidgetJarLibInfo> getModuleJarLibInfos(final File moduleJar)
    {
        return getAllJarLibInfos().stream().filter(info -> info.getJarPath().equals(moduleJar)).collect(Collectors.toList());
    }


    WidgetJarLibInfo getWidgetJarLibInfo(String widgetID);


    File getWidgetJarLibDir();


    default Properties loadLibProps()
    {
        return loadLibProps(new File(getWidgetJarLibDir(), WidgetLibConstants.DEPLOYED_SUBFOLDER_NAME));
    }


    default String libDirAbsolutePath()
    {
        String ret = StringUtils.EMPTY;
        File widgetLibDir = getWidgetJarLibDir();
        if(widgetLibDir != null)
        {
            ret = StringUtils.join(widgetLibDir.toString(), File.separator, WidgetLibConstants.DEPLOYED_SUBFOLDER_NAME);
        }
        return ret;
    }


    Properties loadLibProps(File libDir);


    void storeLibProps(Properties props, File libDir);


    boolean fillPropertiesFromXml(String widgetFile, InputStream resourceAsStream, Properties props);


    List<WidgetJarLibInfo> loadAllWidgetJarLibInfos();


    List<WidgetJarLibInfo> loadAllWidgetJarLibInfos(File file);


    /**
     * @return Cockpit directory where the cockpit configuration data is stored, cf.
     *         {@link com.hybris.cockpitng.core.persistence.packaging.impl.DefaultWidgetLibUtils#rootDirectory}.
     */
    File getRootDir();


    boolean isResourceCacheEnabled();


    default Function<String, String>[] getDirProcessors()
    {
        return WidgetLibHelper.getDirProcessors();
    }
}

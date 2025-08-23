/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.impl;

import com.hybris.cockpitng.core.persistence.packaging.WidgetJarLibInfo;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibConstants;
import com.hybris.cockpitng.core.persistence.packaging.WidgetLibUtils;
import java.io.File;
import java.util.Objects;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Helper class for working with requests.
 */
public final class WidgetRequestUtils
{
    private WidgetRequestUtils()
    {
        //Utility class
    }


    /**
     * Cleans request URI from all elements that are unnecessary to find a widget's resource
     *
     * @param fullURI
     *           full request URI
     * @return URI without any unnecessary additions
     */
    public static String cleanupRequestUri(final String fullURI)
    {
        String result = fullURI;
        result = StringUtils.remove(FilenameUtils.separatorsToUnix(FilenameUtils.normalize(result)),
                        "/" + WidgetLibConstants.CLASSPATH_RESOURCES_PATH_PREFIX);
        // remove non-conform url stuff like ";jsessionid="
        final int indexOf = result.indexOf(';');
        if(indexOf > 0)
        {
            result = result.substring(0, indexOf);
        }
        return result;
    }


    /**
     * Extracts information about widget and its resource from request
     *
     * @param splitRequestURI
     *           request split by /
     * @param startIndex
     *           starting index in splitRequestURI, from which an extraction should start
     * @return two elements array: [widgetID, resourceName]
     */
    public static String[] parseRequestURI(final String[] splitRequestURI, final int startIndex)
    {
        final String widgetID = splitRequestURI[startIndex];
        final StringBuilder resourceName = new StringBuilder(splitRequestURI[startIndex + 1]);
        for(int i = startIndex + 2; i < splitRequestURI.length; i++)
        {
            resourceName.append("/").append(splitRequestURI[i]);
        }
        return new String[]
                        {widgetID, resourceName.toString()};
    }


    /**
     * Extracts information about widget and its resource from request
     *
     * @param widgetLibUtils
     *           {@link WidgetLibUtils}
     * @param requestURI
     *           requested URI after cleanup ({@link #cleanupRequestUri(String)})
     * @return two elements array: [root, widgetID, resourceName]
     */
    public static WidgetRequest parseRequestURI(final WidgetLibUtils widgetLibUtils, final String requestURI)
    {
        String root = null;
        String widgetID = null;
        File jarFile = null;
        String resourceName = null;
        final String[] split = StringUtils.split(requestURI, '/');
        int index = 0;
        for(final String string : split)
        {
            if(WidgetLibConstants.JAR_RESOURCES_PATH_PREFIX.equals(string)
                            || WidgetLibConstants.JAR_ROOT_RESOURCE_PATH_PREFIX.equals(string))
            {
                final String[] parsedRequestURI = parseRequestURI(split, index + 1);
                widgetID = parsedRequestURI[0];
                resourceName = parsedRequestURI[1];
                final WidgetJarLibInfo widgetJarLibInfo = widgetLibUtils.getWidgetJarLibInfo(widgetID);
                if(!resourceName.startsWith("/"))
                {
                    resourceName = "/" + resourceName;
                }
                if(widgetJarLibInfo != null)
                {
                    jarFile = widgetJarLibInfo.getJarPath();
                    widgetID = widgetJarLibInfo.getId();
                }
                else if(WidgetLibConstants.JAR_ROOT_RESOURCE_PATH_PREFIX.equals(string))
                {
                    for(final Object path : widgetLibUtils.loadLibProps().keySet())
                    {
                        if(Objects.toString(path).endsWith(widgetID))
                        {
                            jarFile = new File(Objects.toString(path));
                            widgetID = null;
                            break;
                        }
                    }
                }
                break;
            }
            root = root == null ? string : root.concat("/").concat(string);
            index++;
        }
        if(jarFile != null)
        {
            final WidgetRequest result = new WidgetRequest(root, jarFile, resourceName);
            result.setWidgetId(widgetID);
            return result;
        }
        return null;
    }
}

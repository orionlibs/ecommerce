/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence.packaging.impl;

import static com.hybris.cockpitng.core.persistence.packaging.WidgetLibConstants.CONSTANT_TEMP_DIR;
import static com.hybris.cockpitng.core.persistence.packaging.WidgetLibConstants.CONSTANT_USER_HOME;

import java.io.File;
import java.util.function.Function;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class WidgetLibHelper
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WidgetLibHelper.class);
    private static final String DIR_COCKPIT = ".cockpit";


    private WidgetLibHelper()
    {
        // utility class
    }


    public static File getRootDir(final String configuredRootDir, final Function<String, String>... processors)
    {
        String root = FileUtils.getUserDirectoryPath().concat(File.separator + DIR_COCKPIT);
        if(configuredRootDir != null)
        {
            root = Stream.of(processors).reduce(configuredRootDir, (before, processor) -> processor.apply(before),
                            (before, after) -> after);
            if(root.contains("${"))
            {
                LOGGER.error("Could not resolve variable in path {}", root);
            }
        }
        if(root != null)
        {
            root = FilenameUtils.normalize(root);
        }
        if(root == null)
        {
            final String errorMessage = "root directory path is null";
            LOGGER.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        }
        final File rootDir = new File(root);
        final boolean dirCreated = rootDir.mkdirs();
        if(dirCreated)
        {
            LOGGER.debug("Directory created: {}", rootDir.getAbsolutePath());
        }
        return rootDir;
    }


    public static Function<String, String>[] getDirProcessors()
    {
        return new Function[]
                        {dir -> ((String)dir).replace(CONSTANT_USER_HOME, FileUtils.getUserDirectoryPath()),
                                        dir -> ((String)dir).replace(CONSTANT_TEMP_DIR, FileUtils.getTempDirectoryPath())};
    }
}

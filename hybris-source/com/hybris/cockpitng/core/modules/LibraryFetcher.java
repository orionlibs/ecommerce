/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.modules;

import com.hybris.cockpitng.core.CockpitApplicationException;
import java.io.File;

/**
 * An interface of object capable of fetching modules source code into specified archived library file.
 */
public interface LibraryFetcher
{
    /**
     * Checks if specified module can be fetched by this fetcher.
     * <P>
     * There are some modules that are i.e. injected directly into classpath and does not need any additional fetching. Such
     * modules should still be handled by system, yet not fetched (as they are in fact already fetched). For such situations
     * fetcher should return <code>false</code>.
     * </P>
     *
     * @param moduleInfo
     *           module to be fetched
     * @return <code>true</code> if module is not yet fetched and requires fetching
     */
    boolean canFetchLibrary(ModuleInfo moduleInfo);


    /**
     * Fetches provided module into specified archive.
     *
     * @param moduleInfo
     *           module information
     * @param archiveFile
     *           destination file
     * @throws CockpitApplicationException
     */
    void fetchLibrary(ModuleInfo moduleInfo, File archiveFile) throws CockpitApplicationException;
}

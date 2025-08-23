/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.modules;

import java.util.Collection;
import java.util.Collections;

/**
 * A cockpit module description, containing all necessary info to access a cockpit module.
 */
public class ModuleEntry
{
    private String url;
    private boolean enabled;
    private String label;
    private boolean alive;
    private boolean isUpToDate;
    private Collection<String> parentModuleUrls = Collections.emptyList();


    /**
     * @return The url where the module descriptor can be accessed.
     */
    public String getUrl()
    {
        return url;
    }


    /**
     * Corresponding setter for {@link #getUrl()}.
     */
    public void setUrl(final String url)
    {
        this.url = url;
    }


    /**
     * @return True, if the module should be enabled.
     */
    public boolean isEnabled()
    {
        return enabled;
    }


    /**
     * Corresponding setter for {@link #isEnabled()}.
     */
    public void setEnabled(final boolean enabled)
    {
        this.enabled = enabled;
    }


    /**
     * @return The display label for the module.
     */
    public String getLabel()
    {
        return label;
    }


    /**
     * Corresponding setter for {@link #getLabel()}.
     */
    public void setLabel(final String label)
    {
        this.label = label;
    }


    /**
     * @return true if service/module is alive
     */
    public boolean isAlive()
    {
        return alive;
    }


    /**
     * @param alive
     *           the alive flag to set
     */
    public void setAlive(final boolean alive)
    {
        this.alive = alive;
    }


    /**
     * @return true if service is up to date
     */
    public boolean isUpToDate()
    {
        return isUpToDate;
    }


    /**
     * @param isUpToDate
     *           setter for up to date flag
     */
    public void setUpToDate(final boolean isUpToDate)
    {
        this.isUpToDate = isUpToDate;
    }


    /**
     * @return collection of parent modules i.e. all modules that should preceede the current module in case of in-order
     *         processing.
     */
    public Collection<String> getParentModuleUrls()
    {
        return parentModuleUrls;
    }


    /**
     * @param parentModuleUrls
     *           collection of parent modules urls i.e. all modules that should preceede the current module in case of
     *           in-order processing.
     */
    public void setParentModuleUrls(final Collection<String> parentModuleUrls)
    {
        this.parentModuleUrls = parentModuleUrls;
    }
}

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.jaxb.impl;

import com.google.common.collect.Sets;
import com.hybris.cockpitng.core.util.jaxb.SchemaLocationRegistry;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * Default implementation that contains all cockpit configuration related schema locations.
 * <p>
 * Note: Please see {@link com.hybris.cockpitng.core.config.CockpitConfigurationService} for details.
 */
public class CockpitConfigurationSchemaLocationRegistry implements SchemaLocationRegistry
{
    private Map<String, String> schemaLocations;


    public void setSchemaLocations(final Map<String, String> schemaLocations)
    {
        this.schemaLocations = schemaLocations;
    }


    public Set<String> getSchemaLocations()
    {
        return Sets.newLinkedHashSet(this.schemaLocations.values());
    }


    public String getSchemaLocation(final String id)
    {
        String ret = StringUtils.EMPTY;
        if(this.schemaLocations.containsKey(id))
        {
            ret = this.schemaLocations.get(id);
        }
        return ret;
    }
}

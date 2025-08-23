/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.persistence;

import com.hybris.cockpitng.core.persistence.impl.jaxb.Import;
import com.hybris.cockpitng.core.persistence.impl.jaxb.Requirement;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

/**
 * An interface capable of interpret configuration node and extract required elements from it
 */
public interface ConfigurationInterpreter<C>
{
    C load(final Import importConfiguration, final InputStream configurationStream) throws IOException;


    List<Import> getImports(final C configuration);


    List<Requirement> getRequirements(final C configuration);


    Set<String> getRequiredParameters(final C configuration);


    C merge(final Import importConfiguration, final C target, final C source);
}

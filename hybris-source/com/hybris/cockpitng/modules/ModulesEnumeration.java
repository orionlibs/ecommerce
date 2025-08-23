/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.modules;

import com.hybris.cockpitng.core.modules.ModuleInfo;
import java.util.Enumeration;

/**
 * Enumerator of cockpit modules
 */
public interface ModulesEnumeration extends Enumeration<ModuleInfo>
{
    void reset();
}

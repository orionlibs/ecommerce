/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.user.cache;

import java.util.function.Function;

public interface BackofficeCockpitUserServiceCache
{
    boolean isAdmin(String userId, Function<String, Boolean> defaultValue);
}

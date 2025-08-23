/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.dataaccess.facades.permissions;

public interface ReadPermissionCache
{
    boolean canReadType(String typeCode);


    boolean canReadAttribute(String typeCode, String attribute);
}

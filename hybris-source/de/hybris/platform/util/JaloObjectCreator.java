package de.hybris.platform.util;

import de.hybris.platform.core.Tenant;

public interface JaloObjectCreator
{
    BridgeAbstraction createInstance(Tenant paramTenant, BridgeInterface paramBridgeInterface);
}

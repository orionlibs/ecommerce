package de.hybris.platform.promotionengineservices.util;

import java.util.UUID;

public class ActionUtils
{
    public String createActionUUID()
    {
        return "Action[" + UUID.randomUUID().toString() + "]";
    }


    public boolean isActionUUID(String guid)
    {
        return guid.matches("^Action\\[[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}]$");
    }
}

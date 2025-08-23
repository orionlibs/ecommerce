package de.hybris.platform.util;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.platform.core.MasterTenant;
import java.util.Properties;
import org.apache.velocity.app.Velocity;

public class VelocityHelper
{
    public static void init()
    {
        Properties p = new Properties();
        ConfigUtil.loadVelocityProperties(p, MasterTenant.class);
        Velocity.init(p);
    }
}

package de.hybris.bootstrap.util;

import de.hybris.bootstrap.config.BootstrapConfigException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.velocity.app.Velocity;

public class VelocityHelper
{
    public static void init()
    {
        Properties p = new Properties();
        try
        {
            InputStream stream = VelocityHelper.class.getResourceAsStream("/velocity.properties");
            try
            {
                p.load(stream);
                Velocity.init(p);
                if(stream != null)
                {
                    stream.close();
                }
            }
            catch(Throwable throwable)
            {
                if(stream != null)
                {
                    try
                    {
                        stream.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            throw new BootstrapConfigException("Error during Velocity initialization", e);
        }
    }
}

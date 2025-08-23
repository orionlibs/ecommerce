package de.hybris.platform.commons.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;

public class VelocityFormatter extends GeneratedVelocityFormatter
{
    private static final Logger log = Logger.getLogger(VelocityFormatter.class);


    public Media format(Item item)
    {
        File tempfile = null;
        FileWriter fw = null;
        try
        {
            tempfile = File.createTempFile("velocityfile_temp", ".txt");
            VelocityContext context = new VelocityContext();
            context.put("this", item);
            fw = new FileWriter(tempfile);
            Velocity.evaluate((Context)context, fw, "VelocityFormatter", new InputStreamReader(getDataFromStream()));
            fw.close();
            Media ret = MediaManager.getInstance().createMedia("Velocity2Media-" + System.currentTimeMillis());
            ret.setFile(tempfile);
            return ret;
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e, "Could not initialize velocity engine!", -1);
        }
        finally
        {
            if(fw != null)
            {
                try
                {
                    fw.close();
                }
                catch(Exception exception)
                {
                }
            }
            if(tempfile != null && tempfile.exists())
            {
                try
                {
                    tempfile.delete();
                }
                catch(Exception e)
                {
                    if(log.isDebugEnabled())
                    {
                        log.debug(e.getMessage());
                    }
                }
            }
        }
    }
}

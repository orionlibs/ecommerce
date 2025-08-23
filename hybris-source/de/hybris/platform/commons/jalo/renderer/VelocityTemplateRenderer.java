package de.hybris.platform.commons.jalo.renderer;

import de.hybris.platform.jalo.media.Media;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;

@Deprecated(since = "ages", forRemoval = false)
public class VelocityTemplateRenderer implements Renderer
{
    private static final Logger LOG = Logger.getLogger(VelocityTemplateRenderer.class.getName());


    public void render(RendererTemplate template, Object context, Reader src, Writer output)
    {
        try
        {
            Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(template.getContextClass());
            if(!clazz.isAssignableFrom(context.getClass()))
            {
                String warn = "The context class [" + context.getClass().getName() + "] is not correctly defined.";
                LOG.error(warn);
                output.write(warn);
                return;
            }
            Media media = template.getContent();
            if(media == null)
            {
                String warn = "no content found for template " + template.getCode();
                LOG.error(warn);
                output.write(warn);
                return;
            }
            InputStream inputStream = template.getContent().getDataFromStream();
            try
            {
                Reader velocityReader = new InputStreamReader(getVelocityStream(inputStream, context), "utf-8");
                try
                {
                    IOUtils.copy(velocityReader, output);
                    velocityReader.close();
                }
                catch(Throwable throwable)
                {
                    try
                    {
                        velocityReader.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                    throw throwable;
                }
                if(inputStream != null)
                {
                    inputStream.close();
                }
            }
            catch(Throwable throwable)
            {
                if(inputStream != null)
                {
                    try
                    {
                        inputStream.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    private InputStream getVelocityStream(InputStream inputStream, Object context)
    {
        InputStream result = null;
        VelocityContext ctx = new VelocityContext();
        ctx.put("ctx", context);
        try
        {
            Reader reader = new InputStreamReader(inputStream, "UTF-8");
            File tmpFile = File.createTempFile("xml2pdf-", ".xml");
            tmpFile.deleteOnExit();
            FileOutputStream fileOutputStream = new FileOutputStream(tmpFile);
            try
            {
                OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, "UTF-8");
                try
                {
                    Velocity.evaluate((Context)ctx, writer, getClass().getName(), reader);
                    writer.flush();
                    result = new FileInputStream(tmpFile);
                    writer.close();
                }
                catch(Throwable throwable)
                {
                    try
                    {
                        writer.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                    throw throwable;
                }
                fileOutputStream.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    fileOutputStream.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }
}

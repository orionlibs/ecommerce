package de.hybris.platform.commons.jalo.renderer;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.util.localization.Localization;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class RendererTemplate extends GeneratedRendererTemplate
{
    public static final String CONTEXT_CLASS_NOT_FOUND = "type.renderertemplate.contextclass.notfound";
    private static final Logger log = Logger.getLogger(RendererTemplate.class.getName());


    @Deprecated(since = "ages", forRemoval = false)
    @ForceJALO(reason = "abstract method implementation")
    public Map getAllTemplateScript(SessionContext ctx)
    {
        Map<Object, Object> map = new HashMap<>();
        Language sessionLanguage = ctx.getLanguage();
        for(Language l : C2LManager.getInstance().getAllLanguages())
        {
            ctx.setLanguage(l);
            map.put(l, getTemplateScript(ctx));
        }
        ctx.setLanguage(sessionLanguage);
        return map;
    }


    @Deprecated(since = "ages", forRemoval = false)
    @ForceJALO(reason = "abstract method implementation")
    public void setAllTemplateScript(SessionContext ctx, Map value)
    {
        Language sessionLanguage = ctx.getLanguage();
        for(Language l : value.keySet())
        {
            ctx.setLanguage(l);
            setTemplateScript(ctx, (String)value.get(l));
        }
        ctx.setLanguage(sessionLanguage);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @ForceJALO(reason = "abstract method implementation")
    public String getTemplateScript(SessionContext ctx)
    {
        Map<Language, Media> contents = getAllContent(ctx);
        Media content = contents.get(ctx.getLanguage());
        if(content == null)
        {
            return null;
        }
        if(!content.hasData() && content.getURL() == null)
        {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new InputStreamReader(content.getDataFromStreamSure(), "utf-8"));
            boolean first = true;
            String s;
            for(s = reader.readLine(); s != null; s = reader.readLine())
            {
                if(!first)
                {
                    builder.append("\n");
                }
                else
                {
                    first = false;
                }
                builder.append(s);
            }
            s = builder.toString();
            return s;
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
        catch(IOException e)
        {
            throw new JaloSystemException(e);
        }
        finally
        {
            if(reader != null)
            {
                try
                {
                    reader.close();
                }
                catch(IOException e)
                {
                    if(log.isDebugEnabled())
                    {
                        log.debug(e.getMessage());
                    }
                }
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    @ForceJALO(reason = "abstract method implementation")
    public void setTemplateScript(SessionContext ctx, String templateValue)
    {
        Map<Language, Media> contents = getAllContent(ctx);
        Media content = contents.get(ctx.getLanguage());
        if(templateValue == null || templateValue.length() == 0)
        {
            try
            {
                if(content != null)
                {
                    content.removeData(false);
                }
            }
            catch(JaloBusinessException e)
            {
                throw new JaloSystemException(e);
            }
        }
        else
        {
            try
            {
                ByteArrayOutputStream bos = new ByteArrayOutputStream(65536);
                OutputStreamWriter writer = new OutputStreamWriter(bos, "utf-8");
                writer.write(templateValue);
                writer.flush();
                if(content != null)
                {
                    content.setData(new DataInputStream(new ByteArrayInputStream(bos.toByteArray())), content.getRealFileName(), content
                                    .getMime());
                }
                else
                {
                    Media newMedia = MediaManager.getInstance().createMedia(getCode() + "_" + getCode());
                    newMedia.setRealFileName(getCode() + "_" + getCode());
                    newMedia.setMime("plain/text");
                    setContent(ctx, newMedia);
                    getContent(ctx).setData(new DataInputStream(new ByteArrayInputStream(bos.toByteArray())), newMedia
                                    .getRealFileName(), getOutputMimeType());
                }
            }
            catch(IOException e)
            {
                throw new JaloSystemException(e);
            }
            catch(JaloBusinessException e)
            {
                throw new JaloSystemException(e);
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    @ForceJALO(reason = "abstract method implementation")
    public String getContextClassDescription(SessionContext ctx)
    {
        String contextClassStr = getContextClass();
        StringBuffer result = new StringBuffer();
        if(contextClassStr != null && !contextClassStr.equals(""))
        {
            Boolean contextClassFound = Boolean.TRUE;
            Class<?> contextClass = null;
            try
            {
                contextClass = Class.forName(contextClassStr);
            }
            catch(ClassNotFoundException e)
            {
                contextClassFound = Boolean.FALSE;
            }
            if(contextClassFound.booleanValue() && contextClass != null)
            {
                Method[] methods = contextClass.getMethods();
                for(int i = 0; i < methods.length; i++)
                {
                    Method method = methods[i];
                    String methodName = method.getName();
                    if(methodName.startsWith("get") && methodName.length() > 3)
                    {
                        result.append(methodName
                                        .substring(3, 4).toLowerCase() + methodName.substring(3, 4).toLowerCase() + "  ");
                    }
                    else if(methodName.startsWith("is") && methodName.length() > 2)
                    {
                        result.append(methodName
                                        .substring(2, 3).toLowerCase() + methodName.substring(2, 3).toLowerCase() + "  ");
                    }
                }
            }
            else
            {
                result.append(Localization.getLocalizedString("type.renderertemplate.contextclass.notfound", (Object[])new String[] {contextClassStr}));
            }
        }
        return result.toString();
    }
}

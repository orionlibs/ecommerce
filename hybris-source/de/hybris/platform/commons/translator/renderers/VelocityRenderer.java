package de.hybris.platform.commons.translator.renderers;

import de.hybris.platform.commons.translator.Translator;
import de.hybris.platform.commons.translator.nodes.AbstractNode;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;

public class VelocityRenderer extends AbstractRenderer
{
    protected String templateName;
    protected String template;
    private static final String FORCED_LINEBREAK = String.valueOf((char)Integer.parseInt("000A", 16));
    private static final String PARAGRAPH_BREAK = String.valueOf((char)Integer.parseInt("000D", 16)) + String.valueOf((char)Integer.parseInt("000D", 16));


    public synchronized String renderTextFromNode(AbstractNode node, Translator translator)
    {
        VelocityContext context = new VelocityContext();
        String paragraphBreak = this.properties.getProperty("PARAGRAPH_BREAK");
        if(paragraphBreak == null)
        {
            this.properties.put("PARAGRAPH_BREAK", PARAGRAPH_BREAK);
        }
        else
        {
            paragraphBreak = paragraphBreak.replace("\\r", "\r").replace("\\n", "\n").replace("\\f", "\f").replace("\\t", "\t");
            this.properties.put("PARAGRAPH_BREAK", paragraphBreak);
        }
        String forcedLineBreak = this.properties.getProperty("FORCED_LINEBREAK");
        if(forcedLineBreak == null)
        {
            this.properties.put("FORCED_LINEBREAK", FORCED_LINEBREAK);
        }
        else
        {
            forcedLineBreak = forcedLineBreak.replace("\\r", "\r").replace("\\n", "\n").replace("\\f", "\f").replace("\\t", "\t");
            this.properties.put("FORCED_LINEBREAK", forcedLineBreak);
        }
        Enumeration<?> enumeration = this.properties.propertyNames();
        while(enumeration.hasMoreElements())
        {
            String key = (String)enumeration.nextElement();
            context.put(key, this.properties.getProperty(key));
        }
        HashMap<String, Object> contextProperties = translator.getContextProperties();
        for(Map.Entry<String, Object> entry : contextProperties.entrySet())
        {
            context.put(entry.getKey(), entry.getValue());
        }
        context.put("translator", translator);
        context.put("node", node);
        context.put("utility", new IndesignUtility(this.properties));
        String className = "";
        if(node.getAttributes() != null && node.getAttributes().get("class") != null)
        {
            className = (String)node.getAttributes().get("class");
        }
        context.put("class", className);
        if(this.template == null || this.template.equals(""))
        {
            loadTemplateFromFile();
        }
        if(this.template == null || this.template.equals(""))
        {
            loadTemplateFromURL();
        }
        if(this.template != null && this.properties != null && "true".equals(this.properties.get("ReplaceAllNewLinesSymbols")))
        {
            this.template = this.template.replace("\n", "");
            this.template = this.template.replace("\r", "");
        }
        StringWriter writer = new StringWriter();
        String retString = null;
        try
        {
            Velocity.evaluate((Context)context, writer, "LOG", this.template);
            retString = writer.toString();
        }
        catch(Exception ex)
        {
            throw new IllegalStateException("Velocity evaluation exception!", ex);
        }
        finally
        {
            IOUtils.closeQuietly(writer);
        }
        return retString;
    }


    public synchronized void loadTemplateFromURL()
    {
        InputStream inputStream = null;
        try
        {
            this.template = null;
            URL url = new URL(getTemplateName());
            URLConnection conn = url.openConnection();
            conn.connect();
            inputStream = conn.getInputStream();
            this.template = IOUtils.toString(inputStream, "UTF-8");
        }
        catch(IOException e)
        {
            throw new IllegalArgumentException("Cannot load template from url '" +
                            getTemplateName() + "' due to " + e.getMessage() + ".", e);
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
        }
    }


    public synchronized void loadTemplateFromFile()
    {
        InputStream inputStream = null;
        try
        {
            inputStream = Translator.class.getClassLoader().getResourceAsStream(getTemplateName());
            if(inputStream == null)
            {
                throw new IllegalArgumentException("cannt find resource '" + getTemplateName() + "' via " + Translator.class
                                .getClassLoader());
            }
            this.template = IOUtils.toString(inputStream, "UTF-8");
        }
        catch(IOException e)
        {
            throw new IllegalArgumentException("Cannot load template '" + getTemplateName() + "' due to " + e.getMessage() + ".", e);
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
        }
    }


    public String getTemplateName()
    {
        return this.templateName;
    }


    public synchronized void setTemplateName(String templateName)
    {
        this.templateName = templateName;
    }


    public String getTemplate()
    {
        return this.template;
    }


    public synchronized void setTemplate(String template)
    {
        this.template = template;
    }
}

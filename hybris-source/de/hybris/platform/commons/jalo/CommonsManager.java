package de.hybris.platform.commons.jalo;

import de.hybris.platform.commons.constants.GeneratedCommonsConstants;
import de.hybris.platform.commons.jalo.renderer.Renderer;
import de.hybris.platform.commons.jalo.renderer.RendererTemplate;
import de.hybris.platform.commons.jalo.translator.JaloTranslatorConfiguration;
import de.hybris.platform.commons.jalo.translator.JaloVelocityRenderer;
import de.hybris.platform.commons.jalo.translator.RenderersProperty;
import de.hybris.platform.commons.translator.RenderersFactory;
import de.hybris.platform.commons.translator.RenderersFactoryFromFile;
import de.hybris.platform.commons.translator.TranslatorConfiguration;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloImplementationManager;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.JaloObjectCreator;
import de.hybris.platform.util.Utilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;

public class CommonsManager extends GeneratedCommonsManager
{
    private static final Logger log = Logger.getLogger(CommonsManager.class.getName());
    private volatile VelocityEngine velocityEngine = null;


    public static CommonsManager getInstance()
    {
        return (CommonsManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager().getExtension("commons");
    }


    public static String getJarFileAsString(String filename, String encoding) throws JaloInvalidParameterException, JaloItemNotFoundException, IOException, UnsupportedEncodingException
    {
        if(filename == null)
        {
            throw new JaloInvalidParameterException("filename is not specified.", 0);
        }
        InputStream in = CommonsManager.class.getResourceAsStream(filename);
        if(in == null)
        {
            throw new JaloItemNotFoundException("Did not find file for filename [" + filename + "]", 0);
        }
        StringBuilder result = new StringBuilder();
        try
        {
            int redByte;
            while((redByte = in.read()) != -1)
            {
                result.append((char)redByte);
            }
        }
        finally
        {
            try
            {
                in.close();
            }
            catch(IOException ignore)
            {
                log.error("IOException when closing stream to: " + in.toString());
            }
        }
        String out = result.toString();
        out = new String(out.getBytes("ISO-8859-1"), encoding);
        return out;
    }


    @Deprecated(since = "6.6", forRemoval = false)
    public VelocityEngine getVelocityEngine(Properties p) throws Exception
    {
        if(this.velocityEngine == null)
        {
            synchronized(this)
            {
                if(this.velocityEngine == null)
                {
                    this.velocityEngine = new VelocityEngine();
                    this.velocityEngine.init(p);
                }
            }
        }
        return this.velocityEngine;
    }


    public List<Format> getFormats(ComposedType ct)
    {
        return getFormats(null, ct);
    }


    public List<Format> getFormats(SessionContext ctx, ComposedType ct)
    {
        return ct.getLinkedItems(ctx, false, GeneratedCommonsConstants.Relations.FORMAT2COMTYPREL, null);
    }


    public Collection<Format> getFormatsForItem(Item item)
    {
        Set<ComposedType> toCheck = new HashSet<>();
        toCheck.add(item.getComposedType());
        toCheck.addAll(item.getComposedType().getAllSuperTypes());
        Set<Format> ret = new HashSet<>();
        for(Iterator<ComposedType> iter = toCheck.iterator(); iter.hasNext(); )
        {
            ComposedType ct = iter.next();
            ret.addAll(getFormats(ct));
        }
        return ret;
    }


    public Collection<Document> getDocuments(Item item, Format format)
    {
        FlexibleSearch fs = FlexibleSearch.getInstance();
        Map<Object, Object> values = new HashMap<>();
        values.put("item", item);
        values.put("format", format);
        String query = "SELECT {d:" + Document.PK + "} FROM {" + GeneratedCommonsConstants.TC.DOCUMENT + " AS d} WHERE {d:sourceItem} = ?item AND {d:format} = ?format";
        return fs.search(query, values, Collections.singletonList(Document.class), true, true, 0, -1).getResult();
    }


    public void setDocuments(Item item, Format format, Collection<Document> documents) throws ConsistencyCheckException
    {
        for(Iterator<Document> iter = documents.iterator(); iter.hasNext(); )
        {
            Document doc = iter.next();
            if(!doc.getSourceItem().equals(item))
            {
                throw new JaloInvalidParameterException("The Item 'document' with the code \"" + doc.getCode() + " (PK: " + doc
                                .getPK() + ")\" does not belongs to the given item (PK: " + item
                                .getPK() + ")", 1013);
            }
        }
        Collection<Document> oldcoll = getDocuments(item, format);
        for(Iterator<Document> iterator1 = oldcoll.iterator(); iterator1.hasNext(); )
        {
            Document doc = iterator1.next();
            if(!documents.contains(doc))
            {
                doc.remove();
            }
        }
    }


    public String transform(String inputXML, InputStream inputXSLStream, boolean appendHeaders)
    {
        String output = "", finalInput = "";
        StringWriter outputWriter = new StringWriter();
        try
        {
            if(inputXML == null || inputXSLStream == null)
            {
                throw new IllegalArgumentException();
            }
            if(inputXML.length() <= 0)
            {
                throw new IllegalArgumentException();
            }
            inputXML = removeChars(inputXML, new char[] {'\n', '\t', '\f', '\r'});
            if(appendHeaders)
            {
                finalInput = appendHeaders(inputXML);
            }
            TransformerFactory tFactory = Utilities.getTransformerFactory();
            Transformer transformer = tFactory.newTransformer(new StreamSource(inputXSLStream));
            transformer.transform(new StreamSource(new StringReader(finalInput)), new StreamResult(outputWriter));
        }
        catch(Exception e)
        {
            throw new TransformerException(e);
        }
        output = outputWriter.toString();
        return output;
    }


    public String transform(InputStream inputXMLStream, InputStream inputXSLStream, boolean appendHeaders)
    {
        try
        {
            if(inputXMLStream == null || inputXSLStream == null)
            {
                throw new IllegalArgumentException();
            }
            return transform(loadData(inputXMLStream), inputXSLStream, appendHeaders);
        }
        catch(Exception e)
        {
            throw new TransformerException(e);
        }
    }


    public String transform(String inputXML, Media inputXSLMedia, boolean appendHeaders)
    {
        String output = "", finalInput = "";
        StringWriter outputWriter = new StringWriter();
        try
        {
            if(inputXML == null || inputXSLMedia == null)
            {
                throw new IllegalArgumentException();
            }
            if(inputXML.length() <= 0)
            {
                throw new IllegalArgumentException();
            }
            inputXML = removeChars(inputXML, new char[] {'\n', '\t', '\f', '\r'});
            if(appendHeaders)
            {
                finalInput = appendHeaders(inputXML);
            }
            TransformerFactory tFactory = Utilities.getTransformerFactory();
            Transformer transformer = tFactory.newTransformer(new StreamSource(inputXSLMedia.getDataFromStream()));
            transformer.transform(new StreamSource(new StringReader(finalInput)), new StreamResult(outputWriter));
        }
        catch(Exception e)
        {
            throw new TransformerException(e);
        }
        output = outputWriter.toString();
        return output;
    }


    public String transform(InputStream inputXMLStream, Media inputXSLMedia, boolean appendHeaders)
    {
        String outputString, inputString = null;
        try
        {
            if(inputXMLStream == null || inputXSLMedia == null)
            {
                throw new IllegalArgumentException();
            }
            inputString = loadData(inputXMLStream);
            outputString = transform(inputString, inputXSLMedia, appendHeaders);
        }
        catch(Exception e)
        {
            throw new TransformerException(e);
        }
        return outputString;
    }


    private String removeChars(String s, char[] a)
    {
        StringBuffer r = new StringBuffer();
        for(int i = 0; i < s.length(); i++)
        {
            if(!contains(s.charAt(i), a))
            {
                r.append(s.charAt(i));
            }
        }
        return r.toString();
    }


    private boolean contains(char c, char[] a)
    {
        for(int i = 0; i < a.length; i++)
        {
            if(c == a[i])
            {
                return true;
            }
        }
        return false;
    }


    private String loadData(InputStream inputStream)
    {
        if(inputStream == null)
        {
            throw new IllegalArgumentException();
        }
        StringBuilder output = new StringBuilder();
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            if(!in.ready())
            {
                throw new IOException();
            }
            String line;
            while((line = in.readLine()) != null)
            {
                output.append(line);
            }
            in.close();
        }
        catch(IOException e)
        {
            throw new TransformerException(e);
        }
        return output.toString();
    }


    private String appendHeaders(String inputXML)
    {
        String headers = "<?xml version=\"1.0\"?><html><head><title></title></head><body>";
        String footers = "</body></html>";
        return "<?xml version=\"1.0\"?><html><head><title></title></head><body>" + inputXML + "</body></html>";
    }


    protected static void registerProductSampleEnhancer()
    {
        try
        {
            JaloImplementationManager.replaceCoreJaloClass(Product.class, (JaloObjectCreator)new Object(Product.class, new Method[] {Product.class
                            .getMethod("setCode", new Class[] {SessionContext.class}), Product.class
                            .getMethod("setName", new Class[] {SessionContext.class}), Product.class
                            .getMethod("setAllNames", new Class[] {SessionContext.class, Map.class}), Product.class
                            .getMethod("setUnit", new Class[] {SessionContext.class})}));
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    public Collection<JaloVelocityRenderer> getJaloVelocityRenderers()
    {
        String query = "SELECT {" + JaloVelocityRenderer.PK + "} FROM {" + GeneratedCommonsConstants.TC.JALOVELOCITYRENDERER + "} ";
        SearchResult res = getSession().getFlexibleSearch().search(query, JaloVelocityRenderer.class);
        return res.getResult();
    }


    public Collection<RenderersProperty> getRenderersPropertyList()
    {
        String query = "SELECT {" + RenderersProperty.PK + "} FROM {" + GeneratedCommonsConstants.TC.RENDERERSPROPERTY + "} ";
        SearchResult res = getSession().getFlexibleSearch().search(query, RenderersProperty.class);
        return res.getResult();
    }


    public JaloTranslatorConfiguration getJaloTranslatorConfigurationByCode(String code)
    {
        Map<Object, Object> map = new HashMap<>();
        map.put("code", code);
        String query = "SELECT {" + JaloTranslatorConfiguration.PK + "} FROM {" + GeneratedCommonsConstants.TC.JALOTRANSLATORCONFIGURATION + "} WHERE {code}=?code ";
        SearchResult result = JaloSession.getCurrentSession().getFlexibleSearch().search(query, map,
                        Collections.singletonList(JaloTranslatorConfiguration.class), true, true, 0, 1);
        if(result.getResult().size() > 0)
        {
            return result.getResult().iterator().next();
        }
        return null;
    }


    public TranslatorConfiguration getDefaultTranslatorConfiguration()
    {
        InputStream indesignRenderersConfiguration = null;
        InputStream indesignStyleConfiguration = null;
        InputStream entityReplacementConfiguration = null;
        InputStream htmlParsingConfiguration = null;
        TranslatorConfiguration config = null;
        try
        {
            indesignRenderersConfiguration = CommonsManager.class.getResourceAsStream("/commons/translator_renderers_indesign.xml");
            indesignStyleConfiguration = CommonsManager.class.getResourceAsStream("/commons/indesign.properties");
            entityReplacementConfiguration = CommonsManager.class.getResourceAsStream("/commons/indesign_replace.properties");
            htmlParsingConfiguration = CommonsManager.class.getResourceAsStream("/commons/translator_parsers_html.xml");
            RenderersFactoryFromFile renderersFactoryFromFile = new RenderersFactoryFromFile(indesignRenderersConfiguration, indesignStyleConfiguration, entityReplacementConfiguration);
            config = new TranslatorConfiguration(htmlParsingConfiguration, (RenderersFactory)renderersFactoryFromFile);
        }
        catch(Exception e)
        {
            log.error("Error while creating default TranslatorConfiguration: " + e);
        }
        finally
        {
            try
            {
                indesignRenderersConfiguration.close();
            }
            catch(IOException e)
            {
                log.error("Could not close InputStream " + indesignRenderersConfiguration.toString());
            }
            try
            {
                indesignStyleConfiguration.close();
            }
            catch(IOException e)
            {
                log.error("Could not close InputStream " + indesignStyleConfiguration.toString());
            }
            try
            {
                entityReplacementConfiguration.close();
            }
            catch(IOException e)
            {
                log.error("Could not close InputStream " + entityReplacementConfiguration.toString());
            }
            try
            {
                htmlParsingConfiguration.close();
            }
            catch(IOException e)
            {
                log.error("Could not close InputStream " + htmlParsingConfiguration.toString());
            }
        }
        return config;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void render(RendererTemplate template, Object context, Writer output)
    {
        if("velocity".equals(template.getRendererType().getCode()))
        {
            try
            {
                Class<?> c = Class.forName(Config.getParameter("template.velocity.class"));
                Renderer velocityTemplateRenderer = (Renderer)c.newInstance();
                velocityTemplateRenderer.render(template, context, null, output);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}

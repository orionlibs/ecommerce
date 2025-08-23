package de.hybris.platform.cockpit.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.ComposedType;
import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class CockpitUIComponentConfiguration extends GeneratedCockpitUIComponentConfiguration
{
    private static final Logger log = LoggerFactory.getLogger(CockpitUIComponentConfiguration.class.getName());


    public void setMedia(SessionContext ctx, Media value)
    {
        super.setMedia(ctx, value);
        if(value != null)
        {
            checkMedia(value);
        }
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item item = super.createItem(ctx, type, allAttributes);
        Media media = (Media)allAttributes.get("media");
        if(media != null)
        {
            checkMedia(media);
        }
        return item;
    }


    private void checkMedia(Media media)
    {
        InputStream inputStream = null;
        try
        {
            inputStream = media.getDataFromStreamSure();
            SAXParserFactory factory = SAXParserFactory.newInstance("com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl", null);
            factory.setValidating(false);
            factory.setNamespaceAware(true);
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setErrorHandler((ErrorHandler)new SimpleErrorHandler(media.getRealFileName()));
            reader.parse(new InputSource(inputStream));
        }
        catch(Exception e)
        {
            log.error("Error checking media", e);
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
        }
    }
}

package de.hybris.platform.commons.jalo;

import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.util.Utilities;
import java.io.File;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;

public class XMLTransformFormatter extends GeneratedXMLTransformFormatter
{
    private static final Logger LOG = Logger.getLogger(XMLTransformFormatter.class);


    public Media format(Media media)
    {
        File tempfile = null;
        try
        {
            tempfile = File.createTempFile("temp_XMLTransformFormatter_", ".xml");
            tempfile.deleteOnExit();
            TransformerFactory factory = Utilities.getTransformerFactory();
            Transformer transformer = factory.newTransformer(new StreamSource(getDataFromStream()));
            transformer.setParameter("versionParam", "2.0");
            Source src = new StreamSource(media.getDataFromStream());
            Result rst = new StreamResult(tempfile);
            transformer.transform(src, rst);
            Media ret = MediaManager.getInstance().createMedia("XMLTransform-" + System.currentTimeMillis());
            ret.setFile(tempfile);
            return ret;
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
        finally
        {
            if(tempfile != null && tempfile.exists())
            {
                try
                {
                    if(!tempfile.delete())
                    {
                        tempfile.delete();
                    }
                }
                catch(Exception e)
                {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }
}

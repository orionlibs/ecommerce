package de.hybris.platform.commons.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.media.Media;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Format extends GeneratedFormat
{
    public Document format(Item item) throws JaloBusinessException
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("code", getCode() + "-" + getCode());
        params.put("format", this);
        params.put("sourceItem", item);
        params
                        .put("itemTimestamp",
                                        (item.getModificationTime() == null) ? item.getCreationTime() : item.getModificationTime());
        return innerFormat(item, params);
    }


    private Document innerFormat(Item item, Map docParams) throws JaloBusinessException
    {
        Media m = null;
        try
        {
            m = getInitial().format(item);
            String resultMime = null;
            for(Iterator<MediaFormatter> iter = getChained().iterator(); iter.hasNext(); )
            {
                MediaFormatter mf = iter.next();
                if(!iter.hasNext())
                {
                    resultMime = mf.getOutputMimeType();
                }
                m = mf.format(m);
            }
            Document doc = null;
            if(m != null)
            {
                doc = CommonsManager.getInstance().createDocument(docParams);
                doc.setDataFromStream(m.getDataFromStream());
                doc.setMime((resultMime != null) ? resultMime : m.getMime());
            }
            return doc;
        }
        finally
        {
            if(m != null)
            {
                m.remove();
            }
        }
    }


    public Document format(Item item, Document doc) throws JaloBusinessException
    {
        Media m = null;
        try
        {
            m = getInitial().format(item);
            String resultMime = null;
            for(Iterator<MediaFormatter> iter = getChained().iterator(); iter.hasNext(); )
            {
                MediaFormatter mf = iter.next();
                if(!iter.hasNext())
                {
                    resultMime = mf.getOutputMimeType();
                }
                m = mf.format(m);
            }
            doc.setDataFromStream(m.getDataFromStream());
            doc.setMime((resultMime != null) ? resultMime : m.getMime());
            return doc;
        }
        finally
        {
            if(m != null)
            {
                m.remove();
            }
        }
    }
}

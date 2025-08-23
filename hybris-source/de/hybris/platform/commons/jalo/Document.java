package de.hybris.platform.commons.jalo;

import de.hybris.platform.jalo.JaloBusinessException;
import java.util.Date;
import org.apache.log4j.Logger;

public class Document extends GeneratedDocument
{
    private static final Logger log = Logger.getLogger(Document.class.getName());


    public boolean needRefresh()
    {
        Date itemdate = getSourceItem().getModificationTime();
        if(itemdate == null)
        {
            itemdate = getSourceItem().getCreationTime();
        }
        if(itemdate.getTime() > getItemTimestamp().getTime())
        {
            return true;
        }
        return false;
    }


    public void refresh() throws JaloBusinessException
    {
        getFormat().format(getSourceItem(), this);
    }
}

package de.hybris.platform.impex.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.MediaUtil;
import de.hybris.platform.util.Utilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.log4j.Logger;

public class ImpExMedia extends GeneratedImpExMedia
{
    private static final Logger LOG = Logger.getLogger(ImpExMedia.class.getName());


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        try
        {
            if(allAttributes.get("encoding") == null)
            {
                allAttributes.addInitialProperty("encoding", Utilities.resolveEncoding(CSVConstants.DEFAULT_ENCODING));
            }
        }
        catch(UnsupportedEncodingException e)
        {
            LOG.warn("Can not resolve default encoding " + CSVConstants.DEFAULT_ENCODING);
        }
        if(allAttributes.get("realFileName") == null)
        {
            allAttributes.put("realFileName", allAttributes.get("code"));
        }
        if(allAttributes.get("fieldSeparator") == null)
        {
            allAttributes.put("fieldSeparator", Character.valueOf(CSVConstants.DEFAULT_FIELD_SEPARATOR));
        }
        if(allAttributes.get("commentCharacter") == null)
        {
            allAttributes.put("commentCharacter", Character.valueOf('#'));
        }
        if(allAttributes.get("quoteCharacter") == null)
        {
            allAttributes.put("quoteCharacter", Character.valueOf(CSVConstants.DEFAULT_QUOTE_CHARACTER));
        }
        if(!allAttributes.containsKey("folder"))
        {
            allAttributes.put("folder", ImpExManager.getInstance().getImpExMediaFolder());
        }
        return super.createItem(ctx, type, allAttributes);
    }


    public void copySettings(ImpExMedia media)
    {
        media.setEncoding(getEncoding());
        media.setFieldSeparator(getFieldSeparator());
        media.setQuoteCharacter(getQuoteCharacter());
        media.setCommentCharacter(getCommentCharacter());
        media.setLinesToSkip(getLinesToSkip());
        media.setRemoveOnSuccess(isRemoveOnSuccess());
    }


    public void copyData(ImpExMedia media) throws JaloBusinessException
    {
        media.setData(getDataFromStreamSure(), getRealFileName(), getMime());
    }


    @ForceJALO(reason = "abstract method implementation")
    public String getPreview(SessionContext ctx)
    {
        if(getMime() == null)
        {
            return "Mime type not set, can not get preview.";
        }
        if(getMime().equals(ImpExConstants.File.MIME_TYPE_CSV) || getMime().equals(ImpExConstants.File.MIME_TYPE_TEXT) ||
                        getMime().equals(ImpExConstants.File.MIME_TYPE_IMPEX))
        {
            try
            {
                StringBuilder buffer = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(getDataFromStreamSure()));
                for(int i = 0; i < 100 && reader.ready(); i++)
                {
                    buffer.append(reader.readLine() + "\n");
                }
                if(reader.ready())
                {
                    buffer.append("...");
                }
                return buffer.toString();
            }
            catch(JaloBusinessException e)
            {
                LOG.error("Error while reading from media with code " + getCode() + ": " + e.getMessage(), (Throwable)e);
                return "Error while reading from media: " + e.getMessage();
            }
            catch(IOException e)
            {
                LOG.error("Error while reading from media with code " + getCode() + ": " + e.getMessage(), e);
                return "Error while reading from media: " + e.getMessage();
            }
        }
        if(MediaUtil.isZipRelatedMime(getMime()))
        {
            try
            {
                StringBuilder buffer = new StringBuilder();
                List<String> entries = getZipEntryNames();
                if(entries.isEmpty())
                {
                    buffer.append("Zip-archive has no entries");
                }
                else
                {
                    buffer.append("Entries of this zip-archive are:\n\n");
                }
                for(int i = 0; i < entries.size(); i++)
                {
                    buffer.append(Integer.toString(i + 1) + ". " + Integer.toString(i + 1) + "\n");
                }
                return buffer.toString();
            }
            catch(JaloBusinessException e)
            {
                LOG.error("Error while reading from media with code " + getCode() + ": " + e.getMessage(), (Throwable)e);
                return "Error while reading from media, no preview available";
            }
            catch(IOException e)
            {
                LOG.error("Error while reading from media with code " + getCode() + ": " + e.getMessage(), e);
                return "Error while reading from media, no preview available";
            }
        }
        return "No preview available";
    }
}

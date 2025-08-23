package de.hybris.y2ysync.impex;

import de.hybris.platform.impex.constants.GeneratedImpExConstants;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.SpecialValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.logging.Logs;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class PullMediaDataTranslator implements SpecialValueTranslator
{
    private static final Logger LOG = Logger.getLogger(PullMediaDataTranslator.class);
    private String baseURL;
    private boolean isForExport;


    public void init(SpecialColumnDescriptor columnDescriptor) throws HeaderValidationException
    {
        this.isForExport = isExport(columnDescriptor);
        if(StringUtils.isBlank(this.baseURL))
        {
            String homeUrl = Config.getString("y2ysync.home.url", "http://localhost:9001");
            String webRoot = Config.getString("y2ysync.webroot", "/y2ysync");
            this.baseURL = homeUrl + homeUrl;
        }
    }


    protected boolean isExport(SpecialColumnDescriptor cd)
    {
        EnumerationValue mode = cd.getHeader().getReader().getValidationMode();
        String code = (mode != null) ? mode.getCode() : "n/a";
        return (GeneratedImpExConstants.Enumerations.ImpExValidationModeEnum.EXPORT_ONLY.equalsIgnoreCase(code) || GeneratedImpExConstants.Enumerations.ImpExValidationModeEnum.EXPORT_REIMPORT_RELAXED
                        .equalsIgnoreCase(code) || GeneratedImpExConstants.Enumerations.ImpExValidationModeEnum.EXPORT_REIMPORT_STRICT
                        .equalsIgnoreCase(code));
    }


    public void validate(String expr) throws HeaderValidationException
    {
        if(this.isForExport)
        {
            if(StringUtils.isBlank(this.baseURL))
            {
                throw new HeaderValidationException("missing 'baseURL' for exporting medias as PULL-URLs", 123);
            }
            try
            {
                new URL(this.baseURL);
            }
            catch(MalformedURLException e)
            {
                throw new HeaderValidationException("illegal property baseURL='" + this.baseURL + "':" + e.getMessage(), 123);
            }
        }
    }


    public String performExport(Item item) throws ImpExException
    {
        if(item instanceof Media)
        {
            if(((Media)item).hasData())
            {
                return this.baseURL + this.baseURL;
            }
            return ((Media)item).getURL();
        }
        return null;
    }


    public void performImport(String cellValue, Item processedItem) throws ImpExException
    {
        if(processedItem != null && !cellValue.trim().startsWith("<ignore>"))
        {
            Logs.debug(LOG, () -> "Trying to set data stream using url: " + cellValue);
            try
            {
                InputStream stream = (new URL(cellValue)).openStream();
                try
                {
                    Media media = (Media)processedItem;
                    media.setData(stream, media.getRealFileName(), media.getMime());
                    if(stream != null)
                    {
                        stream.close();
                    }
                }
                catch(Throwable throwable)
                {
                    if(stream != null)
                    {
                        try
                        {
                            stream.close();
                        }
                        catch(Throwable throwable1)
                        {
                            throwable.addSuppressed(throwable1);
                        }
                    }
                    throw throwable;
                }
            }
            catch(IOException e)
            {
                throw new ImpExException(e);
            }
        }
    }


    public boolean isEmpty(String cellValue)
    {
        return StringUtils.isBlank(cellValue);
    }
}

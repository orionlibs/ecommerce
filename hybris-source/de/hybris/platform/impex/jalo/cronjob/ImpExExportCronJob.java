package de.hybris.platform.impex.jalo.cronjob;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.impex.jalo.exp.ExportUtils;
import de.hybris.platform.impex.jalo.exp.ImpExExportMedia;
import de.hybris.platform.impex.jalo.exp.converter.ExportConverter;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.Utilities;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Logger;

public class ImpExExportCronJob extends GeneratedImpExExportCronJob
{
    private static final Logger LOG = Logger.getLogger(ImpExExportCronJob.class.getName());
    protected ZipOutputStream zipOut = null;
    protected int emptyMedias = 0;
    protected int invalidMedias = 0;


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        try
        {
            if(allAttributes.get("encoding") == null)
            {
                allAttributes.addInitialProperty("encoding",
                                Utilities.resolveEncoding(CSVConstants.DEFAULT_ENCODING));
            }
        }
        catch(UnsupportedEncodingException e)
        {
            LOG.warn("Can not resolve default encoding " + CSVConstants.DEFAULT_ENCODING);
        }
        return super.createItem(ctx, type, allAttributes);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ImpExExportMedia createDataExportLocation(String code)
    {
        return ExportUtils.createDataExportTarget(code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ImpExExportMedia createMediasExportLocation(String code)
    {
        return ExportUtils.createMediasExportTarget(code);
    }


    public ExportConverter getConverter()
    {
        if(getConverterClass() == null)
        {
            return null;
        }
        String realclassname = getConverterClass().getCode().replace("_", ".");
        try
        {
            Class<?> clazz = Class.forName(realclassname);
            return (ExportConverter)clazz.newInstance();
        }
        catch(InstantiationException e)
        {
            LOG.error("Error while instantiating export converter " + realclassname, e);
        }
        catch(IllegalAccessException e)
        {
            LOG.error("Error while instantiating export converter " + realclassname, e);
        }
        catch(ClassNotFoundException e)
        {
            LOG.error("Error while instantiating export converter " + realclassname, e);
        }
        return null;
    }


    public void incItemsExported()
    {
        int exported = getItemsExportedAsPrimitive() + 1;
        setProperty(getSession().getSessionContext(), "itemsExported", Integer.valueOf(exported));
    }


    public void incItemsFiltered()
    {
        int filtered = getItemsSkippedAsPrimitive() + 1;
        setProperty(getSession().getSessionContext(), "itemsSkipped", Integer.valueOf(filtered));
    }


    @SLDSafe
    public void setItemsMaxCount(int param)
    {
        setProperty(getSession().getSessionContext(), "itemsMaxCount", Integer.valueOf(param));
    }


    public void resetItemsCounter()
    {
        setProperty(getSession().getSessionContext(), "itemsExported", Integer.valueOf(0));
        setProperty(getSession().getSessionContext(), "itemsSkipped", Integer.valueOf(0));
        setProperty(getSession().getSessionContext(), "itemsMaxCount", Integer.valueOf(0));
    }


    @ForceJALO(reason = "something else")
    public String getDataExportMediaCode(SessionContext ctx)
    {
        String code = super.getDataExportMediaCode(ctx);
        if(code == null)
        {
            code = "dataexport_" + getCode();
        }
        return code;
    }


    @ForceJALO(reason = "something else")
    public String getMediasExportMediaCode(SessionContext ctx)
    {
        String code = super.getMediasExportMediaCode(ctx);
        if(code == null)
        {
            code = "mediasexport_" + getCode();
        }
        return code;
    }


    @ForceJALO(reason = "something else")
    public Character getFieldSeparator(SessionContext ctx)
    {
        Character fieldSeparator = super.getFieldSeparator(ctx);
        if(fieldSeparator == null)
        {
            fieldSeparator = Character.valueOf(CSVConstants.DEFAULT_FIELD_SEPARATOR);
        }
        return fieldSeparator;
    }


    @ForceJALO(reason = "something else")
    public Character getCommentCharacter(SessionContext ctx)
    {
        Character commentCharacter = super.getCommentCharacter(ctx);
        if(commentCharacter == null)
        {
            commentCharacter = Character.valueOf('#');
        }
        return commentCharacter;
    }


    @ForceJALO(reason = "something else")
    public Character getQuoteCharacter(SessionContext ctx)
    {
        Character quoteCharacter = super.getQuoteCharacter(ctx);
        if(quoteCharacter == null)
        {
            quoteCharacter = Character.valueOf(CSVConstants.DEFAULT_QUOTE_CHARACTER);
        }
        return quoteCharacter;
    }
}

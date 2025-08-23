package de.hybris.platform.impex.jalo.util;

import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.constants.GeneratedImpExConstants;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.impex.jalo.ImpExReader;
import de.hybris.platform.impex.jalo.cronjob.ScriptProcessingWizard;
import de.hybris.platform.impex.jalo.exp.ExportUtils;
import de.hybris.platform.impex.jalo.exp.HeaderLibrary;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public final class ImpExUtils
{
    private static final Logger log = Logger.getLogger(ImpExUtils.class.getName());
    private static final int PROBLEMATIC_DB_DEFAULT_CHUNK_SIZE = 400;
    private static final int DEFAULT_CHUNK_SIZE = 0;


    public static boolean isStrictMode(EnumerationValue mode)
    {
        return (mode != null && (mode
                        .equals(ImpExManager.getExportReimportStrictMode()) || mode.equals(ImpExManager.getImportStrictMode())));
    }


    public static boolean isRelaxedMode(EnumerationValue mode)
    {
        return (mode != null && (mode
                        .equals(ImpExManager.getExportReimportRelaxedMode()) || mode.equals(ImpExManager.getImportRelaxedMode())));
    }


    public static boolean isExportMode(EnumerationValue mode)
    {
        return (mode != null && (mode
                        .equals(ImpExManager.getExportReimportStrictMode()) || mode
                        .equals(ImpExManager.getExportReimportRelaxedMode()) || mode.equals(ImpExManager.getExportOnlyMode())));
    }


    public static boolean isImportMode(EnumerationValue mode)
    {
        return (mode != null && (mode
                        .equals(ImpExManager.getExportReimportStrictMode()) || mode.equals(
                        ImpExManager.getImportRelaxedMode()) || mode
                        .equals(ImpExManager.getImportStrictMode())));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static String convertTime(long time)
    {
        return Utilities.formatTime(time);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static StringBuffer mergeHeaderAndItemList(HeaderLibrary template, Collection<Item> itemlist) throws IOException
    {
        return ExportUtils.mergeHeaderAndItemList(template, itemlist);
    }


    public static StringBuilder getContent(Media m)
    {
        StringBuilder sb = new StringBuilder();
        if(m == null || (!m.hasData() && m.getURL() == null))
        {
            return null;
        }
        BufferedReader r = null;
        try
        {
            r = new BufferedReader(new InputStreamReader(m.getDataFromStreamSure()));
            boolean first = false;
            for(String s = r.readLine(); s != null; s = r.readLine())
            {
                if(!first)
                {
                    sb.append("\n");
                }
                else
                {
                    first = false;
                }
                sb.append(s);
            }
            return sb;
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
            if(r != null)
            {
                try
                {
                    r.close();
                }
                catch(IOException e)
                {
                    log.error("Error while closing stream: " + e.getMessage(), e);
                }
            }
        }
    }


    public static void saveScript(ScriptProcessingWizard wizard)
    {
        ImpExMedia m = wizard.getJobMedia();
        String txt = wizard.getImpExScript();
        boolean wasNull = false;
        if(m == null)
        {
            wasNull = true;
            if(txt == null)
            {
                return;
            }
            Map<Object, Object> params = new HashMap<>();
            params.put("mime", ImpExConstants.File.MIME_TYPE_CSV);
            EnumerationValue headerMode = EnumerationManager.getInstance().getEnumerationValue(
                            EnumerationManager.getInstance().getEnumerationType(GeneratedImpExConstants.TC.SCRIPTTYPEENUM), GeneratedImpExConstants.Enumerations.ScriptTypeEnum.HEADERLIBRARY);
            if(wizard.getMode().equals(headerMode))
            {
                params.put("code", wizard.getMediaCode("headerlib"));
                HeaderLibrary headerLibrary = ImpExManager.getInstance().createHeaderLibrary(params);
            }
            else
            {
                params.put("code", wizard.getMediaCode("exportscript"));
                m = ImpExManager.getInstance().createImpExMedia(params);
            }
        }
        if(txt == null)
        {
            try
            {
                m.removeData(false);
            }
            catch(JaloBusinessException e1)
            {
                throw new JaloSystemException(e1);
            }
        }
        else
        {
            try
            {
                ByteArrayOutputStream bos = new ByteArrayOutputStream(txt.length());
                OutputStreamWriter wr = new OutputStreamWriter(bos);
                wr.write(txt);
                wr.flush();
                wr.close();
                m.setData(new DataInputStream(new ByteArrayInputStream(bos.toByteArray())), "exportscript.impex", ImpExConstants.File.MIME_TYPE_CSV);
            }
            catch(IOException e2)
            {
                throw new JaloSystemException(e2);
            }
        }
        if(wasNull)
        {
            wizard.setJobMedia(m);
        }
    }


    public static void closeQuietly(ImpExReader ir)
    {
        if(ir != null)
        {
            try
            {
                ir.close();
            }
            catch(IOException iOException)
            {
            }
        }
    }


    public static int calculateQueryChunkSize(StandardColumnDescriptor descriptor)
    {
        try
        {
            return Integer.valueOf(descriptor.getDescriptorData().getModifier("query-chunk-size")).intValue();
        }
        catch(NumberFormatException ex)
        {
            return getDefaultQueryChunkSize();
        }
    }


    public static int getDefaultQueryChunkSize()
    {
        return Registry.getCurrentTenant().getConfig().getInt("impex.query.chunk.size",
                        dbHasProblemsWithBigQueries() ? 400 : 0);
    }


    private static boolean dbHasProblemsWithBigQueries()
    {
        return (Config.isHanaUsed() || Config.isOracleUsed() || Config.isSQLServerUsed() || Config.isMySQL8Used());
    }
}

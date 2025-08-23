package de.hybris.platform.ldap.jalo;

import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.platform.cronjob.jalo.AbortCronJobException;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.jalo.JobMedia;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.Importer;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.ldap.LDIF2ImpExConverter;
import de.hybris.platform.ldap.connection.LDAPGenericObject;
import de.hybris.platform.ldap.constants.GeneratedLDAPConstants;
import de.hybris.platform.ldap.jalo.configuration.ConfigurationParser;
import de.hybris.platform.ldap.jalo.configuration.valueobject.ConfigValueObject;
import de.hybris.platform.util.CSVReader;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class LDIFImportJob extends GeneratedLDIFImportJob
{
    private static final Logger LOG = Logger.getLogger(LDIFImportJob.class.getName());


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item item = super.createItem(ctx, type, allAttributes);
        return item;
    }


    public CronJob.CronJobResult performCronJob(CronJob cronJob) throws AbortCronJobException
    {
        boolean result = false;
        LDIFImportCronJob importCronJob = null;
        if(!(cronJob instanceof LDIFImportCronJob))
        {
            throw new AbortCronJobException("Given cronjob is not instance of LDIFImportCronJob");
        }
        JobMedia importMedia = null;
        Language sessionLanguage = getSession().getSessionContext().getLanguage();
        try
        {
            importCronJob = (LDIFImportCronJob)cronJob;
            result = performJob((CronJob)importCronJob);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new AbortCronJobException(e.getMessage());
        }
        finally
        {
            importCronJob.setJobMedia(importMedia);
            getSession().getSessionContext().setLanguage(sessionLanguage);
        }
        return importCronJob.getFinishedResult(result);
    }


    protected boolean performJob(CronJob cronJob) throws AbortCronJobException, NamingException, ParseAbortException, JaloBusinessException, IOException
    {
        LDIFImportCronJob cronjob = (LDIFImportCronJob)cronJob;
        EnumerationValue mode = cronjob.getImportmode();
        EnumerationManager enumerationManager = EnumerationManager.getInstance();
        if(mode.equals(enumerationManager
                        .getEnumerationValue(enumerationManager.getEnumerationType(GeneratedLDAPConstants.TC.LDIFIMPORTMODEENUM), GeneratedLDAPConstants.Enumerations.LDIFImportModeEnum.FILEBASED)))
        {
            return executeFileBasedImport(cronjob);
        }
        if(mode.equals(enumerationManager
                        .getEnumerationValue(enumerationManager.getEnumerationType(GeneratedLDAPConstants.TC.LDIFIMPORTMODEENUM), GeneratedLDAPConstants.Enumerations.LDIFImportModeEnum.QUERYBASED)))
        {
            return executeSearchBasedImport(cronjob);
        }
        throw new JaloSystemException("Unsupported importmode: " + mode);
    }


    private boolean executeFileBasedImport(LDIFImportCronJob cronjob) throws AbortCronJobException, ParseAbortException, JaloBusinessException, UnsupportedEncodingException
    {
        ConfigurationMedia configurationMedia = cronjob.getConfigFile();
        LDIFMedia ldifMedia = cronjob.getLdifFile();
        ConfigurationParser parser = new ConfigurationParser();
        parser.parse(configurationMedia.getDataFromStream());
        ConfigValueObject config = parser.getConfig();
        LDIF2ImpExConverter importer = new LDIF2ImpExConverter(ldifMedia.getDataFromStream(), config);
        importer.generateImpExScript();
        if(LOG.isDebugEnabled())
        {
            LOG.debug(importer.getExportAsString());
        }
        cronjob.setDestMedia(importer.getExportAsMedia());
        Importer impexImporter = null;
        try
        {
            DataInputStream dataFromStream = null;
            CSVReader csvReader = null;
            try
            {
                dataFromStream = cronjob.getDestMedia().getDataFromStream();
                csvReader = new CSVReader(dataFromStream, "UTF-8");
                impexImporter = ImpExManager.getInstance().importDataLight(csvReader, true, -1);
            }
            catch(UnsupportedEncodingException ex)
            {
                if(dataFromStream != null)
                {
                    try
                    {
                        dataFromStream.close();
                    }
                    catch(IOException e)
                    {
                        throw new JaloSystemException(e);
                    }
                }
                throw ex;
            }
            catch(ImpExException ex)
            {
                if(csvReader != null)
                {
                    csvReader.closeQuietly();
                    throw ex;
                }
            }
            if(impexImporter.hasUnresolvedLines())
            {
                cronjob.setDumpMedia(impexImporter.getDumpHandler().getDumpAsMedia());
            }
        }
        finally
        {
            if(impexImporter != null)
            {
                impexImporter.close();
            }
        }
        return true;
    }


    private boolean executeSearchBasedImport(LDIFImportCronJob cronjob) throws AbortCronJobException, ParseAbortException, JaloBusinessException, NamingException, UnsupportedEncodingException
    {
        String returnAttributes = cronjob.getResultfilter();
        String searchbase = cronjob.getSearchbase();
        String query = cronjob.getLdapquery();
        StringTokenizer tokenizer = new StringTokenizer(returnAttributes, ",");
        List<String> list = new ArrayList<>();
        while(tokenizer.hasMoreTokens())
        {
            String token = tokenizer.nextToken().trim();
            list.add(token);
        }
        String[] attrs = list.<String>toArray(new String[list.size()]);
        Collection<LDAPGenericObject> ldapSearchResult = LDAPManager.getInstance().browseLDAP(searchbase, query, attrs);
        File tempFile = null;
        OutputStream fout = null;
        OutputStream bout = null;
        OutputStreamWriter out = null;
        try
        {
            tempFile = File.createTempFile("import_ldif", ".temp");
            fout = new FileOutputStream(tempFile);
            bout = new BufferedOutputStream(fout);
            out = new OutputStreamWriter(bout, "UTF-8");
            for(LDAPGenericObject obj : ldapSearchResult)
            {
                out.write(obj.getValue());
                out.write("\n");
            }
            out.flush();
            out.close();
            Item.ItemAttributeMap map = new Item.ItemAttributeMap();
            map.put("code", tempFile.getName());
            map.put("mime", "text");
            LDIFMedia ldif = LDAPManager.getInstance().createLDIFMedia((Map)map);
            ldif.setRealFileName(tempFile.getName());
            ldif.setDataFromStream(new DataInputStream(new FileInputStream(tempFile)));
            cronjob.setLdifFile(ldif);
        }
        catch(IOException e)
        {
            if(fout != null)
            {
                try
                {
                    fout.close();
                }
                catch(IOException iOException)
                {
                }
            }
            if(bout != null)
            {
                try
                {
                    bout.close();
                }
                catch(IOException iOException)
                {
                }
            }
            if(out != null)
            {
                try
                {
                    out.close();
                }
                catch(IOException iOException)
                {
                }
            }
            throw new AbortCronJobException(e.getMessage());
        }
        finally
        {
            if(fout != null)
            {
                try
                {
                    fout.close();
                }
                catch(IOException iOException)
                {
                }
            }
            if(bout != null)
            {
                try
                {
                    bout.close();
                }
                catch(IOException iOException)
                {
                }
            }
            if(out != null)
            {
                try
                {
                    out.close();
                }
                catch(IOException iOException)
                {
                }
            }
        }
        return executeFileBasedImport(cronjob);
    }
}

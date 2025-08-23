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
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.ldap.LDIF2ImpExConverter;
import de.hybris.platform.ldap.connection.LDAPGenericObject;
import de.hybris.platform.ldap.exception.LDAPException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class LDIFGroupImportJob extends GeneratedLDIFGroupImportJob
{
    private static final Logger LOG = Logger.getLogger(LDIFGroupImportJob.class.getName());


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item item = super.createItem(ctx, type, allAttributes);
        return item;
    }


    public CronJob.CronJobResult performCronJob(CronJob cronJob) throws AbortCronJobException
    {
        boolean result = false;
        LDIFGroupImportCronJob importCronJob = null;
        JobMedia importMedia = null;
        Language sessionLanguage = getSession().getSessionContext().getLanguage();
        try
        {
            importCronJob = (LDIFGroupImportCronJob)cronJob;
            result = performJob((CronJob)importCronJob);
        }
        catch(AbortCronJobException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return cronJob.getAbortResult();
        }
        finally
        {
            importCronJob.setJobMedia(importMedia);
            getSession().getSessionContext().setLanguage(sessionLanguage);
        }
        return importCronJob.getFinishedResult(result);
    }


    protected boolean performJob(CronJob cronJob) throws AbortCronJobException, ParseAbortException, JaloBusinessException, NamingException, IOException
    {
        LDIFGroupImportCronJob cronjob = (LDIFGroupImportCronJob)cronJob;
        boolean usermode = cronjob.isImportUsersAsPrimitive();
        return executeSearchBasedImport(cronjob, usermode);
    }


    private boolean executeSearchBasedImport(LDIFGroupImportCronJob cronjob, boolean usermode) throws AbortCronJobException, ParseAbortException, NamingException, JaloBusinessException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("executeSearchBasedImport - usermode: " + usermode);
        }
        String returnAttributes = cronjob.getResultfilter();
        String searchbase = cronjob.getSearchbase();
        String query = cronjob.getLdapquery();
        Map<String, List<String>> members = new HashMap<>();
        StringTokenizer returnAttrs = new StringTokenizer(returnAttributes, ",");
        List<String> list = new ArrayList<>();
        while(returnAttrs.hasMoreTokens())
        {
            String token = returnAttrs.nextToken().trim();
            list.add(token);
        }
        String[] attrs = list.<String>toArray(new String[list.size()]);
        if(usermode && !list.contains("member"))
        {
            LOG.warn("Missing 'member' attribute! Skipping user import!");
            usermode = false;
        }
        Collection<LDAPGenericObject> ldapSearchResult = LDAPManager.getInstance().browseLDAP(searchbase, query, attrs);
        if(ldapSearchResult == null)
        {
            throw new IllegalStateException("LDAP search failed! (<null>)");
        }
        File tempFile = null;
        try
        {
            tempFile = File.createTempFile("group_import_ldif", ".temp");
            OutputStream fout = new FileOutputStream(tempFile);
            OutputStream bout = new BufferedOutputStream(fout);
            OutputStreamWriter out = new OutputStreamWriter(bout, "UTF-8");
            for(LDAPGenericObject obj : ldapSearchResult)
            {
                if(usermode)
                {
                    List<String> users = obj.getAttribute("member");
                    String distinguishedName = obj.getDN();
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("group: " + distinguishedName);
                    }
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("users: " + users);
                    }
                    members.put(distinguishedName, users);
                }
                out.write(obj.getValue());
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Write to LDIF: " + obj.getValue());
                }
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
            e.printStackTrace();
            return false;
        }
        boolean res = executeFileBasedImport(cronjob);
        if(res)
        {
            return executeUserImport(cronjob, members);
        }
        return res;
    }


    private boolean executeUserImport(LDIFGroupImportCronJob cronjob, Map<String, List<String>> members) throws JaloBusinessException, NamingException, AbortCronJobException, ParseAbortException
    {
        String returnAttributes = cronjob.getUserResultfilter();
        String searchbase = "";
        String query = "";
        StringTokenizer returnAttrs = new StringTokenizer(returnAttributes, ",");
        List<String> list = new ArrayList<>();
        while(returnAttrs.hasMoreTokens())
        {
            String token = returnAttrs.nextToken().trim();
            list.add(token);
        }
        if(!list.contains(cronjob.getUserSearchFieldQualifier()))
        {
            LOG.warn("Missing '" + cronjob.getUserSearchFieldQualifier() + "' attribute! Skipping user import!");
            throw new LDAPException("Missing '" + cronjob.getUserSearchFieldQualifier() + "' attribute! Skipping user import!", 678945);
        }
        String[] attrs = list.<String>toArray(new String[list.size()]);
        File tempFile = null;
        try
        {
            tempFile = File.createTempFile("group_user_import_ldif", ".temp");
            OutputStream fout = new FileOutputStream(tempFile);
            OutputStream bout = new BufferedOutputStream(fout);
            OutputStreamWriter out = new OutputStreamWriter(bout, "UTF-8");
            for(Map.Entry<String, List<String>> entry : members.entrySet())
            {
                searchbase = cronjob.getUserRootDN();
                List users = (List)entry.getValue();
                if(users != null)
                {
                    for(Object dn : users)
                    {
                        String userSearchField = (String)dn;
                        userSearchField = userSearchField.replaceAll("\\\\", "\\\\\\\\");
                        query = "(&(objectclass=*)(" + cronjob.getUserSearchFieldQualifier() + "=" + userSearchField + "))";
                        Collection<LDAPGenericObject> ldapSearchResult = LDAPManager.getInstance().browseLDAP(searchbase, query, attrs);
                        for(LDAPGenericObject obj : ldapSearchResult)
                        {
                            if(LOG.isDebugEnabled())
                            {
                                LOG.debug(obj.getValue());
                            }
                            out.write(obj.getValue());
                            out.write("\n");
                        }
                    }
                }
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
            e.printStackTrace();
            return false;
        }
        return executeFileBasedImport(cronjob);
    }


    private boolean executeFileBasedImport(LDIFGroupImportCronJob cronjob) throws AbortCronJobException, ParseAbortException, JaloBusinessException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("executeFileBasedImport...");
        }
        ConfigurationMedia configurationMedia = cronjob.getConfigFile();
        LDIFMedia ldifMedia = cronjob.getLdifFile();
        ConfigurationParser parser = new ConfigurationParser();
        parser.parse(configurationMedia.getDataFromStream());
        ConfigValueObject config = parser.getConfig();
        Importer impexImporter = null;
        try
        {
            LDIF2ImpExConverter importer = new LDIF2ImpExConverter(ldifMedia.getDataFromStream(), config);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("initiate script generation...");
            }
            importer.generateImpExScript();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Generated ImpEx script: " + importer.getExportAsString());
            }
            cronjob.setDestMedia(importer.getExportAsMedia());
            DataInputStream dataFromStream = null;
            CSVReader csvReader = null;
            try
            {
                dataFromStream = cronjob.getDestMedia().getDataFromStream();
                csvReader = new CSVReader(dataFromStream, "UTF-8");
                impexImporter = ImpExManager.getInstance().importDataLight(csvReader, cronjob.isCodeExecutionAsPrimitive(), -1);
            }
            catch(UnsupportedEncodingException ex)
            {
                if(dataFromStream != null)
                {
                    dataFromStream.close();
                }
                throw ex;
            }
            catch(ImpExException ex)
            {
                if(csvReader != null)
                {
                    csvReader.closeQuietly();
                }
                throw ex;
            }
            if(impexImporter.hasUnresolvedLines())
            {
                cronjob.setDumpMedia(impexImporter.getDumpHandler().getDumpAsMedia());
            }
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage(), e);
            return false;
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
}

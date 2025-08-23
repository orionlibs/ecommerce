package de.hybris.platform.ldap.jobs.impl;

import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.Importer;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.ldap.LDIF2ImpExConverter;
import de.hybris.platform.ldap.connection.LDAPGenericObject;
import de.hybris.platform.ldap.jalo.LDAPManager;
import de.hybris.platform.ldap.jalo.configuration.ConfigurationParser;
import de.hybris.platform.ldap.jalo.configuration.valueobject.ConfigValueObject;
import de.hybris.platform.ldap.jobs.LDAPImportException;
import de.hybris.platform.ldap.jobs.LDIFImportStrategy;
import de.hybris.platform.ldap.model.ConfigurationMediaModel;
import de.hybris.platform.ldap.model.LDIFMediaModel;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.CSVReader;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultLDIFImportStrategy implements LDIFImportStrategy
{
    private static final Logger LOG = Logger.getLogger(DefaultLDIFImportStrategy.class.getName());
    private MediaService mediaService;
    private ModelService modelService;
    private LDAPManager ldapManager;
    private ConfigurationMediaModel config;
    private ImpExMediaModel dumpMedia;
    private ImpExMediaModel destMedia;


    public void executeFileBasedImport(LDIFMediaModel ldifMediaModel) throws LDAPImportException
    {
        ConfigurationParser parser = new ConfigurationParser();
        try
        {
            parser.parse(this.mediaService.getStreamFromMedia((MediaModel)this.config));
        }
        catch(ParseAbortException e)
        {
            LOG.debug("Cannot parse given input stream.", (Throwable)e);
            throw new LDAPImportException("Cannot parse given input stream.", e);
        }
        ConfigValueObject config = parser.getConfig();
        try
        {
            LDIF2ImpExConverter importer = new LDIF2ImpExConverter(this.mediaService.getStreamFromMedia((MediaModel)ldifMediaModel), config);
            importer.generateImpExScript();
            if(LOG.isDebugEnabled())
            {
                LOG.debug(importer.getExportAsString());
            }
            this.destMedia = (ImpExMediaModel)this.modelService.get(importer.getExportAsMedia());
            Importer impexImporter = ImpExManager.getInstance().importDataLight(new CSVReader(this.mediaService
                            .getStreamFromMedia((MediaModel)this.destMedia), "UTF-8"), true, -1);
            if(impexImporter.hasUnresolvedLines())
            {
                this.dumpMedia = (ImpExMediaModel)this.modelService.get(impexImporter.getDumpHandler().getDumpAsMedia());
            }
        }
        catch(JaloBusinessException e)
        {
            throw new SystemException("Cannot generate ImpExScript.", e);
        }
        catch(UnsupportedEncodingException e)
        {
            LOG.debug("The Character Encoding is not supported.", e);
            throw new LDAPImportException("The Character Encoding is not supported.", e);
        }
    }


    public void executeFileSearchBasedImport(Map<String, String> searchAttributes) throws LDAPImportException
    {
        LDIFMediaModel ldif;
        String returnAttributes = searchAttributes.get("resultFilter");
        String searchbase = searchAttributes.get("searchBase");
        String query = searchAttributes.get("ldapQuery");
        List<String> list = createAttributesList(returnAttributes);
        String[] attrs = list.<String>toArray(new String[list.size()]);
        File tempFile = null;
        OutputStream fout = null;
        OutputStream bout = null;
        OutputStreamWriter out = null;
        try
        {
            Collection<LDAPGenericObject> ldapSearchResult = this.ldapManager.browseLDAP(searchbase, query, attrs);
            if(ldapSearchResult == null)
            {
                throw new IllegalStateException("LDAP search failed! (<null>)");
            }
            tempFile = File.createTempFile("import_ldif", ".temp");
            fout = new FileOutputStream(tempFile);
            bout = new BufferedOutputStream(fout);
            out = new OutputStreamWriter(bout, "UTF-8");
            for(LDAPGenericObject obj : ldapSearchResult)
            {
                out.write(obj.getValue());
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Write to LDIF: " + obj.getValue());
                }
                out.write("\n");
            }
            out.flush();
            ldif = createLdifFileMedia(tempFile);
        }
        catch(IOException e)
        {
            LOG.debug("IO operation for file import_ldif.temp has failed.", e);
            throw new LDAPImportException("IO operation for file import_ldif.temp has failed.", e);
        }
        catch(NamingException e)
        {
            LOG.debug("Browsing LDAP: the name cannot be resolved.", e);
            throw new LDAPImportException("Browsing LDAP: the name cannot be resolved.", e);
        }
        finally
        {
            closeIO(fout, bout, out, tempFile);
        }
        executeFileBasedImport(ldif);
    }


    public void executeFileSearchBasedUserImport(Map<String, String> searchAttributes) throws LDAPImportException
    {
        LDIFMediaModel ldif;
        String returnAttributes = searchAttributes.get("resultFilter");
        String searchbase = searchAttributes.get("searchBase");
        String query = searchAttributes.get("ldapQuery");
        Map<String, List<String>> members = new HashMap<>();
        List<String> list = createAttributesList(returnAttributes);
        String[] attrs = list.<String>toArray(new String[list.size()]);
        File tempFile = null;
        OutputStream fout = null;
        OutputStream bout = null;
        OutputStreamWriter out = null;
        try
        {
            Collection<LDAPGenericObject> ldapSearchResult = this.ldapManager.browseLDAP(searchbase, query, attrs);
            if(ldapSearchResult == null)
            {
                throw new IllegalStateException("LDAP search failed! (<null>)");
            }
            tempFile = File.createTempFile("import_ldif", ".temp");
            fout = new FileOutputStream(tempFile);
            bout = new BufferedOutputStream(fout);
            out = new OutputStreamWriter(bout, "UTF-8");
            for(LDAPGenericObject obj : ldapSearchResult)
            {
                List<String> users = obj.getAttribute("member");
                String objDn = obj.getDN();
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("group: " + objDn);
                    LOG.debug("users: " + users);
                }
                members.put(objDn, users);
                out.write(obj.getValue());
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Write to LDIF: " + obj.getValue());
                }
                out.write("\n");
            }
            out.flush();
            ldif = createLdifFileMedia(tempFile);
        }
        catch(IOException e)
        {
            LOG.debug("IO operation for file import_ldif.temp has failed.", e);
            throw new LDAPImportException("IO operation for file import_ldif.temp has failed.", e);
        }
        catch(NamingException e)
        {
            LOG.debug("Browsing LDAP: the name cannot be resolved.", e);
            throw new LDAPImportException("Browsing LDAP: the name cannot be resolved.", e);
        }
        finally
        {
            closeIO(fout, bout, out, tempFile);
        }
        executeFileBasedImport(ldif);
        executeUserImport(searchAttributes, members);
    }


    private void executeUserImport(Map<String, String> searchAttributes, Map<String, List<String>> members) throws LDAPImportException
    {
        LDIFMediaModel ldif;
        String returnAttributes = searchAttributes.get("userResultFilter");
        String searchbase = "";
        String query = "";
        List<String> list = createAttributesList(returnAttributes);
        if(!list.contains(searchAttributes.get("userSearchFieldQualifier")))
        {
            LOG.warn("Missing '" + (String)searchAttributes.get("userSearchFieldQualifier") + "' attribute! Skipping user import!");
            throw new SystemException("Missing '" + (String)searchAttributes.get("userSearchFieldQualifier") + "' attribute! Skipping user import!");
        }
        String[] attrs = list.<String>toArray(new String[list.size()]);
        File tempFile = null;
        OutputStream fout = null;
        OutputStream bout = null;
        OutputStreamWriter out = null;
        try
        {
            tempFile = File.createTempFile("group_user_import_ldif", ".temp");
            fout = new FileOutputStream(tempFile);
            bout = new BufferedOutputStream(fout);
            out = new OutputStreamWriter(bout, "UTF-8");
            for(Map.Entry<String, List<String>> entry : members.entrySet())
            {
                searchbase = searchAttributes.get("userRootDN");
                List users = (List)entry.getValue();
                if(users != null)
                {
                    for(Object dn : users)
                    {
                        String userSearchField = (String)dn;
                        query = "(&(objectclass=*)(" + (String)searchAttributes.get("userSearchFieldQualifier") + "=" + userSearchField + "))";
                        Collection<LDAPGenericObject> ldapSearchResult = this.ldapManager.browseLDAP(searchbase, query, attrs);
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
            ldif = createLdifFileMedia(tempFile);
        }
        catch(IOException e)
        {
            LOG.debug("IO operation for file " + tempFile.getName() + " has failed.", e);
            throw new LDAPImportException("IO operation for file " + tempFile.getName() + " has failed.", e);
        }
        catch(NamingException e)
        {
            LOG.debug("Browsing LDAP: the name cannot be resolved.", e);
            throw new LDAPImportException("Browsing LDAP: the name cannot be resolved.", e);
        }
        finally
        {
            closeIO(fout, bout, out, tempFile);
        }
        executeFileBasedImport(ldif);
    }


    private void closeIO(OutputStream fout, OutputStream bout, OutputStreamWriter out, File tempFile)
    {
        IOUtils.closeQuietly(fout);
        IOUtils.closeQuietly(bout);
        IOUtils.closeQuietly(out);
        if(tempFile != null)
        {
            tempFile.delete();
        }
    }


    private LDIFMediaModel createLdifFileMedia(File tempFile) throws FileNotFoundException
    {
        LDIFMediaModel ldif = (LDIFMediaModel)this.modelService.create(LDIFMediaModel.class);
        ldif.setCode(tempFile.getName());
        ldif.setMime("text");
        ldif.setRealfilename(tempFile.getName());
        this.modelService.save(ldif);
        this.mediaService.setStreamForMedia((MediaModel)ldif, new DataInputStream(new FileInputStream(tempFile)));
        return ldif;
    }


    private List<String> createAttributesList(String returnAttributes)
    {
        List<String> list = new ArrayList<>();
        if(returnAttributes == null)
        {
            return list;
        }
        StringTokenizer returnAttrs = new StringTokenizer(returnAttributes, ",");
        while(returnAttrs.hasMoreTokens())
        {
            String token = returnAttrs.nextToken().trim();
            list.add(token);
        }
        return list;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public ImpExMediaModel getDumpMedia()
    {
        return this.dumpMedia;
    }


    public ImpExMediaModel getDestMedia()
    {
        return this.destMedia;
    }


    @Required
    public void setLdapManager(LDAPManager ldapManager)
    {
        this.ldapManager = ldapManager;
    }


    public void setConfig(ConfigurationMediaModel config)
    {
        this.config = config;
    }
}

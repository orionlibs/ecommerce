package de.hybris.platform.ldap;

import de.hybris.bootstrap.xml.UnicodeReader;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.ldap.connection.LDAPGenericObject;
import de.hybris.platform.ldap.impex.ImpExEntry;
import de.hybris.platform.ldap.jalo.configuration.valueobject.AttributeValueObject;
import de.hybris.platform.ldap.jalo.configuration.valueobject.AttributesValueObject;
import de.hybris.platform.ldap.jalo.configuration.valueobject.ConfigValueObject;
import de.hybris.platform.ldap.jalo.configuration.valueobject.HeaderEntry;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import org.apache.log4j.Logger;

public class LDIF2ImpExConverter
{
    private ImpExMedia generatedImpExMedia;
    private StringBuilder impexExport;
    private final ConfigValueObject config;
    private final String patternCollectionHandling = ",";
    private final String replacementCollectionHandling = "\\\\,";
    private final Pattern collectionHandlingPattern = Pattern.compile(",");
    public static final String DEFAULT_ENCODING = "UTF-8";
    private final BufferedReader reader;
    private static final Logger log = Logger.getLogger(LDIF2ImpExConverter.class.getName());


    public LDIF2ImpExConverter(String fileName, ConfigValueObject config) throws UnsupportedEncodingException, FileNotFoundException
    {
        this(new FileInputStream(fileName), config);
    }


    public LDIF2ImpExConverter(File file, ConfigValueObject config) throws UnsupportedEncodingException, FileNotFoundException
    {
        this(new FileInputStream(file), config);
    }


    public LDIF2ImpExConverter(InputStream inputStream, ConfigValueObject config) throws UnsupportedEncodingException
    {
        this((Reader)new UnicodeReader(inputStream, "UTF-8"), config);
    }


    public LDIF2ImpExConverter(Reader reader, ConfigValueObject config)
    {
        this.reader = new BufferedReader(reader);
        this.config = config;
    }


    public boolean generateImpExScript() throws JaloBusinessException, UnsupportedEncodingException
    {
        return generateImpExScript(ImpExConstants.Syntax.Mode.INSERT_UPDATE);
    }


    public boolean generateImpExScript(String mode) throws JaloBusinessException, UnsupportedEncodingException
    {
        this.impexExport = new StringBuilder();
        Attributes entry = null;
        boolean success = false;
        int skipped = 0;
        int imported = 0;
        try
        {
            LDIFReader ldif = new LDIFReader(this.reader);
            while((entry = ldif.getNext()) != null)
            {
                boolean done = generateImpExEntry(entry, mode);
                if(done)
                {
                    imported++;
                    continue;
                }
                skipped++;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        String line = "Read " + imported + skipped + " total entries. ";
        if(skipped == 0 && imported > 0)
        {
            log.info(line + " -- All successfully imported.");
            success = true;
        }
        if(skipped > 0 && imported == 0)
        {
            log.info(line + " -- All skipped to import.");
            success = false;
        }
        if(skipped > 0 && imported > 0)
        {
            log.info(line + " -- " + line + " sucessfuly imported, " + imported + " skipped.");
            success = true;
        }
        else
        {
            success = true;
        }
        this.generatedImpExMedia = ImpExManager.getInstance().createImpExMedia("LDIF2ImpEx-" + System.currentTimeMillis(), "UTF-8");
        this.generatedImpExMedia.setData(new DataInputStream(new ByteArrayInputStream(getExportAsString().getBytes("UTF-8"))), this.generatedImpExMedia
                        .getCode() + ".csv", "text/x-comma-separated-values");
        return success;
    }


    private List<String> escapeValues(String attribute, List<String> values, boolean escaping)
    {
        if(log.isDebugEnabled())
        {
            log.debug("escapeValues: " + attribute + ", " + values + ", " + escaping + " )");
        }
        List<String> str = new ArrayList<>();
        if(values == null)
        {
            if(log.isDebugEnabled())
            {
                log.debug("value for attributte " + attribute + " is null ");
            }
            return str;
        }
        Map<String, String> mappedValues = this.config.getMappedValues(attribute);
        for(String value : values)
        {
            if(value instanceof String)
            {
                String temp = value;
                temp = (mappedValues.get(temp) != null) ? mappedValues.get(temp) : temp;
                if(escaping)
                {
                    Matcher matcher = this.collectionHandlingPattern.matcher(temp);
                    str.add(matcher.replaceAll("\\\\,"));
                    continue;
                }
                str.add(temp);
                continue;
            }
            str.add("<not a string>");
        }
        return str;
    }


    public ImpExEntry generateImportLine(LDAPGenericObject obj, String mode)
    {
        if(log.isDebugEnabled())
        {
            log.debug("generateImportLine...");
        }
        ImpExEntry.HeaderMode impexmode = null;
        if(mode.equalsIgnoreCase(ImpExConstants.Syntax.Mode.INSERT))
        {
            impexmode = ImpExEntry.HeaderMode.INSERT;
        }
        else if(mode.equalsIgnoreCase(ImpExConstants.Syntax.Mode.UPDATE))
        {
            impexmode = ImpExEntry.HeaderMode.UPDATE;
        }
        else if(mode.equalsIgnoreCase(ImpExConstants.Syntax.Mode.INSERT_UPDATE))
        {
            impexmode = ImpExEntry.HeaderMode.INSERT_UPDATE;
        }
        else
        {
            throw new UnsupportedOperationException("Unsupported mode '" + mode + "'");
        }
        ImpExEntry impex = new ImpExEntry(impexmode, obj.getHybrisTypeCode());
        if(obj.getHybrisTypeCode() == null)
        {
            return null;
        }
        AttributesValueObject typeconfig = this.config.getTypeConfiguration(obj.getHybrisTypeCode());
        if(typeconfig == null)
        {
            return null;
        }
        for(AttributeValueObject attrib : typeconfig.getAllAttributes())
        {
            HeaderEntry headerEntry = attrib.getHeaderEntry();
            String currentLdapAttribute = attrib.getLdap();
            Hashtable<String, List> attributes = obj.getAttributes();
            List<String> currentLdapValue = attributes.get(currentLdapAttribute);
            impex.addImpExHeaderAttribute(headerEntry.toString());
            if(headerEntry.isCollectionAttribute(obj.getHybrisTypeCode()))
            {
                impex.addValues(currentLdapAttribute, escapeValues(currentLdapAttribute, currentLdapValue, true));
                continue;
            }
            impex.addValues(currentLdapAttribute,
                            (currentLdapValue != null) ? escapeValues(currentLdapAttribute, currentLdapValue, false) :
                                            Collections.EMPTY_LIST);
        }
        return impex;
    }


    private boolean generateImpExEntry(Attributes entries, String mode) throws NamingException
    {
        if(log.isDebugEnabled())
        {
            log.debug("generateImpExEntry...");
        }
        boolean done = false;
        LDAPGenericObject obj = new LDAPGenericObject(this.config, entries);
        ImpExEntry entry = generateImportLine(obj, mode);
        if(entry != null && obj.getHybrisTypeCode() != null)
        {
            String after = this.config.getAfter(obj.getHybrisTypeCode());
            String before = this.config.getBefore(obj.getHybrisTypeCode());
            entry.addImpExHeaderDefault(this.config.getDefaultHeaderEntry(obj.getHybrisTypeCode()));
            if(log.isDebugEnabled())
            {
                log.debug("#");
                log.debug("# " + obj.getDN());
                log.debug("#");
                log.debug(entry.getHeader());
                log.debug(before);
                log.debug(entry.getValueLine());
                log.debug(after);
                log.debug("");
            }
            this.impexExport.append("#\n# " + obj.getDN() + "\n#\n");
            this.impexExport.append(entry.getHeader());
            this.impexExport.append("\n");
            boolean closeAfter = false;
            boolean closeBefore = false;
            if(before != null && before.length() > 0)
            {
                this.impexExport.append("\"#% beforeEach:\n");
                this.impexExport.append(before);
                this.impexExport.append("\n");
                closeBefore = true;
            }
            if(after != null && after.length() > 0)
            {
                this.impexExport.append("\"#% afterEach:\n");
                this.impexExport.append(after);
                this.impexExport.append("\n");
                closeAfter = true;
            }
            this.impexExport.append(entry.getValueLine());
            if(closeAfter)
            {
                this.impexExport.append("\n#% afterEach:end");
                this.impexExport.append("\n");
            }
            if(closeBefore)
            {
                this.impexExport.append("#% beforeEach:end");
                this.impexExport.append("\n");
            }
            this.impexExport.append("\n\n");
            done = true;
        }
        return done;
    }


    public ImpExMedia getExportAsMedia()
    {
        return this.generatedImpExMedia;
    }


    public String getExportAsString()
    {
        return this.impexExport.toString();
    }
}

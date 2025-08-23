package de.hybris.platform.ldap;

import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.ldap.jalo.configuration.ConfigurationParser;
import de.hybris.platform.ldap.jalo.configuration.valueobject.AttributeValueObject;
import de.hybris.platform.ldap.jalo.configuration.valueobject.AttributesValueObject;
import de.hybris.platform.ldap.jalo.configuration.valueobject.ConfigValueObject;
import de.hybris.platform.util.CSVConstants;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

public class LDIFWriter
{
    private static final Logger log = Logger.getLogger(LDIFWriter.class.getName());
    private final ConfigurationParser parser;
    private final ConfigValueObject config;


    public LDIFWriter(String configuration) throws ParseAbortException, JaloBusinessException, FileNotFoundException
    {
        this.parser = new ConfigurationParser();
        this.parser.parse(new FileInputStream(configuration));
        this.config = this.parser.getConfig();
    }


    public void export(List items) throws ImpExException
    {
        StringBuilder header = new StringBuilder();
        for(Iterator<Item> it = items.iterator(); it.hasNext(); )
        {
            Item item = it.next();
            if(log.isDebugEnabled())
            {
                log.debug("generating header for item: " + item);
            }
            ComposedType type = TypeManager.getInstance().getComposedType(item.getClass());
            header.append("INSERT ").append(type.getCode()).append(CSVConstants.DEFAULT_FIELD_SEPARATOR).append(" ");
            AttributesValueObject avo = this.config.getTypeConfiguration(type.getCode());
            for(AttributeValueObject attrib : avo.getAllAttributes())
            {
                header.append(attrib.getHeaderEntry()).append(CSVConstants.DEFAULT_FIELD_SEPARATOR).append(" ");
            }
            header.append("\n");
        }
        if(log.isDebugEnabled())
        {
            log.debug("generated header: " + header.toString());
        }
    }
}

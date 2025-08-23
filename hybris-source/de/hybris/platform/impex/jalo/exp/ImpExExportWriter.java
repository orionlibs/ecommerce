package de.hybris.platform.impex.jalo.exp;

import de.hybris.platform.impex.jalo.DocumentIDRegistry;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExReader;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.header.UnresolvedValueException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public abstract class ImpExExportWriter
{
    private static final Logger log = Logger.getLogger(ImpExExportWriter.class);
    private HeaderDescriptor currentHeader = null;
    private final Language defaultLanguage;
    private int columnOffset = 0;


    protected ImpExExportWriter()
    {
        this(null);
    }


    protected ImpExExportWriter(SessionContext ctx)
    {
        this.defaultLanguage = JaloSession.getCurrentSession().getSessionContext().getLanguage();
    }


    public abstract void newLine();


    public abstract void comment(String paramString);


    public abstract void close();


    public void setCurrentHeader(String headerLine, EnumerationValue headerValidationMode) throws ImpExException
    {
        setCurrentHeader(headerLine, headerValidationMode, new DocumentIDRegistry(), true);
    }


    public void setCurrentHeader(String headerLine, EnumerationValue headerValidationMode, DocumentIDRegistry docIDRegistry) throws ImpExException
    {
        setCurrentHeader(ImpExReader.parseHeader(headerLine, headerValidationMode, docIDRegistry));
    }


    public void setCurrentHeader(String headerLine, EnumerationValue headerValidationMode, DocumentIDRegistry docIDRegistry, boolean outputHeader) throws ImpExException
    {
        setCurrentHeader(ImpExReader.parseHeader(headerLine, headerValidationMode, docIDRegistry));
    }


    public void setCurrentHeader(HeaderDescriptor header)
    {
        this.currentHeader = header;
    }


    public abstract void writeCurrentHeader(boolean paramBoolean) throws ImpExException;


    public abstract void writeLine(Item paramItem) throws ImpExException;


    public HeaderDescriptor getCurrentHeader()
    {
        return this.currentHeader;
    }


    protected Language getLanguage(StandardColumnDescriptor cd) throws HeaderValidationException
    {
        Language ret = cd.getLanguage();
        if(ret == null)
        {
            if(this.defaultLanguage == null)
            {
                throw new HeaderValidationException("cannot export localized column without default session language", 0);
            }
            return this.defaultLanguage;
        }
        return ret;
    }


    protected Map<String, Object> getAllAttributeValues(Set<StandardColumnDescriptor> columns, Item item) throws UnresolvedValueException
    {
        Map<String, Object> result = new HashMap<>();
        try
        {
            JaloSession.getCurrentSession().createLocalSessionContext().setLanguage(null);
            for(StandardColumnDescriptor cd : columns)
            {
                if(!result.containsKey(cd.getQualifier()))
                {
                    result.put(cd.getQualifier(), getAttributeValue(cd, item));
                }
            }
        }
        finally
        {
            JaloSession.getCurrentSession().removeLocalSessionContext();
        }
        return result;
    }


    protected Object getAttributeValue(StandardColumnDescriptor cd, Item item) throws UnresolvedValueException
    {
        try
        {
            return item.getAttribute(cd.getQualifier());
        }
        catch(Exception e)
        {
            throw new UnresolvedValueException(e, "error reading attribute " + cd
                            .getAttributeDescriptor().getEnclosingType().getCode() + "." + cd.getQualifier() + " : " + e.getMessage(), null);
        }
    }


    public void setColumnOffset(int offset)
    {
        this.columnOffset = offset;
    }


    public int getColumnOffset()
    {
        return this.columnOffset;
    }


    public abstract Object getExportWriter();


    public abstract void writeSrcLine(String paramString) throws IOException;


    public abstract void writeComment(String paramString) throws IOException;
}

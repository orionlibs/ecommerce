package de.hybris.platform.impex.jalo.exp;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.AbstractColumnDescriptor;
import de.hybris.platform.impex.jalo.header.AbstractDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.header.UnresolvedValueException;
import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.impex.jalo.translators.CollectionValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.znerd.xmlenc.LineBreak;
import org.znerd.xmlenc.XMLOutputter;

public class ImpExXMLExportWriter extends ImpExExportWriter
{
    private static final Logger log = Logger.getLogger(ImpExXMLExportWriter.class);
    public static final String IMPEX_ROOT_TAG = "impex";
    public static final String IMPEX_HEADER_TAG = "header";
    public static final String IMPEX_MODE_ATTR = "mode";
    public static final String IMPEX_TYPE_ATTR = "type";
    private final HeaderDescriptor previousHeader = null;
    protected Writer writer = null;
    protected XMLOutputter xmlout = null;


    public ImpExXMLExportWriter(Writer writer, String encoding) throws UnsupportedEncodingException
    {
        this(writer, encoding, null);
    }


    public ImpExXMLExportWriter(Writer writer, String encoding, SessionContext ctx) throws UnsupportedEncodingException
    {
        super(ctx);
        this.writer = writer;
        this.xmlout = new XMLOutputter(this.writer, encoding);
        this.xmlout.setLineBreak(LineBreak.DOS);
        this.xmlout.setIndentation("\t");
    }


    private void ioError(IOException e)
    {
        log.error("Error while accessing output stream", e);
    }


    public void comment(String text)
    {
        try
        {
            this.xmlout.comment(text);
        }
        catch(IOException e)
        {
            ioError(e);
        }
    }


    public void close()
    {
        try
        {
            this.xmlout.endTag();
            this.xmlout.close();
            this.xmlout.getWriter().close();
        }
        catch(IOException e)
        {
            ioError(e);
        }
    }


    public Object getExportWriter()
    {
        return this.writer;
    }


    public void newLine()
    {
    }


    public void writeCurrentHeader(boolean asComment) throws ImpExException
    {
        try
        {
            if(asComment)
            {
                if(this.previousHeader != null)
                {
                    this.xmlout.endTag();
                }
                else
                {
                    this.xmlout.startTag("impex");
                }
                this.xmlout.startTag(getCurrentHeader().getTypeCode());
            }
            else
            {
                AbstractDescriptor.HeaderParams headerParams = (AbstractDescriptor.HeaderParams)getCurrentHeader().getDescriptorData();
                if(this.previousHeader != null)
                {
                    this.xmlout.endTag();
                }
                else
                {
                    this.xmlout.startTag("impex");
                }
                this.xmlout.startTag("header");
                this.xmlout.attribute("mode", headerParams.getMode());
                this.xmlout.attribute("type", headerParams.getType());
                for(Iterator<Map.Entry> it = headerParams.getModifiers().entrySet().iterator(); it.hasNext(); )
                {
                    Map.Entry entry = it.next();
                    this.xmlout.attribute((String)entry.getKey(), (String)entry.getValue());
                }
                List<AbstractColumnDescriptor> columns = getCurrentHeader().getColumns();
                for(int i = 0; i < columns.size(); i++)
                {
                    AbstractColumnDescriptor col = columns.get(i);
                    if(col != null)
                    {
                        writeHeaderColumn(col);
                    }
                }
                this.xmlout.endTag();
            }
        }
        catch(IOException e)
        {
            throw new ImpExException(e);
        }
    }


    protected void writeHeaderColumn(AbstractColumnDescriptor col) throws IOException
    {
        writeHeaderColumn((AbstractDescriptor.ColumnParams)col.getDescriptorData(), col instanceof SpecialColumnDescriptor);
    }


    protected void writeHeaderColumn(AbstractDescriptor.ColumnParams colParams, boolean special) throws IOException
    {
        this.xmlout.startTag(colParams.getQualifier());
        if(special)
        {
            this.xmlout.attribute("special", "true");
        }
        for(Iterator<Map.Entry> it = colParams.getModifiers().entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry entry = it.next();
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            this.xmlout.attribute(key, value);
        }
        List[] patterns = colParams.getItemPatternLists();
        if(patterns != null && patterns.length > 0)
        {
            if(patterns.length > 1)
            {
                throw new IllegalArgumentException("alternative patterns not yet implemented");
            }
            for(int i = 0, s = patterns[0].size(); i < s; i++)
            {
                AbstractDescriptor.ColumnParams params = patterns[0].get(i);
                writeHeaderColumn(params, false);
            }
        }
        this.xmlout.endTag();
    }


    public void writeLine(Item item) throws ImpExException
    {
        if(getCurrentHeader() == null)
        {
            throw new ImpExException("no header set");
        }
        ComposedType targetType = item.getComposedType();
        if(!getCurrentHeader().isPermittedType(targetType))
        {
            throw new ImpExException("type " + targetType.getCode() + " is no permitted type");
        }
        try
        {
            this.xmlout.startTag(getCurrentHeader().getTypeCode());
            this.xmlout.attribute("type", targetType.getCode());
            Map localized = collectLocalizedColumns(getCurrentHeader());
            Set<String> processed = new HashSet();
            for(Iterator<AbstractColumnDescriptor> iter = getCurrentHeader().getColumns().iterator(); iter.hasNext(); )
            {
                AbstractColumnDescriptor cd = iter.next();
                if(cd != null)
                {
                    String qualifier = cd.getQualifier();
                    if(processed.contains(qualifier.toLowerCase(LocaleHelper.getPersistenceLocale())))
                    {
                        break;
                    }
                    if(cd instanceof SpecialColumnDescriptor)
                    {
                        writeSpecialColumn(item, (SpecialColumnDescriptor)cd);
                    }
                    else
                    {
                        StandardColumnDescriptor descriptor = (StandardColumnDescriptor)cd;
                        if(localized.containsKey(qualifier.toLowerCase(LocaleHelper.getPersistenceLocale())))
                        {
                            writeLocalizedColumn(item, (List)localized.get(descriptor.getQualifier()));
                        }
                        else if(descriptor.getAttributeDescriptor().getAttributeType() instanceof de.hybris.platform.jalo.type.CollectionType)
                        {
                            writeCollectionColumn(item, descriptor);
                        }
                        else
                        {
                            writeCommonColumn(item, descriptor);
                        }
                    }
                    processed.add(qualifier.toLowerCase(LocaleHelper.getPersistenceLocale()));
                }
            }
            this.xmlout.endTag();
        }
        catch(IOException e)
        {
            ioError(e);
        }
    }


    private void writeCommonColumn(Item item, StandardColumnDescriptor cd) throws IOException, ImpExException
    {
        String data;
        try
        {
            data = cd.exportValue(getAttributeValue(cd, item));
        }
        catch(UnresolvedValueException e)
        {
            data = "<ignore> (error: " + e.getMessage() + ")";
        }
        this.xmlout.startTag(cd.getQualifier());
        this.xmlout.cdata(data);
        this.xmlout.endTag();
    }


    private void writeSpecialColumn(Item item, SpecialColumnDescriptor cd) throws IOException, ImpExException
    {
        String data;
        try
        {
            data = cd.performExport(item);
        }
        catch(UnresolvedValueException e)
        {
            data = "<ignore> (error: " + e.getMessage() + ")";
        }
        this.xmlout.startTag(cd.getQualifier());
        this.xmlout.cdata(data);
        this.xmlout.endTag();
    }


    private void writeLocalizedColumn(Item item, List<StandardColumnDescriptor> descriptors) throws IOException, ImpExException
    {
        if(descriptors.isEmpty())
        {
            return;
        }
        StandardColumnDescriptor firstColumn = descriptors.get(0);
        this.xmlout.startTag(firstColumn.getQualifier());
        int i, s;
        for(i = 0, s = descriptors.size(); i < s; i++)
        {
            StandardColumnDescriptor cd = descriptors.get(i);
            if(cd.getLanguageIso() == null)
            {
                String data;
                try
                {
                    data = cd.exportValue(((Map)getAttributeValue(cd, item)).get(getLanguage(cd)));
                }
                catch(UnresolvedValueException e)
                {
                    data = "<ignore> (error: " + e.getMessage() + ")";
                }
                this.xmlout.cdata(data);
                break;
            }
        }
        this.xmlout.startTag("localized");
        for(i = 0, s = descriptors.size(); i < s; i++)
        {
            StandardColumnDescriptor cd = descriptors.get(i);
            if(cd.getLanguageIso() != null)
            {
                String data;
                try
                {
                    data = cd.exportValue(getAttributeValue(cd, item));
                }
                catch(UnresolvedValueException e)
                {
                    data = "<ignore> (error: " + e.getMessage() + ")";
                }
                this.xmlout.startTag(cd.getLanguageIso());
                this.xmlout.cdata(data);
                this.xmlout.endTag();
            }
        }
        this.xmlout.endTag();
        this.xmlout.endTag();
    }


    private void writeCollectionColumn(Item item, StandardColumnDescriptor descriptor) throws IOException, ImpExException
    {
        this.xmlout.startTag(descriptor.getQualifier());
        Collection coll = (Collection)getAttributeValue(descriptor, item);
        if(!(descriptor.getValueTranslator() instanceof CollectionValueTranslator))
        {
            this.xmlout.cdata(descriptor.exportValue(coll));
        }
        else if(coll != null)
        {
            this.xmlout.startTag("collection");
            CollectionValueTranslator translator = (CollectionValueTranslator)descriptor.getValueTranslator();
            AbstractValueTranslator valueTranslator = translator.getElementTranslator();
            for(Iterator it = coll.iterator(); it.hasNext(); )
            {
                Object value = it.next();
                String data = valueTranslator.exportValue(value);
                this.xmlout.startTag("entry");
                this.xmlout.cdata(data);
                this.xmlout.endTag();
            }
            this.xmlout.endTag();
        }
        this.xmlout.endTag();
    }


    private Map collectLocalizedColumns(HeaderDescriptor header)
    {
        Map<Object, Object> result = new HashMap<>();
        for(Iterator<AbstractColumnDescriptor> iter = header.getColumns().iterator(); iter.hasNext(); )
        {
            AbstractColumnDescriptor cd = iter.next();
            if(cd != null && !(cd instanceof SpecialColumnDescriptor))
            {
                StandardColumnDescriptor descriptor = (StandardColumnDescriptor)cd;
                if(descriptor.isLocalized())
                {
                    List<StandardColumnDescriptor> descs = (List)result.get(descriptor.getQualifier());
                    if(descs == null)
                    {
                        descs = new ArrayList();
                    }
                    descs.add(descriptor);
                    result.put(descriptor.getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()), descs);
                }
            }
        }
        return result;
    }


    public void writeComment(String scrline) throws IOException
    {
    }


    public void writeSrcLine(String scrline) throws IOException
    {
    }
}

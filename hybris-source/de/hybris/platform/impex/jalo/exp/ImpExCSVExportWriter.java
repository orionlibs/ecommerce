package de.hybris.platform.impex.jalo.exp;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.AbstractColumnDescriptor;
import de.hybris.platform.impex.jalo.header.DocumentIDColumnDescriptor;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.header.UnresolvedValueException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.CSVWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class ImpExCSVExportWriter extends ImpExExportWriter
{
    private static final Logger log = Logger.getLogger(ImpExCSVExportWriter.class);
    private CSVWriter writer = null;


    public ImpExCSVExportWriter(CSVWriter writer)
    {
        this(writer, null);
    }


    public ImpExCSVExportWriter(CSVWriter writer, SessionContext ctx)
    {
        super(ctx);
        this.writer = writer;
    }


    public void comment(String text)
    {
        try
        {
            this.writer.writeSrcLine("" + this.writer.getCommentchar() + " " + this.writer.getCommentchar());
        }
        catch(IOException e)
        {
            log.error("Error while writing comment to underlying writer: " + e.getMessage(), e);
        }
    }


    public void close()
    {
        try
        {
            this.writer.close();
        }
        catch(IOException e)
        {
            log.error("Error while closing underlying writer: " + e.getMessage(), e);
        }
    }


    public Object getExportWriter()
    {
        return this.writer;
    }


    public void newLine()
    {
        try
        {
            this.writer.writeSrcLine("");
        }
        catch(IOException e)
        {
            log.error("Error while writing a new line to underlying writer: " + e.getMessage(), e);
        }
    }


    public void writeCurrentHeader(boolean asComment) throws ImpExException
    {
        int offset = getColumnOffset();
        try
        {
            if(asComment)
            {
                Map<Integer, String> line = new HashMap<>();
                if(offset >= 0)
                {
                    line.put(Integer.valueOf(offset), getCurrentHeader().getConfiguredComposedType().getName());
                }
                int pos = 0;
                for(Iterator<AbstractColumnDescriptor> iter = getCurrentHeader().getColumns().iterator(); iter.hasNext(); )
                {
                    AbstractColumnDescriptor cd = iter.next();
                    if(cd != null && (!(cd instanceof StandardColumnDescriptor) || !((StandardColumnDescriptor)cd).isVirtual()))
                    {
                        String quali = cd.getDescriptorData().getModifier("alias");
                        if(quali == null)
                        {
                            quali = cd.getQualifierForComment();
                        }
                        if((pos = cd.getValuePosition() + getColumnOffset()) >= 0)
                        {
                            line.put(Integer.valueOf(pos), quali);
                        }
                    }
                }
                this.writer.writeComment(" " + this.writer.createCSVLine(line));
            }
            else
            {
                Map<Integer, String> header = getCurrentHeader().dump();
                if(offset != 0)
                {
                    Map<Integer, String> shiftedHeader = new HashMap<>();
                    for(int i = 0; i < offset; i++)
                    {
                        shiftedHeader.put(Integer.valueOf(i), "");
                    }
                    int newKey = 0;
                    for(Map.Entry<Integer, String> entry : header.entrySet())
                    {
                        if((newKey = ((Integer)entry.getKey()).intValue() + offset) >= 0)
                        {
                            shiftedHeader.put(Integer.valueOf(newKey), entry.getValue());
                        }
                    }
                    header = shiftedHeader;
                }
                this.writer.write(header);
            }
        }
        catch(IOException ioe)
        {
            throw new ImpExException(ioe);
        }
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
            throw new ImpExException("type " + targetType.getCode() + " is no permitted type because " +
                            getCurrentHeader().getOmittedTypesMessages(targetType.getPK()));
        }
        Map<Integer, String> data = new HashMap<>();
        int colOffset = getColumnOffset();
        if(colOffset >= 0 && !getCurrentHeader().getConfiguredComposedType().equals(targetType))
        {
            for(int i = 0; i <= colOffset; i++)
            {
                if(i < colOffset)
                {
                    data.put(Integer.valueOf(i), "");
                }
                else if(i == colOffset)
                {
                    data.put(Integer.valueOf(i), targetType.getCode());
                }
            }
        }
        Set<StandardColumnDescriptor> standardColumns = getCurrentHeader().getAllColumns(targetType);
        Map<String, Object> standardValues = getAllAttributeValues(standardColumns, item);
        for(Iterator<StandardColumnDescriptor> iterator1 = standardColumns.iterator(); iterator1.hasNext(); )
        {
            StandardColumnDescriptor cd = iterator1.next();
            if(cd != null && !cd.isVirtual())
            {
                int pos = cd.getValuePosition() + colOffset;
                if(pos >= 0)
                {
                    try
                    {
                        if(cd.isLocalized())
                        {
                            data.put(Integer.valueOf(pos), cd
                                            .exportValue(((Map)standardValues.get(cd.getQualifier())).get(
                                                            getLanguage(cd))));
                            continue;
                        }
                        data.put(Integer.valueOf(pos), cd.exportValue(standardValues.get(cd.getQualifier())));
                    }
                    catch(UnresolvedValueException e)
                    {
                        data.put(Integer.valueOf(pos), "<ignore> (error: " + e.getMessage() + ")");
                    }
                }
            }
        }
        for(Iterator<SpecialColumnDescriptor> iterator = getCurrentHeader().getSpecificColumns(SpecialColumnDescriptor.class).iterator(); iterator.hasNext(); )
        {
            SpecialColumnDescriptor cd = iterator.next();
            if(cd != null)
            {
                int pos = cd.getValuePosition() + colOffset;
                if(pos >= 0)
                {
                    try
                    {
                        data.put(Integer.valueOf(pos), cd.performExport(item));
                    }
                    catch(UnresolvedValueException e)
                    {
                        data.put(Integer.valueOf(pos), "<ignore> (error: " + e.getMessage() + ")");
                    }
                }
            }
        }
        Iterator<DocumentIDColumnDescriptor> iter = getCurrentHeader().getSpecificColumns(DocumentIDColumnDescriptor.class).iterator();
        while(iter.hasNext())
        {
            DocumentIDColumnDescriptor dcd = iter.next();
            if(dcd != null)
            {
                int pos = dcd.getValuePosition() + colOffset;
                if(pos >= 0)
                {
                    data.put(Integer.valueOf(pos), dcd.generateIDForItem(item));
                }
            }
        }
        try
        {
            this.writer.write(data);
        }
        catch(IOException e)
        {
            log.error("Error while writing data to underlying writer: " + e.getMessage(), e);
        }
    }


    public void writeComment(String scrline) throws IOException
    {
        this.writer.writeComment(scrline);
    }


    public void writeSrcLine(String scrline) throws IOException
    {
        this.writer.writeSrcLine(scrline);
    }
}

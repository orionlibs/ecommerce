package de.hybris.platform.impex.jalo.exp;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.impex.jalo.util.ImpExUtils;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.CSVWriter;
import de.hybris.platform.util.Utilities;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class ExportUtils
{
    private static final Logger log = Logger.getLogger(ExportUtils.class);


    public static ImpExExportMedia createDataExportTarget(String code)
    {
        Map<String, Object> exportMediaValues = new HashMap<>();
        exportMediaValues.put("code", code);
        exportMediaValues.put("commentCharacter", Character.valueOf('#'));
        exportMediaValues.put("fieldSeparator", Character.valueOf(CSVConstants.DEFAULT_FIELD_SEPARATOR));
        exportMediaValues.put("quoteCharacter", Character.valueOf(CSVConstants.DEFAULT_QUOTE_CHARACTER));
        try
        {
            exportMediaValues.put("encoding", Utilities.resolveEncoding(CSVConstants.DEFAULT_ENCODING));
        }
        catch(UnsupportedEncodingException e)
        {
            log.warn("Encoding not found! Unable to set default encoding to '" + CSVConstants.DEFAULT_ENCODING + "'", e);
        }
        return ImpExManager.getInstance().createImpExExportMedia(exportMediaValues);
    }


    public static ImpExExportMedia createDataExportTarget()
    {
        return createMediasExportTarget("exported_data_" + System.currentTimeMillis());
    }


    public static ImpExExportMedia createMediasExportTarget(String code)
    {
        Map<String, Object> exportMediaValues = new HashMap<>();
        exportMediaValues.put("code", code);
        return ImpExManager.getInstance().createImpExExportMedia(exportMediaValues);
    }


    public static ImpExExportMedia createMediasExportTarget()
    {
        return createMediasExportTarget("exported_medias_" + System.currentTimeMillis());
    }


    public static ImpExMedia createExportScript(HeaderLibrary headerlibrary, Collection<Item> items) throws ImpExException
    {
        try
        {
            StringBuffer script = mergeHeaderAndItemList(headerlibrary, items);
            return ImpExManager.getInstance().createImpExMedia("exportscript_" + System.currentTimeMillis(), CSVConstants.DEFAULT_ENCODING, script
                            .toString());
        }
        catch(IOException e)
        {
            throw new ImpExException(e);
        }
    }


    public static Set<ComposedType> getExportableRootTypes(ScriptGenerator callback)
    {
        Set<ComposedType> types = new LinkedHashSet<>();
        Set<ComposedType> current = Collections.singleton(TypeManager.getInstance().getComposedType(Item.class));
        while(!current.isEmpty())
        {
            Set<ComposedType> next = new LinkedHashSet<>();
            for(ComposedType t : current)
            {
                if(!filterTypeCompletely(t))
                {
                    if(t.isAbstract())
                    {
                        next.addAll(t.getSubTypes());
                        continue;
                    }
                    types.add(t);
                    types.addAll(collectSubtypesWithOwnDeployment(t));
                }
            }
            current = next;
        }
        return types;
    }


    public static boolean filterTypeCompletely(ComposedType type)
    {
        return (type.isJaloOnly() || type instanceof de.hybris.platform.jalo.type.ViewType);
    }


    public static Set<ComposedType> collectSubtypesWithOwnDeployment(ComposedType rootType)
    {
        String myTable = rootType.getCode().equalsIgnoreCase("GenericItem") ? null : rootType.getTable();
        Set<ComposedType> ret = new HashSet<>();
        Set<ComposedType> current = rootType.getSubTypes();
        while(!current.isEmpty())
        {
            Set<ComposedType> next = new HashSet();
            for(ComposedType t : current)
            {
                if(!filterTypeCompletely(t))
                {
                    String tbl = t.getTable();
                    if(!t.isAbstract() && myTable != tbl && (myTable == null || !myTable.equalsIgnoreCase(tbl)))
                    {
                        ret.add(t);
                        ret.addAll(collectSubtypesWithOwnDeployment(t));
                        continue;
                    }
                    next.addAll(t.getSubTypes());
                }
            }
            current = next;
        }
        return ret;
    }


    public static StringBuffer mergeHeaderAndItemList(HeaderLibrary library, Collection<Item> itemlist) throws IOException
    {
        Collection<Item> itemsSortedByType = null;
        Map<ComposedType, Collection<Item>> tc2item = new HashMap<>();
        StringWriter script = new StringWriter();
        CSVWriter scriptWriter = new CSVWriter(script);
        for(Item item : itemlist)
        {
            ComposedType ct = item.getComposedType();
            if(tc2item.get(ct) == null)
            {
                itemsSortedByType = new ArrayList<>();
                tc2item.put(ct, itemsSortedByType);
            }
            else
            {
                itemsSortedByType = tc2item.get(ct);
            }
            itemsSortedByType.add(item);
        }
        for(Iterator<Map.Entry<ComposedType, Collection<Item>>> it = tc2item.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry<ComposedType, Collection<Item>> me = it.next();
            ComposedType ct = me.getKey();
            Collection<Item> sortedItems = me.getValue();
            String attribs = library.getHeader(ct, String.valueOf(library.getFieldSeparatorAsPrimitive()), true);
            if(attribs == null)
            {
                attribs = "PK";
            }
            String headerSrc = "INSERT " + ct.getCode() + library.getFieldSeparatorAsPrimitive() + attribs;
            scriptWriter.writeSrcLine("");
            scriptWriter.writeComment("-----------------------------------------------------------");
            scriptWriter.writeSrcLine(headerSrc);
            scriptWriter.writeComment("-----------------------------------------------------------");
            scriptWriter.writeSrcLine("\"#% impex.exportItems( new String[]{");
            int size = sortedItems.size();
            int count = 0;
            StringBuffer vl = new StringBuffer();
            for(Item item : sortedItems)
            {
                vl.append("\"\"").append(item.getPK().toString()).append("\"\"");
                if(++count < size)
                {
                    vl.append(", ");
                }
                if(count % 5 == 0 && count != size)
                {
                    scriptWriter.writeSrcLine(vl.toString());
                    vl = new StringBuffer();
                }
            }
            scriptWriter.writeSrcLine(vl.toString());
            scriptWriter.writeSrcLine("} );\"");
            scriptWriter.writeSrcLine("");
        }
        scriptWriter.close();
        return script.getBuffer();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static StringBuilder getScriptContent(ImpExMedia media)
    {
        return ImpExUtils.getContent((Media)media);
    }
}

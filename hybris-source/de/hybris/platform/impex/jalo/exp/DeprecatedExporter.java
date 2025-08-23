package de.hybris.platform.impex.jalo.exp;

import de.hybris.platform.core.BeanShellUtils;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.jalo.Item;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DeprecatedExporter
{
    private final Exporter exporter;


    public int hashCode()
    {
        return getExporter().hashCode();
    }


    public boolean equals(Object obj)
    {
        return getExporter().equals(obj);
    }


    public long getExportedItemsCount()
    {
        return getExporter().getExportedItemsCount();
    }


    public Export export() throws ImpExException
    {
        return getExporter().export();
    }


    public String toString()
    {
        return getExporter().toString();
    }


    public ImpExExportWriter getExExportWriter()
    {
        return getExporter().getExExportWriter();
    }


    public ImpExExportWriter getImpExExportWriter()
    {
        return getExporter().getImpExExportWriter();
    }


    public boolean hasNextHeader()
    {
        return getExporter().hasNextHeader();
    }


    public HeaderDescriptor getNextHeader()
    {
        return getExporter().getNextHeader();
    }


    public int getCurrentHeaderCount()
    {
        return getExporter().getCurrentHeaderCount();
    }


    public void exportAllHeader() throws ImpExException
    {
        getExporter().exportAllHeader();
    }


    public Collection<String> getResultingFiles()
    {
        return getExporter().getResultingFiles();
    }


    public final void exportItems(String typecode) throws ImpExException
    {
        getExporter().exportItems(typecode);
    }


    public final void exportItems(String typecode, boolean inclSubTypes) throws ImpExException
    {
        getExporter().exportItems(typecode, inclSubTypes);
    }


    public void exportItems(String typecode, int count) throws ImpExException
    {
        getExporter().exportItems(typecode, count);
    }


    public void exportItems(String typecode, int count, boolean inclSubTypes) throws ImpExException
    {
        getExporter().exportItems(typecode, count, inclSubTypes);
    }


    public void setTargetFile(String filename, boolean writeHeader) throws ImpExException
    {
        getExporter().setTargetFile(filename, writeHeader);
    }


    public void setTargetFile(String filename, boolean writeHeader, int linesToSkip, int offset) throws ImpExException
    {
        getExporter().setTargetFile(filename, writeHeader, linesToSkip, offset);
    }


    public void setTargetFile(String filename) throws ImpExException
    {
        getExporter().setTargetFile(filename);
    }


    public void setRelaxedMode(boolean isRelaxedMode) throws ImpExException
    {
        getExporter().setRelaxedMode(isRelaxedMode);
    }


    public void exportItems(String query, Map values, List resultClasses, boolean failOnUnknownFields, boolean dontNeedTotal, int start, int count) throws ImpExException
    {
        getExporter().exportItems(query, values, resultClasses, failOnUnknownFields, dontNeedTotal, start, count);
    }


    public void exportItemsFlexibleSearch(String query, Map values, List resultClasses, boolean failOnUnknownFields, boolean dontNeedTotal, int start, int count) throws ImpExException
    {
        getExporter().exportItemsFlexibleSearch(query, values, resultClasses, failOnUnknownFields, dontNeedTotal, start, count);
    }


    public void exportItemsFlexibleSearch(String query) throws ImpExException
    {
        getExporter().exportItemsFlexibleSearch(query);
    }


    public void exportItemsFlexibleSearch(String query, int count) throws ImpExException
    {
        getExporter().exportItemsFlexibleSearch(query, count);
    }


    public void exportItems(String[] pklist) throws ImpExException
    {
        getExporter().exportItems(pklist);
    }


    public void exportItems(Collection<Item> items) throws ImpExException
    {
        getExporter().exportItems(items);
    }


    public void setValidationMode(String mode) throws ImpExException
    {
        getExporter().setValidationMode(mode);
    }


    public void setLocale(Locale l) throws ImpExException
    {
        getExporter().setLocale(l);
    }


    public String getCurrentLocation()
    {
        return getExporter().getCurrentLocation();
    }


    public DeprecatedExporter(Exporter exporter)
    {
        this.exporter = exporter;
    }


    private Exporter getExporter()
    {
        BeanShellUtils.checkSLMode();
        return this.exporter;
    }
}

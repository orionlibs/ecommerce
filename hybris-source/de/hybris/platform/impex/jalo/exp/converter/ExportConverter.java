package de.hybris.platform.impex.jalo.exp.converter;

import de.hybris.platform.impex.jalo.exp.Export;
import de.hybris.platform.impex.jalo.exp.Report;

public interface ExportConverter extends AutoCloseable
{
    void setExport(Export paramExport);


    Export getExport();


    Report getReport();


    void start();


    default void close()
    {
    }
}

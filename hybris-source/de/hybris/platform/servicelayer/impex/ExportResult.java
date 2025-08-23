package de.hybris.platform.servicelayer.impex;

import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.impex.model.exp.ExportModel;

public interface ExportResult
{
    boolean isSuccessful();


    boolean isError();


    boolean isRunning();


    boolean isFinished();


    ImpExMediaModel getExportedData();


    ImpExMediaModel getExportedMedia();


    ExportModel getExport();
}

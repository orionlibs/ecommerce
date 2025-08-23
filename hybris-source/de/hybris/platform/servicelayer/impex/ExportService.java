package de.hybris.platform.servicelayer.impex;

import de.hybris.platform.impex.enums.ImpExValidationModeEnum;

public interface ExportService
{
    ExportResult exportData(ImpExResource paramImpExResource);


    ExportResult exportData(ExportConfig paramExportConfig);


    ImpExValidationResult validateExportScript(String paramString, ImpExValidationModeEnum paramImpExValidationModeEnum);
}

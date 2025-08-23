package de.hybris.platform.patches.data;

import de.hybris.platform.servicelayer.impex.ImportResult;

public class ImpexImportUnitResult
{
    private ImportResult importResult;
    private ImpexImportUnit impexImportUnit;


    public ImportResult getImportResult()
    {
        return this.importResult;
    }


    public void setImportResult(ImportResult importResult)
    {
        this.importResult = importResult;
    }


    public ImpexImportUnit getImpexImportUnit()
    {
        return this.impexImportUnit;
    }


    public void setImpexImportUnit(ImpexImportUnit impexImportUnit)
    {
        this.impexImportUnit = impexImportUnit;
    }
}

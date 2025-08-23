package de.hybris.platform.servicelayer.impex;

import java.util.stream.Stream;

public interface ImportService
{
    public static final String DISTRIBUTED_IMPEX_GLOBAL_FLAG = "impex.import.service.distributed.enabled";


    ImportResult importData(ImpExResource paramImpExResource);


    ImportResult importData(ImportConfig paramImportConfig);


    Stream<? extends ImpExError> collectImportErrors(ImportResult paramImportResult);
}

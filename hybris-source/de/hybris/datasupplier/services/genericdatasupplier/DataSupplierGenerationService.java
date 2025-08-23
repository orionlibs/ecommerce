package de.hybris.datasupplier.services.genericdatasupplier;

import java.io.File;
import java.util.List;
import java.util.Properties;

public interface DataSupplierGenerationService
{
    File generateDataSupplier(List<String> paramList, Properties paramProperties);
}

package com.hybris.backoffice.excel.translators.generic.factory;

import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.translators.generic.RequiredAttribute;

public interface ImportImpexFactoryStrategy
{
    boolean canHandle(RequiredAttribute paramRequiredAttribute, ImportParameters paramImportParameters);


    Impex create(RequiredAttribute paramRequiredAttribute, ImportParameters paramImportParameters);
}

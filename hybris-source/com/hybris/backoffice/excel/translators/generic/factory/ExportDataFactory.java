package com.hybris.backoffice.excel.translators.generic.factory;

import com.hybris.backoffice.excel.translators.generic.RequiredAttribute;
import java.util.Optional;

public interface ExportDataFactory
{
    Optional<String> create(RequiredAttribute paramRequiredAttribute, Object paramObject);
}

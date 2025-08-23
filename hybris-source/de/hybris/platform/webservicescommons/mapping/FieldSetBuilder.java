package de.hybris.platform.webservicescommons.mapping;

import de.hybris.platform.webservicescommons.mapping.impl.FieldSetBuilderContext;
import java.util.Set;

public interface FieldSetBuilder
{
    Set<String> createFieldSet(Class paramClass, String paramString1, String paramString2);


    Set<String> createFieldSet(Class paramClass, String paramString1, String paramString2, FieldSetBuilderContext paramFieldSetBuilderContext);
}

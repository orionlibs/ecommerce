package de.hybris.platform.impex;

import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import java.util.Map;
import java.util.Set;

public interface QueryParametrable<TYPE>
{
    Set<StandardColumnDescriptor> getUniqueColumns();


    Map<StandardColumnDescriptor, Object> getUniqueValues();


    Set<StandardColumnDescriptor> getSearchableColumns();


    Set<StandardColumnDescriptor> getNonSearchableColumns();


    TYPE getType();
}

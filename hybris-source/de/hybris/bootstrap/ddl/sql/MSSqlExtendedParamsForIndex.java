package de.hybris.bootstrap.ddl.sql;

import de.hybris.bootstrap.util.LocaleHelper;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.ddlutils.model.IndexColumn;

public class MSSqlExtendedParamsForIndex implements ExtendedParamsForIndex
{
    private final Set<IndexColumn> includeColumnCollection = new LinkedHashSet<>();


    public void addColumn(IndexColumn col)
    {
        this.includeColumnCollection.add(col);
    }


    public Collection<IndexColumn> getIncludeColumnCollection()
    {
        return this.includeColumnCollection;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof MSSqlExtendedParamsForIndex))
        {
            return false;
        }
        MSSqlExtendedParamsForIndex that = (MSSqlExtendedParamsForIndex)o;
        return Objects.equals(this.includeColumnCollection, that.includeColumnCollection);
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.includeColumnCollection});
    }


    public Object hashCodeIgnoreCase()
    {
        return getLowerColumnNamesFromIndexColumnStream(this.includeColumnCollection.stream());
    }


    public boolean equalsIgnoreCase(ExtendedParamsForIndex other)
    {
        if(this == other)
        {
            return true;
        }
        if(!(other instanceof MSSqlExtendedParamsForIndex))
        {
            return false;
        }
        MSSqlExtendedParamsForIndex that = (MSSqlExtendedParamsForIndex)other;
        Set<String> thisIncludeColumn = getLowerColumnNamesFromIndexColumnStream(this.includeColumnCollection.stream());
        Set<String> thatIncludeColumn = getLowerColumnNamesFromIndexColumnStream(that.includeColumnCollection.stream());
        return Objects.equals(thisIncludeColumn, thatIncludeColumn);
    }


    private Set<String> getLowerColumnNamesFromIndexColumnStream(Stream<IndexColumn> indexColumnStream)
    {
        return (Set<String>)indexColumnStream.map(IndexColumn::getName).map(name -> name.toLowerCase(LocaleHelper.getPersistenceLocale()))
                        .collect(Collectors.toSet());
    }
}

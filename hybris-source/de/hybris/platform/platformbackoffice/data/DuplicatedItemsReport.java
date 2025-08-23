package de.hybris.platform.platformbackoffice.data;

import de.hybris.platform.catalog.DuplicatedItemIdentifier;
import java.util.Collection;

public class DuplicatedItemsReport
{
    private final String catalog;
    private final String version;
    private final Collection<DuplicatedItemIdentifier> duplicatedIdentifiers;


    public DuplicatedItemsReport(String catalog, String version, Collection<DuplicatedItemIdentifier> duplicatedIdentifiers)
    {
        this.catalog = catalog;
        this.version = version;
        this.duplicatedIdentifiers = duplicatedIdentifiers;
    }


    public String getCatalog()
    {
        return this.catalog;
    }


    public String getVersion()
    {
        return this.version;
    }


    public Collection<DuplicatedItemIdentifier> getDuplicatedIdentifiers()
    {
        return this.duplicatedIdentifiers;
    }


    public String renderTextualReport()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Catalog: ").append(getCatalog()).append("\n");
        sb.append("Version: ").append(getVersion()).append("\n");
        sb.append("----------------------------------------------\n");
        sb.append("Composed type\tIdentifier\tCount\n");
        for(DuplicatedItemIdentifier duplicate : this.duplicatedIdentifiers)
        {
            sb.append(duplicate.getComposedType()).append("; [").append(duplicate.getCode().replace(";", " ")).append("]")
                            .append("; ").append(duplicate.getCount()).append("\n");
        }
        return sb.toString();
    }
}

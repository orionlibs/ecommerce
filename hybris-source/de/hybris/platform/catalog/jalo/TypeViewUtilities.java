package de.hybris.platform.catalog.jalo;

import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.ViewType;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public class TypeViewUtilities
{
    public static final String DUPLICATECATALOGITEMCODESVIEW = "DuplicateCatalogItemCodesView";
    public static final String CATALOGOVERVIEW = "CatalogOverview";
    private final CatalogManager catman = CatalogManager.getInstance();
    private static final String queryheader = "SELECT tbl.type, tbl.code, tbl.amount, ?cv, tbl.grp FROM ( \r\n";
    private static final String queryfooter = ") tbl ORDER BY tbl.grp ASC, tbl.code ASC, tbl.amount DESC";
    private static final String union = "UNION ALL\n";


    public void generateQuery(ViewType viewtype)
    {
        if("DuplicateCatalogItemCodesView".equals(viewtype.getCode()))
        {
            viewtype.setQuery(generateDuplicateCodesForCatalogVersionQuery());
        }
        else if("CatalogOverview".equals(viewtype.getCode()))
        {
            viewtype.setQuery(generateCatalogOverviewQuery());
        }
    }


    public String generateDuplicateCodesForCatalogVersionQuery()
    {
        int groupnumber = 1;
        boolean firstSubQuery = true;
        StringBuilder query = new StringBuilder();
        query.append("SELECT tbl.type, tbl.code, tbl.amount, ?cv, tbl.grp FROM ( \r\n");
        for(ComposedType catalogComposedType : this.catman.getAllCatalogItemRootTypes())
        {
            if(firstSubQuery)
            {
                firstSubQuery = false;
            }
            else
            {
                query.append("UNION\n");
            }
            createInnerSQLQuery(query, catalogComposedType, 1);
        }
        query.append(") tbl ORDER BY tbl.grp ASC, tbl.code ASC, tbl.amount DESC");
        return query.toString();
    }


    private String createInnerSQLQuery(StringBuilder query, ComposedType comptyp, int groupnumber)
    {
        String catverqualifier = this.catman.getCatalogVersionAttribute(comptyp).getQualifier();
        List<String> groupBySet = new ArrayList<>();
        for(AttributeDescriptor ad : this.catman.getUniqueKeyAttributes(comptyp))
        {
            groupBySet.add("{ct:" + ad.getQualifier() + "}");
        }
        String groupbyString = StringUtils.join(groupBySet, ",");
        String codeColumnString = concatCodeTupels(createCodeTupel(this.catman.getUniqueKeyAttributes(comptyp)));
        query.append("{{\n\tSELECT ");
        query.append(comptyp.getPK().getLongValueAsString());
        query.append(" AS type, ");
        query.append(codeColumnString);
        query.append(" AS code, count(*) as amount, ");
        query.append(groupnumber);
        query.append(" AS grp\n\tFROM {");
        query.append(comptyp.getCode());
        query.append(" AS ct}\n\tWHERE {ct:");
        query.append(catverqualifier);
        query.append("} = ?cv\n\tGROUP BY ");
        query.append(groupbyString);
        query.append(" HAVING count(*) > 1\n}}\n");
        return query.toString();
    }


    private List<String> createCodeTupel(Collection<AttributeDescriptor> attrDescColl)
    {
        List<String> tupels = new ArrayList<>();
        for(AttributeDescriptor attrDesc : attrDescColl)
        {
            tupels.add("'" + attrDesc.getQualifier() + ":'");
            tupels.add(createValuePart(attrDesc));
        }
        return tupels;
    }


    private String concatCodeTupels(List<String> tupellist)
    {
        List<String> newtupellist = new ArrayList<>();
        String delimiter = "||";
        if(Config.isSQLServerUsed())
        {
            delimiter = "+";
        }
        else if(Config.isMySQLUsed())
        {
            delimiter = ",";
        }
        int i;
        for(i = 0; i < tupellist.size(); i += 2)
        {
            newtupellist.add((String)tupellist.get(i) + (String)tupellist.get(i) + delimiter);
        }
        if(Config.isMySQLUsed())
        {
            return "CONCAT(" + StringUtils.join(newtupellist, delimiter + "';'" + delimiter) + ")";
        }
        return StringUtils.join(newtupellist, delimiter + "';'" + delimiter);
    }


    private String createValuePart(AttributeDescriptor attrDesc)
    {
        String qualifier = "{ct:" + attrDesc.getQualifier() + "}";
        String specialDBcase = qualifier;
        String dbValueForNull = "'0'";
        Type type = attrDesc.getRealAttributeType();
        if(Config.isHSQLDBUsed())
        {
            if(type instanceof ComposedType)
            {
                specialDBcase = "convert(" + qualifier + ", varchar(255) )";
            }
            else if(type instanceof AtomicType && ((AtomicType)type).getJavaClass().isAssignableFrom(Date.class))
            {
                specialDBcase = "YEAR(" + qualifier + ")||'-'||MONTH(" + qualifier + ")||'-'||DAYOFMONTH(" + qualifier + ")||' '||HOUR(" + qualifier + ")||':'||MINUTE(" + qualifier + ")||':'||SECOND(" + qualifier + ")";
            }
            else if(type instanceof AtomicType && !((AtomicType)type).getJavaClass().isAssignableFrom(String.class))
            {
                specialDBcase = "convert(" + qualifier + ", varchar(255) )";
            }
        }
        else if(Config.isMySQLUsed())
        {
            specialDBcase = qualifier;
        }
        else if(Config.isOracleUsed())
        {
            if(type instanceof ComposedType)
            {
                specialDBcase = "substr(" + qualifier + ", 1)";
            }
            else if(type instanceof AtomicType)
            {
                if(((AtomicType)type).getJavaClass().isAssignableFrom(Date.class))
                {
                    specialDBcase = "to_char(" + qualifier + ",'YYYY-MM-DD HH24:MI:SS')";
                }
                else if(!((AtomicType)type).getJavaClass().isAssignableFrom(String.class))
                {
                    specialDBcase = "substr(" + qualifier + ", 1)";
                }
            }
        }
        else if(Config.isSQLServerUsed())
        {
            if(type instanceof ComposedType)
            {
                specialDBcase = "convert(varchar, " + qualifier + ")";
            }
            else if(type instanceof AtomicType && ((AtomicType)type).getJavaClass().isAssignableFrom(Date.class))
            {
                specialDBcase = "convert(varchar, " + qualifier + ", 120)";
            }
            else if(type instanceof AtomicType && !((AtomicType)type).getJavaClass().isAssignableFrom(String.class))
            {
                specialDBcase = "convert(varchar, " + qualifier + ")";
            }
        }
        else if(Config.isHanaUsed())
        {
            if(type instanceof ComposedType)
            {
                specialDBcase = "substring(" + qualifier + ", 1)";
            }
            else if(type instanceof AtomicType)
            {
                if(((AtomicType)type).getJavaClass().isAssignableFrom(Date.class))
                {
                    specialDBcase = "to_char(" + qualifier + ",'YYYY-MM-DD HH24:MI:SS')";
                }
                else if(!((AtomicType)type).getJavaClass().isAssignableFrom(String.class))
                {
                    specialDBcase = "substring(" + qualifier + ", 1)";
                }
            }
        }
        else if(Config.isPostgreSQLUsed())
        {
            specialDBcase = qualifier;
            if(type instanceof AtomicType && ((AtomicType)type).getJavaClass().isAssignableFrom(Date.class))
            {
                dbValueForNull = "to_timestamp(0)";
            }
        }
        else
        {
            throw new RuntimeException("Unsupported database: " + Config.getDatabase());
        }
        return "CASE WHEN " + qualifier + " IS NULL THEN " + dbValueForNull + " ELSE " + specialDBcase + " END";
    }


    private String generateCatalogOverviewQuery()
    {
        StringBuilder query = new StringBuilder();
        boolean firstSubQuery = true;
        query.append("SELECT tbl.type, tbl.amount, tbl.cv FROM (\n");
        for(ComposedType comptype : this.catman.getAllCatalogItemTypes())
        {
            if(!comptype.isAbstract())
            {
                String cvqualifier = this.catman.getCatalogVersionAttribute(comptype).getQualifier();
                if(firstSubQuery)
                {
                    firstSubQuery = false;
                }
                else
                {
                    query.append("UNION ALL\n");
                }
                query.append("{{\n");
                query.append("SELECT " + comptype.getPK().getLongValue() + " AS type, COUNT(*) AS amount, {ct:" + cvqualifier + "} AS cv \n");
                query.append("FROM {" + comptype.getCode() + "! AS ct} \n");
                query.append("WHERE {ct:" + cvqualifier + "} IN ( {{ SELECT {PK} FROM {CatalogVersion AS subcv} WHERE {subcv:catalog} = ?cat }} ) \n");
                query.append("GROUP BY {ct:" + cvqualifier + "} \nHAVING COUNT(*) > 0");
                query.append("}}\n");
            }
        }
        query.append(") tbl ORDER BY tbl.type ASC, tbl.cv ASC\n");
        return query.toString();
    }
}

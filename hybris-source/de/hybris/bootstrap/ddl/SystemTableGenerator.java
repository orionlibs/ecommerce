package de.hybris.bootstrap.ddl;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.bootstrap.ddl.jdbc.JdbcType;
import de.hybris.bootstrap.ddl.jdbc.PlatformJDBCMappingProvider;
import de.hybris.bootstrap.ddl.model.YTable;
import java.util.Properties;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.IndexColumn;
import org.apache.ddlutils.model.NonUniqueIndex;
import org.apache.log4j.Logger;

class SystemTableGenerator
{
    private static final Logger LOG = Logger.getLogger(SystemTableGenerator.class);
    private static final String MYSQL_WORKAROUND_LANGPK = "mysql.workaround.index.langpk";


    static void addInternalColumnsToLPTable(YTable lpTable, PlatformJDBCMappingProvider jdbcProvider)
    {
        if(lpTable == null)
        {
            return;
        }
        if(lpTable.findColumn("ITEMPK") == null)
        {
            lpTable.addColumn(0, createColumn("ITEMPK", jdbcProvider.getMapping("HYBRIS.PK"), false, true));
        }
        if(lpTable.findColumn("ITEMTYPEPK") == null)
        {
            lpTable.addColumn(1, createColumn("ITEMTYPEPK", jdbcProvider.getMapping("HYBRIS.PK"), false, false));
        }
        if(lpTable.findColumn("LANGPK") == null)
        {
            lpTable.addColumn(2, createColumn("LANGPK", jdbcProvider.getMapping("HYBRIS.PK"), false, true));
        }
    }


    static void addInternalColumnsToAuditTable(YTable auditTable, PlatformJDBCMappingProvider jdbcProvider)
    {
        if(auditTable == null)
        {
            return;
        }
        if(auditTable.findColumn("ID") == null)
        {
            Column idColumn = createColumn("ID", jdbcProvider.getMapping("java.lang.Long"), false, true);
            idColumn.setAutoIncrement(true);
            auditTable.addColumn(0, idColumn);
        }
        if(auditTable.findColumn("ITEMPK") == null)
        {
            auditTable.addColumn(1, createColumn("ITEMPK", jdbcProvider.getMapping("HYBRIS.PK"), false, false));
        }
        if(auditTable.findColumn("ITEMTYPEPK") == null)
        {
            auditTable.addColumn(2, createColumn("ITEMTYPEPK", jdbcProvider.getMapping("HYBRIS.PK"), false, false));
        }
        if(auditTable.findColumn("timestamp") == null)
        {
            auditTable.addColumn(3, createColumn("timestamp", jdbcProvider.getMapping("java.util.Date"), false, false));
        }
        if(auditTable.findColumn("currenttimestamp") == null)
        {
            auditTable.addColumn(4, createColumn("currenttimestamp", jdbcProvider.getMapping("java.util.Date"), false, false));
        }
    }


    static void addIndexesToLPTable(YTable lpTable, String dbName, PlatformConfig platformConfig)
    {
        if("mysql".equalsIgnoreCase(dbName))
        {
            applyWorkaroundForLangPKs(lpTable, platformConfig);
        }
    }


    private static void applyWorkaroundForLangPKs(YTable lpTable, PlatformConfig platformConfig)
    {
        Properties properties = new Properties();
        ConfigUtil.loadRuntimeProperties(properties, platformConfig);
        String mysqlWorkaroundForLang = properties.getProperty("mysql.workaround.index.langpk");
        if(mysqlWorkaroundForLang == null)
        {
            return;
        }
        String trimmedProperty = mysqlWorkaroundForLang.trim();
        if(trimmedProperty.equalsIgnoreCase("true"))
        {
            Column pkColumn = lpTable.findColumn("LANGPK");
            lpTable.addIndex((Index)createIndexForColumn(pkColumn, "Idx" + lpTable.getName() + "LangPk"));
        }
    }


    private static NonUniqueIndex createIndexForColumn(Column pkColumn, String name)
    {
        IndexColumn indexColumn = new IndexColumn();
        indexColumn.setColumn(pkColumn);
        indexColumn.setSize(pkColumn.getSize());
        NonUniqueIndex nonUniqueIndex = new NonUniqueIndex();
        nonUniqueIndex.setName(name);
        nonUniqueIndex.addColumn(indexColumn);
        return nonUniqueIndex;
    }


    private static Column createColumn(String colName, JdbcType columnDetails, boolean optional, boolean primaryKey)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("-------creating the  Column (%s) for jave type  (%s)", new Object[] {colName, columnDetails}));
        }
        Column col = new Column();
        col.setName(colName);
        col.setPrimaryKey(primaryKey);
        col.setTypeCode(columnDetails.getJdbcType());
        if(columnDetails.getSize() != null)
        {
            col.setSize(columnDetails.getSize());
        }
        if(columnDetails.getScale() > 0)
        {
            col.setScale(columnDetails.getScale());
        }
        col.setDefaultValue(columnDetails.getDefaultValue());
        return col;
    }
}

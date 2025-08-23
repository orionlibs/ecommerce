package de.hybris.platform.util.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OracleIndexStatementBuilder
{
    private final String table;
    private final String schema;


    public OracleIndexStatementBuilder(String schema, String table)
    {
        this.schema = schema;
        this.table = table.toUpperCase();
    }


    public PreparedStatement buildQuery(Connection connection) throws SQLException
    {
        String indexesQuery = "select null as table_cat, owner as table_schem, table_name, 0 as NON_UNIQUE, null as index_qualifier, null as index_name, 0 as type, 0 as ordinal_position, null as column_name, null as asc_or_desc, num_rows as cardinality, blocks as pages, null as filter_condition from all_tables where table_name = ? and owner = ?  union all  select null as table_cat, i.owner as table_schem, i.table_name, decode (i.uniqueness, 'UNIQUE', 0, 1), null as index_qualifier, i.index_name, 1 as type, c.column_position as ordinal_position, c.column_name, null as asc_or_desc, i.distinct_keys as cardinality, i.leaf_blocks as pages, null as filter_condition from all_indexes i, all_ind_columns c where i.table_name = ? and i.owner = ? and i.index_name = c.index_name and i.table_owner = c.table_owner and i.table_name = c.table_name and i.owner = c.index_owner order by non_unique, type, index_name, ordinal_position";
        PreparedStatement stmt = connection.prepareStatement(
                        "select null as table_cat, owner as table_schem, table_name, 0 as NON_UNIQUE, null as index_qualifier, null as index_name, 0 as type, 0 as ordinal_position, null as column_name, null as asc_or_desc, num_rows as cardinality, blocks as pages, null as filter_condition from all_tables where table_name = ? and owner = ?  union all  select null as table_cat, i.owner as table_schem, i.table_name, decode (i.uniqueness, 'UNIQUE', 0, 1), null as index_qualifier, i.index_name, 1 as type, c.column_position as ordinal_position, c.column_name, null as asc_or_desc, i.distinct_keys as cardinality, i.leaf_blocks as pages, null as filter_condition from all_indexes i, all_ind_columns c where i.table_name = ? and i.owner = ? and i.index_name = c.index_name and i.table_owner = c.table_owner and i.table_name = c.table_name and i.owner = c.index_owner order by non_unique, type, index_name, ordinal_position");
        try
        {
            stmt.setString(1, this.table);
            stmt.setString(2, this.schema);
            stmt.setString(3, this.table);
            stmt.setString(4, this.schema);
            PreparedStatement preparedStatement = stmt;
            if(stmt != null)
            {
                stmt.close();
            }
            return preparedStatement;
        }
        catch(Throwable throwable)
        {
            if(stmt != null)
            {
                try
                {
                    stmt.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }
}

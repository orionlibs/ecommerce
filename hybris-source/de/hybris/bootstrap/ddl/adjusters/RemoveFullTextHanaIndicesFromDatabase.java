package de.hybris.bootstrap.ddl.adjusters;

import de.hybris.bootstrap.ddl.SchemaAdjuster;
import java.util.List;
import java.util.Objects;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;
import org.springframework.jdbc.core.JdbcTemplate;

public class RemoveFullTextHanaIndicesFromDatabase implements SchemaAdjuster
{
    private final JdbcTemplate jdbcTemplate;
    private final Database database;
    private static final String QUERY_FOR_INDICES = "SELECT INDEX_NAME FROM SYS.FULLTEXT_INDEXES";


    public RemoveFullTextHanaIndicesFromDatabase(JdbcTemplate jdbcTemplate, Database database)
    {
        this.jdbcTemplate = Objects.<JdbcTemplate>requireNonNull(jdbcTemplate, "jdbcTemplate is required");
        this.database = Objects.<Database>requireNonNull(database, "database is required");
    }


    public void adjust()
    {
        List<String> indices = getExistingFulltextIndices();
        for(Table table : this.database.getTables())
        {
            for(Index index : table.getIndices())
            {
                if(indices.contains(index.getName()))
                {
                    table.removeIndex(index);
                }
            }
        }
    }


    private List<String> getExistingFulltextIndices()
    {
        return this.jdbcTemplate.query("SELECT INDEX_NAME FROM SYS.FULLTEXT_INDEXES", (resultSet, i) -> resultSet.getString(1));
    }
}

package de.hybris.platform.directpersistence.statement;

import com.google.common.base.Preconditions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public abstract class AbstractManyToManyRelationStatementsBuilder extends AbstractStoreStatementsBuilder
{
    protected final JdbcTemplate jdbcTemplate;
    protected final RowMapper<LinkRow> linkRowMapper;


    public AbstractManyToManyRelationStatementsBuilder(JdbcTemplate jdbcTemplate)
    {
        Preconditions.checkArgument((jdbcTemplate != null), "jdbcTemplate is required!");
        this.jdbcTemplate = jdbcTemplate;
        this.linkRowMapper = (RowMapper<LinkRow>)new Object(this);
    }
}

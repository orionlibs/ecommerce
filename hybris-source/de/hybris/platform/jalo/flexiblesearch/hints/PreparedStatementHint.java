package de.hybris.platform.jalo.flexiblesearch.hints;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementHint extends Hint
{
    PreparedStatement apply(PreparedStatement paramPreparedStatement) throws SQLException;


    default boolean needsAutoCommitDisabled()
    {
        return false;
    }
}

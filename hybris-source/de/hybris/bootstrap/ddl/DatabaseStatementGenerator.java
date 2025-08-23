package de.hybris.bootstrap.ddl;

import de.hybris.bootstrap.ddl.model.YRecord;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.List;
import org.apache.ddlutils.DdlUtilsException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;

public interface DatabaseStatementGenerator
{
    void generateDDL(Writer paramWriter) throws IOException;


    void generateDropDDL(Writer paramWriter) throws IOException;


    void createInserts(Writer paramWriter, Collection<YRecord> paramCollection) throws DdlUtilsException, IOException;


    void generateDropStatementsForCustomTypeSystemTables(Writer paramWriter, List<String> paramList) throws IOException;


    List<String> retrieveAllSystemDeploymentsTablesForAllTypeSystems(JdbcTemplate paramJdbcTemplate, String paramString) throws BadSqlGrammarException;
}

package de.hybris.bootstrap.ddl;

import com.google.common.base.Stopwatch;
import de.hybris.bootstrap.ddl.tools.SqlScriptParser;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class DefaultDbScriptsExecutor implements HybrisDbScriptsExecutor
{
    private static final Logger LOG = Logger.getLogger(DefaultDbScriptsExecutor.class);
    private final JdbcTemplate jdbcTemplate;
    private final int ddlBatchSize;
    private final int dmlBatchSize;
    protected SqlScriptParser sqlScriptParser;
    private final Stopwatch stopwatch;


    public DefaultDbScriptsExecutor(JdbcTemplate jdbcTemplate, SqlScriptParser sqlScriptParser, int ddlBatchSize, int dmlBatchSize)
    {
        this.jdbcTemplate = jdbcTemplate;
        this.ddlBatchSize = ddlBatchSize;
        this.dmlBatchSize = dmlBatchSize;
        this.sqlScriptParser = sqlScriptParser;
        this.stopwatch = Stopwatch.createUnstarted();
    }


    public void executeDDl(Path scriptPath)
    {
        try
        {
            Reader reader = Files.newBufferedReader(scriptPath, Charset.forName("UTF-8"));
            try
            {
                executeDDl(reader);
                if(reader != null)
                {
                    reader.close();
                }
            }
            catch(Throwable throwable)
            {
                if(reader != null)
                {
                    try
                    {
                        reader.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    public void executeDDl(Reader scriptReader)
    {
        LOG.info("###### Executing DDL Statements in batch mode ###############");
        Iterable<String> statements = parseDdl(scriptReader);
        executeBatch(statements, this.ddlBatchSize, false);
    }


    public void executeDropDdl(Path scriptPath)
    {
        try
        {
            Reader reader = Files.newBufferedReader(scriptPath, Charset.forName("UTF-8"));
            try
            {
                executeDropDdl(reader);
                if(reader != null)
                {
                    reader.close();
                }
            }
            catch(Throwable throwable)
            {
                if(reader != null)
                {
                    try
                    {
                        reader.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    public void executeDropDdl(Reader scriptReader)
    {
        LOG.info("###### Executing DDL Drop Statements in batch mode ##########");
        Iterable<String> statements = parseDropDdl(scriptReader);
        executeBatch(statements, this.ddlBatchSize, true);
    }


    public void executeDml(Path scriptPath)
    {
        try
        {
            Reader reader = Files.newBufferedReader(scriptPath, Charset.forName("UTF-8"));
            try
            {
                executeDml(reader);
                if(reader != null)
                {
                    reader.close();
                }
            }
            catch(Throwable throwable)
            {
                if(reader != null)
                {
                    try
                    {
                        reader.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    public void executeDml(Reader scriptReader)
    {
        LOG.info("###### Executing DML Statements in batch mode ###############");
        Iterable<String> statements = parseDml(scriptReader);
        executeBatch(statements, this.dmlBatchSize, false);
    }


    protected Iterable<String> parseDdl(Reader scriptReader)
    {
        return this.sqlScriptParser.parse(scriptReader);
    }


    protected Iterable<String> parseDropDdl(Reader scriptReader)
    {
        return this.sqlScriptParser.parse(scriptReader);
    }


    protected Iterable<String> parseDml(Reader scriptReader)
    {
        return this.sqlScriptParser.parse(scriptReader);
    }


    private void executeBatch(Iterable<String> statements, int batchSize, boolean ignoreErrors)
    {
        this.stopwatch.start();
        int numStatement = 0;
        for(List<String> batch = readBatch(statements, batchSize); CollectionUtils.isNotEmpty(batch); batch = readBatch(statements, batchSize))
        {
            numStatement += batch.size();
            try
            {
                this.jdbcTemplate.batchUpdate(batch.<String>toArray(new String[batch.size()]));
            }
            catch(Exception e)
            {
                String message = "problem executing sql [reason: " + e.getMessage() + "]";
                if(ignoreErrors)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug(message + " however ignoring errors was enabled");
                    }
                }
                else
                {
                    LOG.error(message);
                    throw new IllegalStateException(message, e);
                }
            }
        }
        this.stopwatch.stop();
        LOG.info("Executed " + numStatement + " Statements. Time taken: " + this.stopwatch);
        this.stopwatch.reset();
    }


    private List<String> readBatch(Iterable<String> statements, int batchSize)
    {
        List<String> batch = new ArrayList<>(batchSize);
        int i = 0;
        for(String statement : statements)
        {
            batch.add(statement);
            i++;
            if(i == batchSize)
            {
                break;
            }
        }
        return batch;
    }
}

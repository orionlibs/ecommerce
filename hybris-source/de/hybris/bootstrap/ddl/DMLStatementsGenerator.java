package de.hybris.bootstrap.ddl;

import de.hybris.bootstrap.ddl.dbtypesystem.DbTypeSystem;
import de.hybris.bootstrap.ddl.dbtypesystem.DbTypeSystemItem;
import de.hybris.bootstrap.ddl.dbtypesystem.HashGenerationStrategy;
import de.hybris.bootstrap.ddl.dbtypesystem.Row;
import de.hybris.bootstrap.ddl.dbtypesystem.UniqueIdentifier;
import de.hybris.bootstrap.ddl.model.YRecord;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.lang.StringUtils;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.dynabean.SqlDynaClass;
import org.apache.ddlutils.model.Database;

public class DMLStatementsGenerator
{
    private final DbTypeSystem dbTypeSystem;
    private final Database modelDatabase;
    private final Platform platform;
    private final String tablePrefix;
    private final String changesFileName;
    private final UniqueIdExtractor uniqueIdExtractor = (UniqueIdExtractor)new ChainOfExtractors(
                    new UniqueIdExtractor[] {(UniqueIdExtractor)new FromNumberseriesExtractor(), (UniqueIdExtractor)new FromDeploymentsExtractor(), (UniqueIdExtractor)new FromPropsExtractor(), (UniqueIdExtractor)new FromTypeSystemPropsExtractor(), (UniqueIdExtractor)new FromPKExtractor(),
                                    (UniqueIdExtractor)new BlowUpExtractor()});
    private final RecordToRowWrapper recordToRowWrapper = (RecordToRowWrapper)new GenericWrapper();
    private final Set<String> notUpdatableTables = (Set<String>)new Object(this);
    final AtomicBoolean changesDetected;


    public DMLStatementsGenerator(DbTypeSystem dbTypeSystem, Database modelDatabase, Platform platform, String tablePrefix, String changesFileName)
    {
        this.changesDetected = new AtomicBoolean(false);
        Objects.requireNonNull(dbTypeSystem);
        Objects.requireNonNull(modelDatabase);
        Objects.requireNonNull(platform);
        this.dbTypeSystem = dbTypeSystem;
        this.modelDatabase = modelDatabase;
        this.platform = platform;
        this.tablePrefix = tablePrefix;
        this.changesFileName = changesFileName;
    }


    public void generateStatementsFor(Collection<YRecord> records, Writer writer) throws IOException
    {
        if(this.changesFileName != null)
        {
            BufferedWriter changesFileWriter = Files.newBufferedWriter(Paths.get(this.changesFileName, new String[0]), new OpenOption[] {StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING});
            try
            {
                changesFileWriter.write("[");
                writeRecords(records, writer, changesFileWriter);
                changesFileWriter.write("]");
                changesFileWriter.flush();
                if(changesFileWriter != null)
                {
                    changesFileWriter.close();
                }
            }
            catch(Throwable throwable)
            {
                if(changesFileWriter != null)
                {
                    try
                    {
                        changesFileWriter.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        else
        {
            writeRecords(records, writer, null);
        }
        writer.flush();
    }


    private void writeRecords(Collection<YRecord> records, Writer writer, BufferedWriter changesFileWriter) throws IOException
    {
        for(YRecord record : records)
        {
            if(belongsToNotUpdatableTable(record))
            {
                continue;
            }
            if(changesFileWriter != null)
            {
                changesFileWriter.flush();
            }
            String statement = getStatementFor(record, changesFileWriter, this.changesDetected);
            if(statement == null || statement.length() == 0)
            {
                continue;
            }
            writer.append(statement).append(";\n");
        }
    }


    public boolean getChangesDetected()
    {
        return this.changesDetected.get();
    }


    private boolean belongsToNotUpdatableTable(YRecord record)
    {
        return this.notUpdatableTables.contains(removeTablePrefix(record.getDynaBean().getDynaClass().getName(), this.tablePrefix));
    }


    public static String removeTablePrefix(String tableName, String tablePrefix)
    {
        return StringUtils.removeStartIgnoreCase(tableName, tablePrefix);
    }


    private String getStatementFor(YRecord record, BufferedWriter changesFileWriter, AtomicBoolean changesDetected) throws IOException
    {
        DbTypeSystemItem typeSystemItem = tryToFindDbTypeSystemItemFor(record);
        String modelHash = getHashFrom(record);
        String tableName = getTableName(record);
        if(typeSystemItem == null)
        {
            writeChangesToFile(changesFileWriter, null, modelHash, changesDetected.getAndSet(true), tableName);
            return getInsertStatementFor(record);
        }
        String dbHash = typeSystemItem.getHash();
        if(!dbHash.equals(modelHash))
        {
            writeChangesToFile(changesFileWriter, dbHash, modelHash, changesDetected.getAndSet(true), tableName);
            return getUpdateStatementFor(record);
        }
        return "";
    }


    private String getTableName(YRecord record)
    {
        SqlDynaClass dynaClass = this.modelDatabase.getDynaClassFor(record.getDynaBean());
        return removeTablePrefix(dynaClass.getTableName(), this.tablePrefix);
    }


    private void writeChangesToFile(BufferedWriter changesFileWriter, String dbHash, String modelHash, boolean changes, String tableName) throws IOException
    {
        if(changesFileWriter == null)
        {
            return;
        }
        if(changes)
        {
            changesFileWriter.write(",");
        }
        changesFileWriter.write("\n{ ");
        changesFileWriter.write("\"table\": \"" + tableName + "\",");
        changesFileWriter.write("\"old\":" + ((dbHash == null) ? "{}" : dbHash) + ",");
        changesFileWriter.write("\"new\":" + modelHash);
        changesFileWriter.write("}\n");
        changesFileWriter.flush();
    }


    private String getHashFrom(YRecord record)
    {
        Row row = getRecordToRowWrapperFor().wrap(record);
        return HashGenerationStrategy.getFor(removeTablePrefix(record.getDynaBean().getDynaClass().getName(), this.tablePrefix))
                        .getHashFor(row);
    }


    private RecordToRowWrapper getRecordToRowWrapperFor()
    {
        return this.recordToRowWrapper;
    }


    private String getInsertStatementFor(YRecord record)
    {
        return this.platform.getInsertSql(this.modelDatabase, record.getDynaBean());
    }


    private String getUpdateStatementFor(YRecord record)
    {
        return this.platform.getUpdateSql(this.modelDatabase, record.getDynaBean());
    }


    private DbTypeSystemItem tryToFindDbTypeSystemItemFor(YRecord record)
    {
        UniqueIdentifier recordId = getIdFor(record);
        return this.dbTypeSystem.findDbTypeSystemItemById(recordId);
    }


    private UniqueIdentifier getIdFor(YRecord record)
    {
        UniqueIdExtractor idExtractor = getUniqueIdExtractor();
        return idExtractor.extractIdFrom(record, this.tablePrefix);
    }


    private UniqueIdExtractor getUniqueIdExtractor()
    {
        return this.uniqueIdExtractor;
    }
}

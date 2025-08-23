package de.hybris.bootstrap.ddl;

import com.google.common.base.Preconditions;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.platform.CreationParameters;

public class DatabaseStatementGeneratorBuilder
{
    private final Platform platform;
    private Database existingDatabase;
    private Database targetDatabase;
    private boolean createDropTableStatement;
    private DMLStatementsGenerator dmlStatementsGenerator;
    private CreationParameters tableCreationParameters = new CreationParameters();


    DatabaseStatementGeneratorBuilder(Platform platform)
    {
        Preconditions.checkNotNull(platform, "given platform should not be null");
        this.platform = platform;
    }


    public static DatabaseStatementGeneratorBuilder builder(Platform platform)
    {
        return new DatabaseStatementGeneratorBuilder(platform);
    }


    public DatabaseStatementGeneratorBuilder withExistingDataBase(Database db)
    {
        Preconditions.checkNotNull(db, "given existingDB should not be null");
        this.existingDatabase = db;
        return this;
    }


    public DatabaseStatementGeneratorBuilder withTargetDataBase(Database db)
    {
        Preconditions.checkNotNull(db, "given existingDB should not be null");
        this.targetDatabase = db;
        return this;
    }


    public DatabaseStatementGeneratorBuilder withDropTableStatement()
    {
        this.createDropTableStatement = true;
        return this;
    }


    public DatabaseStatementGeneratorBuilder withDMLStatementsGenerator(DMLStatementsGenerator dmlStatementsGenerator)
    {
        Preconditions.checkNotNull(dmlStatementsGenerator, "given dmlStatementsGenerator should not be null");
        this.dmlStatementsGenerator = dmlStatementsGenerator;
        return this;
    }


    public DatabaseStatementGenerator build()
    {
        if(this.existingDatabase == null)
        {
            return (DatabaseStatementGenerator)new DatabaseStatementGenerator.InitializingDatabaseStatementGenerator(this.platform, this.targetDatabase, this.createDropTableStatement, this.tableCreationParameters);
        }
        return (DatabaseStatementGenerator)new DatabaseStatementGenerator.UpdatingDatabaseStatementGenerator(this.platform, this.existingDatabase, this.targetDatabase, this.dmlStatementsGenerator, this.tableCreationParameters);
    }


    public void withTableCreationParameters(CreationParameters tableCreationParameters)
    {
        this.tableCreationParameters = tableCreationParameters;
    }
}

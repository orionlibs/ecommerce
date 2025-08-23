package de.hybris.platform.directpersistence.record.visitor;

import de.hybris.platform.directpersistence.BatchCollector;
import de.hybris.platform.directpersistence.PersistResult;
import de.hybris.platform.directpersistence.record.EntityRecord;
import de.hybris.platform.directpersistence.record.impl.AbstractEntityRecord;
import de.hybris.platform.directpersistence.record.impl.AbstractModificationRecord;
import de.hybris.platform.directpersistence.record.impl.DeleteRecord;
import de.hybris.platform.directpersistence.record.impl.InsertRecord;
import de.hybris.platform.directpersistence.record.impl.UpdateRecord;
import de.hybris.platform.directpersistence.statement.NewItemStatementsBuilder;
import de.hybris.platform.directpersistence.statement.RemoveItemStatementsBuilder;
import de.hybris.platform.directpersistence.statement.StatementHolder;
import de.hybris.platform.directpersistence.statement.UpdateItemStatementsBuilder;
import de.hybris.platform.persistence.hjmp.HJMPUtils;
import de.hybris.platform.servicelayer.internal.i18n.LocalizationService;
import de.hybris.platform.util.Config;
import java.util.Set;
import org.springframework.jdbc.core.JdbcTemplate;

public class DefaultEntityRecordVisitor implements EntityRecord.EntityRecordVisitor<Set<PersistResult>>
{
    private final JdbcTemplate jdbcTemplate;
    private final BatchCollector batchCollector;
    private final LocalizationService localizationService;
    private Boolean optimisticLockingEnabled;
    private final Config.DatabaseName databaseName;


    public DefaultEntityRecordVisitor(BatchCollector batchCollector, JdbcTemplate jdbcTemplate, boolean optimisticLockingEnabled, LocalizationService localizationService, Config.DatabaseName databaseName)
    {
        this(batchCollector, jdbcTemplate, localizationService, databaseName);
        this.optimisticLockingEnabled = Boolean.valueOf(optimisticLockingEnabled);
    }


    public DefaultEntityRecordVisitor(BatchCollector batchCollector, JdbcTemplate jdbcTemplate, LocalizationService localizationService, Config.DatabaseName databaseName)
    {
        this.jdbcTemplate = jdbcTemplate;
        this.batchCollector = batchCollector;
        this.localizationService = localizationService;
        this.databaseName = databaseName;
    }


    public Set<PersistResult> visit(UpdateRecord record)
    {
        UpdateItemStatementsBuilder builder = new UpdateItemStatementsBuilder(record, this.localizationService, getOptimisticLockingConfiguration((AbstractEntityRecord)record), this.jdbcTemplate, this.databaseName);
        Set<StatementHolder> statementHolders = builder.createStatements();
        for(StatementHolder statementHolder : statementHolders)
        {
            if(statementHolder.isSetterBased())
            {
                this.batchCollector.collectQuery(statementHolder.getStatement(), statementHolder.getSetter(), statementHolder
                                .getResultCheck());
                continue;
            }
            this.batchCollector.collectQuery(statementHolder.getStatement(), statementHolder.getParams());
        }
        return builder.getPersistResults();
    }


    private boolean getOptimisticLockingConfiguration(AbstractEntityRecord record)
    {
        if(this.optimisticLockingEnabled == null)
        {
            return HJMPUtils.isOptimisticLockingEnabledForType(record.getType());
        }
        return this.optimisticLockingEnabled.booleanValue();
    }


    public Set<PersistResult> visit(DeleteRecord record)
    {
        RemoveItemStatementsBuilder builder = RemoveItemStatementsBuilder.getInstance(record, this.jdbcTemplate, this.databaseName,
                        getOptimisticLockingConfiguration((AbstractEntityRecord)record));
        Set<StatementHolder> statementHolders = builder.createStatements();
        for(StatementHolder statementHolder : statementHolders)
        {
            if(statementHolder.isSetterBased())
            {
                this.batchCollector.collectQuery(statementHolder.getStatement(), statementHolder.getSetter(), statementHolder
                                .getResultCheck());
                continue;
            }
            this.batchCollector.collectQuery(statementHolder.getStatement(), statementHolder.getParams(), statementHolder
                            .getResultCheck());
        }
        return builder.getPersistResults();
    }


    public Set<PersistResult> visit(InsertRecord record)
    {
        NewItemStatementsBuilder builder = new NewItemStatementsBuilder((AbstractModificationRecord)record, this.localizationService, this.databaseName);
        Set<StatementHolder> statementHolders = builder.createStatements();
        for(StatementHolder statementHolder : statementHolders)
        {
            this.batchCollector.collectQuery(statementHolder.getStatement(), statementHolder.getSetter(), statementHolder
                            .getResultCheck());
        }
        return builder.getPersistResults();
    }
}

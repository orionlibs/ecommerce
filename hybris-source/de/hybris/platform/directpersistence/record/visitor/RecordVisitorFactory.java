package de.hybris.platform.directpersistence.record.visitor;

import de.hybris.platform.directpersistence.BatchCollector;
import de.hybris.platform.directpersistence.PersistResult;
import de.hybris.platform.directpersistence.record.EntityRecord;
import de.hybris.platform.directpersistence.record.RelationChanges;
import de.hybris.platform.servicelayer.internal.i18n.LocalizationService;
import de.hybris.platform.util.Config;
import java.util.Set;
import org.springframework.jdbc.core.JdbcTemplate;

public class RecordVisitorFactory
{
    public EntityRecord.EntityRecordVisitor<Set<PersistResult>> createEntityRecordVisitor(BatchCollector batchCollector, JdbcTemplate jdbcTemplate, LocalizationService localizationService, Config.DatabaseName databaseName)
    {
        return (EntityRecord.EntityRecordVisitor<Set<PersistResult>>)new DefaultEntityRecordVisitor(batchCollector, jdbcTemplate, localizationService, databaseName);
    }


    public RelationChanges.RelationChangesVisitor<Set<DefaultRelationRecordVisitor.RelationRecordsContainer>> createRelationChangesVisitor(LocalizationService localizationService)
    {
        return (RelationChanges.RelationChangesVisitor<Set<DefaultRelationRecordVisitor.RelationRecordsContainer>>)new DefaultRelationRecordVisitor(localizationService);
    }
}

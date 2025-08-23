package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.ItemSyncTimestamp;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.Utilities;
import java.io.IOException;
import java.util.function.Function;
import org.apache.log4j.Logger;

class SyncSchedulerUpdateCallable extends SyncSchedulerCallableBase
{
    private static final Logger LOG = Logger.getLogger(SyncSchedulerUpdateCallable.class.getName());
    private final boolean forceUpdate;


    public SyncSchedulerUpdateCallable(CatalogVersionSyncJob catalogVersionSyncJob, SessionContext ctx, ComposedType type, SyncScheduleWriter writer, boolean forceUpdate)
    {
        super(catalogVersionSyncJob, ctx, type, writer);
        this.forceUpdate = forceUpdate;
    }


    protected String handleGenerateQuery()
    {
        logInfo();
        if(this.synchronizationWithinDeployment)
        {
            Function<String, String> queryForType = code -> generateQueryForType(code);
            SyncSchedulerQueryDivider helper = SyncSchedulerQueryDivider.buildForCoreQuery(queryForType).withType(this.type).withHeader("SELECT x.pk1,x.pk2,x.pk3 FROM ( ").withFooter(") x ").build();
            return helper.generateQueryWithUnion();
        }
        return generateQueryForType(this.type.getCode());
    }


    private void logInfo()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Searching for modifed " + this.type.getCode() + " items (force=" + this.forceUpdate + ") ...");
        }
    }


    String generateQueryForType(String typeCode)
    {
        boolean exclusive = this.catalogVersionSyncJob.isExclusiveModeAsPrimitive();
        String query = "SELECT {p1:" + Item.PK + "} as pk1 ,{p2:" + Item.PK + "} as pk2,{ts:" + ItemSyncTimestamp.PK + "} as pk3 FROM {" + typeCode + " AS p1 JOIN " + GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP + " AS ts ON {ts.syncJob}" + (exclusive ? "=?me" : "=0") + " AND " + (exclusive
                        ? ""
                        : "{ts.targetVersion}=?tgtVer AND {ts.sourceVersion}=?srcVer AND ") + "{p1:" + Item.PK + "}={ts:sourceItem} JOIN " + typeCode + " AS p2 ON {p2:" + Item.PK + "}={ts:targetItem} }" + (!this.forceUpdate ? ("WHERE {ts:lastSyncSourceModifiedTime} < {p1:" + Item.MODIFIED_TIME
                        + "} ") : "");
        return query;
    }


    protected void handleWriteSchedule(JDBCQuery jdbcQuery) throws IOException
    {
        this.writer.write(new SyncSchedule(jdbcQuery.readPK(1), jdbcQuery.readPK(2), jdbcQuery.readPK(3), null, null));
        this.writer.flush();
    }


    protected void handleLogCompletion(int count, long time)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(" " + count + " " + this.type.getCode() + " items for update (" + Utilities.formatTime(time) + ")");
        }
    }
}

package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.Utilities;
import java.io.IOException;
import java.util.Collection;
import java.util.function.Function;
import org.apache.log4j.Logger;

class SyncSchedulerConnectCallable extends SyncSchedulerCallableBase
{
    private static final Logger LOG = Logger.getLogger(SyncSchedulerConnectCallable.class.getName());
    private final String additionalQueryRestrictions;


    public SyncSchedulerConnectCallable(CatalogVersionSyncJob catalogVersionSyncJob, SessionContext ctx, ComposedType type, SyncScheduleWriter writer, String additionalQueryRestrictions)
    {
        super(catalogVersionSyncJob, ctx, type, writer);
        this.additionalQueryRestrictions = additionalQueryRestrictions;
    }


    private void logInfo()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Searching for non-synchronized " + this.type.getCode() + " item pairs ...");
        }
    }


    protected String handleGenerateQuery()
    {
        logInfo();
        if(this.synchronizationWithinDeployment)
        {
            Function<String, String> queryForType = code -> generateQueryForType(code);
            SyncSchedulerQueryDivider helper = SyncSchedulerQueryDivider.buildForCoreQuery(queryForType).withType(this.type).withHeader("SELECT x.pk1 as zpk1,x.pk2 as zpk2 ,x.pCreationTime1 FROM ( ").withFooter(") x ORDER BY x.pCreationTime1 ASC, x.pk1 ASC ").build();
            return helper.generateQueryWithUnion();
        }
        return generateQueryForType(this.type.getCode());
    }


    String generateQueryForType(String typeCode)
    {
        boolean exclusive = this.catalogVersionSyncJob.isExclusiveModeAsPrimitive();
        Collection<AttributeDescriptor> uniqueKeyAttributes = CatalogManager.getInstance().getUniqueKeyAttributes(this.ctx, this.type);
        String versionAttr = CatalogManager.getInstance().getCatalogVersionAttribute(this.ctx, this.type).getQualifier();
        String query = "SELECT {p1:" + Item.PK + "} as pk1,{p2:" + Item.PK + "} as pk2, {p1:" + Item.CREATION_TIME + "} as pCreationTime1 FROM {" + typeCode + " AS p1 JOIN " + typeCode + " AS p2 ON {p1:" + versionAttr + "}=?srcVer AND {p2:" + versionAttr + "}=?tgtVer ";
        for(AttributeDescriptor keyAd : uniqueKeyAttributes)
        {
            query = query + " AND {p1:" + query + "}={p2:" + keyAd.getQualifier() + "}";
        }
        query = query + " } ";
        query = query + "WHERE " + query + "NOT EXISTS ({{SELECT {" + ((this.additionalQueryRestrictions != null) ? ("( " + this.additionalQueryRestrictions + ") AND ") : "") + "} FROM {" + Item.PK + " AS ts} WHERE {ts:syncJob}" + GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP + " AND " + (exclusive
                        ? "=?me"
                        : "=0") + "({ts:sourceItem}={p1:" + (exclusive ? "" : "{ts:targetVersion}=?tgtVer AND {ts:sourceVersion}=?srcVer AND ") + "} )}}) AND NOT EXISTS ({{ SELECT {" + Item.PK + "} FROM {" + Item.PK + " AS ts} WHERE {ts:syncJob}" + GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP
                        + " AND " + (exclusive ? "=?me" : "=0") + "({ts:targetItem}={p2:" + (exclusive ? "" : "{ts:targetVersion}=?tgtVer AND {ts:sourceVersion}=?srcVer AND ") + "} )}})";
        if(!this.synchronizationWithinDeployment)
        {
            query = query + " ORDER BY {p1:" + query + "} ASC, {p1:" + Item.CREATION_TIME + "} ASC";
        }
        return query;
    }


    protected void handleWriteSchedule(JDBCQuery jdbcQuery) throws IOException
    {
        this.writer.write(new SyncSchedule(jdbcQuery.readPK(1), jdbcQuery.readPK(2), null, null, null));
        this.writer.flush();
    }


    protected void handleLogCompletion(int count, long time)
    {
        if(LOG.isInfoEnabled())
        {
            LOG.info(" " + count + " " + this.type.getCode() + " items for connect (" + Utilities.formatTime(time) + ")");
        }
    }
}

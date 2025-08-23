package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.Utilities;
import java.io.IOException;
import java.util.function.Function;
import org.apache.log4j.Logger;

class SyncSchedulerAddCallable extends SyncSchedulerCallableBase
{
    private static final Logger LOG = Logger.getLogger(SyncSchedulerAddCallable.class.getName());
    private final String additionalQueryRestrictions;


    public SyncSchedulerAddCallable(CatalogVersionSyncJob catalogVersionSyncJob, SessionContext ctx, ComposedType type, SyncScheduleWriter writer, String additionalQueryRestrictions)
    {
        super(catalogVersionSyncJob, ctx, type, writer);
        this.additionalQueryRestrictions = additionalQueryRestrictions;
    }


    protected String handleGenerateQuery()
    {
        logInfo();
        if(this.synchronizationWithinDeployment)
        {
            Function<String, String> queryForType = code -> generateQueryForType(code);
            SyncSchedulerQueryDivider helper = SyncSchedulerQueryDivider.buildForCoreQuery(queryForType).withType(this.type).withHeader("SELECT x.pk1 as zpk1,x.pCreationTime1 FROM ( ").withFooter(") x ORDER BY x.pCreationTime1 ASC, x.pk1 ASC").build();
            return helper.generateQueryWithUnion();
        }
        return generateQueryForType(this.type.getCode());
    }


    private void logInfo()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Searching for new " + this.type.getCode() + " items ...");
        }
    }


    String generateQueryForType(String typeCode)
    {
        boolean exclusive = this.catalogVersionSyncJob.isExclusiveModeAsPrimitive();
        String versionAttr = CatalogManager.getInstance().getCatalogVersionAttribute(this.ctx, this.type).getQualifier();
        String query = "SELECT {p:" + Item.PK + "} as pk1,{p:" + Item.CREATION_TIME + "} as pCreationTime1 FROM {" + typeCode + " AS p} WHERE {p:" + versionAttr + "}=?srcVer AND " + ((this.additionalQueryRestrictions != null) ? ("( " + this.additionalQueryRestrictions + ") AND ") : "")
                        + "NOT EXISTS ( {{SELECT {ts:" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP + " AS ts} WHERE {ts:syncJob}" + (exclusive ? "=?me" : "=0") + " AND " + (exclusive ? "" : "{ts:targetVersion}=?tgtVer AND {ts:sourceVersion}=?srcVer AND ")
                        + "{ts:sourceItem}={p:" + Item.PK + "} }} ) AND NOT EXISTS( {{\tSELECT {pp:" + Item.PK + "} FROM {" + typeCode + " as pp LEFT JOIN " + GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP + " AS ts2\tON {ts2:targetItem}={pp:" + Item.PK + "} AND {ts2:syncJob}" + (exclusive
                        ? "=?me"
                        : "=0") + (exclusive ? "}" : " AND {ts2:targetVersion}=?tgtVer AND {ts2:sourceVersion}=?srcVer } ") + "\tWHERE ";
        for(AttributeDescriptor keyAd : CatalogManager.getInstance().getUniqueKeyAttributes(this.ctx, this.type))
        {
            query = query + "{pp:" + query + "}={p:" + keyAd.getQualifier() + "} AND ";
        }
        query = query + query + "{pp:" + ((this.additionalQueryRestrictions != null) ? ("( " + this.additionalQueryRestrictions + ") AND ") : "") + "}=?tgtVer AND {ts2:" + versionAttr + "} IS NULL }} ) ";
        if(!this.synchronizationWithinDeployment)
        {
            query = query + "ORDER BY {p:" + query + "} ASC, {p:" + Item.CREATION_TIME + "} ASC";
        }
        return query;
    }


    protected void handleWriteSchedule(JDBCQuery jdbcQuery) throws IOException
    {
        this.writer.write(new SyncSchedule(jdbcQuery.readPK(1), null, null, null, null));
        this.writer.flush();
    }


    protected void handleLogCompletion(int count, long time)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(" " + count + " " + this.type.getCode() + " items for add (" + Utilities.formatTime(time) + ")");
        }
    }
}

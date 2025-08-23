package de.hybris.platform.servicelayer.media;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.media.services.MimeService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.MediaUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;

public class LegacyMediaMigrator
{
    private FlexibleSearchService flexibleSearchService;
    private MimeService mimeService;
    private SessionService sessionService;
    private TypeService typeService;
    private int batchSize = 200;
    private static final String LEGACY_MEDIA_QRY = "SELECT {pk} FROM {Media} WHERE {internalURL} = 'replicated273654712'  AND ({dataPK} IS NULL OR {location} IS NULL)";


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.UPDATE)
    public void migrateMediaLocationAttribute()
    {
        this.sessionService.executeInLocalViewWithParams(disabledRestrictionsAndQueryCache(), (SessionExecutionBody)new Object(this));
    }


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.UPDATE)
    public void migrateLegacyMimeAndRealFileName()
    {
        JdbcTemplate jdbcTemplate = new JdbcTemplate((DataSource)Registry.getCurrentTenant().getDataSource());
        jdbcTemplate.update(createNullifyUnderscoreRealFileNamesStmt());
        jdbcTemplate.update(createNullifyUnderscoreMimesStmt());
    }


    private String createNullifyUnderscoreRealFileNamesStmt()
    {
        ComposedTypeModel mediaType = this.typeService.getComposedTypeForClass(MediaModel.class);
        String realFileNameColumn = this.typeService.getAttributeDescriptor(mediaType, "realFileName").getDatabaseColumn();
        return String.format("UPDATE %s SET %s = null WHERE %s='_'", new Object[] {mediaType.getTable(), realFileNameColumn, realFileNameColumn});
    }


    private String createNullifyUnderscoreMimesStmt()
    {
        ComposedTypeModel mediaType = this.typeService.getComposedTypeForClass(MediaModel.class);
        String mimeColumn = this.typeService.getAttributeDescriptor(mediaType, "mime").getDatabaseColumn();
        return String.format("UPDATE %s SET %s = null WHERE %s='_'", new Object[] {mediaType.getTable(), mimeColumn, mimeColumn});
    }


    private String createMigrateLegacyMediaStmt()
    {
        ComposedTypeModel mediaType = this.typeService.getComposedTypeForClass(MediaModel.class);
        String locationColumn = this.typeService.getAttributeDescriptor(mediaType, "location").getDatabaseColumn();
        String dataPKColumn = this.typeService.getAttributeDescriptor(mediaType, "dataPK").getDatabaseColumn();
        String pkColumn = this.typeService.getAttributeDescriptor(mediaType, "pk").getDatabaseColumn();
        return "UPDATE " + mediaType.getTable() + " SET " + locationColumn + "=?, " + dataPKColumn + "=? WHERE " + pkColumn + "=?";
    }


    private void batchProcessLegacyMedia()
    {
        int notProcessedLegacyMediaCount;
        do
        {
            FlexibleSearchQuery pagedLegacyMediaQuery = new FlexibleSearchQuery("SELECT {pk} FROM {Media} WHERE {internalURL} = 'replicated273654712'  AND ({dataPK} IS NULL OR {location} IS NULL)");
            pagedLegacyMediaQuery.setCount(this.batchSize);
            List<MediaModel> medias = this.flexibleSearchService.search(pagedLegacyMediaQuery).getResult();
            notProcessedLegacyMediaCount = medias.size();
            migrateLegacyMedias(medias);
        }
        while(notProcessedLegacyMediaCount > 0);
    }


    private void migrateLegacyMedias(List<MediaModel> medias)
    {
        if(medias.isEmpty())
        {
            return;
        }
        JdbcTemplate jdbcTemplate = new JdbcTemplate((DataSource)Registry.getCurrentTenant().getDataSource());
        jdbcTemplate.batchUpdate(createMigrateLegacyMediaStmt(), toStmtParams(medias));
    }


    private List<Object[]> toStmtParams(List<MediaModel> medias)
    {
        List<Object[]> params = new ArrayList();
        for(MediaModel m : medias)
        {
            Long dataPK = (m.getDataPK() != null) ? m.getDataPK() : createNewDataPk();
            params.add(new Object[] {generateLocationForLegacyMedia(m), dataPK, m.getPk().getLong()});
        }
        return params;
    }


    private Long createNewDataPk()
    {
        return Long.valueOf(PK.createCounterPK(30).getLongValue());
    }


    private Map<String, Object> disabledRestrictionsAndQueryCache()
    {
        return (Map<String, Object>)ImmutableMap.of("disableRestrictions", Boolean.TRUE, "disableCache", Boolean.TRUE);
    }


    private String generateLocationForLegacyMedia(MediaModel m)
    {
        if(m.getDataPK() == null)
        {
            String str1 = this.mimeService.getBestExtensionFromMime(m.getMime());
            String str2 = Strings.nullToEmpty(m.getFolder().getPath());
            String location = MediaUtil.addTrailingFileSepIfNeeded(str2) + MediaUtil.addTrailingFileSepIfNeeded(str2) + "." + m.getPk();
            return location;
        }
        String subFolderPath = Strings.nullToEmpty(m.getSubFolderPath());
        String mimeExtension = this.mimeService.getBestExtensionFromMime(m.getMime());
        String folderPath = Strings.nullToEmpty(m.getFolder().getPath());
        return MediaUtil.addTrailingFileSepIfNeeded(folderPath) + MediaUtil.addTrailingFileSepIfNeeded(folderPath) + MediaUtil.addTrailingFileSepIfNeeded(subFolderPath) + "." + m
                        .getDataPK();
    }


    @Required
    public void setMimeService(MimeService mimeService)
    {
        this.mimeService = mimeService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public void setBatchSize(int batchSize)
    {
        this.batchSize = batchSize;
    }
}

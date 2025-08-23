package de.hybris.platform.cockpit.systemsetup;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.model.CockpitUIConfigurationMediaModel;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.internal.model.impl.ItemModelCloneCreator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

@SystemSetup(extension = "catalog")
public class CockpitConfigurationMediaSystemSetup
{
    private static final String CATALOG_MIGRATION_UI_CONFIG_DEFAULT_CATALOG = "catalog.migration.ui.config.default.catalog";
    private static final String CATALOG_MIGRATION_UI_CONFIG_DEFAULT_CATALOG_VERSION = "catalog.migration.ui.config.default.catalog.version";
    private static final String CATALOG_MIGRATION_UI_CONFIG_MIME_TYPES = "catalog.migration.ui.config.mime.types";
    private static final String CATALOG_MIGRATION_UI_CONFIG_CODE_PATTERNS = "catalog.migration.ui.config.code.patterns";
    private static final Logger LOG = LoggerFactory.getLogger(CockpitConfigurationMediaSystemSetup.class);
    private ConfigurationService configurationService;
    private CatalogService catalogService;
    private FlexibleSearchService flexibleSearchService;
    private ModelService modelService;
    private TypeService typeService;
    private ItemModelCloneCreator itemModelCloneCreator;


    @SystemSetup(process = SystemSetup.Process.ALL, type = SystemSetup.Type.ESSENTIAL)
    public void adjustCockpitConfigurationMedias()
    {
        Configuration cfg = this.configurationService.getConfiguration();
        String defaultCatalogCode = cfg.getString("catalog.migration.ui.config.default.catalog", "Default");
        String defaultCatalogVersionCode = cfg.getString("catalog.migration.ui.config.default.catalog.version", "Staged");
        String codePatterns = cfg.getString("catalog.migration.ui.config.code.patterns");
        String mimeTypes = cfg.getString("catalog.migration.ui.config.mime.types");
        List<String> params = StringUtils.isNotBlank(codePatterns) ? parseComaSeparatedParameters(codePatterns) : prepareDefaultLikeParameters();
        List<String> mimeParams = StringUtils.isNotBlank(mimeTypes) ? parseComaSeparatedParameters(mimeTypes) : Collections.<String>emptyList();
        for(CatalogModel catalog : this.catalogService.getAllCatalogs())
        {
            if(defaultCatalogCode.equals(catalog.getId()))
            {
                for(CatalogVersionModel cvm : catalog.getCatalogVersions())
                {
                    if(defaultCatalogVersionCode.equals(cvm.getVersion()))
                    {
                        changeAllCockpitConfigurationMedias(cvm, params, mimeParams);
                        continue;
                    }
                    removeAllCockpitConfigurationMedias(cvm, params, mimeParams);
                }
                return;
            }
        }
        LOG.warn(defaultCatalogCode + ":" + defaultCatalogCode + " not found! Skipping cockpit media adjustment!");
    }


    private List<String> prepareDefaultLikeParameters()
    {
        ArrayList<String> list = new ArrayList<>();
        list.add("%ui_config");
        list.add("%ui_config2");
        list.add("%jasperMediaconfig");
        list.add("editor_%onfig");
        list.add("base_%onfig");
        list.add("listView_%onfig");
        list.add("wizard_%onfig");
        return list;
    }


    private List<String> parseComaSeparatedParameters(String codePatterns)
    {
        List<String> res = new ArrayList<>();
        for(String like : codePatterns.split(","))
        {
            if(StringUtils.isNotBlank(like))
            {
                res.add(like.trim());
            }
        }
        return res;
    }


    private void changeAllCockpitConfigurationMedias(CatalogVersionModel catalogVersionModel, List<String> likeParams, List<String> mimeParams)
    {
        Map<String, Object> queryParams = new HashMap<>();
        String query = buildMediaSearchQuery(catalogVersionModel, likeParams, mimeParams, queryParams);
        SearchResult<MediaModel> result = this.flexibleSearchService.search(new FlexibleSearchQuery(query, queryParams));
        if(result.getCount() > 0)
        {
            LOG.info("Changing UI config media types in: " + catalogVersionModel.getCatalog().getId() + ":" + catalogVersionModel.getVersion());
            int changeCnt = 0;
            for(MediaModel media : result.getResult())
            {
                CockpitUIConfigurationMediaModel clone = (CockpitUIConfigurationMediaModel)this.itemModelCloneCreator.copy(this.typeService
                                .getComposedTypeForClass(CockpitUIConfigurationMediaModel.class), (ItemModel)media, new ItemModelCloneCreator.CopyContext());
                clone.setCatalogVersion(null);
                this.modelService.remove(media);
                this.modelService.save(clone);
                changeCnt++;
            }
            LOG.info("Changed " + changeCnt + " UI config media files.");
        }
    }


    private String buildMediaSearchQuery(CatalogVersionModel catalogVersionModel, List<String> likeParams, List<String> mimeParams, Map<String, Object> queryParams)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT {PK} FROM {Media as m} WHERE {m.catalogVersion} = ?cv");
        queryParams.put("cv", catalogVersionModel.getPk().getLong());
        int cnt = 0;
        for(String param : likeParams)
        {
            String paramCode = "likeParam" + cnt;
            queryParams.put(paramCode, param);
            if(cnt == 0)
            {
                query.append(" AND ( {m.code} LIKE ?" + paramCode);
            }
            else
            {
                query.append(" OR {m.code} LIKE ?" + paramCode);
            }
            cnt++;
        }
        for(String param : mimeParams)
        {
            String paramCode = "likeParam" + cnt;
            queryParams.put(paramCode, param);
            if(cnt == 0)
            {
                query.append(" AND ( {m.mime} LIKE ?" + paramCode);
            }
            else
            {
                query.append(" OR {m.mime} LIKE ?" + paramCode);
            }
            cnt++;
        }
        if(cnt > 0)
        {
            query.append(")");
        }
        return query.toString();
    }


    private void removeAllCockpitConfigurationMedias(CatalogVersionModel catalogVersionModel, List<String> likeParams, List<String> mimeParams)
    {
        Map<String, Object> queryParams = new HashMap<>();
        String query = buildMediaSearchQuery(catalogVersionModel, likeParams, mimeParams, queryParams);
        SearchResult<MediaModel> result = this.flexibleSearchService.search(new FlexibleSearchQuery(query, queryParams));
        if(result.getCount() > 0)
        {
            LOG.info("Deleting UI config medias in: " + catalogVersionModel.getCatalog().getId() + ":" + catalogVersionModel.getVersion());
            int deleteCnt = 0;
            for(MediaModel media : result.getResult())
            {
                this.modelService.remove(media);
                deleteCnt++;
            }
            LOG.info("Deleted " + deleteCnt + " UI config media files.");
        }
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    @Required
    public void setCatalogService(CatalogService catalogService)
    {
        this.catalogService = catalogService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setItemModelCloneCreator(ItemModelCloneCreator itemModelCloneCreator)
    {
        this.itemModelCloneCreator = itemModelCloneCreator;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}

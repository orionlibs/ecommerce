package de.hybris.y2ysync.task.distributed;

import com.google.common.base.Preconditions;
import de.hybris.deltadetection.StreamConfiguration;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.processing.distributed.ProcessCreationData;
import de.hybris.y2ysync.enums.Y2YSyncType;
import de.hybris.y2ysync.model.Y2YColumnDefinitionModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationModel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Y2YSyncProcessCreationData implements ProcessCreationData
{
    private static final Logger LOG = LoggerFactory.getLogger(Y2YSyncProcessCreationData.class);
    private static final String COLUMNS_DELIMITER = ";";
    private final Y2YStreamConfigurationContainerModel container;
    private final String processId;
    private final int batchSize;
    private final Y2YSyncType syncType;
    private final Map<String, Object> globalDeltaDetectionSearchParams;
    private final String dataHubUrl;
    private final String nodeGroup;


    private Y2YSyncProcessCreationData(String processId, int batchSize, Y2YSyncType syncType, String dataHubUrl, Y2YStreamConfigurationContainerModel container, String nodeGroup)
    {
        this.processId = Objects.<String>requireNonNull(processId, "processId is required");
        this.container = Objects.<Y2YStreamConfigurationContainerModel>requireNonNull(container, "container is required");
        this.syncType = Objects.<Y2YSyncType>requireNonNull(syncType, "syncType is required");
        this.batchSize = batchSize;
        this.nodeGroup = nodeGroup;
        if(syncType == Y2YSyncType.DATAHUB)
        {
            this.dataHubUrl = Objects.<String>requireNonNull(dataHubUrl, "syncType set to DATAHUB but dataHubUrl is empty!");
        }
        else
        {
            this.dataHubUrl = dataHubUrl;
        }
        this.globalDeltaDetectionSearchParams = getDeltaDetectionSearchParams(container.getCatalogVersion());
    }


    public String getProcessId()
    {
        return this.processId;
    }


    public Stream<? extends StreamConfiguration> initialBatches()
    {
        return this.container.getConfigurations().stream().filter(c -> c.getActive().booleanValue())
                        .map(this::toDeltaDetectionStreamConfig);
    }


    private StreamConfiguration toDeltaDetectionStreamConfig(StreamConfigurationModel configuration)
    {
        Y2YStreamConfigurationModel _configuration = asY2yStreamConfiguration(configuration);
        Map<String, Object> streamQueryParameters = new HashMap<>(this.globalDeltaDetectionSearchParams);
        streamQueryParameters.putAll(getDeltaDetectionSearchParams(_configuration.getCatalogVersion()));
        return StreamConfiguration.buildFor(_configuration.getStreamId())
                        .withItemTypeCode(_configuration.getItemTypeForStream().getCode())
                        .withDataHubType(_configuration.getDataHubType())
                        .withItemSelector(_configuration.getWhereClause())
                        .withVersionValue(_configuration.getVersionSelectClause())
                        .withExcludedTypeCodes(_configuration.getExcludedTypes())
                        .withDataHubColumns(join(_configuration, Y2YColumnDefinitionModel::getColumnName))
                        .withImpExHeader(join(_configuration, Y2YColumnDefinitionModel::getImpexHeader))
                        .withSyncType(this.syncType.getCode())
                        .withParameters(streamQueryParameters);
    }


    private Y2YStreamConfigurationModel asY2yStreamConfiguration(StreamConfigurationModel configuration)
    {
        Preconditions.checkState(configuration instanceof Y2YStreamConfigurationModel);
        return (Y2YStreamConfigurationModel)configuration;
    }


    private Map<String, Object> getDeltaDetectionSearchParams(CatalogVersionModel catalogVersion)
    {
        if(catalogVersion == null)
        {
            return Collections.emptyMap();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("catalog", catalogVersion.getCatalog());
        params.put("catalogVersion", catalogVersion);
        params.put("catalogVersionCode", catalogVersion.getVersion());
        String catalogName = catalogVersion.getCatalog().getName();
        if(StringUtils.isNotEmpty(catalogName))
        {
            params.put("catalogName", catalogName);
        }
        return params;
    }


    private String join(Y2YStreamConfigurationModel streamConfiguration, Function<Y2YColumnDefinitionModel, String> mapper)
    {
        String columns = streamConfiguration.getColumnDefinitions().stream().sorted((def1, def2) -> def1.getPosition().compareTo(def2.getPosition())).<CharSequence>map((Function)mapper).collect(Collectors.joining(";"));
        LOG.debug("StreamConfig Columns string for type: {} >>> {}", streamConfiguration.getItemTypeForStream().getCode(), columns);
        return columns;
    }


    public Y2YStreamConfigurationContainerModel getContainer()
    {
        return this.container;
    }


    public Y2YSyncType getSyncType()
    {
        return this.syncType;
    }


    public String getDataHubUrl()
    {
        return this.dataHubUrl;
    }


    public int getBatchSize()
    {
        return this.batchSize;
    }


    public String getHandlerBeanId()
    {
        return "y2ySyncDistributedProcessHandler";
    }


    public String getNodeGroup()
    {
        return this.nodeGroup;
    }


    public static Builder builder()
    {
        return new Builder();
    }
}

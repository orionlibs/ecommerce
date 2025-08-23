package de.hybris.deltadetection;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.processing.distributed.BatchCreationData;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class StreamConfiguration implements BatchCreationData, Serializable
{
    private final String streamId;
    private String itemTypeCode;
    private String itemSelector;
    private String dataHubType;
    private String impExHeader;
    private String dataHubColumns;
    private String syncType;
    private Map<String, Object> parameters = Collections.emptyMap();
    private String versionValue;
    private Set<String> excludedTypeCodes;


    protected StreamConfiguration(String streamId)
    {
        Objects.requireNonNull(streamId, "streamId can't be null");
        this.streamId = streamId;
    }


    public static StreamConfiguration buildFor(String streamId)
    {
        return new StreamConfiguration(streamId);
    }


    public StreamConfiguration withItemSelector(String itemSelector)
    {
        this.itemSelector = itemSelector;
        return this;
    }


    public StreamConfiguration withItemTypeCode(String itemTypeCode)
    {
        this.itemTypeCode = itemTypeCode;
        return this;
    }


    public StreamConfiguration withDataHubColumns(String dataHubColumns)
    {
        this.dataHubColumns = dataHubColumns;
        return this;
    }


    public StreamConfiguration withImpExHeader(String impExHeader)
    {
        this.impExHeader = impExHeader;
        return this;
    }


    public StreamConfiguration withSyncType(String syncType)
    {
        this.syncType = syncType;
        return this;
    }


    public StreamConfiguration withParameters(Map<String, Object> parameters)
    {
        Objects.requireNonNull(parameters, "parameters can't be null");
        this.parameters = Collections.unmodifiableMap(parameters);
        return this;
    }


    public StreamConfiguration withExcludedTypeCodes(Set<ComposedTypeModel> excludedTypeCodes)
    {
        Objects.requireNonNull(excludedTypeCodes, "excludedTypeCodes can't be null");
        this.excludedTypeCodes = (Set<String>)excludedTypeCodes.stream().map(TypeModel::getCode).collect(Collectors.toSet());
        return this;
    }


    public StreamConfiguration withVersionValue(String versionValue)
    {
        this.versionValue = versionValue;
        return this;
    }


    public StreamConfiguration withDataHubType(String dataHubType)
    {
        this.dataHubType = dataHubType;
        return this;
    }


    public String getStreamId()
    {
        return this.streamId;
    }


    public Map<String, Object> getParameters()
    {
        return this.parameters;
    }


    public String getItemSelector()
    {
        return this.itemSelector;
    }


    public String getVersionValue()
    {
        return this.versionValue;
    }


    public Set<String> getExcludedTypeCodes()
    {
        return (this.excludedTypeCodes == null) ? Collections.EMPTY_SET : this.excludedTypeCodes;
    }


    public String getItemTypeCode()
    {
        return this.itemTypeCode;
    }


    public String getDataHubType()
    {
        return this.dataHubType;
    }


    public String getImpExHeader()
    {
        return this.impExHeader;
    }


    public String getDataHubColumns()
    {
        return this.dataHubColumns;
    }


    public String getSyncType()
    {
        return this.syncType;
    }
}

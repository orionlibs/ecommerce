package de.hybris.platform.genericsearch.impl;

import com.google.common.collect.ImmutableSet;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YTypeSystem;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericFieldComparisonCondition;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.GenericSubQueryCondition;
import de.hybris.platform.core.GenericValueCondition;
import de.hybris.platform.core.Operator;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.typesystem.TypeSystemUtils;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.lang3.tuple.Pair;

public class GenericSearchQueryAdjuster
{
    protected static final int DEFAULT_MAX_SUBSTRING = 1024;
    public static final Set<String> LARGE_OBJECT_DATA_TYPES = (Set<String>)ImmutableSet.of("BLOB", "CLOB", "NCLOB", "TEXT");
    public static final Set<String> SUPPORTED_DATA_TYPES = Set.of("java.lang.String", "String", "HYBRIS.LONG_STRING", "localized:java.lang.String");
    private final Map<Pair<String, String>, Boolean> blobTypeItemAttributeMap;
    private final String currentDb;
    private final int substringMax;
    private static final AtomicReference<GenericSearchQueryAdjuster> QUERY_ADJUSTER = new AtomicReference<>();


    private GenericSearchQueryAdjuster(YTypeSystem yTypeSystem, String currentDb)
    {
        this.currentDb = currentDb;
        this.blobTypeItemAttributeMap = getItemAttributeSetWithBlobDbType(yTypeSystem);
        this.substringMax = Config.getInt("query.adjuster.substring.max", 1024);
    }


    public static GenericSearchQueryAdjuster getDefault()
    {
        if(Config.isHanaUsed() || Config.isOracleUsed())
        {
            if(QUERY_ADJUSTER.get() == null)
            {
                YTypeSystem yTypeSystem = TypeSystemUtils.loadViaClassLoader(Utilities.getExtensionNames(), true);
                GenericSearchQueryAdjuster adjuster = new GenericSearchQueryAdjuster(yTypeSystem, Config.getDatabase());
                QUERY_ADJUSTER.compareAndSet(null, adjuster);
            }
            return QUERY_ADJUSTER.get();
        }
        return empty();
    }


    private static GenericSearchQueryAdjuster empty()
    {
        return (GenericSearchQueryAdjuster)new Object(null, null);
    }


    static GenericSearchQueryAdjuster create(YTypeSystem yTypeSystem, String currentDb)
    {
        return new GenericSearchQueryAdjuster(yTypeSystem, currentDb);
    }


    public static GenericSearchQueryAdjuster create(YTypeSystem yTypeSystem)
    {
        return new GenericSearchQueryAdjuster(yTypeSystem, Config.getDatabase());
    }


    private Map<Pair<String, String>, Boolean> getItemAttributeSetWithBlobDbType(YTypeSystem yTypeSystem)
    {
        if(yTypeSystem == null)
        {
            return Map.of();
        }
        Map<Pair<String, String>, Boolean> blobTypeCodeQualifierAttributeMap = new HashMap<>();
        for(YAttributeDescriptor descriptor : yTypeSystem.getAttributes())
        {
            Map<String, String> dbColumnDefinitions = descriptor.getDbColumnDefinitions();
            if(isConfiguredAsLargeObject(dbColumnDefinitions))
            {
                boolean sortable = (descriptor.getType() != null && descriptor.getType().getCode() != null && SUPPORTED_DATA_TYPES.contains(descriptor.getType().getCode()));
                blobTypeCodeQualifierAttributeMap
                                .put(Pair.of(descriptor.getEnclosingTypeCode().toLowerCase(Locale.ENGLISH), descriptor
                                                                .getQualifier().toLowerCase(Locale.ENGLISH)),
                                                Boolean.valueOf(sortable));
            }
        }
        return blobTypeCodeQualifierAttributeMap;
    }


    private boolean isConfiguredAsLargeObject(Map<String, String> dbColumnDefinitions)
    {
        return LARGE_OBJECT_DATA_TYPES.contains(dbColumnDefinitions.get(this.currentDb));
    }


    public void adjust(GenericQuery genericQuery)
    {
        GenericCondition condition = genericQuery.getCondition();
        translate(genericQuery.getInitialTypeCode(), condition);
    }


    private void translate(String initialTypeCode, GenericCondition condition)
    {
        if(condition instanceof GenericConditionList)
        {
            GenericConditionList conditionList = (GenericConditionList)condition;
            for(GenericCondition c : conditionList.getConditionList())
            {
                translate(initialTypeCode, c);
            }
        }
        else if(condition instanceof GenericSubQueryCondition)
        {
            GenericSubQueryCondition subQueryCondition = (GenericSubQueryCondition)condition;
            adjust(subQueryCondition.getSubQuery());
        }
        else if(condition instanceof GenericFieldComparisonCondition)
        {
            GenericFieldComparisonCondition fieldComparison = (GenericFieldComparisonCondition)condition;
            if(isAttributeStoredAsBlobInDb(fieldComparison.getField(), initialTypeCode) &&
                            isAttributeStoredAsBlobInDb(fieldComparison.getComparisonField(), initialTypeCode))
            {
                fieldComparison.setOperator(Operator.LIKE);
            }
        }
        else if(condition instanceof GenericValueCondition)
        {
            GenericValueCondition valueCondition = (GenericValueCondition)condition;
            if(isAttributeStoredAsBlobInDb(valueCondition.getField(), initialTypeCode))
            {
                valueCondition.setOperator(Operator.LIKE);
            }
        }
    }


    public void adjustQueryForOrderBy(StringBuilder queryBuffer, StringBuilder orderByBuffer, String typeCode, String qualifier)
    {
        if(shouldAdjustBlobOrderByForOracle(typeCode, qualifier))
        {
            queryBuffer.append("dbms_lob.substr(").append(orderByBuffer).append(", ").append(this.substringMax).append(", 1)");
        }
        else if(shouldAdjustBlobOrderByForHana(typeCode, qualifier))
        {
            queryBuffer.append("SUBSTRING(").append(orderByBuffer).append(", 1, ").append(this.substringMax).append(")");
        }
        else
        {
            queryBuffer.append(orderByBuffer);
        }
    }


    protected boolean shouldAdjustBlobOrderByForHana(String typeCode, String qualifier)
    {
        return (Config.isHanaUsed() && isAttributeStoredAsBlobInDb(typeCode, qualifier) &&
                        isBlobAttributeSupportedAsSortable(typeCode, qualifier));
    }


    protected boolean shouldAdjustBlobOrderByForOracle(String typeCode, String qualifier)
    {
        return (Config.isOracleUsed() && isAttributeStoredAsBlobInDb(typeCode, qualifier) &&
                        isBlobAttributeSupportedAsSortable(typeCode, qualifier));
    }


    public boolean isAttributeStoredAsBlobInDb(GenericSearchField field, String initialTypeCode)
    {
        String conditionTypeIdentifier = field.getTypeIdentifier();
        String typeCode = (conditionTypeIdentifier != null) ? conditionTypeIdentifier : initialTypeCode;
        return isAttributeStoredAsBlobInDb(typeCode, field.getQualifier());
    }


    protected boolean isAttributeStoredAsBlobInDb(String typeCode, String qualifier)
    {
        return (typeCode != null && qualifier != null && this.blobTypeItemAttributeMap
                        .containsKey(Pair.of(typeCode.toLowerCase(Locale.ENGLISH), qualifier.toLowerCase(Locale.ENGLISH))));
    }


    protected boolean isBlobAttributeSupportedAsSortable(String typeCode, String qualifier)
    {
        return ((Boolean)this.blobTypeItemAttributeMap.get(Pair.of(typeCode.toLowerCase(Locale.ENGLISH), qualifier.toLowerCase(Locale.ENGLISH)))).booleanValue();
    }
}

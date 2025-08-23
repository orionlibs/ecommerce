package de.hybris.bootstrap.ddl.sql;

import com.google.common.primitives.Longs;
import de.hybris.bootstrap.ddl.DatabaseSettings;
import de.hybris.bootstrap.ddl.model.YColumn;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.util.LocaleHelper;
import java.util.Objects;
import java.util.OptionalLong;
import org.apache.ddlutils.model.Column;
import org.apache.log4j.Logger;

public class HanaBlobColumnNativeTypeDecorator implements ColumnNativeTypeDecorator
{
    private static final Logger LOG = Logger.getLogger(HanaBlobColumnNativeTypeDecorator.class);
    public static final String HANADB_LOB_COLUMN_MEMORY_THRESHOLD = "hanadb.lob.memory.threshold";
    private final DatabaseSettings databaseSettings;


    public HanaBlobColumnNativeTypeDecorator(DatabaseSettings databaseSettings)
    {
        Objects.requireNonNull(databaseSettings, "databaseSettings mustn't be null");
        this.databaseSettings = databaseSettings;
    }


    public boolean canBeUsed(Column column, String nativeType)
    {
        return (column != null && nativeType != null && isBlobType(nativeType));
    }


    public String decorate(Column column, String nativeType)
    {
        return nativeType + nativeType;
    }


    private boolean isBlobType(String nativeType)
    {
        if(nativeType == null)
        {
            return false;
        }
        String normalizedTypeName = nativeType.toLowerCase(LocaleHelper.getPersistenceLocale());
        return ("blob".equals(normalizedTypeName) || "clob".equals(normalizedTypeName) || "nclob".equals(normalizedTypeName));
    }


    private String getBlobTypeConfigurationFor(Column column)
    {
        OptionalLong columnThreshold = getColumnHanaBlobMemoryThreshold(column);
        if(columnThreshold.isPresent())
        {
            return " MEMORY THRESHOLD " + columnThreshold.getAsLong();
        }
        OptionalLong globalThreshold = getGlobalHanaBlobMemoryThreshold();
        if(globalThreshold.isPresent())
        {
            return " MEMORY THRESHOLD " + globalThreshold.getAsLong();
        }
        return "";
    }


    private OptionalLong getGlobalHanaBlobMemoryThreshold()
    {
        String thresholdString = this.databaseSettings.getProperty("hanadb.lob.memory.threshold", null);
        if(thresholdString == null)
        {
            return OptionalLong.empty();
        }
        Long threshold = Longs.tryParse(thresholdString);
        if(threshold == null)
        {
            return OptionalLong.empty();
        }
        return OptionalLong.of(threshold.longValue());
    }


    private OptionalLong getColumnHanaBlobMemoryThreshold(Column column)
    {
        if(!(column instanceof YColumn))
        {
            return OptionalLong.empty();
        }
        YColumn yColumn = (YColumn)column;
        return yColumn.getAttributeDescriptors().stream()
                        .filter(d -> (d.getCustomProps() != null))
                        .filter(d -> d.getCustomProps().containsKey("hanadb.lob.memory.threshold"))
                        .filter(d -> isValidThresholdValue(d))
                        .mapToLong(d -> Long.parseLong((String)d.getCustomProps().get("hanadb.lob.memory.threshold")))
                        .max();
    }


    private boolean isValidThresholdValue(YAttributeDescriptor descriptor)
    {
        boolean isValid = (Longs.tryParse((String)descriptor.getCustomProps().get("hanadb.lob.memory.threshold")) != null);
        if(!isValid)
        {
            LOG.warn("Cannot parse memory threshold value for " + descriptor);
        }
        return isValid;
    }
}

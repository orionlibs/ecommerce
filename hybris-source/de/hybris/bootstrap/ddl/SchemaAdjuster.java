package de.hybris.bootstrap.ddl;

import java.util.Objects;

public interface SchemaAdjuster
{
    public static final SchemaAdjuster NONE = chained(new SchemaAdjuster[0]);


    void adjust();


    static SchemaAdjuster chained(SchemaAdjuster... adjusters)
    {
        Objects.requireNonNull(adjusters, "adjusters can't be null");
        return (SchemaAdjuster)new ChainedSchemaAdjuster(adjusters);
    }
}

package de.hybris.bootstrap.ddl.sql;

import java.util.Set;
import org.apache.ddlutils.model.Column;

public class SqlTransformersRegistry
{
    private final Set<SqlValueTransformer> transformers;


    public SqlTransformersRegistry(Set<SqlValueTransformer> transformers)
    {
        this.transformers = transformers;
    }


    public SqlValueTransformer getValueTransformerFor(Column column, Object value)
    {
        if(this.transformers != null)
        {
            for(SqlValueTransformer transformer : this.transformers)
            {
                if(transformer.isCompatibleWith(column, value))
                {
                    return transformer;
                }
            }
        }
        return null;
    }
}

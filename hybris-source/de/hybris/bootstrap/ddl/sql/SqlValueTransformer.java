package de.hybris.bootstrap.ddl.sql;

import org.apache.ddlutils.model.Column;

interface SqlValueTransformer
{
    String transform(Column paramColumn, Object paramObject);


    boolean isCompatibleWith(Column paramColumn, Object paramObject);
}

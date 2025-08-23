package de.hybris.bootstrap.ddl.sql;

import org.apache.ddlutils.model.Column;

public interface ColumnNativeTypeDecorator
{
    boolean canBeUsed(Column paramColumn, String paramString);


    String decorate(Column paramColumn, String paramString);
}

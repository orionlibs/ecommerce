package de.hybris.bootstrap.ddl.sql;

public interface DbAwareIndex
{
    IndexCreationMode getCreationMode();


    boolean isOnline();
}

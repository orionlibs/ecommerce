package de.hybris.bootstrap.ddl.dbtypesystem;

public interface EnumerationValue extends DbTypeSystemItem
{
    long getPk();


    String getCodeLowerCase();


    long getTypePk();
}

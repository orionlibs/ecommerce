package de.hybris.bootstrap.ddl.dbtypesystem;

public interface AtomicType extends DbTypeSystemItem
{
    String getInternalCodeLowerCase();


    long getPk();
}

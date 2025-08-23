package de.hybris.platform.persistence.type;

public interface TypeRemote extends TypeManagerManagedRemote
{
    String getCode();


    boolean isAssignableFrom(TypeRemote paramTypeRemote);
}

package de.hybris.platform.persistence.type;

public interface DescriptorRemote extends TypeManagerManagedRemote
{
    String getQualifier();


    TypeRemote getAttributeType();


    boolean isLocalized();
}

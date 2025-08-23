package com.hybris.datahub.domain;

import java.util.Set;

public interface CanonicalAttributeModelDefinition extends AttributeDefinition
{
    public static final String _TYPECODE = "CanonicalAttributeModelDefinition";


    CanonicalItemMetadata getCanonicalItemMetadata();


    void setCanonicalItemMetadata(CanonicalItemMetadata paramCanonicalItemMetadata);


    boolean isPrimaryKey();


    void setPrimaryKey(boolean paramBoolean);


    Set<CanonicalAttributeDefinition> getAllCanonicalAttributeTransformationDefinitions();


    Set<CanonicalAttributeDefinition> getCanonicalAttributeTransformationDefinitions();


    @Deprecated(since = "ages", forRemoval = true)
    void setCanonicalAttributeTransformationDefinitions(Set<CanonicalAttributeDefinition> paramSet);


    void addCanonicalAttributeTransformationDefinition(CanonicalAttributeDefinition paramCanonicalAttributeDefinition);
}

package com.hybris.datahub.domain;

import com.hybris.datahub.runtime.domain.CanonicalItemPublicationStatusType;
import java.util.Set;
import javax.validation.constraints.Size;

public interface TargetItemMetadata extends ItemMetadata
{
    public static final String _TYPECODE = "TargetItemMetadata";


    TargetSystem getTargetSystem();


    void setTargetSystem(TargetSystem paramTargetSystem);


    @Size(max = 255)
    String getExportCode();


    void setExportCode(String paramString);


    CanonicalItemMetadata getCanonicalItemSource();


    void setCanonicalItemSource(CanonicalItemMetadata paramCanonicalItemMetadata);


    boolean getIsUpdatable();


    void setIsUpdatable(boolean paramBoolean);


    Set<TargetAttributeDefinition> getTargetAttributeDefinitions();


    @Deprecated(since = "ages", forRemoval = true)
    void setTargetAttributeDefinitions(Set<TargetAttributeDefinition> paramSet);


    Set<TargetAttributeDefinition> getAllTargetAttributeDefinitions();


    void addTargetAttributeDefinition(TargetAttributeDefinition paramTargetAttributeDefinition);


    boolean getIsExportCodeExpression();


    void setIsExportCodeExpression(boolean paramBoolean);


    Set<TargetItemMetadata> getDependsOn();


    @Deprecated(since = "ages", forRemoval = true)
    void setDependsOn(Set<TargetItemMetadata> paramSet);


    void addDependsOn(TargetItemMetadata paramTargetItemMetadata);


    Set<TargetItemMetadata> getRequiredBy();


    @Deprecated(since = "ages", forRemoval = true)
    void setRequiredBy(Set<TargetItemMetadata> paramSet);


    void addRequiredBy(TargetItemMetadata paramTargetItemMetadata);


    boolean containsAttribute(String paramString);


    String getFilterExpression();


    void setFilterExpression(String paramString);


    CanonicalItemPublicationStatusType getFilteredItemPublicationStatus();


    void setFilteredItemPublicationStatus(CanonicalItemPublicationStatusType paramCanonicalItemPublicationStatusType);
}

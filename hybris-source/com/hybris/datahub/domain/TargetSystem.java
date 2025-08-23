package com.hybris.datahub.domain;

import com.hybris.datahub.runtime.domain.TargetSystemPublication;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface TargetSystem
{
    public static final String _TYPECODE = "TargetSystem";


    @NotNull
    Long getTargetSystemId();


    @Deprecated(since = "6.0", forRemoval = true)
    void setTargetSystemId(Long paramLong);


    @NotNull
    @Size(max = 255)
    String getTargetSystemName();


    void setTargetSystemName(String paramString);


    @NotNull
    @Size(max = 255)
    String getTargetSystemType();


    void setTargetSystemType(String paramString);


    Set<TargetItemMetadata> getTargetItemMetadata();


    @Deprecated(since = "ages", forRemoval = true)
    void setTargetItemMetadata(Set<TargetItemMetadata> paramSet);


    void addTargetItemMetadata(TargetItemMetadata paramTargetItemMetadata);


    Set<String> getExportCodes();


    @Deprecated(since = "ages", forRemoval = true)
    void setExportCodes(Set<String> paramSet);


    void addExportCode(String paramString);


    @NotNull
    @Size(max = 255)
    String getExportURL();


    void setExportURL(String paramString);


    @Size(max = 255)
    String getUserName();


    void setUserName(String paramString);


    @Size(max = 255)
    String getPassword();


    void setPassword(String paramString);


    Set<TargetSystemPublication> getTargetSystemPublications();


    @Deprecated(since = "ages", forRemoval = true)
    void setTargetSystemPublications(Set<TargetSystemPublication> paramSet);


    void addTargetSystemPublications(TargetSystemPublication paramTargetSystemPublication);
}

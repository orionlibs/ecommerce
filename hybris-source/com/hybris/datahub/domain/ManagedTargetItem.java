package com.hybris.datahub.domain;

import com.hybris.datahub.runtime.domain.TargetSystemPublication;
import java.util.Map;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface ManagedTargetItem extends ManagedBaseDataItem
{
    public static final String _TYPECODE = "TargetItem";


    @NotNull
    ManagedCanonicalItem getCanonicalItem();


    void setCanonicalItem(ManagedCanonicalItem paramManagedCanonicalItem);


    TargetSystemPublication getTargetSystemPublication();


    void setTargetSystemPublication(TargetSystemPublication paramTargetSystemPublication);


    @Size(max = 255)
    String getExportCode();


    void setExportCode(String paramString);


    Map<String, String> getExportCodeAttributeMap();


    void setExportCodeAttributeMap(Map<String, String> paramMap);


    long getTargetItemId();


    void setTargetItemId(long paramLong);


    long getCanonicalItemId();
}

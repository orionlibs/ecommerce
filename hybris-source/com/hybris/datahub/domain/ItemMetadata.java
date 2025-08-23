package com.hybris.datahub.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface ItemMetadata
{
    public static final String _TYPECODE = "ItemMetadata";


    @NotNull
    @Size(max = 255)
    String getItemType();


    void setItemType(String paramString);


    @Size(max = 255)
    String getDescription();


    void setDescription(String paramString);


    @NotNull
    Long getItemMetadataId();


    void setItemMetadataId(Long paramLong);
}

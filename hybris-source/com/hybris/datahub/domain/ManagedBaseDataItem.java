package com.hybris.datahub.domain;

import com.hybris.datahub.runtime.domain.DataHubPool;
import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import javax.validation.constraints.Size;

public interface ManagedBaseDataItem
{
    Date getCreationTime();


    Date getModifiedTime();


    DataHubPool getDataPool();


    void setDataPool(DataHubPool paramDataHubPool);


    @Size(max = 255)
    String getItemType();


    void setItemType(String paramString);


    Object getProperty(String paramString);


    Object getProperty(String paramString, Locale paramLocale);


    void setProperty(String paramString, Serializable paramSerializable);


    void setProperty(String paramString, Serializable paramSerializable, Locale paramLocale);


    Map<String, Object> getAllProperties();
}

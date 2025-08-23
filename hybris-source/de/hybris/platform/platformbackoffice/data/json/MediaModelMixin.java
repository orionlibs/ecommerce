package de.hybris.platform.platformbackoffice.data.json;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class MediaModelMixin
{
    @JsonIgnore
    public abstract String getUrl2();


    @JsonIgnore
    public abstract String getUrl();
}

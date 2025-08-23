package com.hybris.datahub.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface AttributeDefinition
{
    public static final String _TYPECODE = "AttributeDefinition";


    @NotNull
    @Size(max = 1000)
    String getAttributeName();


    void setAttributeName(String paramString);


    @NotNull
    boolean isSecured();


    void setSecured(boolean paramBoolean);


    boolean isLocalizable();


    void setLocalizable(boolean paramBoolean);


    boolean isCollection();


    void setCollection(boolean paramBoolean);
}

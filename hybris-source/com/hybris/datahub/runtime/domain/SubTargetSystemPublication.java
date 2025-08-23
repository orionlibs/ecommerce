package com.hybris.datahub.runtime.domain;

public interface SubTargetSystemPublication extends TargetSystemPublication
{
    CompositeTargetSystemPublication getCompositeTargetSystemPublication();


    void setCompositeTargetSystemPublication(CompositeTargetSystemPublication paramCompositeTargetSystemPublication);


    Integer getSubPubOrder();


    void setSubPubOrder(Integer paramInteger);
}

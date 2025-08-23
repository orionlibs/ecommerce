package com.hybris.datahub.api.event;

public interface ActionEvent extends DataHubPoolEvent
{
    long getActionId();
}

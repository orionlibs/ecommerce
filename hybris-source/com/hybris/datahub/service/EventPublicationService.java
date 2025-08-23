package com.hybris.datahub.service;

import com.hybris.datahub.api.event.DataHubEvent;

public interface EventPublicationService
{
    void publishEvent(DataHubEvent paramDataHubEvent);
}

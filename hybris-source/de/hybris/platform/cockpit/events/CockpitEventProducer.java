package de.hybris.platform.cockpit.events;

public interface CockpitEventProducer
{
    void addCockpitEventAcceptor(CockpitEventAcceptor paramCockpitEventAcceptor);


    void removeCockpitEventAcceptor(CockpitEventAcceptor paramCockpitEventAcceptor);
}

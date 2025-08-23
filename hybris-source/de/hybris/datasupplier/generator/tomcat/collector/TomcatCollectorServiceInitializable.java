package de.hybris.datasupplier.generator.tomcat.collector;

public interface TomcatCollectorServiceInitializable
{
    void setServiceInitializer(TomcatCollectorServiceInitializer paramTomcatCollectorServiceInitializer);


    TomcatCollectorServiceInitializer getServiceInitializer();
}

package de.hybris.deltadetection;

public interface ChangesCollectorFactory<T extends ChangesCollector>
{
    T create();
}

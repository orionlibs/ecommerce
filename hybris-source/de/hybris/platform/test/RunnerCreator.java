package de.hybris.platform.test;

public interface RunnerCreator<T extends Runnable>
{
    T newRunner(int paramInt);
}

package de.hybris.platform.ruleengine.concurrency;

public interface GuardedSuspension<T>
{
    GuardStatus checkPreconditions(T paramT);
}

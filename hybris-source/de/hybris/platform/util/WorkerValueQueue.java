package de.hybris.platform.util;

import java.util.concurrent.TimeUnit;

public interface WorkerValueQueue<E>
{
    Object executeOnTakenValues(ExecuteOnTaken<E> paramExecuteOnTaken);


    E take(int paramInt);


    void clearValueTaken(int paramInt);


    void put(E paramE);


    boolean put(E paramE, ExecuteWhileWaiting<E> paramExecuteWhileWaiting);


    void waitUntilEmpty();


    void waitUntilEmpty(ExecuteWhileWaiting<E> paramExecuteWhileWaiting);


    void waitUntilEmpty(long paramLong, TimeUnit paramTimeUnit, ExecuteWhileWaiting<E> paramExecuteWhileWaiting);


    void clear();


    boolean isValueTakenOrQueueNotEmpty();


    void stop();
}

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.spring;

import com.hybris.cockpitng.core.Executable;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton object which holds scope contexts specific to thread tree
 */
public class RequestOperationContextHolder
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestOperationContextHolder.class);
    private static RequestOperationContextHolder instance = new RequestOperationContextHolder();
    private ThreadLocal<UUID> operationIds;


    private RequestOperationContextHolder()
    {
        operationIds = new ThreadLocal<>();
    }


    /**
     * Retrieve the instance of the singleton
     *
     * @return The {@link RequestOperationContextHolder} singleton instance
     */
    public static RequestOperationContextHolder instance()
    {
        return instance;
    }


    /**
     * Initializes new request operation context and binds it to current thread. If any other context is already bound with
     * current thread, thread detaches from it (see {@link #detachFromContext()}).
     */
    public void initializeContext()
    {
        if(hasContext())
        {
            LOGGER.debug("Context initialization for already initialized thread: {}, detach from context.", getContext());
            detachFromContext();
        }
        final UUID context = UUID.randomUUID();
        operationIds.set(context);
        LOGGER.debug("New context initialized: {}", getContext());
    }


    /**
     * @deprecated since 2105, not used any more
     * @return {@link RequestOperationContext}
     */
    @Deprecated(since = "2105", forRemoval = true)
    protected RequestOperationContext createContext()
    {
        return new RequestOperationContext();
    }


    /**
     * Prepares a context for current thread (if not yet created). Method should be called in the thread that has a context
     * already attached or is a root thread. It reserves resources for new operation context. It is required that an
     * operation is actually started and attaches itself by calling {@link #attachToContext(ContextRequest)}.
     *
     * @return a handle to prepared context
     * @see #attachToContext(ContextRequest)
     */
    public ContextRequest prepareForOperation()
    {
        if(!hasContext())
        {
            throw new IllegalStateException("There is no context assigned to current thread: " + Thread.currentThread().toString()
                            + ". Call #initalizeContext first!");
        }
        final ContextRequest contextRequest = createContextRequest();
        LOGGER.debug("New operation for initialized context prepared: {}", getContext());
        return contextRequest;
    }


    protected ContextRequest createContextRequest()
    {
        return new ContextRequest(operationIds.get());
    }


    /**
     * Executes provided operation in context prepared earlier by calling {@link #prepareForOperation()}. Method should be
     * called in the operation's thread.
     *
     * @param operation
     *           operation to be performed
     * @param contextRequest
     *           scope context request enquired by {@link #prepareForOperation()} call
     */
    public void executeWithContext(final Executable operation, ContextRequest contextRequest)
    {
        try
        {
            attachToContext(contextRequest);
            operation.execute();
        }
        finally
        {
            detachFromContext();
        }
    }


    /**
     * Joins operation performed by current thread to scope context prepared earlier by calling
     * {@link #prepareForOperation()}. Method should be called in the operation's thread.
     *
     * @param contextRequest
     *           scope context request enquired by {@link #prepareForOperation()} call
     * @deprecated since 2105, use {@link #executeWithContext(com.hybris.cockpitng.core.Executable, ContextRequest)}
     */
    @Deprecated(since = "2105", forRemoval = true)
    public void attachToContext(final ContextRequest contextRequest)
    {
        if(hasContext())
        {
            LOGGER.debug("An attempt to attach thread with context already initialized: {}, detach from context.", getContext());
            detachFromContext();
        }
        operationIds.set(contextRequest.context);
        LOGGER.debug("New operation attached to context: {}", getContext());
    }


    /**
     * Disjoints operation performed by current thread from scope context prepared earlier by calling
     * {@link #initializeContext()}. Method should be called in the same thread that it was initialized. If context is no
     * longer used, then it is cleared and removed.
     *
     * @see #detachFromContext(Thread)
     */
    public void detachFromContext()
    {
        final UUID context = operationIds.get();
        operationIds.remove();
        LOGGER.debug("Operation detached from context: {}", context);
    }


    /**
     * Disjoints operation performed by specified thread from scope context prepared earlier by calling
     * {@link #initializeContext()}. If context is no longer used, then it is cleared and removed.
     *
     * @see #detachFromContext(Thread)
     *
     * @deprecated since 2105, not used any more
     */
    @Deprecated(since = "2105", forRemoval = true)
    public void detachFromContext(final Thread thread)
    {
        detachFromContext();
    }


    /**
     * @deprecated since 2105, not used any more
     * @param thread
     */
    @Deprecated(since = "2105", forRemoval = true)
    protected void detachThreadFromContext(final Thread thread)
    {
        detachFromContext();
    }


    /**
     * @deprecated since 2105, not used any more
     */
    @Deprecated(since = "2105", forRemoval = true)
    protected void detachRequestThreadFromContext(final Object requestId)
    {
        detachFromContext();
    }


    /**
     * Checks if context for current thread is available
     *
     * @return <code>true</code> if there is a context correlated with this thread
     * @see #initializeContext()
     */
    public boolean hasContext()
    {
        return operationIds.get() != null;
    }


    /**
     * Obtains a context for current thread if available
     *
     * @return context correlated with this thread
     */
    public UUID getContext()
    {
        return operationIds.get();
    }


    /**
     * An object representing a request for scope context.
     * <P>
     * Whenever a new operation is about to be lunched, a new request for context is being created (or current one is being
     * attached to existing one). Then it should be used to attach particular operation to scope context.
     * </P>
     */
    public static class ContextRequest
    {
        private UUID context;


        public ContextRequest(final UUID context)
        {
            this.context = context;
        }


        @Override
        public boolean equals(final Object o)
        {
            if(this == o)
            {
                return true;
            }
            if(o == null)
            {
                return false;
            }
            if(o.getClass() != this.getClass())
            {
                return false;
            }
            final ContextRequest that = (ContextRequest)o;
            return Objects.equals(context, that.context);
        }


        @Override
        public int hashCode()
        {
            return Objects.hash(context);
        }
    }
}

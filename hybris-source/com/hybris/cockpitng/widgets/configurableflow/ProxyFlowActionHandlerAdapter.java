/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Proxy adapter which intercepts all calls on done,cancel,back,next,custom and calls one of actions on given proxied
 * adapter when {@link #doProxiedAction()} is called.
 */
public class ProxyFlowActionHandlerAdapter extends FlowActionHandlerAdapter
{
    private static final Logger LOG = LoggerFactory.getLogger(ProxyFlowActionHandlerAdapter.class);


    /**
     * Enum representation of actions.
     */
    protected enum ExecutedProxyAction
    {
        DONE, CANCEL, BACK, NEXT, CUSTOM
    }


    private final FlowActionHandlerAdapter proxiedHandler;
    private final Set<ExecutedProxyAction> recorderActions;


    /**
     * @param handler
     *           proxied handler.
     */
    public ProxyFlowActionHandlerAdapter(final FlowActionHandlerAdapter handler)
    {
        super(handler.getWidgetInstanceManager());
        this.proxiedHandler = handler;
        this.recorderActions = new HashSet<>();
    }


    /**
     * Calls an action on proxied handler passed as constructor argument. If different actions have been recorder it will
     * call proxied adapter with following priority
     * <ul>
     * <li>done()</li>
     * <li>cancel()</li>
     * <li>back()</li>
     * <li>next()</li>
     * </ul>
     * custom() will be never called.
     */
    public void doProxiedAction()
    {
        if(recorderActions.isEmpty())
        {
            return;
        }
        ExecutedProxyAction actionToExecute = null;
        if(recorderActions.size() == 1)
        {
            actionToExecute = recorderActions.iterator().next();
        }
        else
        {
            if(recorderActions.contains(ExecutedProxyAction.DONE))
            {
                actionToExecute = ExecutedProxyAction.DONE;
            }
            else if(recorderActions.contains(ExecutedProxyAction.CANCEL))
            {
                actionToExecute = ExecutedProxyAction.CANCEL;
            }
            else if(recorderActions.contains(ExecutedProxyAction.BACK))
            {
                actionToExecute = ExecutedProxyAction.BACK;
            }
            else if(recorderActions.contains(ExecutedProxyAction.NEXT))
            {
                actionToExecute = ExecutedProxyAction.NEXT;
            }
            if(actionToExecute != null)
            {
                LOG.warn("Handlers inside composed handler called different adapter actions, {} will be called", actionToExecute);
            }
        }
        executeAction(actionToExecute);
    }


    protected void executeAction(final ExecutedProxyAction actionToExecute)
    {
        if(actionToExecute == null)
        {
            return;
        }
        switch(actionToExecute)
        {
            case DONE:
                proxiedHandler.done();
                break;
            case CANCEL:
                proxiedHandler.cancel();
                break;
            case BACK:
                proxiedHandler.back();
                break;
            case NEXT:
                proxiedHandler.next();
                break;
            case CUSTOM:
                break;
        }
    }


    @Override
    public void cancel()
    {
        recorderActions.add(ExecutedProxyAction.CANCEL);
    }


    @Override
    public void done()
    {
        recorderActions.add(ExecutedProxyAction.DONE);
    }


    @Override
    public void back()
    {
        recorderActions.add(ExecutedProxyAction.BACK);
    }


    @Override
    public void next()
    {
        recorderActions.add(ExecutedProxyAction.NEXT);
    }


    @Override
    public void custom()
    {
        recorderActions.add(ExecutedProxyAction.DONE);
    }
}

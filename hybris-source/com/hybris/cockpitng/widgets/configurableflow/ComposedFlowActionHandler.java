/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.configurableflow;

import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Composed flow action handler which allows to call sub handlers {@link FlowActionHandler}
 *
 * <pre>
 *	Sub handler should be defined in params using {@value #PREFIX_HANDLER} prefix
 *    {@code
 *     <wz:parameter>
 *			<wz:name>handler:savePicture</wz:name>
 *			<wz:value>mediaReferenceCreateHandler</wz:value>
 *		 </wz:parameter>
 * }
 * </pre>
 *
 * <pre>
 * Every handler will be invoked with params {@link FlowActionHandler#perform(CustomType, FlowActionHandlerAdapter, Map)} which are prefixed with it's name e.g.
 * {@code
 *		<wz:parameter>
 *			<wz:name>savePicture:mediaProperty</wz:name>
 *			<wz:value>newProduct.picture</wz:value>
 *		</wz:parameter>
 *    }
 * </pre>
 *
 * All sub handlers will be called with {@link ProxyFlowActionHandlerAdapter} which records actions invoked by all sub
 * handlers.
 */
public class ComposedFlowActionHandler implements FlowActionHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(ComposedFlowActionHandler.class);
    public static final String PREFIX_HANDLER = "handler:";
    public static final String HANDLER_NAME_POSTFIX = ":";
    private String proxyAdapterName;


    @Override
    public void perform(final CustomType customType, final FlowActionHandlerAdapter adapter, final Map<String, String> parameters)
    {
        final ProxyFlowActionHandlerAdapter proxyAdapter = createProxyFlowActionAdapter(adapter);
        for(final Map.Entry<String, String> handlerInfo : getHandlers(parameters).entrySet())
        {
            loadHandler(handlerInfo.getValue()).ifPresent(
                            handler -> handler.perform(customType, proxyAdapter, extractHandlerParams(handlerInfo.getKey(), parameters)));
        }
        proxyAdapter.doProxiedAction();
    }


    protected Optional<FlowActionHandler> loadHandler(final String beanName)
    {
        final FlowActionHandler handler = loadHandlerBean(beanName);
        if(handler == null)
        {
            LOG.warn("Cannot find handler bean:{}", beanName);
        }
        return Optional.ofNullable(handler);
    }


    protected FlowActionHandler loadHandlerBean(final String beanName)
    {
        return BackofficeSpringUtil.getBean(beanName);
    }


    protected Map<String, String> extractHandlerParams(final String handlerPrefix, final Map<String, String> parameters)
    {
        final String paramPrefix = handlerPrefix + HANDLER_NAME_POSTFIX;
        return parameters.entrySet().stream()//
                        .filter(e -> StringUtils.startsWith(e.getKey(), paramPrefix))//
                        .collect(Collectors.toMap(e -> StringUtils.substringAfterLast(e.getKey(), paramPrefix), Map.Entry::getValue,
                                        ObjectUtils::defaultIfNull));
    }


    protected Map<String, String> getHandlers(final Map<String, String> parameters)
    {
        final Map<String, String> handlers = new HashMap<>();
        parameters.entrySet().stream()
                        .filter(entry -> StringUtils.startsWith(entry.getKey(), PREFIX_HANDLER) && StringUtils.isNotEmpty(entry.getValue()))
                        .forEach(entry -> {
                            final String handlerName = StringUtils.substringAfterLast(entry.getKey(), PREFIX_HANDLER);
                            if(StringUtils.isNotEmpty(handlerName))
                            {
                                handlers.put(handlerName, entry.getValue());
                            }
                        });
        return handlers;
    }


    protected ProxyFlowActionHandlerAdapter createProxyFlowActionAdapter(final FlowActionHandlerAdapter handler)
    {
        ProxyFlowActionHandlerAdapter adapter = null;
        if(proxyAdapterName != null)
        {
            adapter = loadAdapterBean(handler);
        }
        if(adapter == null)
        {
            adapter = new ProxyFlowActionHandlerAdapter(handler);
            LOG.warn("Cannot find proxy adapter:'{}', default one will be used", getProxyAdapterName());
        }
        return adapter;
    }


    protected ProxyFlowActionHandlerAdapter loadAdapterBean(final FlowActionHandlerAdapter handler)
    {
        return BackofficeSpringUtil.getBean(proxyAdapterName, handler);
    }


    @Required
    public void setProxyAdapterName(final String proxyAdapterName)
    {
        this.proxyAdapterName = proxyAdapterName;
    }


    public String getProxyAdapterName()
    {
        return proxyAdapterName;
    }
}

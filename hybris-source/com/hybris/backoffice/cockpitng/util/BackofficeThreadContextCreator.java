/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.util;

import com.hybris.backoffice.user.BackofficeRoleService;
import com.hybris.cockpitng.modules.core.impl.CockpitModuleComponentDefinitionService;
import com.hybris.cockpitng.util.CockpitThreadContextCreator;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.AtomicCounter;
import de.hybris.platform.util.MediaUtil;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.zkoss.util.Locales;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.sys.SessionsCtrl;

public class BackofficeThreadContextCreator implements CockpitThreadContextCreator
{
    public static final String MEDIA_SSL_MODE_ENABLED = "mediaSSLModeEnabled";
    protected static final String SPRING_SECURITY_CONTEXT = "_spring_security_context";
    protected static final String SPRING_REQUEST_CONTEXT = "_spring_request_context";
    protected static final String ZK_SESSION = "zk_session";
    protected static final String HYBRIS_TENANT = "tenant";
    protected static final String CURRENT_LOCALE = "_locales";
    protected static final String SESSION_USER = "user";
    protected static final String HYBRIS_SESSION_CATALOG_VERSIONS = "sessionCatalogVersions";
    protected static final String PUBLIC_MEDIA_URL_RENDERER = "publicMediaUrlRenderer";
    protected static final String SECURE_MEDIA_URL_RENDERER = "secureMediaUrlRenderer";
    protected static final String BACKOFFICE_ROLE_ID = "backoffice_role_id";
    private static final Logger LOG = LoggerFactory.getLogger(BackofficeThreadContextCreator.class);
    private CatalogVersionService catalogVersionService;
    private UserService userService;
    private I18NService i18nService;
    private CockpitModuleComponentDefinitionService componentDefinitionService;
    private BackofficeRoleService backofficeRoleService;
    private AtomicCounter atomicCounter = new AtomicCounter();
    private List<BackofficeThreadRequestAttributes> attributes;


    @Override
    public void execute(final Runnable runnable)
    {
        final int operationId = atomicCounter.generateNext();
        final Map<String, Object> parentThreadCtx = createThreadContext();
        parentThreadCtx.put("BackgroundOperationId", operationId);
        new RegistrableThread(new RunnableWithParentThreadContext(runnable, parentThreadCtx), "BackofficeLO-" + operationId)
                        .withInitialInfo(
                                        OperationInfo.builder().asNotSuspendableOperation().withCategory("Backoffice Long Operation").build())
                        .start();
    }


    private class RunnableWithParentThreadContext implements Runnable
    {
        private final Runnable runnable;
        private final Map<String, Object> parentThreadCtx;


        protected RunnableWithParentThreadContext(final Runnable runnable, final Map<String, Object> parentThreadCtx)
        {
            this.runnable = runnable;
            this.parentThreadCtx = parentThreadCtx;
        }


        @Override
        public void run()
        {
            try
            {
                initThreadContext(parentThreadCtx);
                setCockpitNGClassLoader();
                runnable.run();
            }
            finally
            {
                cleanUp(parentThreadCtx);
            }
        }


        private void setCockpitNGClassLoader()
        {
            final ApplicationContext externalApplicationContext = componentDefinitionService.getExternalApplicationContext();
            if(externalApplicationContext != null)
            {
                Thread.currentThread().setContextClassLoader(externalApplicationContext.getClassLoader());
            }
        }
    }


    @Override
    public Map<String, Object> createThreadContext()
    {
        final Map<String, Object> ret = new HashMap<>();
        ret.put(HYBRIS_TENANT, Registry.getCurrentTenant());
        ret.put(SESSION_USER, userService.getCurrentUser());
        ret.put(HYBRIS_SESSION_CATALOG_VERSIONS, catalogVersionService.getSessionCatalogVersions());
        ret.put(ZK_SESSION, Sessions.getCurrent());
        ret.put(SPRING_SECURITY_CONTEXT, SecurityContextHolder.getContext());
        ret.put(CURRENT_LOCALE, Locales.getCurrent());
        ret.put(SPRING_REQUEST_CONTEXT, createRequestAttributesCopy());
        ret.put(PUBLIC_MEDIA_URL_RENDERER, MediaUtil.getCurrentPublicMediaURLRenderer());
        ret.put(SECURE_MEDIA_URL_RENDERER, MediaUtil.getCurrentSecureMediaURLRenderer());
        ret.put(MEDIA_SSL_MODE_ENABLED, MediaUtil.isCurrentRequestSSLModeEnabled());
        ret.put(BACKOFFICE_ROLE_ID, getBackofficeRoleService().getActiveRole().orElse(null));
        return ret;
    }


    protected RequestAttributes createRequestAttributesCopy()
    {
        final HttpServletRequest request = (HttpServletRequest)Executions.getCurrent().getNativeRequest();
        final HttpServletRequestWrapper requestWrapper = new BackofficeThreadHttpServletRequestWrapper(request,
                        extractRequestAttributesToCopy(request));
        return new ServletRequestAttributes(requestWrapper);
    }


    protected Map<String, Object> extractRequestAttributesToCopy(final HttpServletRequest request)
    {
        if(CollectionUtils.isNotEmpty(getAttributes()))
        {
            return getAttributes().stream().map(attributeProvider -> getAttributesToCopy(request, attributeProvider))
                            .flatMap(Function.identity()).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        }
        else
        {
            return Collections.emptyMap();
        }
    }


    protected Stream<Pair<String, Object>> getAttributesToCopy(final HttpServletRequest request,
                    final BackofficeThreadRequestAttributes attributesProvider)
    {
        final ArrayList<String> attributeNames = Collections.list(request.getAttributeNames());
        return StreamSupport
                        .stream(Spliterators.spliteratorUnknownSize(attributesProvider.getAttributeNames(request).asIterator(),
                                        Spliterator.ORDERED), false)
                        .filter(attributeNames::contains)
                        .map(attributeName -> ImmutablePair.of(attributeName, attributesProvider.getAttribute(request, attributeName)));
    }


    protected static class BackofficeThreadHttpServletRequestWrapper extends HttpServletRequestWrapper
    {
        private static final HttpServletRequest UNSUPPORTED_REQUEST = (HttpServletRequest)Proxy
                        .newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]
                                        {HttpServletRequest.class}, new UnsupportedOperationExceptionInvocationHandler());
        private final String requestURI;
        private final String contextPath;
        private final String servletPath;
        private final String pathInfo;
        private final String queryString;
        private final String method;
        private final Session session;
        private final ServletContext servletContext;
        private final Map<String, Object> attributes;


        public BackofficeThreadHttpServletRequestWrapper(final HttpServletRequest request)
        {
            this(request, Collections.emptyMap());
        }


        public BackofficeThreadHttpServletRequestWrapper(final HttpServletRequest request, final Map<String, Object> attributes)
        {
            super(UNSUPPORTED_REQUEST);
            this.session = Sessions.getCurrent(false);
            this.servletContext = request.getServletContext();
            this.requestURI = request.getRequestURI();
            this.contextPath = request.getContextPath();
            this.servletPath = request.getServletPath();
            this.pathInfo = request.getPathInfo();
            this.queryString = request.getQueryString();
            this.method = request.getMethod();
            if(MapUtils.isNotEmpty(attributes))
            {
                this.attributes = new HashMap<>(attributes);
            }
            else
            {
                this.attributes = new HashMap<>();
            }
        }


        @Override
        public HttpSession getSession()
        {
            return (HttpSession)session.getNativeSession();
        }


        @Override
        public HttpSession getSession(final boolean create)
        {
            return getSession();
        }


        @Override
        public ServletContext getServletContext()
        {
            try
            {
                return super.getServletContext();
            }
            catch(final Exception e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Cannot return servlet context from parent. Use fallback context.", e);
                }
                // TSB-2935, CNG-2806 Avoid NullPointer in background thread after parent thread's request is recycled.
                return servletContext;
            }
        }


        @Override
        public String getContextPath()
        {
            return contextPath;
        }


        @Override
        public String getRequestURI()
        {
            return requestURI;
        }


        @Override
        public String getServletPath()
        {
            return servletPath;
        }


        @Override
        public String getPathInfo()
        {
            return pathInfo;
        }


        @Override
        public String getQueryString()
        {
            return queryString;
        }


        @Override
        public String getMethod()
        {
            return method;
        }


        @Override
        public Object getAttribute(final String name)
        {
            return attributes.get(name);
        }


        @Override
        public Enumeration<String> getAttributeNames()
        {
            return Collections.enumeration(attributes.keySet());
        }
    }


    private static class UnsupportedOperationExceptionInvocationHandler implements InvocationHandler
    {
        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args)
        {
            throw new UnsupportedOperationException("LongOperation does not cover invocation: " + method.toGenericString());
        }
    }


    @Override
    public void initThreadContext(final Map<String, Object> ctx)
    {
        if(ctx != null)
        {
            final Optional<Tenant> tenant = getFromContext(ctx, HYBRIS_TENANT, Tenant.class);
            if(tenant.isPresent())
            {
                Registry.setCurrentTenant(tenant.get());
            }
            else
            {
                Registry.activateMasterTenant();
            }
            getFromContext(ctx, CURRENT_LOCALE, Locale.class).ifPresent(this::setCurrentLocale);
            getFromContext(ctx, ZK_SESSION, Session.class).ifPresent(SessionsCtrl::setCurrent);
            getFromContext(ctx, SPRING_SECURITY_CONTEXT, SecurityContext.class).ifPresent(SecurityContextHolder::setContext);
            getFromContext(ctx, SPRING_REQUEST_CONTEXT, RequestAttributes.class)
                            .ifPresent(RequestContextHolder::setRequestAttributes);
            getFromContext(ctx, SESSION_USER, UserModel.class).ifPresent(userService::setCurrentUser);
            getFromContext(ctx, HYBRIS_SESSION_CATALOG_VERSIONS, Collection.class).ifPresent(this::setSessionCatalogVersions);
            getFromContext(ctx, PUBLIC_MEDIA_URL_RENDERER, MediaUtil.PublicMediaURLRenderer.class)
                            .ifPresent(MediaUtil::setCurrentPublicMediaURLRenderer);
            getFromContext(ctx, SECURE_MEDIA_URL_RENDERER, MediaUtil.SecureMediaURLRenderer.class)
                            .ifPresent(MediaUtil::setCurrentSecureMediaURLRenderer);
            getFromContext(ctx, MEDIA_SSL_MODE_ENABLED, Boolean.class).ifPresent(MediaUtil::setCurrentRequestSSLModeEnabled);
            getFromContext(ctx, BACKOFFICE_ROLE_ID, String.class)
                            .ifPresent(roleId -> getBackofficeRoleService().setActiveRole(roleId));
        }
    }


    protected void setSessionCatalogVersions(final Collection<CatalogVersionModel> catalogVersions)
    {
        catalogVersionService.setSessionCatalogVersions(catalogVersions);
    }


    protected void setCurrentLocale(final Locale currentLocale)
    {
        final Locale sessionLocale = i18nService.getSupportedLocales().contains(currentLocale) ? currentLocale
                        : JaloSession.getCurrentSession().getSessionContext().getLocale();
        Locales.setThreadLocal(sessionLocale);
        i18nService.setCurrentLocale(sessionLocale);
    }


    protected <T> Optional<T> getFromContext(final Map<String, Object> ctx, final String paramName, final Class<T> paramValueType)
    {
        final Object value = ctx.get(paramName);
        if(value != null && paramValueType.isInstance(value))
        {
            return Optional.of((T)value);
        }
        return Optional.empty();
    }


    protected void cleanUp(final Map<String, Object> ctx)
    {
        SecurityContextHolder.clearContext();
        RequestContextHolder.resetRequestAttributes();
        SessionsCtrl.setCurrent((Session)null);
        final Tenant currentTenant = Registry.getCurrentTenant();
        if(currentTenant instanceof AbstractTenant)
        {
            ((AbstractTenant)currentTenant).setActiveSessionForCurrentThread(null);
            ((AbstractTenant)currentTenant).getActiveSessionContextList().clear();
        }
        MediaUtil.unsetCurrentPublicMediaURLRenderer();
        MediaUtil.unsetCurrentSecureMediaURLRenderer();
        MediaUtil.unsetCurrentRequestSSLModeEnabled();
        getBackofficeRoleService().setActiveRole(null);
    }


    @Required
    public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    @Required
    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setI18nService(final I18NService i18nService)
    {
        this.i18nService = i18nService;
    }


    @Required
    public void setComponentDefinitionService(final CockpitModuleComponentDefinitionService componentDefinitionService)
    {
        this.componentDefinitionService = componentDefinitionService;
    }


    protected BackofficeRoleService getBackofficeRoleService()
    {
        return backofficeRoleService;
    }


    @Required
    public void setBackofficeRoleService(final BackofficeRoleService backofficeRoleService)
    {
        this.backofficeRoleService = backofficeRoleService;
    }


    protected List<BackofficeThreadRequestAttributes> getAttributes()
    {
        return attributes;
    }


    public void setAttributes(final List<BackofficeThreadRequestAttributes> attributes)
    {
        this.attributes = attributes;
    }
}

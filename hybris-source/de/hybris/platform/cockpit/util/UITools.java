package de.hybris.platform.cockpit.util;

import com.google.common.collect.Sets;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.testing.TestIdBuilder;
import de.hybris.platform.cockpit.util.testing.TestIdBuilderRegistry;
import de.hybris.platform.cockpit.util.testing.TestIdContext;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.servlet.ServletContext;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.impl.AbstractTag;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.LayoutRegion;
import org.zkoss.zkmax.zul.Portallayout;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Rows;
import org.zkoss.zul.impl.XulElement;

public final class UITools
{
    public static final String TEST_ID_BUILDER_REGISTRY = "testIdBuilderRegistry";
    public static final String ON_DRAG_HOVER_CLICKED = "onDragHoverClicked";
    private static final String LOADING_DIV = "loadingDiv";
    private static final String LAZY_LOAD_COMPONENTS = "lazyLoadComponents";
    private static final String ON_LAZYLOAD_NEXT = "onLazyloadNext";
    private static final String LAZY_LOAD_CHILDREN = "lazyLoadChildren";
    private static final Logger LOG = LoggerFactory.getLogger(UITools.class);


    public static boolean setTooltipText(Component component, String text)
    {
        boolean ret = true;
        if(component instanceof HtmlBasedComponent)
        {
            ((HtmlBasedComponent)component).setTooltiptext(text);
        }
        else if(component instanceof AbstractTag)
        {
            ((AbstractTag)component).setDynamicProperty("title", text);
        }
        else
        {
            ret = false;
        }
        return ret;
    }


    public static void applyLazyload(HtmlBasedComponent component)
    {
        String cockpitParameter = getCockpitParameter("default.lazyloading", Executions.getCurrent());
        if("disabled".equals(cockpitParameter))
        {
            return;
        }
        if(component instanceof Listbox)
        {
            component.setAttribute("lazyLoadSelIndex", Integer.valueOf(((Listbox)component).getSelectedIndex()));
        }
        List<Component> lazyLoadChildren = new ArrayList<>();
        List<? extends Component> children = component.getChildren();
        lazyLoadChildren.addAll(children);
        component.getChildren().clear();
        boolean skipLoadingScreen = component instanceof Listbox;
        if(skipLoadingScreen)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Lazy Loading: Loading screen not available for Listbox components.");
            }
        }
        else
        {
            Div loadingDiv = new Div();
            loadingDiv.setSclass("lazyLoadDiv");
            component.appendChild((Component)loadingDiv);
            component.setAttribute("loadingDiv", loadingDiv);
        }
        component.setAttribute("lazyLoadChildren", lazyLoadChildren);
        Component currentZKRoot = getCurrentZKRoot();
        Set<Component> lazyLoadComponents = (Set<Component>)currentZKRoot.getAttribute("lazyLoadComponents");
        boolean firstLazy = false;
        if(lazyLoadComponents == null)
        {
            firstLazy = true;
            lazyLoadComponents = new LinkedHashSet<>();
            currentZKRoot.setAttribute("lazyLoadComponents", lazyLoadComponents);
        }
        else
        {
            firstLazy = CollectionUtils.isEmpty(lazyLoadComponents);
        }
        lazyLoadComponents.add(component);
        if(!Events.isListened(currentZKRoot, "onLazyloadNext", false))
        {
            currentZKRoot.addEventListener("onLazyloadNext", (EventListener)new Object(currentZKRoot));
        }
        if(firstLazy)
        {
            Events.echoEvent("onLazyloadNext", currentZKRoot, null);
        }
        modifySClass(component, "lazyLoadContainer", true);
    }


    public static Component getCurrentZKRoot()
    {
        Collection<Component> components = Executions.getCurrent().getDesktop().getComponents();
        if(components != null && components.iterator().hasNext())
        {
            return ((Component)components.iterator().next()).getRoot();
        }
        return null;
    }


    public static void addDragHoverClickEventListener(XulElement component, EventListener eventListener, int timeout, String dragID)
    {
        component.setAction("onmouseover: dragHoverClick(#{self}, event, false, '" + dragID + "', " + timeout + "); onmouseout: dragHoverClick(#{self}, event, true, null, 0);");
        component.addEventListener("onUser", (EventListener)new Object(component));
        if(eventListener != null)
        {
            component.addEventListener("onDragHoverClicked", eventListener);
        }
    }


    public static String getZKPreference(String key)
    {
        String ret = Config.getParameter(key);
        if(ret == null)
        {
            try
            {
                ret = Executions.getCurrent().getDesktop().getWebApp().getConfiguration().getPreference(key, null);
            }
            catch(Exception e)
            {
                LOG.error(e.getMessage(), e);
            }
        }
        return ret;
    }


    public static void cleanupFCKEditorIframeGhosts()
    {
        Clients.evalJavaScript("cleanupFCKEditorIframeGhosts();");
    }


    public static void clearBrowserTextSelection()
    {
        Clients.evalJavaScript("clearSelection();");
    }


    public static void applyTestID(Component component, String id)
    {
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            component.setAttribute("cockpitTestID", id);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Cockpit_test_id '" + id + "' has been applied for component " + component.toString());
            }
        }
    }


    public static void applyTestID(Component component, TestIdContext ctx)
    {
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            Object bean = SpringUtil.getBean("testIdBuilderRegistry");
            if(bean instanceof TestIdBuilderRegistry)
            {
                TestIdBuilderRegistry registry = (TestIdBuilderRegistry)bean;
                TestIdBuilder testIdBuilder = registry.getTestIdBuilder(ctx.getClass());
                if(testIdBuilder != null)
                {
                    component.setAttribute("cockpitTestID", testIdBuilder.buildTestId(ctx));
                    String id = component.getUuid();
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Cockpit_test_id '" + id + "' has been applied for component " + component.toString());
                    }
                }
            }
        }
    }


    public static void resizeBorderLayout(Component comp, boolean allParents)
    {
        for(Component c = comp; c != null; c = c.getParent())
        {
            if(c instanceof Borderlayout)
            {
                ((Borderlayout)c).resize();
                if(allParents)
                {
                    break;
                }
            }
        }
    }


    public static void invalidateNextLayoutregion(Component comp)
    {
        for(Component c = comp; c != null; c = c.getParent())
        {
            if(c instanceof LayoutRegion)
            {
                ((LayoutRegion)c).invalidate();
                break;
            }
        }
    }


    public static void maximize(HtmlBasedComponent component)
    {
        component.setWidth("100%");
        component.setHeight("100%");
    }


    public static void detachChildren(Component parent)
    {
        if(parent != null)
        {
            List<Component> children = new ArrayList<>();
            children.addAll(parent.getChildren());
            for(Component component : children)
            {
                if(!isFromOtherDesktop(component))
                {
                    component.detach();
                    continue;
                }
                component.setParent(null);
            }
        }
    }


    public static <T extends Component> T getNextParentOfType(Class clazz, Component child, int maxDepth)
    {
        int index = 0;
        for(Component c = child; c != null; c = c.getParent(), index++)
        {
            if(clazz.isInstance(c))
            {
                return (T)c;
            }
            if(index > maxDepth)
            {
                return null;
            }
        }
        return null;
    }


    public static void invalidateGroupbox(Component child)
    {
        for(Component c = child; c != null; c = c.getParent())
        {
            if(c instanceof Groupbox)
            {
                ((Groupbox)c).invalidate();
            }
        }
    }


    public static boolean different(Object object1, Object object2)
    {
        return (object1 != object2 && (object1 == null || !object1.equals(object2)));
    }


    public static Grid createGridSkeleton(int columnsnumber)
    {
        Grid grid = new Grid();
        grid.setSclass("transparentGrid");
        Columns columns = new Columns();
        columns.setParent((Component)grid);
        for(int i = 0; i < columnsnumber; i++)
        {
            columns.appendChild((Component)new Column());
        }
        Rows rows = new Rows();
        rows.setParent((Component)grid);
        return grid;
    }


    public static void modifySClass(HtmlBasedComponent component, String className, boolean add)
    {
        if(component != null && StringUtils.isNotBlank(className))
        {
            Set<String> actual = Sets.newLinkedHashSet();
            String sclass = component.getSclass();
            if(!StringUtils.isBlank(sclass))
            {
                Collections.addAll(actual, sclass.split("\\s+"));
            }
            for(String clazz : className.trim().split("\\s+"))
            {
                if(add)
                {
                    actual.add(clazz);
                }
                else
                {
                    actual.remove(clazz);
                }
            }
            component.setSclass(actual.isEmpty() ? null : StringUtils.join(actual, " "));
        }
    }


    public static void modifySClass(AbstractTag component, String className, boolean add)
    {
        if(component != null && StringUtils.isNotBlank(className))
        {
            Set<String> actual = Sets.newLinkedHashSet();
            String sclass = component.getSclass();
            if(!StringUtils.isBlank(sclass))
            {
                Collections.addAll(actual, sclass.split("\\s+"));
            }
            for(String clazz : className.trim().split("\\s+"))
            {
                if(add)
                {
                    actual.add(clazz);
                }
                else
                {
                    actual.remove(clazz);
                }
            }
            component.setSclass(actual.isEmpty() ? null : StringUtils.join(actual, " "));
        }
    }


    public static void modifyStyle(HtmlBasedComponent component, String key, String value)
    {
        String current = component.getStyle();
        int pos = (current != null) ? current.indexOf(key) : -1;
        if(value != null)
        {
            if(pos < 0)
            {
                component.setStyle(((current != null) ? (current + ";") : "") + ((current != null) ? (current + ";") : "") + ":" + key);
            }
            else
            {
                int end = current.indexOf(";", pos + 1);
                String before = current.substring(0, pos).trim();
                if(before.length() > 0 && !before.endsWith(";"))
                {
                    before = before + ";";
                }
                String after = (end > -1) ? current.substring(end).trim() : "";
                component.setStyle(before + before + ":" + key + value);
            }
        }
        else if(pos >= 0)
        {
            int end = current.indexOf(";", pos + 1);
            String before = current.substring(0, pos).trim();
            String after = (end > -1) ? current.substring(end).trim() : "";
            String complete = before + before;
            component.setStyle((complete.length() > 0) ? complete : null);
        }
    }


    public static void modifyStyle(AbstractTag component, String key, String value)
    {
        String current = component.getStyle();
        int pos = (current != null) ? current.indexOf(key) : -1;
        if(value != null)
        {
            if(pos < 0)
            {
                component.setStyle(((current != null) ? (current + ";") : "") + ((current != null) ? (current + ";") : "") + ":" + key);
            }
            else
            {
                int end = current.indexOf(";", pos + 1);
                String before = current.substring(0, pos).trim();
                if(before.length() > 0 && !before.endsWith(";"))
                {
                    before = before + ";";
                }
                String after = (end > -1) ? current.substring(end).trim() : "";
                component.setStyle(before + before + ":" + key + value);
            }
        }
        else if(pos >= 0)
        {
            int end = current.indexOf(";", pos + 1);
            String before = current.substring(0, pos).trim();
            String after = (end > -1) ? current.substring(end).trim() : "";
            String complete = before + before;
            component.setStyle((complete.length() > 0) ? complete : null);
        }
    }


    public static boolean isFromOtherDesktop(Component comp)
    {
        if(comp == null)
        {
            return true;
        }
        Execution executions = Executions.getCurrent();
        Desktop desk = (executions != null) ? executions.getDesktop() : null;
        return (desk == null || !desk.equals(comp.getDesktop()));
    }


    public static Collection<Object> toColl(Object object)
    {
        return (object instanceof Collection) ? (Collection<Object>)object : Collections.<Object>singletonList(object);
    }


    public static boolean isParent(Component parent, Component child)
    {
        for(Component c = child; c != null; c = c.getParent())
        {
            if(parent.equals(c))
            {
                return true;
            }
        }
        return false;
    }


    public static String getAdjustedUrl(String url)
    {
        if(StringUtils.isNotEmpty(url))
        {
            url = url.trim();
            try
            {
                URI tmpUri = new URI(url);
                if(!tmpUri.isAbsolute() && url.charAt(0) != '~' && url.charAt(0) == '/')
                {
                    url = "~" + url.substring(1);
                }
            }
            catch(Exception e)
            {
                LOG.warn(e.getMessage(), e);
            }
        }
        return url;
    }


    public static boolean isUrlAbsolute(String url)
    {
        try
        {
            URI tmpUri = new URI(url);
            return tmpUri.isAbsolute();
        }
        catch(Exception e)
        {
            LOG.warn(e.getMessage(), e);
            return false;
        }
    }


    public static void showNoPermissionMessage()
    {
        try
        {
            Messagebox.show(Labels.getLabel("no_permission"), Labels.getLabel("general_warning"), 1, "z-msgbox z-msgbox-exclamation");
        }
        catch(InterruptedException e)
        {
            LOG.warn(e.getMessage(), e);
        }
    }


    public static List<String> searchForLabel(Object obj, Method method, Collection<LanguageModel> firstPrioLanguages) throws InvocationTargetException, IllegalArgumentException, IllegalAccessException
    {
        List<String> ret = new ArrayList<>();
        Locale selectedLocale = null;
        I18NService i18nService = (I18NService)Registry.getApplicationContext().getBean("i18nService", I18NService.class);
        Locale sessionLocale = UISessionUtils.getCurrentSession().getLocale();
        Set<Locale> langLocales = new LinkedHashSet<>();
        langLocales.add(sessionLocale);
        langLocales.addAll(Arrays.asList(i18nService.getFallbackLocales(sessionLocale)));
        if(firstPrioLanguages != null)
        {
            for(LanguageModel lang : firstPrioLanguages)
            {
                langLocales.add(createLocale(lang.getIsocode()));
            }
            if(langLocales.contains(sessionLocale) && method.invoke(obj, new Object[] {sessionLocale}) != null)
            {
                selectedLocale = sessionLocale;
            }
            else
            {
                for(Locale loc : langLocales)
                {
                    if(method.invoke(obj, new Object[] {loc}) != null)
                    {
                        selectedLocale = loc;
                        break;
                    }
                }
            }
        }
        if(selectedLocale == null)
        {
            langLocales.clear();
            for(LanguageModel lang : UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguages())
            {
                langLocales.add(createLocale(lang.getIsocode()));
            }
            if(langLocales.contains(sessionLocale) && method.invoke(obj, new Object[] {sessionLocale}) != null)
            {
                selectedLocale = sessionLocale;
            }
            else
            {
                for(Locale loc : langLocales)
                {
                    if(method.invoke(obj, new Object[] {loc}) != null)
                    {
                        selectedLocale = loc;
                        break;
                    }
                }
            }
        }
        if(selectedLocale != null)
        {
            ret.add((String)method.invoke(obj, new Object[] {selectedLocale}));
            ret.add(selectedLocale.getLanguage());
        }
        return ret;
    }


    private static Locale createLocale(String isocode)
    {
        String[] loc = Utilities.parseLocaleCodes(isocode);
        return new Locale(loc[0], loc[1], loc[2]);
    }


    public static void forceRenderCombobox(Combobox comboBox, ComboitemRenderer renderer, Collection<? extends Object> model)
    {
        if(comboBox != null && model != null)
        {
            Object object = renderer;
            if(renderer == null)
            {
                object = new Object();
            }
            for(Object data : model)
            {
                Comboitem comboItem = new Comboitem();
                comboBox.appendChild((Component)comboItem);
                try
                {
                    object.render(comboItem, data);
                }
                catch(Exception e)
                {
                    LOG.error("Rendering of item '" + data + "' failed.");
                    break;
                }
            }
        }
        else
        {
            LOG.error("Argument with null value found. Aborting rendering.");
        }
    }


    public static String getLocalDate(Date date)
    {
        Locale locale = UISessionUtils.getCurrentSession().getGlobalDataLocale();
        DateFormat formatter = DateFormat.getDateInstance(3, locale);
        return formatter.format(date);
    }


    public static String getLocalDateTime(Date date)
    {
        Locale locale = UISessionUtils.getCurrentSession().getGlobalDataLocale();
        DateFormat formatter = DateFormat.getDateTimeInstance(3, 3, locale);
        return formatter.format(date);
    }


    public static void addBusyListener(Component component, String evtnm, EventListener listener, String data, String i3BusyMessage)
    {
        component.addEventListener(evtnm, (EventListener)new Object(i3BusyMessage, data, evtnm, component));
        component.addEventListener(evtnm + "Later", (EventListener)new Object(listener));
    }


    public static void performDoubleClickDelay(WebApp webApp)
    {
        String delayStr = webApp.getConfiguration().getPreference("de.hybris.platform.cockpit.doubleClickDelay", null);
        if(delayStr != null)
        {
            try
            {
                int delay = Integer.parseInt(delayStr);
                Thread.sleep(delay);
            }
            catch(Exception e)
            {
                LOG.error(e.getMessage());
            }
        }
    }


    public static String getWebAppName(Execution execution)
    {
        return ((ServletContext)execution.getDesktop().getWebApp().getNativeContext()).getServletContextName();
    }


    public static String getWebAppName(Desktop desktop)
    {
        return ((ServletContext)desktop.getWebApp().getNativeContext()).getServletContextName();
    }


    public static String getCockpitParameter(String key, Desktop desktop)
    {
        String parameter = null;
        try
        {
            String webapp = getWebAppName(desktop);
            parameter = Config.getParameter(webapp + "." + webapp);
        }
        catch(Exception e)
        {
            LOG.error("Could not get webapp-specific config, reason was:\n", e);
        }
        return (parameter == null) ? Config.getParameter("cockpit." + key) : parameter;
    }


    public static String getCockpitParameter(String key, Execution execution)
    {
        if(execution != null)
        {
            return getCockpitParameter(key, execution.getDesktop());
        }
        LOG.warn("Execution cannot be null. Is ZK available?");
        return null;
    }


    public static void addMacCommandClickListener(XulElement component)
    {
        component.setAction("onClick: doCmd(this,event)");
    }


    public static void setComponentWidths(String[] widths, HtmlBasedComponent... comps)
    {
        if(widths != null && comps != null)
        {
            int maxIndex = Math.min(widths.length, comps.length);
            for(int i = 0; i < maxIndex; i++)
            {
                String width = widths[i];
                comps[i].setWidth(width);
            }
        }
    }


    public static void invalidateAllPortallayouts()
    {
        Collection components = Executions.getCurrent().getDesktop().getComponents();
        for(Object object : components)
        {
            if(object instanceof Portallayout)
            {
                ((Portallayout)object).invalidate();
            }
        }
    }


    public static String removeHtml(String html)
    {
        return StringEscapeUtils.unescapeHtml(html.replaceAll("\\<[^>]*>", ""));
    }


    public static void removeEventListenersForComponent(String eventType, Component component)
    {
        if(component != null)
        {
            for(Iterator<EventListener> listeners = component.getListenerIterator(eventType); listeners.hasNext(); )
            {
                component.removeEventListener(eventType, listeners.next());
            }
        }
    }


    public static void scrollIntoViewIfNeeded(Component component, Component scrollContainer)
    {
        scrollIntoViewIfNeeded(component, scrollContainer, 0);
    }


    public static void scrollIntoViewIfNeeded(Component component, Component scrollContainer, int childDepth)
    {
        StringBuilder firstChildSuffixBuilder = new StringBuilder("");
        for(int i = 0; i < childDepth; i++)
        {
            firstChildSuffixBuilder.append(".firstChild");
        }
        Clients.evalJavaScript("scrollIfVerticallyHidden(document.getElementById('" + component.getUuid() + "'),document.getElementById('" + scrollContainer
                        .getUuid() + "')" + firstChildSuffixBuilder.toString() + ")");
    }


    public static void updateMediaModel(MediaModel currentMediaModel, Media mediaContent, MediaService mediaService)
    {
        try
        {
            byte[] byteData;
            if(mediaContent.isBinary())
            {
                if(mediaContent.inMemory())
                {
                    byteData = mediaContent.getByteData();
                }
                else
                {
                    byteData = IOUtils.toByteArray(mediaContent.getStreamData());
                }
            }
            else if(mediaContent.inMemory())
            {
                byteData = mediaContent.getStringData().getBytes();
            }
            else
            {
                byteData = IOUtils.toByteArray(mediaContent.getReaderData(), Charset.defaultCharset());
            }
            currentMediaModel.setRealFileName(mediaContent.getName());
            currentMediaModel.setMime(mediaContent.getContentType());
            mediaService.setDataForMedia(currentMediaModel, byteData);
        }
        catch(Exception e)
        {
            LOG.error("Could not set media data, reason: ", e);
        }
    }


    public static boolean searchRestrictionsDisabledInCockpit()
    {
        Object param = getCockpitParameter("disableRestrictions",
                        Executions.getCurrent());
        return (!(param instanceof String) || StringUtils.isBlank((String)param) || Boolean.parseBoolean((String)param));
    }


    public static boolean setManagedEventListener(Component component, String evtnm, EventListener listener)
    {
        String evtkey = "mxListener_" + evtnm;
        Object attribute = component.getAttribute(evtkey);
        if(attribute instanceof EventListener)
        {
            component.removeEventListener(evtnm, (EventListener)attribute);
        }
        component.setAttribute(evtkey, listener);
        return component.addEventListener(evtnm, listener);
    }
}

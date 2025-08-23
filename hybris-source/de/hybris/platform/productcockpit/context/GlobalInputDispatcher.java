package de.hybris.platform.productcockpit.context;

import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zkplus.spring.SpringUtil;

public class GlobalInputDispatcher
{
    private static final Logger LOG = LoggerFactory.getLogger(GlobalInputDispatcher.class);
    public static final String FOCUSED_STYLE = "focused";
    public static final String GLOBAL_INPUT_DISPATCHER_BEAN = "GlobalInputDispatcher";
    public static final String PASTE_TARGET = "paste_target";
    private Map<String, List<KeyBinding>> bindings;
    private List<KeyBinding> currentBindings;


    public static GlobalInputDispatcher getInstance()
    {
        return (GlobalInputDispatcher)SpringUtil.getBean("GlobalInputDispatcher");
    }


    private final List<GlobalInputListener> listeners = new ArrayList<>();
    private String messageBoxURI = "/messagebox.zul";
    private final Map<String, Object> context = new HashMap<>();
    private Component focused;


    @Deprecated
    public void initView(Component parent)
    {
    }


    public void triggerKey(String code)
    {
        List<KeyBinding> kb = matchBindings(code);
        if(!kb.isEmpty())
        {
            fireKeyPressed(kb.get(0), null);
        }
        else
        {
            LOG.error("no key binding found for " + code + " got " + getCurrentKeyBindings());
        }
    }


    public String getKeyBindingText(String code)
    {
        List<KeyBinding> kb = matchBindings(code);
        if(!kb.isEmpty())
        {
            StringBuilder sb = new StringBuilder("( ");
            boolean first = true;
            for(KeyBinding k : kb)
            {
                if(first)
                {
                    first = false;
                }
                else
                {
                    sb.append(", ");
                }
                String ctrl = null;
                if(k.isCtrl())
                {
                    ctrl = Labels.getLabel("ctrl");
                }
                else if(k.isAlt())
                {
                    ctrl = Labels.getLabel("alt");
                }
                else if(k.isShift())
                {
                    ctrl = Labels.getLabel("shift");
                }
                sb.append((ctrl != null) ? (ctrl + ctrl) : "").append(k.getKeyText());
            }
            sb.append(" )");
            return sb.toString();
        }
        return null;
    }


    @Required
    public void setKeyBindings(Map<String, List<KeyBinding>> bindings)
    {
        this.bindings = Collections.unmodifiableMap(bindings);
    }


    public String getMessageBoxURI()
    {
        return this.messageBoxURI;
    }


    public void setMessageBoxURI(String messageBoxURI)
    {
        this.messageBoxURI = messageBoxURI;
    }


    @Required
    public void setGlobalInputListeners(List<GlobalInputListener> listeners)
    {
        this.listeners.clear();
        if(listeners != null)
        {
            this.listeners.addAll(listeners);
        }
    }


    public void addGlobalInputListener(GlobalInputListener l)
    {
        this.listeners.add(l);
    }


    public void removeGlobalInputListener(GlobalInputListener l)
    {
        this.listeners.remove(l);
    }


    public String getControlKeys()
    {
        StringBuilder sb = new StringBuilder();
        for(KeyBinding kb : getCurrentKeyBindings())
        {
            sb.append(kb.getControlKey());
        }
        return sb.toString();
    }


    protected List<KeyBinding> getCurrentKeyBindings()
    {
        if(this.currentBindings == null)
        {
            List<KeyBinding> ret = Collections.EMPTY_LIST;
            HttpServletRequest req = (HttpServletRequest)Executions.getCurrent().getNativeRequest();
            String userAgent = req.getHeader("user-agent");
            for(Map.Entry<String, List<KeyBinding>> entry : this.bindings.entrySet())
            {
                Pattern p = Pattern.compile(entry.getKey(), 2);
                Matcher m = p.matcher(userAgent);
                if(m.matches())
                {
                    ret = entry.getValue();
                    break;
                }
            }
            this.currentBindings = ret;
        }
        return this.currentBindings;
    }


    protected KeyBinding matchBinding(KeyEvent evt)
    {
        int code = evt.getKeyCode();
        int modifiers = 0;
        if(evt.isAltKey())
        {
            modifiers |= 0x4;
        }
        if(evt.isShiftKey())
        {
            modifiers |= 0x1;
        }
        if(evt.isCtrlKey())
        {
            modifiers |= 0x2;
        }
        return matchBinding(code, modifiers);
    }


    protected KeyBinding matchBinding(int keyCode, int modifiers)
    {
        for(KeyBinding kb : getCurrentKeyBindings())
        {
            if(kb.getKeyCode() == keyCode && (kb.getModifiers() & modifiers) == modifiers)
            {
                return kb;
            }
        }
        return null;
    }


    protected List<KeyBinding> matchBindings(String code)
    {
        List<KeyBinding> ret = null;
        for(KeyBinding kb : getCurrentKeyBindings())
        {
            if(code.equalsIgnoreCase(kb.getName()))
            {
                if(ret == null)
                {
                    ret = new ArrayList<>(3);
                }
                ret.add(kb);
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_LIST;
    }


    protected void fireFocusChanged(Component prev, Component newOne)
    {
        FocusLostEvent e;
        Events.postEvent((Event)(e = new FocusLostEvent(prev, newOne)));
        if(!this.listeners.isEmpty())
        {
            for(GlobalInputListener l : new ArrayList(this.listeners))
            {
                try
                {
                    l.focusChanged(e);
                }
                catch(RuntimeException ex)
                {
                    LOG.error("error notifying " + l + " : " + ex.getMessage(), ex);
                }
            }
        }
    }


    protected void fireKeyPressed(KeyBinding kb, KeyEvent original)
    {
        GlobalKeyEvent e = new GlobalKeyEvent(this.focused, kb, original);
        if(this.focused != null)
        {
            Events.postEvent((Event)e);
        }
        if(!this.listeners.isEmpty())
        {
            for(GlobalInputListener l : new ArrayList(this.listeners))
            {
                try
                {
                    l.keyPressed(e);
                }
                catch(RuntimeException ex)
                {
                    LOG.error("error notifying " + l + " : " + ex.getMessage(), ex);
                }
            }
        }
    }


    protected void keyPressed(KeyEvent evt)
    {
        KeyBinding kb = matchBinding(evt);
        if(kb != null)
        {
            fireKeyPressed(kb, evt);
        }
    }


    @Deprecated
    public void deactivate()
    {
    }


    @Deprecated
    public void activate()
    {
    }


    public Component getFocused()
    {
        return this.focused;
    }


    public void setFocused(Component comp)
    {
        if(this.focused != comp && (this.focused == null || !this.focused.equals(comp)))
        {
            Component previousFocused = this.focused;
            this.focused = comp;
            if(previousFocused == null || Executions.getCurrent().getDesktop().equals(previousFocused.getDesktop()))
            {
                showFocus(previousFocused, false);
                fireFocusChanged(previousFocused, this.focused);
            }
            if(this.focused instanceof HtmlBasedComponent)
            {
                showFocus(this.focused, true);
            }
        }
    }


    protected void showFocus(Component comp, boolean on)
    {
        if(comp instanceof HtmlBasedComponent)
        {
            UITools.modifySClass((HtmlBasedComponent)comp, "focused", on);
        }
    }


    public void clearFocus()
    {
        setFocused(null);
    }


    public void setAttribute(String key, Object value)
    {
        this.context.put(key, value);
    }


    public Object getAttribute(String key)
    {
        return this.context.get(key);
    }
}

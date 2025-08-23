package de.hybris.platform.productcockpit.context;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.KeyEvent;

public class GlobalKeyEvent extends Event
{
    public static final String ON_GLOBAL_KEY = "onGlobalKey";
    private final KeyBinding keyBinding;
    private final KeyEvent keyEvent;


    public KeyBinding getKeyBinding()
    {
        return this.keyBinding;
    }


    public KeyEvent getKeyEvent()
    {
        return this.keyEvent;
    }


    public GlobalKeyEvent(Component target, KeyBinding binding, KeyEvent original)
    {
        super("onGlobalKey", target);
        this.keyBinding = binding;
        this.keyEvent = original;
    }
}

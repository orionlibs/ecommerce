package de.hybris.platform.cockpit.components;

import de.hybris.platform.cockpit.session.Lockable;
import de.hybris.platform.cockpit.util.UITools;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Div;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Toolbarbutton;

public class LockableGroupbox extends AdvancedGroupbox implements Lockable
{
    private static final String LOCK_SCLASS = "sectionLock";
    private final boolean lockable;
    private boolean locked = false;
    private transient Toolbarbutton lockBtn = null;


    public LockableGroupbox()
    {
        this(false);
    }


    public LockableGroupbox(boolean lockable)
    {
        this(lockable, false);
    }


    public LockableGroupbox(boolean lockable, boolean locked)
    {
        this(lockable, locked, null);
    }


    public LockableGroupbox(boolean lockable, boolean locked, EventListener lockListener)
    {
        this.lockable = lockable;
        this.locked = locked;
        if(lockable)
        {
            initLock(lockListener);
        }
    }


    public boolean isLockable()
    {
        return this.lockable;
    }


    public boolean isLocked()
    {
        return this.locked;
    }


    public void setLocked(boolean locked)
    {
        if(this.lockable)
        {
            this.locked = locked;
            if(this.lockBtn != null)
            {
                this.lockBtn.setImage(locked ? "/cockpit/images/icon_locked.png" : "/cockpit/images/icon_unlocked.png");
                this.lockBtn.setTooltiptext(Labels.getLabel("lockablegbox." + (locked ? "unlock" : "lock")));
            }
        }
    }


    private void initLock(EventListener lockListener)
    {
        UITools.modifySClass((HtmlBasedComponent)this, "lockableGroupbox", true);
        Div lockDiv = new Div();
        lockDiv.setParent((Component)getPreLabelComponent());
        lockDiv.setSclass("sectionLock");
        this.lockBtn = new Toolbarbutton("", this.locked ? "/cockpit/images/icon_locked.png" : "/cockpit/images/icon_unlocked.png");
        this.lockBtn.setParent((Component)lockDiv);
        this.lockBtn.setTooltiptext(Labels.getLabel("lockablegbox." + (this.locked ? "unlock" : "lock")));
        if(lockListener != null)
        {
            this.lockBtn.addEventListener("onClick", lockListener);
        }
    }


    public void addLockListener(EventListener lockListener)
    {
        if(this.lockable && this.lockBtn != null)
        {
            this.lockBtn.addEventListener("onClick", lockListener);
        }
    }


    public void remmoveLockListener(EventListener lockListener)
    {
        if(this.lockBtn != null)
        {
            this.lockBtn.removeEventListener("onClick", lockListener);
        }
    }
}

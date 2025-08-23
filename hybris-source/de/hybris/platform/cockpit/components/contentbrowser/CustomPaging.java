package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.util.UITools;
import org.apache.commons.lang.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Toolbarbutton;

public class CustomPaging extends Div
{
    public static final String ON_PAGING_MODE_CHANGE = "onPagingModeChange";
    protected static final String ON_PAGING = "onPaging";
    protected static final String NORMAL_MOLD = "normal";
    protected static final String SIMPLE_MOLD = "simple";
    protected static final String MANAGED_MOLD = "managed";
    private transient Paging paging;
    private transient Toolbarbutton previousButton;
    private transient Toolbarbutton nextButton;
    private transient Checkbox checkbox;
    private transient Div simplePanel;
    private transient Intbox activePageTextbox;
    private final transient String mold;
    private boolean simpleMode;
    private int totalSize;
    private int activePage;
    private int pageSize;
    private boolean disabledNextButton;
    private boolean disabledPreviousButton;


    public CustomPaging(boolean simpleMode, String mold)
    {
        this.mold = mold;
        this.simpleMode = simpleMode;
    }


    public void setSimpleMode(boolean simpleMode)
    {
        if(this.simpleMode != simpleMode)
        {
            this.simpleMode = simpleMode;
            this.simplePanel.setVisible(simpleMode);
            this.paging.setVisible(!simpleMode);
            Events.postEvent("onPagingModeChange", (Component)this, Boolean.valueOf(this.simpleMode));
        }
    }


    public void setPreviousButtonDisalbled(boolean disabled)
    {
        this.disabledPreviousButton = disabled;
        this.previousButton.setDisabled(disabled);
    }


    public void setNextButtonDisabled(boolean disabled)
    {
        this.disabledNextButton = disabled;
        this.nextButton.setDisabled(disabled);
    }


    protected int getActivePageNormalized()
    {
        return this.activePage + 1;
    }


    public void update()
    {
        this.previousButton.setDisabled((this.activePage == 0 || this.disabledPreviousButton));
        this.activePageTextbox.setValue(Integer.valueOf(getActivePageNormalized()));
        this.nextButton.setDisabled(this.disabledNextButton);
    }


    public void init()
    {
        UITools.detachChildren((Component)this);
        Div customPagingCnt = new Div();
        customPagingCnt.setSclass("custompaging_cnt");
        Hbox internalCnt = new Hbox();
        internalCnt.setParent((Component)customPagingCnt);
        this.paging = new Paging();
        this.simplePanel = new Div();
        this.checkbox = new Checkbox(Labels.getLabel("paging.use_simple_mode"));
        this.checkbox.setSclass("custompaging_checkbox");
        this.checkbox.setChecked(this.simpleMode);
        this.checkbox.addEventListener("onCheck", (EventListener)new Object(this));
        this.previousButton = new Toolbarbutton("", "/cockpit/images/pg-prev-enabled.png");
        this.previousButton.setTooltiptext(Labels.getLabel("paging.previous_page"));
        this.previousButton.setSclass("custompaging_prev_btn");
        this.previousButton.setDisabled((this.activePage == 0 || this.disabledPreviousButton));
        this.previousButton.addEventListener("onClick", (EventListener)new Object(this));
        this.nextButton = new Toolbarbutton("", "/cockpit/images/pg-next-enabled.png");
        this.nextButton.setTooltiptext(Labels.getLabel("paging.next_page"));
        this.nextButton.setSclass("custompaging_next_btn");
        this.nextButton.setDisabled(this.disabledNextButton);
        this.nextButton.addEventListener("onClick", (EventListener)new Object(this));
        internalCnt.appendChild((Component)this.checkbox);
        internalCnt.appendChild((Component)this.paging);
        internalCnt.appendChild((Component)this.simplePanel);
        this.simplePanel.appendChild((Component)this.previousButton);
        this.activePageTextbox = new Intbox(getActivePageNormalized());
        this.activePageTextbox.setSclass("custompaging_activepage_textbox");
        this.activePageTextbox.setCols(3);
        this.simplePanel.appendChild((Component)this.activePageTextbox);
        this.simplePanel.appendChild((Component)this.nextButton);
        Object object = new Object(this);
        this.activePageTextbox.addEventListener("onChange", (EventListener)object);
        this.activePageTextbox.addEventListener("onOK", (EventListener)object);
        appendChild((Component)customPagingCnt);
        applyCorrectMold();
    }


    public boolean addEventListener(String eventName, EventListener eventListener)
    {
        boolean ret = false;
        if(StringUtils.equals("onPaging", eventName))
        {
            ret = this.paging.addEventListener(eventName, eventListener);
        }
        else
        {
            ret = super.addEventListener(eventName, eventListener);
        }
        return ret;
    }


    protected void applyCorrectMold()
    {
        if(StringUtils.equals("normal", this.mold))
        {
            this.simpleMode = false;
            this.checkbox.setVisible(false);
        }
        else if(StringUtils.equals("simple", this.mold))
        {
            this.simpleMode = true;
            this.checkbox.setVisible(false);
        }
        else if(StringUtils.equals("managed", this.mold))
        {
            this.checkbox.setVisible(true);
        }
        else
        {
            this.simpleMode = false;
            this.checkbox.setVisible(false);
        }
        this.simplePanel.setVisible(this.simpleMode);
        this.paging.setVisible(!this.simpleMode);
    }


    public boolean isSimpleMode()
    {
        return this.simpleMode;
    }


    public int getTotalSize()
    {
        return this.totalSize;
    }


    public int getPageSize()
    {
        return this.pageSize;
    }


    public int getActivePage()
    {
        int ret = this.paging.getActivePage();
        if(isSimpleMode())
        {
            ret = this.activePage;
        }
        return ret;
    }


    public void setTotalSize(int totalSize)
    {
        this.totalSize = totalSize;
        this.paging.setTotalSize(this.totalSize);
    }


    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
        this.paging.setPageSize(this.pageSize);
    }


    public void setActivePage(int activePage)
    {
        this.activePage = activePage;
        if(!this.simpleMode && activePage <= this.paging.getPageCount())
        {
            this.paging.setActivePage(activePage);
        }
    }


    public void setDetailed(boolean detailed)
    {
        this.paging.setDetailed(detailed);
    }


    public void setAutohide(boolean autohide)
    {
        this.paging.setAutohide(autohide);
    }
}

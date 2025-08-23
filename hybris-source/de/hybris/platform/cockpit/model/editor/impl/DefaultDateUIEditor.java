package de.hybris.platform.cockpit.model.editor.impl;

import de.hybris.platform.cockpit.model.editor.DateParametersEditorHelper;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Calendar;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Menuseparator;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.impl.InputElement;

public class DefaultDateUIEditor extends AbstractTextBasedUIEditor
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultDateUIEditor.class);
    protected boolean editingCanceled = false;
    private AbstractUIEditor.CancelButtonContainer cancelButtonContainer = null;
    private boolean popupClosed = false;
    private boolean showTime = true;
    private static final ThreadLocal<List<Date>> lastDatesTL = new ThreadLocal<>();


    private void assureLastDatesAvailable()
    {
        if(lastDatesTL.get() == null)
        {
            lastDatesTL.set(new ArrayList<>());
        }
    }


    private List<Date> getLastDates()
    {
        assureLastDatesAvailable();
        return lastDatesTL.get();
    }


    private void addToLastDates(Date date)
    {
        assureLastDatesAvailable();
        List<Date> list = lastDatesTL.get();
        Date truncatedDate = DateUtils.truncate(date, 12);
        list.remove(truncatedDate);
        list.add(truncatedDate);
        if(list.size() > 5)
        {
            list.remove(0);
        }
    }


    private void addDateHistoryMenuitems(Menupopup popup, Datebox editorView, EditorListener listener)
    {
        List<Menuitem> toAdd = new ArrayList<>();
        List<Date> lastDates = new ArrayList<>(getLastDates());
        for(Date date : lastDates)
        {
            String dateString = DateFormat.getDateTimeInstance(3, 3, UISessionUtils.getCurrentSession().getLocale()).format(date);
            Menuitem item = new Menuitem(dateString);
            item.addEventListener("onClick", (EventListener)new Object(this, editorView, date, listener));
            toAdd.add(0, item);
        }
        if(!toAdd.isEmpty())
        {
            popup.appendChild((Component)new Menuseparator());
            for(Menuitem menuitem : toAdd)
            {
                popup.appendChild((Component)menuitem);
            }
        }
    }


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        Datebox editorView = new Datebox();
        String dateFormat = null;
        dateFormat = DateParametersEditorHelper.getDateFormat(parameters, editorView);
        if(isEditable())
        {
            editorView.setLenient(DateParametersEditorHelper.getLenientFlag(parameters));
            editorView.setButtonVisible(false);
            editorView.setFormat(dateFormat);
            this.showTime = dateFormat.contains(":m");
            editorView.setWidth("100px");
            if(initialValue == null || initialValue instanceof Date)
            {
                editorView.setValue((Date)initialValue);
                this.initialEditValue = initialValue;
            }
            else
            {
                LOG.error("Initial value not of type Date.");
            }
            String attQual = (String)parameters.get("attributeQualifier");
            String customTestID = "date_ed";
            if(attQual != null)
            {
                customTestID = attQual + "_" + attQual;
            }
            UITools.applyTestID((Component)editorView, customTestID);
            return (HtmlBasedComponent)createViewComponentInternal((InputElement)editorView, listener, parameters);
        }
        Label dateLabel = new Label((initialValue != null) ? (new SimpleDateFormat(dateFormat)).format(initialValue) : "-");
        return (HtmlBasedComponent)dateLabel;
    }


    protected AbstractUIEditor.CancelButtonContainer createViewComponentInternal(InputElement editorView, EditorListener listener, Map<String, ? extends Object> parameters)
    {
        parseInitialInputString(parameters);
        if(!isEditable())
        {
            editorView.setDisabled(true);
        }
        Datebox mainDatebox = (Datebox)editorView;
        this.inEditMode = false;
        this.cancelButtonContainer = new AbstractUIEditor.CancelButtonContainer((AbstractUIEditor)this, listener, (AbstractUIEditor.CancelListener)new Object(this, listener, editorView));
        this.cancelButtonContainer.setSclass("dateEditorContainer");
        Div containerDiv = new Div();
        containerDiv.appendChild((Component)editorView);
        Div butnDiv = new Div();
        butnDiv.setSclass("z-datebox");
        butnDiv.setStyle("display: inline; position: relative; left: -4px;");
        Popup dateTimePopup = new Popup();
        Image dateBoxBtn = new Image();
        butnDiv.appendChild((Component)dateBoxBtn);
        dateBoxBtn.setSclass("z-datebox-img");
        dateBoxBtn.setStyle("border-right: 1px solid #86A4BE;");
        dateBoxBtn.setWidth("16px");
        dateBoxBtn.setHeight("18px");
        if(isEditable())
        {
            dateBoxBtn.addEventListener("onClick", (EventListener)new Object(this, dateTimePopup, containerDiv));
        }
        dateTimePopup.setSclass("date_time_popup");
        containerDiv.appendChild((Component)dateTimePopup);
        Calendar cal = new Calendar();
        Timebox time = new Timebox();
        dateTimePopup.addEventListener("onOpen", (EventListener)new Object(this, editorView, mainDatebox, cal, time, listener));
        cal.setCompact(true);
        dateTimePopup.appendChild((Component)cal);
        Div timeOKDiv = new Div();
        timeOKDiv.setSclass("time_ok_container");
        dateTimePopup.appendChild((Component)timeOKDiv);
        if(this.showTime)
        {
            timeOKDiv.appendChild((Component)time);
        }
        Button okBtn = new Button(Labels.getLabel("general.ok"));
        timeOKDiv.appendChild((Component)okBtn);
        okBtn.setSclass("date_time_ok btnblue");
        okBtn.addEventListener("onClick", (EventListener)new Object(this, mainDatebox, cal, time, dateTimePopup));
        containerDiv.appendChild((Component)butnDiv);
        Object showExtrasStr = parameters.get("showExtrasMenu");
        if(isEditable() && ((showExtrasStr instanceof String &&
                        !((String)showExtrasStr).trim().equalsIgnoreCase("false")) || showExtrasStr == null))
        {
            Div extrasDiv = new Div();
            extrasDiv.setSclass("z-combobox dateExtras");
            extrasDiv.setStyle("display: inline; position: relative; left: 4px;");
            Image extrasBtn = new Image();
            extrasBtn.setSclass("z-combobox-img dateExtras");
            extrasBtn.setTooltiptext(Labels.getLabel("datePicker.dateExtras"));
            extrasBtn.setWidth("16px");
            extrasBtn.setHeight("18px");
            extrasDiv.appendChild((Component)extrasBtn);
            butnDiv.appendChild((Component)extrasDiv);
            Menupopup extrasMenuPop = new Menupopup();
            butnDiv.appendChild((Component)extrasMenuPop);
            butnDiv.addEventListener("onClick", (EventListener)new Object(this, extrasMenuPop, containerDiv));
            extrasMenuPop.appendChild((Component)new Menuitem(Labels.getLabel("editor.date.setToClient")));
            extrasMenuPop.appendChild((Component)new Menuitem(Labels.getLabel("editor.date.setToServer")));
            extrasMenuPop.addEventListener("onOpen", (EventListener)new Object(this, extrasMenuPop, mainDatebox, editorView, listener));
        }
        this.cancelButtonContainer.setContent((Component)containerDiv);
        editorView.addEventListener("onFocus", (EventListener)new Object(this));
        editorView.addEventListener("onBlur", (EventListener)new Object(this, editorView, listener));
        editorView.addEventListener("onOK", (EventListener)new Object(this, editorView, listener));
        editorView.addEventListener("onCancel", (EventListener)new Object(this, editorView, listener));
        editorView.addEventListener("onLater", (EventListener)new Object(this, listener));
        return this.cancelButtonContainer;
    }


    private void doSetValueAndExit(InputElement editorView, EditorListener listener)
    {
        this.inEditMode = true;
        setValue(editorView.getRawValue());
        fireValueChanged(listener, (Component)editorView);
        this.cancelButtonContainer.showButton(false);
    }


    protected void fireValueChanged(EditorListener listener, Component component)
    {
        if(this.inEditMode)
        {
            Events.echoEvent("onLater", component, null);
        }
    }


    protected void fireValueChanged(EditorListener listener)
    {
        if(this.inEditMode)
        {
            super.fireValueChanged(listener);
            this.editingCanceled = false;
            this.initialEditValue = getValue();
            if(getValue() instanceof Date)
            {
                addToLastDates((Date)getValue());
            }
        }
    }


    private Date mergeDate(Date date, Date time)
    {
        if(time == null)
        {
            return date;
        }
        int hours = time.getHours();
        int minutes = time.getMinutes();
        Date ret = DateUtils.setHours(date, hours);
        ret = DateUtils.setMinutes(ret, minutes);
        return ret;
    }


    public String getEditorType()
    {
        return "DATE";
    }


    public boolean isInline()
    {
        return true;
    }


    public void setFocus(HtmlBasedComponent rootEditorComponent, boolean selectAll)
    {
        Datebox element = (Datebox)((AbstractUIEditor.CancelButtonContainer)rootEditorComponent).getContent().getFirstChild();
        element.setFocus(true);
        if(selectAll)
        {
            element.select();
        }
        if(this.initialInputString != null && this.initialInputString.length() == 1)
        {
            Integer firstNumber = null;
            try
            {
                firstNumber = Integer.valueOf(Integer.parseInt(this.initialInputString));
            }
            catch(Exception e)
            {
                LOG.debug(e.getMessage(), e);
            }
            if(firstNumber != null && firstNumber.intValue() < 4)
            {
                String tmpStr = this.initialInputString + "0.00.0000";
                element.setText(tmpStr);
                element.setSelectionRange(1, tmpStr.length());
            }
        }
    }
}

/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.common.messagebox;

import com.hybris.cockpitng.core.Executable;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

/**
 * Message box which allows to display modal message box which overlays parent component only (not entire application).
 * To configure popup use {@link Builder}. To show popup use {@link #show(HtmlBasedComponent)}
 */
public class WidgetModalMessageBox
{
    protected static final String DEFAULT_NOTIFICATION_ID = "widgetModalMessageOverlay";
    protected static final String SCLASS_YW_WIDGET_MODAL = "yw-widget-modal";
    protected static final String SCLASS_MESSAGEBOX = SCLASS_YW_WIDGET_MODAL + "-messagebox";
    protected static final String SCLASS_TITLE = SCLASS_MESSAGEBOX + "-title";
    protected static final String SCLASS_NAVIGATION = SCLASS_MESSAGEBOX + "-navigation";
    protected static final String SCLASS_MESSAGE = SCLASS_MESSAGEBOX + "-message";
    public static final String SCLASS_WARNING = SCLASS_MESSAGEBOX + "-warning";
    private String title;
    private String cancelLabel;
    private String confirmLabel;
    private Executable onCancel;
    private Executable onConfirm;
    private Component content;
    private String headerText;
    private String footerText;
    private String messageText;
    private String sclass;
    private String headerSclass;
    private String messageSclass;
    private String footerSclass;
    private String identifier;


    /**
     * Adds to given parent configured message box. Parent must accept div as children. If there is already defined child
     * of given {@link #getIdentifierOrDefault()} then it will be replaced.
     *
     * @param parent
     *           parent on which modal msg box should be displayed.
     */
    public void show(final HtmlBasedComponent parent)
    {
        if(parent == null)
        {
            return;
        }
        final HtmlBasedComponent widgetModalMessageOverlay = createAnReplaceOverlay(parent);
        widgetModalMessageOverlay.setVisible(true);
        final Div modalWindow = new Div();
        modalWindow.setSclass(computeSclass(SCLASS_MESSAGEBOX, sclass));
        final Div titleContainer = new Div();
        titleContainer.setParent(modalWindow);
        titleContainer.appendChild(createTitleLabel());
        final Button closeBtn = new Button();
        closeBtn.setSclass(SCLASS_TITLE + "-btn-close");
        titleContainer.appendChild(closeBtn);
        titleContainer.setSclass(SCLASS_TITLE);
        modalWindow.appendChild(createContentArea());
        final Div navigationArea = new Div();
        navigationArea.setParent(modalWindow);
        navigationArea.setSclass(SCLASS_NAVIGATION);
        if(onConfirm != null)
        {
            final Button confirm = createConfirmBtn();
            navigationArea.appendChild(confirm);
            confirm.addEventListener(Events.ON_CLICK, event -> closeAndExecute(widgetModalMessageOverlay, onConfirm));
        }
        final Button cancelBtn = createCancelBtn();
        navigationArea.appendChild(cancelBtn);
        final EventListener<Event> close = event -> closeAndExecute(widgetModalMessageOverlay, onCancel);
        cancelBtn.addEventListener(Events.ON_CLICK, close);
        closeBtn.addEventListener(Events.ON_CLICK, close);
        widgetModalMessageOverlay.appendChild(modalWindow);
    }


    protected String computeSclass(final String mainSclass, final String additionalSclass)
    {
        return StringUtils.isNotBlank(additionalSclass) ? String.format("%s %s", mainSclass, additionalSclass) : mainSclass;
    }


    public void closeExisting(final HtmlBasedComponent parent)
    {
        if(parent == null)
        {
            return;
        }
        final Component widgetModalMessageOverlay = parent.getFellowIfAny(getIdentifierOrDefault());
        if(widgetModalMessageOverlay != null)
        {
            parent.removeChild(widgetModalMessageOverlay);
        }
    }


    protected Div createContentArea()
    {
        final Div textArea = new Div();
        textArea.setSclass(SCLASS_MESSAGE);
        if(content != null)
        {
            textArea.appendChild(content);
        }
        else
        {
            if(StringUtils.isNotBlank(headerText))
            {
                final Label label = new Label(headerText);
                label.setSclass(computeSclass(SCLASS_MESSAGE + "-header", headerSclass));
                textArea.appendChild(label);
            }
            if(StringUtils.isNotBlank(messageText))
            {
                final Label label = new Label(messageText);
                label.setSclass(computeSclass(SCLASS_MESSAGE + "-text", messageSclass));
                textArea.appendChild(label);
            }
            if(StringUtils.isNotBlank(footerText))
            {
                final Label label = new Label(footerText);
                label.setSclass(computeSclass(SCLASS_MESSAGE + "-footer", footerSclass));
                textArea.appendChild(label);
            }
        }
        return textArea;
    }


    protected Button createConfirmBtn()
    {
        final String label = StringUtils.defaultIfBlank(confirmLabel, Labels.getLabel("widget.modal.messagebox.btn.confirm"));
        final Button confirm = new Button(label);
        confirm.setSclass(SCLASS_NAVIGATION + "-btn-confirm y-btn-primary");
        return confirm;
    }


    protected Button createCancelBtn()
    {
        final String label = StringUtils.defaultIfBlank(cancelLabel, Labels.getLabel("widget.modal.messagebox.btn.cancel"));
        final Button cancelBtn = new Button(label);
        cancelBtn.setSclass(SCLASS_NAVIGATION + "-btn-cancel");
        return cancelBtn;
    }


    protected Label createTitleLabel()
    {
        final Label titleLabel = new Label();
        if(StringUtils.isNotBlank(title))
        {
            titleLabel.setValue(title);
        }
        titleLabel.setSclass(SCLASS_TITLE + "-label");
        return titleLabel;
    }


    protected void closeAndExecute(final HtmlBasedComponent widgetModalMessageOverlay, final Executable action)
    {
        widgetModalMessageOverlay.detach();
        if(action != null)
        {
            action.execute();
        }
    }


    protected HtmlBasedComponent createAnReplaceOverlay(final HtmlBasedComponent parent)
    {
        closeExisting(parent);
        final Div overlay = new Div();
        overlay.setId(getIdentifierOrDefault());
        overlay.setSclass(SCLASS_YW_WIDGET_MODAL);
        parent.appendChild(overlay);
        return overlay;
    }


    protected String getIdentifierOrDefault()
    {
        return StringUtils.defaultIfBlank(identifier, DEFAULT_NOTIFICATION_ID);
    }


    public String getTitle()
    {
        return title;
    }


    public void setTitle(final String title)
    {
        this.title = title;
    }


    public String getCancelLabel()
    {
        return cancelLabel;
    }


    public void setCancelLabel(final String cancelLabel)
    {
        this.cancelLabel = cancelLabel;
    }


    public String getConfirmLabel()
    {
        return confirmLabel;
    }


    public void setConfirmLabel(final String confirmLabel)
    {
        this.confirmLabel = confirmLabel;
    }


    public Executable getOnCancel()
    {
        return onCancel;
    }


    public void setOnCancel(final Executable onCancel)
    {
        this.onCancel = onCancel;
    }


    public Executable getOnConfirm()
    {
        return onConfirm;
    }


    public void setOnConfirm(final Executable onConfirm)
    {
        this.onConfirm = onConfirm;
    }


    public Component getContent()
    {
        return content;
    }


    public void setContent(final Component content)
    {
        this.content = content;
    }


    public String getHeaderText()
    {
        return headerText;
    }


    public void setHeaderText(final String headerText)
    {
        this.headerText = headerText;
    }


    public String getFooterText()
    {
        return footerText;
    }


    public void setFooterText(final String footerText)
    {
        this.footerText = footerText;
    }


    public String getMessageText()
    {
        return messageText;
    }


    public void setMessageText(final String messageText)
    {
        this.messageText = messageText;
    }


    public String getSclass()
    {
        return sclass;
    }


    public void setSclass(final String sclass)
    {
        this.sclass = sclass;
    }


    public String getHeaderSclass()
    {
        return headerSclass;
    }


    public void setHeaderSclass(final String headerSclass)
    {
        this.headerSclass = headerSclass;
    }


    public String getMessageSclass()
    {
        return messageSclass;
    }


    public void setMessageSclass(final String messageSclass)
    {
        this.messageSclass = messageSclass;
    }


    public String getFooterSclass()
    {
        return footerSclass;
    }


    public void setFooterSclass(final String footerSclass)
    {
        this.footerSclass = footerSclass;
    }


    public String getIdentifier()
    {
        return identifier;
    }


    public void setIdentifier(final String identifier)
    {
        this.identifier = identifier;
    }


    /**
     * Builder for {@link WidgetModalMessageBox}
     */
    public static class Builder
    {
        private String title;
        private String cancelLabel;
        private String confirmLabel;
        private Executable onCancel;
        private Executable onConfirm;
        private Component content;
        private String headerText;
        private String footerText;
        private String messageText;
        private String sclass;
        private String headerSclass;
        private String messageSclass;
        private String footerSclass;
        private String identifier;


        /**
         * Builds message box
         *
         * @return message box configured according to used settings.
         */
        public WidgetModalMessageBox build()
        {
            final WidgetModalMessageBox msgBox = new WidgetModalMessageBox();
            msgBox.setTitle(title);
            msgBox.setCancelLabel(cancelLabel);
            msgBox.setConfirmLabel(confirmLabel);
            msgBox.setOnCancel(onCancel);
            msgBox.setOnConfirm(onConfirm);
            msgBox.setContent(content);
            msgBox.setHeaderText(headerText);
            msgBox.setFooterText(footerText);
            msgBox.setMessageText(messageText);
            msgBox.setSclass(sclass);
            msgBox.setHeaderSclass(headerSclass);
            msgBox.setMessageSclass(messageSclass);
            msgBox.setFooterSclass(footerSclass);
            msgBox.setIdentifier(identifier);
            return msgBox;
        }


        /**
         * Defines title of of widget.
         *
         * @param title
         *           title to display
         * @return this
         */
        public Builder withTitle(final String title)
        {
            this.title = title;
            return this;
        }


        /**
         * Defines cancel button label.
         *
         * @param cancelLabel
         *           label to be displayed on the button. If not defined then default label will be used.
         * @return this.
         */
        public Builder withCancel(final String cancelLabel)
        {
            return withCancel(cancelLabel, null);
        }


        /**
         * Defines cancel button label and on click callback.
         *
         * @param cancelLabel
         *           label to be displayed on the button. If not defined then default label will be used.
         * @param onCancel
         *           executable which will be executed on cancel click.
         * @return this
         */
        public Builder withCancel(final String cancelLabel, final Executable onCancel)
        {
            this.cancelLabel = cancelLabel;
            this.onCancel = onCancel;
            return this;
        }


        /**
         * Defines confirm button label and on click callback.
         *
         * @param confirmLabel
         *           label to be displayed on the button. If not defined then default label will be used.
         * @param onConfirm
         *           executable which will be executed on confirm click.
         * @return this
         */
        public Builder withConfirm(final String confirmLabel, final Executable onConfirm)
        {
            this.confirmLabel = confirmLabel;
            this.onConfirm = onConfirm;
            return this;
        }


        /**
         * Defines content displayed in the the message box. If defined then
         * {@link #withHeader(String)},{@link #withFooter(String)} (String)},{@link #withMessage(String)} will be skipped.
         *
         * @param content
         *           content to be rendered on the msg box.
         * @return this.
         */
        public Builder withContent(final Component content)
        {
            this.content = content;
            return this;
        }


        /**
         * Defines header text in content.
         *
         * @param headerText
         *           header text.
         * @return this.
         */
        public Builder withHeader(final String headerText)
        {
            return withHeader(headerText, null);
        }


        /**
         * Defines header text with specific sclass.
         *
         * @param headerText
         *           header text.
         * @param sclass
         *           sclass to be set on header.
         * @return this.
         */
        public Builder withHeader(final String headerText, final String sclass)
        {
            this.headerText = headerText;
            this.headerSclass = sclass;
            return this;
        }


        /**
         * Defines message text.
         *
         * @param messageText
         *           message text.
         * @return this.
         */
        public Builder withMessage(final String messageText)
        {
            return withMessage(messageText, null);
        }


        /**
         * Defines message text with specific sclass.
         *
         * @param messageText
         *           message text.
         * @param sclass
         *           sclass to be set on message.
         * @return this.
         */
        public Builder withMessage(final String messageText, final String sclass)
        {
            this.messageText = messageText;
            this.messageSclass = sclass;
            return this;
        }


        /**
         * Defines footer text.
         *
         * @param footerText
         *           message text.
         * @return this.
         */
        public Builder withFooter(final String footerText)
        {
            return withFooter(footerText, null);
        }


        /**
         * Defines footer text with specific sclass.
         *
         * @param footerText
         *           message text.
         * @param sclass
         *           sclass to be set on footer.
         * @return this.
         */
        public Builder withFooter(final String footerText, final String sclass)
        {
            this.footerText = footerText;
            this.footerSclass = sclass;
            return this;
        }


        /**
         * Defines sclass applied on entire msg box.
         *
         * @param sclass
         *           sclass to be applied.
         * @return this.
         */
        public Builder withSclass(final String sclass)
        {
            this.sclass = sclass;
            return this;
        }


        /**
         * Defines warning title as title of msg box {@link #withTitle(String)}.
         *
         * @return this.
         */
        public Builder withWarningTitle()
        {
            return withTitle(Labels.getLabel("widget.modal.messagebox.title.warning"));
        }


        /**
         * Defines message box id which should be unique for different kind of message. There can be displayed only one
         * type of message box with the same id in given id space.
         *
         * @param identifier
         *           identifier to be set.
         * @return this.
         */
        public Builder withId(final String identifier)
        {
            this.identifier = identifier;
            return this;
        }
    }
}

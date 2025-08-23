/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.validation;

import com.hybris.cockpitng.core.Cleanable;
import com.hybris.cockpitng.core.Initializable;
import com.hybris.cockpitng.core.model.ValueObserver;
import com.hybris.cockpitng.json.JSONMapper;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.validation.model.ValidationResult;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

/**
 * Renders result of validation in window when users clicks on editor area save button
 */
public class DefaultValidationResultsPopup extends Window implements Cleanable, Initializable
{
    public static final String EVENT_ON_HEIGHT_REQUEST = "onValidationPopupHeightRequest";
    private static final String EVENT_LOOPBACK_HEIGHT_REQUEST = "onHeightRequestLoopback";
    private static final String SCRIPT_REQUEST_COORDINATES = "PopupUtils.sendAnchorCoordinates('%s', '%s', '%s', '%s', '%s');";
    private static final String SCLASS_POPUP_INVISIBLE = "ye-validation-popup-invisible";
    private static final String SCLASS_SECONDARY_BUTTON = "y-btn-secondary";
    private static final String SCLASS_VALIDATION_POPUP = "ye-validation-results-popup";
    private static final String SCLASS_POPUP = "y-popup";
    private static final String SCLASS_BUTTON_CLOSE = "ye-validation-results-close";
    private static final String SCLASS_BUTTON_CONFIRM = "ye-validation-results-confirm y-btn-warning";
    private static final String YTESTID_VALIDATION_POPUP = "validation-popup";
    private static final String SCLASS_POPUP_ABOVE = "ye-validation-results-popup-above";
    private static final String SCLASS_POPUP_LEFT = "ye-validation-results-popup-left";
    private static final String POPUP_POSITION = "parent";
    private final transient ValidatableContainer container;
    private final Listbox results;
    private transient JSONMapper mapper;
    private Button closeButton;
    private transient ValueObserver validationObserver;
    private VerticalAlignment verticalAlignment = VerticalAlignment.BOTTOM;
    private HorizontalAlignment horizontalAlignment = HorizontalAlignment.LEFT;


    public DefaultValidationResultsPopup(final ValidatableContainer container, final Listbox resultsList)
    {
        this.container = container;
        this.results = resultsList;
        this.results.setParent(this);
        DefaultValidationResultsPopup.this.createComponents();
        DefaultValidationResultsPopup.this.layoutComponents();
        DefaultValidationResultsPopup.this.initializeComponents();
        DefaultValidationResultsPopup.this.installListeners();
    }


    protected void createComponents()
    {
        closeButton = new Button();
    }


    protected void layoutComponents()
    {
        final Vlayout layout = new Vlayout();
        layout.setSpacing("auto");
        layout.appendChild(results);
        layout.appendChild(closeButton);
        layout.setParent(this);
    }


    protected void initializeComponents()
    {
        UITools.modifySClass(this, SCLASS_VALIDATION_POPUP, true);
        UITools.modifySClass(this, SCLASS_POPUP, true);
        UITools.modifySClass(closeButton, SCLASS_SECONDARY_BUTTON, true);
        YTestTools.modifyYTestId(this, YTESTID_VALIDATION_POPUP);
        setVisible(false);
        setClosable(true);
        setPosition(POPUP_POSITION);
    }


    protected void installListeners()
    {
        addEventListener(Events.ON_CLOSE, e -> {
            if(e instanceof ForwardEvent)
            {
                e = ((ForwardEvent)e).getOrigin();
            }
            e.stopPropagation(); // stop event to prevent window be detached
            e.getTarget().setVisible(false); // using setVisible to hide the window
        });
    }


    @Override
    public void initialize()
    {
        if(validationObserver == null)
        {
            validationObserver = new ValueObserver()
            {
                @Override
                public void modelChanged()
                {
                    modelChanged(StringUtils.EMPTY);
                }


                @Override
                public void modelChanged(final String property)
                {
                    if(!container.reactOnValidationChange(property))
                    {
                        return;
                    }
                    final ValidationResult rootResult = container.getCurrentValidationResult();
                    final ValidationSeverity highestNotConfirmedSeverity = rootResult.getHighestNotConfirmedSeverity();
                    setTitle(Labels.getLabel("validation.popup.title", new Object[]
                                    {rootResult.getNotConfirmed().collect().size()}));
                    if(highestNotConfirmedSeverity.equals(ValidationSeverity.NONE))
                    {
                        setVisible(false);
                        return;
                    }
                    UITools.modifySClass(closeButton, SCLASS_BUTTON_CLOSE, false);
                    UITools.modifySClass(closeButton, SCLASS_BUTTON_CONFIRM, false);
                    final String caption;
                    if(highestNotConfirmedSeverity.equals(ValidationSeverity.WARN))
                    {
                        caption = getConfirmationLabel();
                        UITools.modifySClass(closeButton, SCLASS_BUTTON_CONFIRM, true);
                    }
                    else
                    {
                        caption = Labels.getLabel("validation.popup.button.close");
                        UITools.modifySClass(closeButton, SCLASS_BUTTON_CLOSE, true);
                    }
                    closeButton.setLabel(caption);
                    if(!isVisible())
                    {
                        showPopup();
                    }
                    else
                    {
                        refreshPosition();
                    }
                    closeButton.setLabel(caption);
                }
            };
            container.addValidationObserver(validationObserver);
            addEventListener(EVENT_ON_HEIGHT_REQUEST, event -> {
                final PopupCoordinates coordinates = getMapper().fromJSONString(ObjectUtils.toString(event.getData()),
                                PopupCoordinates.class);
                if(coordinates != null)
                {
                    handleCoordinates(coordinates);
                }
            });
        }
    }


    protected void handleCoordinates(final PopupCoordinates coordinates)
    {
        setLeft(coordinates.getHorizontal().getTransition() + "px");
        setTop(coordinates.getVertical().getTransition() + "px");
        final boolean above = (VerticalAlignment.TOP.equals(getVerticalAlignment()) && !coordinates.getVertical().isInverted())
                        || (VerticalAlignment.BOTTOM.equals(getVerticalAlignment()) && coordinates.getVertical().isInverted());
        UITools.modifySClass(this, SCLASS_POPUP_ABOVE, above);
        final boolean left = (HorizontalAlignment.LEFT.equals(getHorizontalAlignment())
                        && !coordinates.getHorizontal().isInverted())
                        || (HorizontalAlignment.RIGHT.equals(getHorizontalAlignment()) && coordinates.getHorizontal().isInverted());
        UITools.modifySClass(this, SCLASS_POPUP_LEFT, left);
        invalidate();
        UITools.removeSClass(this, SCLASS_POPUP_INVISIBLE);
    }


    @Override
    public void setParent(final Component parent)
    {
        super.setParent(parent);
        refreshPosition();
    }


    public VerticalAlignment getVerticalAlignment()
    {
        return verticalAlignment;
    }


    public void setVerticalAlignment(final VerticalAlignment verticalAlignment)
    {
        this.verticalAlignment = verticalAlignment;
        refreshPosition();
    }


    public HorizontalAlignment getHorizontalAlignment()
    {
        return horizontalAlignment;
    }


    public void setHorizontalAlignment(final HorizontalAlignment horizontalAlignment)
    {
        this.horizontalAlignment = horizontalAlignment;
        refreshPosition();
    }


    protected void refreshPosition()
    {
        if(isVisible())
        {
            sendHeightRequestEvent();
        }
    }


    protected void sendHeightRequestEvent()
    {
        UITools.addSClass(this, SCLASS_POPUP_INVISIBLE);
        addEventListener(EVENT_LOOPBACK_HEIGHT_REQUEST, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event) throws Exception
            {
                Clients.evalJavaScript(String.format(SCRIPT_REQUEST_COORDINATES, EVENT_ON_HEIGHT_REQUEST, getUuid(),
                                getParent().getUuid(), getVerticalAlignment().name(), getHorizontalAlignment().name()));
                removeEventListener(EVENT_LOOPBACK_HEIGHT_REQUEST, this);
            }
        });
        Events.echoEvent(EVENT_LOOPBACK_HEIGHT_REQUEST, this, null);
    }


    @Override
    public void cleanup()
    {
        if(validationObserver != null)
        {
            container.removeValidationObserver(validationObserver);
            validationObserver = null;
        }
    }


    public void addOkListener(final EventListener<Event> eventListener)
    {
        if(eventListener != null)
        {
            final EventListener listener = e -> {
                eventListener
                                .onEvent(new Event(Events.ON_OK, DefaultValidationResultsPopup.this, container.getCurrentValidationResult()));
                setVisible(false);
            };
            closeButton.addEventListener(Events.ON_CLICK, listener);
        }
    }


    protected void showPopup()
    {
        sendHeightRequestEvent();
        doOverlapped();
    }


    protected void updatePositionRelativelyToParent(final String left, final String top)
    {
        setLeft(left);
        setTop(top);
    }


    protected String getConfirmationLabel()
    {
        return Labels.getLabel("validation.popup.button.confirm");
    }


    protected ValidatableContainer getContainer()
    {
        return container;
    }


    protected Listbox getResults()
    {
        return results;
    }


    public JSONMapper getMapper()
    {
        if(mapper == null)
        {
            mapper = (JSONMapper)SpringUtil.getBean("jsonMapper", JSONMapper.class);
        }
        return mapper;
    }


    public enum VerticalAlignment
    {
        TOP, BOTTOM
    }


    public enum HorizontalAlignment
    {
        LEFT, RIGHT
    }
}

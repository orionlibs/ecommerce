/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.operations;

import com.hybris.cockpitng.core.async.Operation;
import com.hybris.cockpitng.core.async.Progress;
import com.hybris.cockpitng.core.async.impl.DefaultProgress;
import java.math.BigDecimal;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Toolbarbutton;

/**
 * Long operation notifier.
 */
public class LongOperationNotifier
{
    protected static final String SCLASS_PROGRESS_VALUE_LABEL = "progressValueLabel";
    protected static final String SCLASS_PROGRESS_METER_CONTAINER = "progressMeterContainer";
    protected static final String SCLASS_PROGRESS_CANCEL = "progressCancel";
    protected static final String SCLASS_PROGRESS_TITLE = "progressTitle";
    protected static final int HUNDRED = 100;
    protected static final int PROGRESS_TIMER_DELAY = 1000;
    protected static final String PERCENT = "%";
    private final Operation operation;
    private final Progress progress;
    private Component renderComponent;


    /**
     * @param operation
     *           holds long operation information which is used to display notification
     *           {@link Operation#isTerminable()},{@link Operation#getProgressType()}, {@link Operation#getLabel()}
     */
    public LongOperationNotifier(final Operation operation)
    {
        this.operation = operation;
        progress = new DefaultProgress();
    }


    /**
     * Hides long operation notification by detaching renderComponent passed in {@link #showNotification(Component)}
     */
    public void removeNotification()
    {
        if(renderComponent != null && renderComponent.getParent() != null)
        {
            renderComponent.getParent().detach();
        }
    }


    public Progress getProgress()
    {
        return progress;
    }


    /**
     * Shows long operation notification.
     *
     * @param renderComponent
     *           component on which notification will be rendered. The component will be detached from parent when the
     *           notification is removed.
     */
    public void showNotification(final Component renderComponent)
    {
        showNotification(renderComponent, null);
    }


    /**
     * Shows long operation notification.
     *
     * @param renderComponent
     *           component on which notification will be rendered. The component will be detached from parent when the
     *           notification is removed.
     * @param cancellable
     *           on cancel request from ui {@link Cancellable#cancel()} will be called.
     */
    public void showNotification(final Component renderComponent, final Cancellable cancellable)
    {
        if(renderComponent != null && renderComponent.getParent() != null)
        {
            this.renderComponent = renderComponent;
            final Label label = new Label(operation.getLabel());
            label.setSclass(SCLASS_PROGRESS_TITLE);
            renderComponent.appendChild(label);
            if(!Progress.ProgressType.NONE.equals(operation.getProgressType()))
            {
                final Label progressLabel = new Label("0".concat(PERCENT));
                progressLabel.setSclass(SCLASS_PROGRESS_VALUE_LABEL);
                final Progressmeter progressMeter = new Progressmeter(0);
                progressMeter.setWidth("");
                final Timer progressTimer = new Timer(PROGRESS_TIMER_DELAY);
                progressTimer.setRepeats(true);
                progressTimer.addEventListener(Events.ON_TIMER,
                                createEventListener(progressMeter, progressLabel, progress, operation.getProgressType()));
                progressTimer.start();
                final Div progressMeterContainer = new Div();
                progressMeterContainer.setSclass(SCLASS_PROGRESS_METER_CONTAINER);
                progressMeterContainer.appendChild(progressMeter);
                progressMeterContainer.appendChild(progressTimer);
                progressMeterContainer.appendChild(progressLabel);
                renderComponent.appendChild(progressMeterContainer);
                if(operation.isTerminable())
                {
                    final Toolbarbutton cancelBtn = new Toolbarbutton();
                    cancelBtn.setSclass(SCLASS_PROGRESS_CANCEL);
                    cancelBtn.addEventListener(Events.ON_CLICK, event -> {
                        progress.requestCancel();
                        progressTimer.stop();
                        progressLabel.setValue("Cancel...");
                        cancelBtn.detach();
                        if(cancellable != null)
                        {
                            cancellable.cancel();
                        }
                    });
                    renderComponent.appendChild(cancelBtn);
                }
            }
        }
    }


    private EventListener<Event> createEventListener(final Progressmeter progressMeter, final Label progressLabel,
                    final Progress progress, final Progress.ProgressType progressType)
    {
        if(Progress.ProgressType.MANAGED.equals(progressType))
        {
            return event -> {
                progressMeter.setValue(progress.get());
                progressLabel.setValue(progressMeter.getValue() + PERCENT);
            };
        }
        else
        {
            return new EventListener<Event>()
            {
                private int counter = 1;


                @Override
                public void onEvent(final Event event) throws Exception
                {
                    final double x = counter + 2d;
                    final double val = -(x * x) / ((x * x) - x) + 2;
                    progressMeter.setValue(BigDecimal.valueOf(Math.round(val * HUNDRED)).intValue());
                    progressLabel.setValue(progressMeter.getValue() + PERCENT);
                    counter++;
                }
            };
        }
    }
}

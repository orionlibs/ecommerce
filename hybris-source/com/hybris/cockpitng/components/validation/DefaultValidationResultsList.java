/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.validation;

import com.hybris.cockpitng.core.Cleanable;
import com.hybris.cockpitng.core.Initializable;
import com.hybris.cockpitng.core.model.ValueObserver;
import com.hybris.cockpitng.core.util.ObjectValuePath;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationResult;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;

/**
 * List of validation violation with implemented functionality of transferring focus on item click
 */
public class DefaultValidationResultsList extends Listbox implements Cleanable, Initializable
{
    private static final String SCLASS_RESULTS_LIST = "ye-validation-results-list";
    private static final String MESSAGE_POPUP_POSITION = "start_center";
    private static final String NOTIFICATION_SOURCE_UNKNOWN_PATH = "attributeConstraintUnKnownPath";
    private static final String NOTIFICATION_SOURCE_UNKNOWN_ERROR = "attributeConstraintUnKnownError";
    private static final int MESSAGE_POPUP_TIMEOUT = 4000;
    private final transient ValidatableContainer container;
    private final transient ValidationFocusTransferHandler transfer;
    private ListModelList<ValidationInfo> resultsModel;
    private String unknownPathMessage = Labels.getLabel("validation.popup.message.unknownPath");
    private String unknownErrorMessage = Labels.getLabel("validation.popup.message.unknownError");
    private transient ValueObserver validationObserver;
    private transient NotificationService notificationService;


    public DefaultValidationResultsList(final ValidatableContainer container, final ValidationRenderer validationRenderer)
    {
        this.container = container;
        this.transfer = container.getFocusTransfer();
        DefaultValidationResultsList.this.setModel(new ListModelList<>());
        DefaultValidationResultsList.this.setItemRenderer(DefaultValidationResultsList.this.createItemRenderer(validationRenderer));
        DefaultValidationResultsList.this.installListeners();
        UITools.modifySClass(this, SCLASS_RESULTS_LIST, true);
    }


    public DefaultValidationResultsList(final ValidatableContainer container, final ValidationRenderer validationRenderer,
                    final NotificationService notificationService)
    {
        this(container, validationRenderer);
        this.notificationService = notificationService;
    }


    @Override
    public void setModel(final ListModel<?> model)
    {
        assert model instanceof ListModelList;
        resultsModel = (ListModelList<ValidationInfo>)model;
        super.setModel(model);
    }


    protected ListitemRenderer<ValidationInfo> createItemRenderer(final ValidationRenderer validationRenderer)
    {
        return new DefaultValidationResultsListitemRenderer(container, validationRenderer);
    }


    protected void installListeners()
    {
        addEventListener(Events.ON_CLICK, event -> {
            final int idx = getSelectedIndex();
            if(idx > -1)
            {
                final ValidationInfo validationInfo = resultsModel.get(idx);
                final String property = validationInfo.getInvalidPropertyPath();
                final int result = transfer.focusValidationPath(container.getContainer(), property);
                handleErrorResult(result);
            }
        });
    }


    private void handleErrorResult(final int result)
    {
        if(result == ValidationFocusTransferHandler.TRANSFER_ERROR_UNKNOWN_PATH)
        {
            if(notificationService == null)
            {
                Clients.showNotification(getUnknownPathMessage(), Clients.NOTIFICATION_TYPE_WARNING, getSelectedItem(),
                                MESSAGE_POPUP_POSITION, MESSAGE_POPUP_TIMEOUT, true);
            }
            else
            {
                notificationService.notifyUser(NOTIFICATION_SOURCE_UNKNOWN_PATH, NotificationEventTypes.EVENT_TYPE_GENERAL,
                                NotificationEvent.Level.WARNING);
            }
        }
        else if(result == ValidationFocusTransferHandler.TRANSFER_ERROR_OTHER)
        {
            if(notificationService == null)
            {
                Clients.showNotification(getUnknownErrorMessage(), Clients.NOTIFICATION_TYPE_ERROR, getSelectedItem(),
                                MESSAGE_POPUP_POSITION, MESSAGE_POPUP_TIMEOUT, true);
            }
            else
            {
                notificationService.notifyUser(NOTIFICATION_SOURCE_UNKNOWN_ERROR, NotificationEventTypes.EVENT_TYPE_GENERAL,
                                NotificationEvent.Level.FAILURE);
            }
        }
    }


    public String getUnknownPathMessage()
    {
        return unknownPathMessage;
    }


    public void setUnknownPathMessage(final String unknownPathMessage)
    {
        this.unknownPathMessage = unknownPathMessage;
    }


    public String getUnknownErrorMessage()
    {
        return unknownErrorMessage;
    }


    public void setUnknownErrorMessage(final String unknownErrorMessage)
    {
        this.unknownErrorMessage = unknownErrorMessage;
    }


    protected void updateValidationNotifications(final String path, final ValidationResult result)
    {
        resultsModel.stream().filter(info -> ObjectValuePath.parse(info.getInvalidPropertyPath()).startsWith(path))
                        .collect(Collectors.toList()).forEach(info -> resultsModel.remove(info));
        resultsModel.addAll(result.getAll());
        resultsModel.sort(ValidationInfo.COMPARATOR_SEVERITY_DESC);
    }


    protected void presentValidationNotifications(final ValidationResult result)
    {
        resultsModel.clear();
        resultsModel.addAll(result.getNotConfirmed().collect());
    }


    @Override
    public void initialize()
    {
        if(validationObserver == null)
        {
            validationObserver = new ValueObserver()
            {
                @Override
                public void modelChanged(final String property)
                {
                    if(!container.reactOnValidationChange(property))
                    {
                        return;
                    }
                    final ValidationResult result = container.getCurrentValidationResult(property);
                    if(container.isRootPath(property))
                    {
                        presentValidationNotifications(result);
                    }
                    else if(isVisible())
                    {
                        updateValidationNotifications(property, result);
                    }
                }


                @Override
                public void modelChanged()
                {
                    if(!container.reactOnValidationChange())
                    {
                        return;
                    }
                    final ValidationResult rootResult = container.getCurrentValidationResult();
                    if(ValidationSeverity.NONE.isLowerThan(rootResult.getHighestNotConfirmedSeverity())
                                    || CollectionUtils.isEmpty(rootResult.getAll()))
                    {
                        presentValidationNotifications(rootResult);
                    }
                }
            };
            container.addValidationObserver(validationObserver);
        }
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


    @Override
    public boolean equals(final Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null)
        {
            return false;
        }
        if(o.getClass() != this.getClass())
        {
            return false;
        }
        if(!super.equals(o))
        {
            return false;
        }
        final DefaultValidationResultsList that = (DefaultValidationResultsList)o;
        if(container != null ? !container.equals(that.container) : that.container != null)
        {
            return false;
        }
        if(transfer != null ? !transfer.equals(that.transfer) : that.transfer != null)
        {
            return false;
        }
        if(resultsModel != null ? !resultsModel.equals(that.resultsModel) : that.resultsModel != null)
        {
            return false;
        }
        if(unknownPathMessage != null ? !unknownPathMessage.equals(that.unknownPathMessage) : that.unknownPathMessage != null)
        {
            return false;
        }
        if(unknownErrorMessage != null ? !unknownErrorMessage.equals(that.unknownErrorMessage) : that.unknownErrorMessage != null)
        {
            return false;
        }
        return validationObserver != null ? validationObserver.equals(that.validationObserver) : that.validationObserver == null;
    }


    @Override
    public int hashCode()
    {
        int result = container != null ? container.hashCode() : 0;
        result = 31 * result + (transfer != null ? transfer.hashCode() : 0);
        result = 31 * result + (resultsModel != null ? resultsModel.hashCode() : 0);
        result = 31 * result + (unknownPathMessage != null ? unknownPathMessage.hashCode() : 0);
        result = 31 * result + (unknownErrorMessage != null ? unknownErrorMessage.hashCode() : 0);
        result = 31 * result + (validationObserver != null ? validationObserver.hashCode() : 0);
        return result;
    }
}

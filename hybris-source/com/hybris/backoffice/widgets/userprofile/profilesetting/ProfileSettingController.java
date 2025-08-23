/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.userprofile.profilesetting;

import com.hybris.backoffice.masterdetail.MDDetailLogic;
import com.hybris.backoffice.masterdetail.MasterDetailService;
import com.hybris.backoffice.masterdetail.SettingButton;
import com.hybris.backoffice.masterdetail.SettingItem;
import com.hybris.backoffice.media.BackofficeMediaConstants;
import com.hybris.backoffice.media.MediaUtil;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.notifications.NotificationService;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

public class ProfileSettingController extends DefaultWidgetController implements MDDetailLogic
{
    private static final Logger LOG = LoggerFactory.getLogger(ProfileSettingController.class);
    protected static final String SOCKET_OUTPUT_SHOW_CROPPER = "showCropper";
    protected static final String SOCKET_OUTPUT_AVATAR_CHANGED = "avatarChanged";
    protected static final String SOCKET_INPUT_CROP_FINISHED = "cropFinished";
    protected static final String USER_PROFILE_AVATAR_PREFIX = "UserAvatar_";
    protected static final String NOTIFICATION_AREA = "profileSetting";
    protected static final String NOTIFICATION_TYPE_USER_PROFILE_CHANGED = "useProfileChanged";
    @WireVariable
    private transient MasterDetailService userProfileSettingService;
    @WireVariable
    private transient ModelService modelService;
    @WireVariable
    private transient MediaUtil backofficeMediaUtil;
    @WireVariable
    private transient NotificationService notificationService;
    @WireVariable
    private transient UserService userService;
    private boolean isDataChanged = false;
    private SettingItem settingItem;
    @Wire
    protected Image avatarImage;
    @Wire
    protected Button uploadBtn;
    @Wire
    protected Label userNameLabel;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        getUserProfileSettingService().registerDetail(this);
        final UserModel currentUser = getUserService().getCurrentUser();
        userNameLabel
                        .setValue(StringUtils.isNotBlank(currentUser.getDisplayName()) ? currentUser.getDisplayName() : currentUser.getUid());
        uploadBtn.addEventListener(Events.ON_UPLOAD, this::onFileUpload);
        uploadBtn.setUpload(String.format("true,maxsize=%d,accept=%s", BackofficeMediaConstants.FILE_UPLOAD_MAX_SIZE,
                        BackofficeMediaConstants.FILE_UPLOAD_AVATAR_ACCEPT_TYPE));
        reloadAvatar();
    }


    private void reloadAvatar()
    {
        final MediaModel userAvatar = getUserService().getCurrentUser().getAvatar();
        avatarImage
                        .setSrc(userAvatar == null ? String.format("%s%s", getWidgetRoot(), "/../images/user.png") : userAvatar.getURL());
    }


    protected void onFileUpload(final Event event)
    {
        if(event instanceof UploadEvent)
        {
            final var media = ((UploadEvent)event).getMedia();
            if(media instanceof org.zkoss.image.Image)
            {
                sendOutput(SOCKET_OUTPUT_SHOW_CROPPER, media);
            }
            else
            {
                Messagebox.show(getLabel("profile.setting.not.supported.file.type"));
            }
        }
    }


    @SocketEvent(socketId = SOCKET_INPUT_CROP_FINISHED)
    public void fileCropped(final Media media)
    {
        if(media instanceof org.zkoss.image.Image)
        {
            avatarImage.setContent((org.zkoss.image.Image)media);
            getUserProfileSettingService().enableSave(true);
            isDataChanged = true;
        }
    }


    @Override
    public boolean save()
    {
        try
        {
            final UserModel currentUser = getUserService().getCurrentUser();
            final var avatarMediaModel = currentUser.getAvatar();
            final Media media = avatarImage.getContent();
            if(avatarMediaModel == null)
            {
                Transaction.current().execute(new TransactionBody()
                {
                    public Object execute()
                    {
                        final var newAvatarModel = getBackofficeMediaUtil().createMedia(
                                        String.format("%s%s", USER_PROFILE_AVATAR_PREFIX, currentUser.getUid()),
                                        BackofficeMediaConstants.BACKOFFICE_USER_AVATAR_MEDIA_FOLDER, MediaModel._TYPECODE, media);
                        currentUser.setAvatar(newAvatarModel);
                        getModelService().save(currentUser);
                        sendOutput(SOCKET_OUTPUT_AVATAR_CHANGED, newAvatarModel);
                        return null;
                    }
                });
            }
            else
            {
                getBackofficeMediaUtil().updateMedia(avatarMediaModel, media);
                sendOutput(SOCKET_OUTPUT_AVATAR_CHANGED, avatarMediaModel);
            }
            getNotificationService().notifyUser(NOTIFICATION_AREA, NOTIFICATION_TYPE_USER_PROFILE_CHANGED,
                            NotificationEvent.Level.SUCCESS);
            getUserProfileSettingService().enableSave(false);
            isDataChanged = false;
            return true;
        }
        catch(final Exception ex)
        {
            LOG.error(String.format("Save avatar for user %s failed", getUserService().getCurrentUser().getUid()), ex);
            getNotificationService().notifyUser(NOTIFICATION_AREA, NOTIFICATION_TYPE_USER_PROFILE_CHANGED,
                            NotificationEvent.Level.FAILURE);
            return false;
        }
    }


    @Override
    public SettingItem getSettingItem()
    {
        if(settingItem == null)
        {
            final UserModel currentUser = getUserService().getCurrentUser();
            final String subTitle = StringUtils.isNotBlank(currentUser.getDisplayName()) ? currentUser.getDisplayName()
                            : currentUser.getUid();
            settingItem = new SettingItem("backoffice-profile-view", "customer", getLabel("profile.setting.title"), subTitle, true,
                            Arrays.asList(new SettingButton.Builder().setType(SettingButton.TypesEnum.SAVE).setDisabled(true).build(),
                                            new SettingButton.Builder().setType(SettingButton.TypesEnum.SAVE_AND_CLOSE).setDisabled(true).build(),
                                            new SettingButton.Builder().setType(SettingButton.TypesEnum.CANCEL).build()),
                            1);
        }
        return settingItem;
    }


    @Override
    public void reset()
    {
        reloadAvatar();
        isDataChanged = false;
    }


    @Override
    public boolean isDataChanged()
    {
        return isDataChanged;
    }


    @Override
    public boolean needRefreshUI()
    {
        return false;
    }


    public void setUserProfileSettingService(final MasterDetailService userProfileSettingService)
    {
        this.userProfileSettingService = userProfileSettingService;
    }


    protected MasterDetailService getUserProfileSettingService()
    {
        return userProfileSettingService;
    }


    protected UserService getUserService()
    {
        return userService;
    }


    public void setUserService(final UserService userService)
    {
        this.userService = userService;
    }


    public ModelService getModelService()
    {
        return modelService;
    }


    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }


    public NotificationService getNotificationService()
    {
        return notificationService;
    }


    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    public MediaUtil getBackofficeMediaUtil()
    {
        return backofficeMediaUtil;
    }


    public void setBackofficeMediaUtil(final MediaUtil backofficeMediaUtil)
    {
        this.backofficeMediaUtil = backofficeMediaUtil;
    }
}

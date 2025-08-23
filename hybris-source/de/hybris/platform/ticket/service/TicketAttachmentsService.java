package de.hybris.platform.ticket.service;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.UserModel;

public interface TicketAttachmentsService
{
    MediaModel createAttachment(String paramString1, String paramString2, byte[] paramArrayOfbyte, UserModel paramUserModel);
}

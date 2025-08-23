/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.media;

import com.hybris.cockpitng.core.logo.LogoService;
import de.hybris.platform.core.model.media.MediaModel;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

/**
 * Service used to get the logo url
 */
public class DefaultBackofficeLogoService implements LogoService
{
    private MediaUtil backofficeMediaUtil;


    public String getShellBarLogoUrl()
    {
        return getMediaUrl(BackofficeMediaConstants.BACKOFFICE_SHELLBAR_LOGO_CODE);
    }


    public String getLoginPageLogoUrl()
    {
        return getMediaUrl(BackofficeMediaConstants.BACKOFFICE_LOGINPAGE_LOGO_CODE);
    }


    private String getMediaUrl(final String code)
    {
        final Optional<MediaModel> mediaOpt = getBackofficeMediaUtil().getMedia(code);
        return mediaOpt.isPresent() ? mediaOpt.get().getURL() : StringUtils.EMPTY;
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

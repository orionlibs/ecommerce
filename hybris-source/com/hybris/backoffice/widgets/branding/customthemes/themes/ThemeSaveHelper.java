/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.branding.customthemes.themes;

import com.hybris.backoffice.media.BackofficeMediaConstants;
import com.hybris.backoffice.media.MediaUtil;
import com.hybris.backoffice.model.CustomThemeModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import org.springframework.util.MimeTypeUtils;

public class ThemeSaveHelper
{
    private ModelService modelService;
    private MediaUtil backofficeMediaUtil;
    protected static final String STYLE_MEDIA_CODE_SUFFIX = "_style_";
    protected static final String THUMBNAIL_MEDIA_CODE_SUFFIX = "_thumbnail_";


    public ThemeSaver setTheme(final CustomThemeModel theme)
    {
        return new ThemeSaver(theme);
    }


    protected final class ThemeSaver
    {
        private CustomThemeModel theme;


        private ThemeSaver(final CustomThemeModel theme)
        {
            this.theme = theme;
        }


        public ThemeSaver init(final Consumer<CustomThemeModel> initializer)
        {
            initializer.accept(getTheme());
            return this;
        }


        public ThemeSaver initStyle(final Consumer<MediaModel> initializer)
        {
            MediaModel media = getTheme().getStyle();
            if(Objects.isNull(media))
            {
                media = createMedia(getMediaCode(STYLE_MEDIA_CODE_SUFFIX), "text/css");
                getTheme().setStyle(media);
            }
            initializer.accept(media);
            return this;
        }


        public ThemeSaver initThumbnail(final Consumer<MediaModel> initializer)
        {
            MediaModel media = getTheme().getThumbnail();
            if(Objects.isNull(media))
            {
                media = createMedia(getMediaCode(THUMBNAIL_MEDIA_CODE_SUFFIX), MimeTypeUtils.IMAGE_PNG_VALUE);
                getTheme().setThumbnail(media);
            }
            initializer.accept(media);
            return this;
        }


        public void save()
        {
            getModelService().save(getTheme());
        }


        public String getMediaCode(final String keyword)
        {
            return getTheme().getCode() + keyword + UUID.randomUUID();
        }


        private MediaModel createMedia(final String code, final String mimeType)
        {
            return getBackofficeMediaUtil().createMedia(code, BackofficeMediaConstants.BACKOFFICE_THEMES_FOLDER,
                            MediaModel._TYPECODE, mimeType, code);
        }


        private CustomThemeModel getTheme()
        {
            if(Objects.isNull(theme))
            {
                theme = getModelService().create(CustomThemeModel._TYPECODE);
            }
            return theme;
        }
    }


    protected MediaUtil getBackofficeMediaUtil()
    {
        return backofficeMediaUtil;
    }


    public void setBackofficeMediaUtil(final MediaUtil backofficeMediaUtil)
    {
        this.backofficeMediaUtil = backofficeMediaUtil;
    }


    protected ModelService getModelService()
    {
        return modelService;
    }


    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }
}

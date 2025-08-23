/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.cockpitng.modules;

import com.hybris.backoffice.constants.BackofficeConstants;
import com.hybris.cockpitng.core.persistence.packaging.impl.DefaultWidgetLibUtils;
import com.hybris.cockpitng.core.persistence.packaging.impl.WidgetLibHelper;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import java.io.File;
import java.util.function.Function;
import org.apache.commons.lang3.ArrayUtils;

public class BackofficeWidgetLibUtils extends DefaultWidgetLibUtils
{
    public static final String CONSTANT_DATA_HOME = "${data.home}";
    public static final String CONFIG_KEY_BACKOFFICE_LIBRARY_HOME = "backoffice.library.home";


    @Override
    public String getRootDirectory()
    {
        return Config.getString(CONFIG_KEY_BACKOFFICE_LIBRARY_HOME, CONSTANT_DATA_HOME);
    }


    @Override
    public Function<String, String>[] getDirProcessors()
    {
        return getBackofficeDirProcessors();
    }


    public static Function<String, String>[] getBackofficeDirProcessors()
    {
        return ArrayUtils.addAll(WidgetLibHelper.getDirProcessors(), dir -> dir.replace(CONSTANT_DATA_HOME,
                        Utilities.getPlatformConfig().getSystemConfig().getDataDir() + File.separator + BackofficeConstants.EXTENSIONNAME));
    }
}


package de.hybris.platform.cockpit.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.util.ThemeProvider;

public class CockpitThemeProvider implements ThemeProvider
{
    private CockpitThemeConfig themeConfig;


    public Collection getThemeURIs(Execution exec, List uris)
    {
        List ret = new ArrayList();
        if(this.themeConfig == null)
        {
            Object bean = SpringUtil.getBean("themeConfig");
            if(bean instanceof CockpitThemeConfig)
            {
                this.themeConfig = (CockpitThemeConfig)bean;
            }
        }
        if(this.themeConfig != null)
        {
            if(this.themeConfig.isKeepZKCss())
            {
                ret.addAll(uris);
            }
            if(CollectionUtils.isNotEmpty(this.themeConfig.getUris()))
            {
                ret.addAll(this.themeConfig.getUris());
            }
            if(exec.isExplorer() && this.themeConfig.getIeCssUris() != null)
            {
                ret.addAll(this.themeConfig.getIeCssUris());
            }
        }
        return ret;
    }
}

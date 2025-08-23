package de.hybris.platform.platformbackoffice.interceptors.theme;

import com.hybris.backoffice.model.ThemeModel;
import com.hybris.backoffice.theme.BackofficeThemeService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

public class BackofficeThemeInterceptor implements RemoveInterceptor<ThemeModel>, ValidateInterceptor<ThemeModel>
{
    private BackofficeThemeService backofficeThemeService;


    public void onRemove(ThemeModel themeModel, InterceptorContext ctx) throws InterceptorException
    {
        if(themeModel instanceof com.hybris.backoffice.model.CustomThemeModel && (getBackofficeThemeService().getSystemTheme().getCode().equals(themeModel.getCode()) ||
                        getBackofficeThemeService().getUserLevelDefaultTheme().getCode().equals(themeModel.getCode())))
        {
            throw new RemoveUsedThemeInterceptorException("Can not remove the used custom theme");
        }
    }


    public void onValidate(ThemeModel themeModel, InterceptorContext ctx) throws InterceptorException
    {
        if(themeModel instanceof com.hybris.backoffice.model.CustomThemeModel && ctx.getModelService().isNew(themeModel) &&
                        getBackofficeThemeService().getCustomThemes().size() >= getBackofficeThemeService().getMaximumOfCustomTheme())
        {
            throw new ExceedMaximumThemeInterceptorException("Can not create custom theme as the maximum reached");
        }
    }


    public BackofficeThemeService getBackofficeThemeService()
    {
        return this.backofficeThemeService;
    }


    public void setBackofficeThemeService(BackofficeThemeService backofficeThemeService)
    {
        this.backofficeThemeService = backofficeThemeService;
    }
}

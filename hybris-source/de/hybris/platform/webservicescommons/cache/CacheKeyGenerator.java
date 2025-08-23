package de.hybris.platform.webservicescommons.cache;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.cache.interceptor.KeyGenerator;

public class CacheKeyGenerator implements KeyGenerator
{
    private CommonI18NService commonI18NService;
    private UserService userService;


    public Object generate(Object target, Method method, Object... params)
    {
        return generate(false, false, params);
    }


    public Object generate(boolean addUserToKey, boolean addCurrencyToKey, Object... params)
    {
        List<Object> key = new ArrayList();
        addLanguage(key);
        addCurrency(addCurrencyToKey, key);
        addUser(addUserToKey, key);
        addParams(key, params);
        return key;
    }


    public static Object generateKey(boolean addUserToKey, boolean addCurrencyToKey, Object... params)
    {
        CacheKeyGenerator keyGeneratorBean = (CacheKeyGenerator)Registry.getApplicationContext().getBean("wsCacheKeyGenerator");
        return keyGeneratorBean.generate(addUserToKey, addCurrencyToKey, params);
    }


    protected void addLanguage(List<Object> key)
    {
        LanguageModel language = this.commonI18NService.getCurrentLanguage();
        key.add((language == null) ? null : language.getIsocode());
    }


    protected void addCurrency(boolean shouldBeAdded, List<Object> key)
    {
        if(shouldBeAdded)
        {
            CurrencyModel currency = this.commonI18NService.getCurrentCurrency();
            key.add((currency == null) ? null : currency.getIsocode());
        }
    }


    protected void addUser(boolean shouldBeAdded, List<Object> key)
    {
        if(shouldBeAdded)
        {
            UserModel user = this.userService.getCurrentUser();
            key.add((user == null) ? null : user.getUid());
        }
    }


    protected void addParams(List<Object> key, Object... params)
    {
        key.addAll(Arrays.asList(params));
    }


    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}

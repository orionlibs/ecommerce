package de.hybris.platform.servicelayer.test;

import de.hybris.platform.core.model.test.TestItemType2Model;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.attribute.DynamicLocalizedAttributeHandler;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Required;

public class DynamicLocalizedAttributesStringSampleBean implements DynamicLocalizedAttributeHandler<String, TestItemType2Model>
{
    private I18NService i18NService;


    public String get(TestItemType2Model item)
    {
        if(item == null)
        {
            throw new IllegalArgumentException("Item model is required");
        }
        return item.getTestProperty2(this.i18NService.getCurrentLocale());
    }


    public void set(TestItemType2Model item, String value)
    {
        if(item != null && value != null)
        {
            item.setTestProperty2(value, this.i18NService.getCurrentLocale());
        }
    }


    public String get(TestItemType2Model item, Locale loc)
    {
        if(item == null)
        {
            throw new IllegalArgumentException("Item model is required");
        }
        return item.getTestProperty2(loc);
    }


    public void set(TestItemType2Model item, String value, Locale loc)
    {
        if(item != null && value != null)
        {
            item.setTestProperty2(value, loc);
        }
    }


    @Required
    public void setI18NService(I18NService i18nService)
    {
        this.i18NService = i18nService;
    }


    @Deprecated(since = "ages", forRemoval = true)
    protected I18NService init()
    {
        throw new UnsupportedOperationException("instead inject dependent I18NService via spring ");
    }
}

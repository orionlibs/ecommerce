package de.hybris.platform.servicelayer.internal.model.attribute;

import de.hybris.platform.servicelayer.model.AbstractItemModel;
import java.util.Locale;

public interface DynamicAttributesProvider
{
    void set(AbstractItemModel paramAbstractItemModel, String paramString, Object paramObject);


    Object get(AbstractItemModel paramAbstractItemModel, String paramString);


    void setLocalized(AbstractItemModel paramAbstractItemModel, String paramString, Object paramObject, Locale paramLocale);


    Object getLocalized(AbstractItemModel paramAbstractItemModel, String paramString, Locale paramLocale);


    boolean isDynamic(String paramString);
}

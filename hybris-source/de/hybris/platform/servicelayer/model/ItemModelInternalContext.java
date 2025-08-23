package de.hybris.platform.servicelayer.model;

import de.hybris.platform.core.PK;
import java.io.ObjectStreamException;
import java.util.Locale;

public interface ItemModelInternalContext extends ItemModelContext
{
    <T> T getValue(String paramString, T paramT);


    <T> T setValue(String paramString, T paramT);


    <T> T getLocalizedValue(String paramString, Locale paramLocale);


    <T> T getLocalizedRelationValue(String paramString, Locale paramLocale);


    <T> void setLocalizedValue(String paramString, Locale paramLocale, T paramT);


    <T> T getDynamicValue(AbstractItemModel paramAbstractItemModel, String paramString);


    <T> void setDynamicValue(AbstractItemModel paramAbstractItemModel, String paramString, T paramT);


    <T> T getLocalizedDynamicValue(AbstractItemModel paramAbstractItemModel, String paramString, Locale paramLocale);


    <T> void setLocalizedDynamicValue(AbstractItemModel paramAbstractItemModel, String paramString, Locale paramLocale, T paramT);


    int hashCode(AbstractItemModel paramAbstractItemModel);


    Object writeReplace(Object paramObject) throws ObjectStreamException;


    PK getNewPK();


    PK generateNewPK();


    <T> T getPropertyValue(String paramString);


    <T> void setPropertyValue(String paramString, T paramT);


    boolean isDynamicAttribute(String paramString);


    Object loadOriginalValue(String paramString);


    Object loadOriginalValue(String paramString, Locale paramLocale);


    void unloadAttribute(String paramString);
}

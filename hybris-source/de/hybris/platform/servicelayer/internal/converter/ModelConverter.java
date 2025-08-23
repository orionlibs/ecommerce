package de.hybris.platform.servicelayer.internal.converter;

import de.hybris.platform.servicelayer.internal.model.ModelContext;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public interface ModelConverter
{
    void init(ConverterRegistry paramConverterRegistry);


    Object create(String paramString);


    String getType(Object paramObject);


    Object load(Object paramObject);


    void reload(Object paramObject);


    void remove(Object paramObject);


    void save(Object paramObject, Collection<String> paramCollection);


    @Deprecated(since = "5.7.0", forRemoval = true)
    Object getSource(Object paramObject);


    boolean isModified(Object paramObject);


    boolean isModified(Object paramObject, String paramString);


    boolean isModified(Object paramObject, String paramString, Locale paramLocale);


    boolean exists(Object paramObject);


    boolean isRemoved(Object paramObject);


    boolean isNew(Object paramObject);


    boolean isUpToDate(Object paramObject);


    Map<String, Set<Locale>> getDirtyAttributes(Object paramObject);


    Object getAttributeValue(Object paramObject, String paramString);


    Object getLocalizedAttributeValue(Object paramObject, String paramString, Locale paramLocale);


    void setAttributeValue(Object paramObject1, String paramString, Object paramObject2);


    void beforeAttach(Object paramObject, ModelContext paramModelContext);


    void afterDetach(Object paramObject, ModelContext paramModelContext);


    Set<String> getWritablePartOfAttributes(TypeService paramTypeService);


    Set<String> getPartOfAttributes(TypeService paramTypeService);


    PersistenceObject getPersistenceSource(Object paramObject);
}

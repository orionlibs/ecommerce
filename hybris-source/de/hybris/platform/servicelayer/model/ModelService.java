package de.hybris.platform.servicelayer.model;

import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.exceptions.ModelInitializationException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.internal.model.ModelCloningContext;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

public interface ModelService
{
    void attach(Object paramObject);


    void detach(Object paramObject);


    void detach(PK paramPK);


    void detachAll();


    <T> T clone(T paramT);


    <T> T clone(Object paramObject, Class<T> paramClass);


    <T> T clone(T paramT, ModelCloningContext paramModelCloningContext);


    <T> T clone(Object paramObject, Class<T> paramClass, ModelCloningContext paramModelCloningContext);


    <T> T create(Class paramClass);


    <T> T create(String paramString);


    <T> T get(Object paramObject);


    <T> T get(Object paramObject, String paramString);


    <T> T get(PK paramPK);


    <T extends Collection> T getAll(Collection<? extends Object> paramCollection, T paramT);


    <T extends Collection> T getAll(Collection<? extends Object> paramCollection, T paramT, String paramString);


    void refresh(Object paramObject);


    void save(Object paramObject) throws ModelSavingException;


    void saveAll(Collection<? extends Object> paramCollection) throws ModelSavingException;


    void saveAll(Object... paramVarArgs) throws ModelSavingException;


    void saveAll() throws ModelSavingException;


    boolean isUniqueConstraintErrorAsRootCause(Exception paramException);


    void remove(Object paramObject) throws ModelRemovalException;


    void removeAll(Collection<? extends Object> paramCollection) throws ModelRemovalException;


    void removeAll(Object... paramVarArgs) throws ModelRemovalException;


    void remove(PK paramPK) throws ModelRemovalException;


    <T> T getSource(Object paramObject);


    <T extends Collection> T getAllSources(Collection<? extends Object> paramCollection, T paramT);


    String getModelType(Class paramClass);


    Class getModelTypeClass(Class paramClass);


    String getModelType(Object paramObject);


    <T> T toModelLayer(Object paramObject);


    <T> T toPersistenceLayer(Object paramObject);


    void initDefaults(Object paramObject) throws ModelInitializationException;


    <T> T getAttributeValue(Object paramObject, String paramString);


    <T> T getAttributeValue(Object paramObject, String paramString, Locale paramLocale);


    <T> Map<Locale, T> getAttributeValues(Object paramObject, String paramString, Locale... paramVarArgs);


    void setAttributeValue(Object paramObject1, String paramString, Object paramObject2);


    <T> void setAttributeValue(Object paramObject, String paramString, Map<Locale, T> paramMap);


    boolean isUpToDate(Object paramObject);


    boolean isModified(Object paramObject);


    boolean isNew(Object paramObject);


    boolean isRemoved(Object paramObject);


    boolean isAttached(Object paramObject);


    boolean isSourceAttached(Object paramObject);


    void enableTransactions();


    void disableTransactions();


    void clearTransactionsSettings();


    @Deprecated(since = "6.1.0", forRemoval = true)
    <T> T getByExample(T paramT);


    void lock(PK paramPK);


    void lock(Object paramObject);


    <T> T getWithLock(Object paramObject);
}

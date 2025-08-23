package de.hybris.platform.catalog;

import de.hybris.platform.jalo.c2l.Language;
import java.util.Map;
import java.util.Set;

public interface SynchronizationPersistenceAdapter<ENTITY, TYPE>
{
    ENTITY create(TYPE paramTYPE, Map<String, Object> paramMap) throws SynchronizationPersistenceException;


    Map<String, Object> read(ENTITY paramENTITY, Set<String> paramSet) throws SynchronizationPersistenceException;


    Map<String, Object> readLocalized(ENTITY paramENTITY, Set<String> paramSet, Set<Language> paramSet1) throws SynchronizationPersistenceException;


    void remove(ENTITY paramENTITY) throws SynchronizationPersistenceException, RecoverableSynchronizationPersistenceException;


    void update(ENTITY paramENTITY, Map.Entry<String, Object> paramEntry) throws SynchronizationPersistenceException;


    void update(ENTITY paramENTITY, Map<String, Object> paramMap) throws SynchronizationPersistenceException;


    void resetUnitOfWork();


    void disableTransactions();


    void clearTransactionsSettings();
}

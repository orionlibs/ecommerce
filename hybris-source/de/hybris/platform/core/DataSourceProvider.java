package de.hybris.platform.core;

import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import java.util.Collection;
import java.util.Set;

public interface DataSourceProvider
{
    HybrisDataSource getDataSource(String paramString);


    HybrisDataSource getMasterDataSource();


    HybrisDataSource getDataSource();


    boolean isSlaveDataSource();


    boolean isAlternativeMasterDataSource();


    boolean isForceMaster();


    String activateSlaveDataSource();


    void activateSlaveDataSource(String paramString);


    void activateAlternativeMasterDataSource(String paramString);


    Collection<HybrisDataSource> getAllSlaveDataSources();


    Collection<HybrisDataSource> getAllAlternativeMasterDataSources();


    @Deprecated(since = "ages", forRemoval = true)
    Set<String> getAllDataSourceIDs();


    Set<String> getAllSlaveDataSourceIDs();


    Set<String> getAllAlternativeMasterDataSourceIDs();


    @Deprecated(since = "ages", forRemoval = true)
    void deactivateSlaveDataSource();


    void deactivateAlternativeDataSource();


    void forceMasterDataSource();
}

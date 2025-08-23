package de.hybris.platform.persistence.property;

import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface PersistenceManager
{
    public static final String NO_DATABASE = "_no_db_";


    String getSQLTypeDef(Class paramClass, String paramString);


    boolean isLoaded();


    void loadPersistenceInfos();


    Collection<PropertyTableDefinition> createInitialPersistenceInfos(Collection<ComposedTypeRemote> paramCollection, boolean paramBoolean);


    PropertyTableDefinition getInitialPersistenceInfos(ComposedTypeRemote paramComposedTypeRemote, boolean paramBoolean);


    TypeInfoMap getPersistenceInfo(String paramString);


    TypeInfoMap getPersistenceInfo(PK paramPK);


    boolean cachesInfoFor(PK paramPK);


    ItemDeployment getItemDeployment(String paramString);


    ItemDeployment getItemDeployment(int paramInt);


    Collection<ItemDeployment> getAllSubDeployments(ItemDeployment paramItemDeployment);


    String getJNDIName(int paramInt);


    String getJNDIName(String paramString);


    PK getTypePK(String paramString);


    boolean isRootRelationType(String paramString);


    Set<PK> getExternalTableTypes(PK paramPK);


    Set<Integer> getBeanTypeCodes(PK paramPK);


    void clearComposedType(PK paramPK, String paramString);


    Map<Integer, ItemDeployment> getDuplicatedItemDeployments();
}

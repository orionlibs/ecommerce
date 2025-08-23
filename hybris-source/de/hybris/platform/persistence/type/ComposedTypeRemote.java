package de.hybris.platform.persistence.type;

import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import java.util.List;

public interface ComposedTypeRemote extends HierarchieTypeRemote
{
    void reinitializeType(ComposedTypeRemote paramComposedTypeRemote1, ItemDeployment paramItemDeployment, String paramString, ComposedTypeRemote paramComposedTypeRemote2);


    PK getSuperTypeItemPK();


    String getJaloClassName();


    void setJaloClassName(String paramString);


    void setItemJNDIName(String paramString);


    String getItemTableName();


    ItemDeployment getDeployment();


    String getItemPropertyTableName();


    boolean getPropertyTableStatus();


    void setPropertyTableStatus(boolean paramBoolean);


    ComposedTypeRemote getSuperType();


    List getInheritancePath();


    String getInheritancePathString();


    void setInheritancePathString(String paramString);


    boolean isRemovable();


    boolean isAbstract();


    void setAbstract();


    int getItemTypeCode();


    String getItemJNDIName();


    boolean isSingleton();


    void setSingleton(boolean paramBoolean);


    void changeSTPK(String paramString);
}

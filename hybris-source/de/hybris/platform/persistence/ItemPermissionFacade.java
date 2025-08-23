package de.hybris.platform.persistence;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.security.PermissionContainer;
import de.hybris.platform.persistence.security.EJBSecurityException;
import de.hybris.platform.util.ItemPropertyValue;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ItemPermissionFacade
{
    Collection<PK> getRestrictedPrincipalPKs();


    Map<ItemPropertyValue, List<Boolean>> getPrincipalToBooleanListMap(List<PK> paramList);


    void setPrincipalToBooleanListMap(List<PK> paramList, Map<PK, List<Boolean>> paramMap) throws EJBSecurityException;


    Collection<PK> getPermissionPKs(PK paramPK, boolean paramBoolean);


    boolean setPermission(PK paramPK1, PK paramPK2, boolean paramBoolean) throws EJBSecurityException;


    boolean setGlobalPermission(PK paramPK, boolean paramBoolean) throws EJBSecurityException;


    boolean setPermissions(Collection<PermissionContainer> paramCollection) throws EJBSecurityException;


    boolean setGlobalPermissions(Collection<PermissionContainer> paramCollection) throws EJBSecurityException;


    boolean removePermission(PK paramPK1, PK paramPK2) throws EJBSecurityException;


    boolean removeGlobalPermission(PK paramPK) throws EJBSecurityException;


    boolean removePermissions(Collection<PermissionContainer> paramCollection) throws EJBSecurityException;


    boolean removeGlobalPermissions(Collection<PermissionContainer> paramCollection) throws EJBSecurityException;


    Collection<PK> getGlobalPermissionPKs(boolean paramBoolean);


    int checkOwnGlobalPermission(PK paramPK);


    int checkItemPermission(PK paramPK1, PK paramPK2);
}

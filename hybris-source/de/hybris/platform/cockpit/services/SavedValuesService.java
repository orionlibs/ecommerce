package de.hybris.platform.cockpit.services;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.user.UserModel;
import java.util.List;
import java.util.Set;

public interface SavedValuesService
{
    Set<TypedObject> getSavedValues(TypedObject paramTypedObject);


    String getModificationTime(TypedObject paramTypedObject);


    String getAuthor(TypedObject paramTypedObject);


    String getModificationType(TypedObject paramTypedObject);


    List<TypedObject> getSavedValuesEntries(TypedObject paramTypedObject);


    String getModifiedAttribute(TypedObject paramTypedObject);


    List<String> getOldValues(TypedObject paramTypedObject);


    List<String> getNewValues(TypedObject paramTypedObject);


    UserModel getLastModifyingUser(TypedObject paramTypedObject);
}

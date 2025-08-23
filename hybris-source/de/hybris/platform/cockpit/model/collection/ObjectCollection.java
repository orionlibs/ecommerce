package de.hybris.platform.cockpit.model.collection;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.UserModel;
import java.util.List;

public interface ObjectCollection
{
    String getLabel();


    String getLabel(String paramString);


    String getDescription();


    int getTotalCount();


    UserModel getUser();


    List<TypedObject> getElements();


    String getQualifier();


    PK getPK();


    String getType();
}

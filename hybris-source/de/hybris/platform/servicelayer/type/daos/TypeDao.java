package de.hybris.platform.servicelayer.type.daos;

import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;

public interface TypeDao
{
    ComposedTypeModel findComposedTypeByCode(String paramString);


    AtomicTypeModel findAtomicTypeByCode(String paramString);


    AtomicTypeModel findAtomicTypeByJavaClass(Class paramClass);


    TypeModel findTypeByCode(String paramString);
}

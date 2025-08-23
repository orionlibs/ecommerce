package de.hybris.bootstrap.ddl.pk;

import de.hybris.bootstrap.typesystem.YAtomicType;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YCollectionType;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YEnumValue;
import de.hybris.bootstrap.typesystem.YMapType;
import de.hybris.platform.core.PK;
import java.util.Map;

public interface PkFactory
{
    Map<String, Long> getCurrentNumberSeries();


    PK createNewPK(int paramInt);


    PK createNewPK(YComposedType paramYComposedType);


    PK getOrCreatePK(YComposedType paramYComposedType, YAttributeDescriptor paramYAttributeDescriptor);


    PK getOrCreatePK(YComposedType paramYComposedType);


    PK getOrCreatePK(YEnumValue paramYEnumValue);


    PK getOrCreatePK(YAtomicType paramYAtomicType);


    PK getOrCreatePK(YMapType paramYMapType);


    PK getOrCreatePK(YCollectionType paramYCollectionType);
}

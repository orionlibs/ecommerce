package de.hybris.platform.personalizationintegration.service;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationintegration.mapping.MappingData;
import de.hybris.platform.personalizationservices.CxCalculationContext;
import java.util.Optional;

public interface CxIntegrationMappingService
{
    Optional<MappingData> mapExternalData(Object paramObject, String paramString);


    void assignSegmentsToUser(UserModel paramUserModel, MappingData paramMappingData, boolean paramBoolean);


    void assignSegmentsToUser(UserModel paramUserModel, MappingData paramMappingData, boolean paramBoolean, CxCalculationContext paramCxCalculationContext);
}

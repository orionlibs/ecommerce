package de.hybris.platform.personalizationintegration.strategies.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationintegration.mapping.MappingData;
import de.hybris.platform.personalizationintegration.service.CxIntegrationMappingService;
import de.hybris.platform.personalizationservices.CxCalculationContext;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import de.hybris.platform.personalizationservices.segment.CxSegmentService;
import de.hybris.platform.personalizationservices.strategies.UpdateUserSegmentStrategy;
import de.hybris.platform.servicelayer.model.ModelService;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCxTestUpdateUserSegmentStrategy implements UpdateUserSegmentStrategy
{
    private ModelService modelService;
    private CxSegmentService cxSegmentService;
    private CxIntegrationMappingService cxIntegrationMappingService;
    private List<String> externalIds;


    public void updateUserSegments(UserModel user)
    {
        Set<CxSegmentModel> cxSegments = findSegmentsFromProfile(this.externalIds);
        setSegmentsOnUser(user, cxSegments);
    }


    public void updateUserSegments(UserModel user, CxCalculationContext context)
    {
        updateUserSegments(user);
    }


    protected Set<CxSegmentModel> findSegmentsFromProfile(List<String> externalIds)
    {
        Optional<MappingData> profile = this.cxIntegrationMappingService.mapExternalData(externalIds, "testConverterName");
        if(!profile.isPresent())
        {
            return Collections.emptySet();
        }
        Set<CxSegmentModel> cxSegments = (Set<CxSegmentModel>)((MappingData)profile.get()).getSegments().stream().map(s -> s.getCode()).distinct().map(c -> this.cxSegmentService.getSegment(c)).filter(s -> s.isPresent()).map(s -> (CxSegmentModel)s.get()).collect(Collectors.toSet());
        return cxSegments;
    }


    protected void setSegmentsOnUser(UserModel user, Set<CxSegmentModel> segments)
    {
        Collection<CxUserToSegmentModel> previousUserToSegments = this.cxSegmentService.getUserToSegmentForUser(user);
        this.cxSegmentService.removeUserToSegments(previousUserToSegments);
        List<CxUserToSegmentModel> userToSegments = (List<CxUserToSegmentModel>)segments.stream().map(s -> createUserToSegment(user, s)).collect(Collectors.toList());
        this.cxSegmentService.saveUserToSegments(userToSegments);
    }


    protected CxUserToSegmentModel createUserToSegment(UserModel user, CxSegmentModel segment)
    {
        CxUserToSegmentModel uts = new CxUserToSegmentModel();
        uts.setSegment(segment);
        uts.setUser(user);
        uts.setAffinity(BigDecimal.ONE);
        return uts;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public CxSegmentService getCxSegmentService()
    {
        return this.cxSegmentService;
    }


    @Required
    public void setCxSegmentService(CxSegmentService cxSegmentService)
    {
        this.cxSegmentService = cxSegmentService;
    }


    public CxIntegrationMappingService getCxIntegrationMappingService()
    {
        return this.cxIntegrationMappingService;
    }


    public void setCxIntegrationMappingService(CxIntegrationMappingService cxIntegrationMappingService)
    {
        this.cxIntegrationMappingService = cxIntegrationMappingService;
    }


    public List<String> getExternalIds()
    {
        return this.externalIds;
    }


    public void setExternalIds(List<String> externalIds)
    {
        this.externalIds = externalIds;
    }
}

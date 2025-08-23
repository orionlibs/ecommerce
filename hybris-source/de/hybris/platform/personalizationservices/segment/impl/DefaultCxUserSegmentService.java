package de.hybris.platform.personalizationservices.segment.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.CxCalculationContext;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import de.hybris.platform.personalizationservices.segment.CxUserSegmentService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCxUserSegmentService implements CxUserSegmentService
{
    private static final String USER_NOT_NULL = "user must not be null";
    private ModelService modelService;


    public Collection<CxUserToSegmentModel> getUserSegments(UserModel user)
    {
        return getUserSegments(user, null);
    }


    public Collection<CxUserToSegmentModel> getUserSegments(UserModel user, BaseSiteModel baseSite)
    {
        ServicesUtil.validateParameterNotNull(user, "user must not be null");
        Collection<CxUserToSegmentModel> userSegments = user.getUserToSegments();
        if(userSegments == null)
        {
            return Collections.emptyList();
        }
        if(baseSite == null)
        {
            return userSegments;
        }
        return (Collection<CxUserToSegmentModel>)userSegments.stream()
                        .filter(us -> baseSite.equals(us.getBaseSite()))
                        .collect(Collectors.toSet());
    }


    public void setUserSegments(UserModel user, Collection<CxUserToSegmentModel> userSegments)
    {
        ServicesUtil.validateParameterNotNull(user, "user must not be null");
        if(CollectionUtils.isEmpty(userSegments))
        {
            user.setUserToSegments(Collections.emptyList());
            this.modelService.save(user);
            return;
        }
        validateUserSegments(user.getUid(), userSegments);
        updateUserSegments(user, userSegments, false);
    }


    protected void validateUserSegments(String uid, Collection<CxUserToSegmentModel> userSegments)
    {
        userSegments.forEach(us -> Preconditions.checkArgument(uid.equals(us.getUser().getUid()), "User segment relation belong to different user"));
    }


    protected void updateUserSegments(UserModel user, Collection<CxUserToSegmentModel> userSegments, boolean onlyAdd)
    {
        Collection<CxUserToSegmentModel> newUserToSegments = updateUserSegments(user.getUserToSegments(), userSegments, onlyAdd);
        user.setUserToSegments(newUserToSegments);
        this.modelService.save(user);
        this.modelService.saveAll(newUserToSegments);
    }


    protected Collection<CxUserToSegmentModel> updateUserSegments(Collection<CxUserToSegmentModel> currentUserToSegments, Collection<CxUserToSegmentModel> userSegmentsForUpdate, boolean onlyAdd)
    {
        Map<String, CxUserToSegmentModel> userToSegmentsMap = createValueMap(userSegmentsForUpdate, this::getUserSegmentKey, this::mergeUserToSegmentModel);
        Collection<CxUserToSegmentModel> newUserToSegments = (Collection<CxUserToSegmentModel>)currentUserToSegments.stream().filter(us -> (onlyAdd || userToSegmentsMap.containsKey(getUserSegmentKey(us))))
                        .map(us -> updateUserToSegment(us, (CxUserToSegmentModel)userToSegmentsMap.get(getUserSegmentKey(us)))).collect(Collectors.toList());
        Map<String, CxUserToSegmentModel> newUserToSegmentsMap = createValueMap(newUserToSegments, this::getUserSegmentKey, this::mergeUserToSegmentModel);
        Objects.requireNonNull(newUserToSegments);
        userToSegmentsMap.values().stream().filter(Objects::nonNull).filter(us -> !newUserToSegmentsMap.containsKey(getUserSegmentKey(us))).forEach(newUserToSegments::add);
        return newUserToSegments;
    }


    protected String getUserSegmentKey(CxUserToSegmentModel us)
    {
        return us.getSegment().getCode() + us.getSegment().getCode() + ((us.getBaseSite() == null) ? "" : us.getBaseSite().getUid());
    }


    private static <K, V> Map<K, V> createValueMap(Collection<V> valueCollection, Function<? super V, ? extends K> keyMapper, BinaryOperator<V> mergeFunction)
    {
        return (Map<K, V>)valueCollection.stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toMap(keyMapper,
                                        (Function)Function.identity(), mergeFunction, java.util.HashMap::new));
    }


    protected CxUserToSegmentModel mergeUserToSegmentModel(CxUserToSegmentModel m1, CxUserToSegmentModel m2)
    {
        return (m1.getAffinity().compareTo(m2.getAffinity()) > 0) ? m1 : m2;
    }


    protected CxUserToSegmentModel updateUserToSegment(CxUserToSegmentModel currentUserToSegment, CxUserToSegmentModel newUserToSegment)
    {
        if(newUserToSegment != null && newUserToSegment.getAffinity() != null)
        {
            currentUserToSegment.setAffinity(newUserToSegment.getAffinity());
        }
        return currentUserToSegment;
    }


    public void setUserSegments(UserModel user, BaseSiteModel baseSite, Collection<CxUserToSegmentModel> userSegments)
    {
        ServicesUtil.validateParameterNotNull(user, "user must not be null");
        if(baseSite == null)
        {
            setUserSegments(user, userSegments);
            return;
        }
        validateUserSegments(user.getUid(), baseSite.getUid(), userSegments);
        updateUserSegments(user, baseSite, userSegments, false);
    }


    protected void validateUserSegments(String uid, String baseSiteId, Collection<CxUserToSegmentModel> userSegments)
    {
        userSegments.forEach(us -> {
            Preconditions.checkArgument(uid.equals(us.getUser().getUid()), "User segment relation belong to different user");
            Preconditions.checkArgument(baseSiteId.equals(us.getBaseSite().getUid()), "User segment relation belong to different baseSite");
        });
    }


    protected void updateUserSegments(UserModel user, BaseSiteModel baseSite, Collection<CxUserToSegmentModel> userSegments, boolean onlyAdd)
    {
        Map<Boolean, List<CxUserToSegmentModel>> segmentsGroupedForBaseSite = (Map<Boolean, List<CxUserToSegmentModel>>)user.getUserToSegments().stream().collect(Collectors.partitioningBy(us -> baseSite.equals(us.getBaseSite())));
        Collection<CxUserToSegmentModel> newUserSegmentsForBaseSite = updateUserSegments(segmentsGroupedForBaseSite
                        .get(Boolean.TRUE), userSegments, onlyAdd);
        Collection<CxUserToSegmentModel> finalUserToSegments = CollectionUtils.union(newUserSegmentsForBaseSite, segmentsGroupedForBaseSite
                        .get(Boolean.FALSE));
        user.setUserToSegments(finalUserToSegments);
        try
        {
            this.modelService.save(user);
            this.modelService.saveAll(finalUserToSegments);
        }
        catch(ModelSavingException e)
        {
            this.modelService.refresh(user);
            throw e;
        }
    }


    public void setUserSegments(UserModel user, BaseSiteModel baseSite, Collection<CxUserToSegmentModel> userSegments, CxCalculationContext context)
    {
        ServicesUtil.validateParameterNotNull(user, "user must not be null");
        if(context == null || CollectionUtils.isEmpty(context.getSegmentUpdateProviders()))
        {
            setUserSegments(user, baseSite, userSegments);
            return;
        }
        if(baseSite == null)
        {
            validateUserSegments(user.getUid(), userSegments);
        }
        else
        {
            validateUserSegments(user.getUid(), baseSite.getUid(), userSegments);
        }
        updateUserSegments(user, baseSite, userSegments, context.getSegmentUpdateProviders(), false);
    }


    protected void updateUserSegments(UserModel user, BaseSiteModel baseSite, Collection<CxUserToSegmentModel> userSegments, Set<String> providers, boolean onlyAdd)
    {
        Collection<CxUserToSegmentModel> userSegmentsForBaseSiteAndProviders = (Collection<CxUserToSegmentModel>)userSegments.stream().filter(us -> (baseSite == null || baseSite.equals(us.getBaseSite()))).filter(us -> providers.contains(us.getProvider())).collect(Collectors.toList());
        Map<Boolean, List<CxUserToSegmentModel>> segmentsGroupedForBaseSite = (Map<Boolean, List<CxUserToSegmentModel>>)user.getUserToSegments().stream().collect(Collectors.partitioningBy(us -> (baseSite == null || baseSite.equals(us.getBaseSite()))));
        Map<Boolean, List<CxUserToSegmentModel>> segmentsGroupedForProviders = (Map<Boolean, List<CxUserToSegmentModel>>)((List)segmentsGroupedForBaseSite.get(Boolean.TRUE)).stream().collect(Collectors.partitioningBy(us -> providers.contains(us.getProvider())));
        Collection<CxUserToSegmentModel> newUserSegmentsForBaseSite = updateUserSegments(segmentsGroupedForProviders
                        .get(Boolean.TRUE), userSegmentsForBaseSiteAndProviders, onlyAdd);
        Collection<CxUserToSegmentModel> finalUserToSegments = CollectionUtils.union(newUserSegmentsForBaseSite, segmentsGroupedForBaseSite
                        .get(Boolean.FALSE));
        finalUserToSegments.addAll(segmentsGroupedForProviders.get(Boolean.FALSE));
        user.setUserToSegments(finalUserToSegments);
        this.modelService.save(user);
        this.modelService.saveAll(finalUserToSegments);
    }


    public void addUserSegments(UserModel user, Collection<CxUserToSegmentModel> userSegments)
    {
        ServicesUtil.validateParameterNotNull(user, "user must not be null");
        if(CollectionUtils.isEmpty(userSegments))
        {
            return;
        }
        validateUserSegments(user.getUid(), userSegments);
        updateUserSegments(user, userSegments, true);
    }


    public void removeUserSegments(UserModel user, Collection<CxUserToSegmentModel> userSegmentsToRemove)
    {
        ServicesUtil.validateParameterNotNull(user, "user must not be null");
        if(CollectionUtils.isEmpty(userSegmentsToRemove))
        {
            return;
        }
        validateUserSegments(user.getUid(), userSegmentsToRemove);
        Collection<CxUserToSegmentModel> currentUserToSegments = user.getUserToSegments();
        Set<String> segmentsToRemove = (Set<String>)userSegmentsToRemove.stream().map(this::getUserSegmentKey).collect(Collectors.toSet());
        Collection<CxUserToSegmentModel> newUserToSegments = (Collection<CxUserToSegmentModel>)currentUserToSegments.stream().filter(us -> !segmentsToRemove.contains(getUserSegmentKey(us))).collect(Collectors.toList());
        user.setUserToSegments(newUserToSegments);
        this.modelService.save(user);
        this.modelService.saveAll(newUserToSegments);
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}

package de.hybris.platform.warehousing.sourcing.fitness.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.warehousing.data.sourcing.FitSourcingLocation;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingFactor;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.sourcing.factor.SourcingFactorService;
import de.hybris.platform.warehousing.sourcing.fitness.FitnessService;
import de.hybris.platform.warehousing.sourcing.fitness.evaluation.FitnessEvaluator;
import de.hybris.platform.warehousing.sourcing.fitness.evaluation.FitnessEvaluatorFactory;
import de.hybris.platform.warehousing.sourcing.fitness.normalize.FitnessNormalizer;
import de.hybris.platform.warehousing.sourcing.fitness.normalize.FitnessNormalizerFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultFitnessService implements FitnessService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultFitnessService.class);
    private static final double ONE_HUNDRED = 100.0D;
    private FitnessEvaluatorFactory fitnessEvaluatorFactory;
    private SourcingFactorService sourcingFactorService;
    private FitnessNormalizerFactory fitnessNormalizerFactory;
    private Comparator<FitSourcingLocation> fitnessComparator;


    public List<SourcingLocation> sortByFitness(SourcingContext sourcingContext)
    {
        ServicesUtil.validateParameterNotNull(sourcingContext, "SourcingContext cannot be null");
        ServicesUtil.validateIfAnyResult(sourcingContext.getSourcingLocations(), "No location found to check for its fitness");
        Collection<SourcingLocation> sourcingLocations = sourcingContext.getSourcingLocations();
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("Original sourcing location order: {}", sourcingLocations.stream().map(sl -> sl.getWarehouse().getCode())
                            .collect(Collectors.joining(",", "{", "}")));
        }
        FitSourcingLocation[] fitSourcingLocations = calculateFitness(sourcingContext);
        Arrays.sort(fitSourcingLocations, this.fitnessComparator);
        if(LOGGER.isDebugEnabled())
        {
            LOGGER.debug("Post-Fitness sourcing location order: {}", Arrays.<FitSourcingLocation>stream(fitSourcingLocations)
                            .map(sl -> sl.getWarehouse().getCode()).collect(Collectors.joining(",", "{", "}")));
        }
        return (List)Arrays.asList((Object[])fitSourcingLocations);
    }


    protected FitSourcingLocation[] calculateFitness(SourcingContext sourcingContext)
    {
        ServicesUtil.validateParameterNotNull(sourcingContext, "SourcingContext cannot be null");
        ServicesUtil.validateIfAnyResult(sourcingContext.getSourcingLocations(), "No location found to check for its fitness");
        ServicesUtil.validateIfAnyResult(sourcingContext.getOrderEntries(), "No OrderEntries found in the SourcingContext");
        ServicesUtil.validateParameterNotNull(((AbstractOrderEntryModel)sourcingContext.getOrderEntries().iterator().next()).getOrder(), "No Order found for the OrderEntries in the sourcingContext");
        ServicesUtil.validateParameterNotNull(((AbstractOrderEntryModel)sourcingContext.getOrderEntries().iterator().next()).getOrder().getStore(), "No BaseStore found for the Order in the sourcingContext");
        Collection<SourcingLocation> sourcingLocations = sourcingContext.getSourcingLocations();
        BaseStoreModel baseStore = ((AbstractOrderEntryModel)sourcingContext.getOrderEntries().iterator().next()).getOrder().getStore();
        Set<SourcingFactor> sourcingFactors = getSourcingFactorService().getAllSourcingFactorsForBaseStore(baseStore);
        return getFitSourcingLocations(sourcingLocations, sourcingFactors);
    }


    protected FitSourcingLocation[] getFitSourcingLocations(Collection<SourcingLocation> sourcingLocations, Set<SourcingFactor> sourcingFactors)
    {
        ServicesUtil.validateIfAnyResult(sourcingLocations, "No location found to check for its fitness");
        ServicesUtil.validateIfAnyResult(sourcingFactors, "No sourcing factors found");
        FitSourcingLocation[] fitSourcingLocations = new FitSourcingLocation[sourcingLocations.size()];
        int rows = sourcingLocations.size();
        int cols = sourcingFactors.size();
        double[][] fitnessMatrix = new double[rows][cols];
        double[] totalsMatrix = new double[cols];
        int locationCursor = 0;
        int factorCursor = 0;
        for(SourcingLocation sourcingLocation : sourcingLocations)
        {
            for(SourcingFactor factor : sourcingFactors)
            {
                FitnessEvaluator evaluator = this.fitnessEvaluatorFactory.getEvaluator(factor.getFactorId());
                fitnessMatrix[locationCursor][factorCursor] = evaluator.evaluate(sourcingLocation).doubleValue();
                totalsMatrix[factorCursor] = totalsMatrix[factorCursor] + fitnessMatrix[locationCursor][factorCursor];
                factorCursor++;
            }
            factorCursor = 0;
            locationCursor++;
        }
        locationCursor = 0;
        factorCursor = 0;
        double locationFitness = 0.0D;
        for(SourcingLocation sourcingLocation : sourcingLocations)
        {
            for(SourcingFactor factor : sourcingFactors)
            {
                FitnessNormalizer normalizer = this.fitnessNormalizerFactory.getNormalizer(factor.getFactorId());
                fitnessMatrix[locationCursor][factorCursor] = normalizer.normalize(Double.valueOf(fitnessMatrix[locationCursor][factorCursor]),
                                Double.valueOf((Double.doubleToLongBits(totalsMatrix[factorCursor]) == 0L) ? 1.0D : totalsMatrix[factorCursor])).doubleValue();
                locationFitness += fitnessMatrix[locationCursor][factorCursor] * factor.getWeight() / 100.0D;
                factorCursor++;
            }
            FitSourcingLocation fitSourcingLocation = buildFitSourcingLocation(sourcingLocation);
            fitSourcingLocation.setFitness(Double.valueOf(locationFitness));
            fitSourcingLocations[locationCursor] = fitSourcingLocation;
            factorCursor = 0;
            locationCursor++;
            locationFitness = 0.0D;
        }
        return fitSourcingLocations;
    }


    protected FitSourcingLocation buildFitSourcingLocation(SourcingLocation sourcingLocation)
    {
        ServicesUtil.validateParameterNotNull(sourcingLocation, "SourcingLocation cannot be null");
        FitSourcingLocation fitSourcingLocation = new FitSourcingLocation();
        try
        {
            BeanUtils.copyProperties(fitSourcingLocation, sourcingLocation);
        }
        catch(IllegalAccessException | java.lang.reflect.InvocationTargetException e)
        {
            throw new IllegalArgumentException("Sourcing location was not properly formatted.");
        }
        return fitSourcingLocation;
    }


    protected FitnessEvaluatorFactory getFitnessEvaluatorFactory()
    {
        return this.fitnessEvaluatorFactory;
    }


    @Required
    public void setFitnessEvaluatorFactory(FitnessEvaluatorFactory fitnessEvaluatorFactory)
    {
        this.fitnessEvaluatorFactory = fitnessEvaluatorFactory;
    }


    protected Comparator<FitSourcingLocation> getFitnessComparator()
    {
        return this.fitnessComparator;
    }


    @Required
    public void setFitnessComparator(Comparator<FitSourcingLocation> fitnessComparator)
    {
        this.fitnessComparator = fitnessComparator;
    }


    protected SourcingFactorService getSourcingFactorService()
    {
        return this.sourcingFactorService;
    }


    @Required
    public void setSourcingFactorService(SourcingFactorService sourcingFactorService)
    {
        this.sourcingFactorService = sourcingFactorService;
    }


    public FitnessNormalizerFactory getFitnessNormalizerFactory()
    {
        return this.fitnessNormalizerFactory;
    }


    @Required
    public void setFitnessNormalizerFactory(FitnessNormalizerFactory fitnessNormalizerFactory)
    {
        this.fitnessNormalizerFactory = fitnessNormalizerFactory;
    }
}

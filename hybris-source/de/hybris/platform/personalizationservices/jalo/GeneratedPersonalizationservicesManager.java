package de.hybris.platform.personalizationservices.jalo;

import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.personalizationservices.constants.GeneratedPersonalizationservicesConstants;
import de.hybris.platform.personalizationservices.jalo.config.CxConfig;
import de.hybris.platform.personalizationservices.jalo.config.CxPeriodicVoterConfig;
import de.hybris.platform.personalizationservices.jalo.config.CxUrlVoterConfig;
import de.hybris.platform.personalizationservices.jalo.process.CxDefaultPersonalizationCalculationCronJob;
import de.hybris.platform.personalizationservices.jalo.process.CxPersonalizationProcess;
import de.hybris.platform.personalizationservices.jalo.process.CxPersonalizationProcessCleanupCronJob;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.validation.jalo.constraints.RegExpConstraint;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedPersonalizationservicesManager extends Extension
{
    protected static final OneToManyHandler<CxUserToSegment> CXUSERTOSEGMENTRELATIONUSERTOSEGMENTSHANDLER = new OneToManyHandler(GeneratedPersonalizationservicesConstants.TC.CXUSERTOSEGMENT, true, "user", null, false, true, 0);
    protected static final OneToManyHandler<CxResults> CXUSERTOCXRESULTSCXRESULTSHANDLER = new OneToManyHandler(GeneratedPersonalizationservicesConstants.TC.CXRESULTS, true, "user", null, false, true, 0);
    protected static String CXPERSPROCTOCATVER_SRC_ORDERED = "relation.CxPersProcToCatVer.source.ordered";
    protected static String CXPERSPROCTOCATVER_TGT_ORDERED = "relation.CxPersProcToCatVer.target.ordered";
    protected static String CXPERSPROCTOCATVER_MARKMODIFIED = "relation.CxPersProcToCatVer.markmodified";
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("cxConfig", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.basecommerce.jalo.site.BaseSite", Collections.unmodifiableMap(tmp));
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public CxConfig createCxConfig(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPersonalizationservicesConstants.TC.CXCONFIG);
            return (CxConfig)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CxConfig : " + e.getMessage(), 0);
        }
    }


    public CxConfig createCxConfig(Map attributeValues)
    {
        return createCxConfig(getSession().getSessionContext(), attributeValues);
    }


    public CxCustomization createCxCustomization(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPersonalizationservicesConstants.TC.CXCUSTOMIZATION);
            return (CxCustomization)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CxCustomization : " + e.getMessage(), 0);
        }
    }


    public CxCustomization createCxCustomization(Map attributeValues)
    {
        return createCxCustomization(getSession().getSessionContext(), attributeValues);
    }


    public CxCustomizationsGroup createCxCustomizationsGroup(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPersonalizationservicesConstants.TC.CXCUSTOMIZATIONSGROUP);
            return (CxCustomizationsGroup)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CxCustomizationsGroup : " + e.getMessage(), 0);
        }
    }


    public CxCustomizationsGroup createCxCustomizationsGroup(Map attributeValues)
    {
        return createCxCustomizationsGroup(getSession().getSessionContext(), attributeValues);
    }


    public CxDefaultPersonalizationCalculationCronJob createCxDefaultPersonalizationCalculationCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPersonalizationservicesConstants.TC.CXDEFAULTPERSONALIZATIONCALCULATIONCRONJOB);
            return (CxDefaultPersonalizationCalculationCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CxDefaultPersonalizationCalculationCronJob : " + e.getMessage(), 0);
        }
    }


    public CxDefaultPersonalizationCalculationCronJob createCxDefaultPersonalizationCalculationCronJob(Map attributeValues)
    {
        return createCxDefaultPersonalizationCalculationCronJob(getSession().getSessionContext(), attributeValues);
    }


    public CxDefaultTrigger createCxDefaultTrigger(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPersonalizationservicesConstants.TC.CXDEFAULTTRIGGER);
            return (CxDefaultTrigger)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CxDefaultTrigger : " + e.getMessage(), 0);
        }
    }


    public CxDefaultTrigger createCxDefaultTrigger(Map attributeValues)
    {
        return createCxDefaultTrigger(getSession().getSessionContext(), attributeValues);
    }


    public CxExpressionTrigger createCxExpressionTrigger(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPersonalizationservicesConstants.TC.CXEXPRESSIONTRIGGER);
            return (CxExpressionTrigger)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CxExpressionTrigger : " + e.getMessage(), 0);
        }
    }


    public CxExpressionTrigger createCxExpressionTrigger(Map attributeValues)
    {
        return createCxExpressionTrigger(getSession().getSessionContext(), attributeValues);
    }


    public CxPeriodicVoterConfig createCxPeriodicVoterConfig(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPersonalizationservicesConstants.TC.CXPERIODICVOTERCONFIG);
            return (CxPeriodicVoterConfig)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CxPeriodicVoterConfig : " + e.getMessage(), 0);
        }
    }


    public CxPeriodicVoterConfig createCxPeriodicVoterConfig(Map attributeValues)
    {
        return createCxPeriodicVoterConfig(getSession().getSessionContext(), attributeValues);
    }


    public CxPersonalizationProcess createCxPersonalizationProcess(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPersonalizationservicesConstants.TC.CXPERSONALIZATIONPROCESS);
            return (CxPersonalizationProcess)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CxPersonalizationProcess : " + e.getMessage(), 0);
        }
    }


    public CxPersonalizationProcess createCxPersonalizationProcess(Map attributeValues)
    {
        return createCxPersonalizationProcess(getSession().getSessionContext(), attributeValues);
    }


    public CxPersonalizationProcessCleanupCronJob createCxPersonalizationProcessCleanupCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPersonalizationservicesConstants.TC.CXPERSONALIZATIONPROCESSCLEANUPCRONJOB);
            return (CxPersonalizationProcessCleanupCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CxPersonalizationProcessCleanupCronJob : " + e.getMessage(), 0);
        }
    }


    public CxPersonalizationProcessCleanupCronJob createCxPersonalizationProcessCleanupCronJob(Map attributeValues)
    {
        return createCxPersonalizationProcessCleanupCronJob(getSession().getSessionContext(), attributeValues);
    }


    public CxResults createCxResults(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPersonalizationservicesConstants.TC.CXRESULTS);
            return (CxResults)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CxResults : " + e.getMessage(), 0);
        }
    }


    public CxResults createCxResults(Map attributeValues)
    {
        return createCxResults(getSession().getSessionContext(), attributeValues);
    }


    public CxResultsCleaningCronJob createCxResultsCleaningCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPersonalizationservicesConstants.TC.CXRESULTSCLEANINGCRONJOB);
            return (CxResultsCleaningCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CxResultsCleaningCronJob : " + e.getMessage(), 0);
        }
    }


    public CxResultsCleaningCronJob createCxResultsCleaningCronJob(Map attributeValues)
    {
        return createCxResultsCleaningCronJob(getSession().getSessionContext(), attributeValues);
    }


    public CxSegment createCxSegment(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPersonalizationservicesConstants.TC.CXSEGMENT);
            return (CxSegment)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CxSegment : " + e.getMessage(), 0);
        }
    }


    public CxSegment createCxSegment(Map attributeValues)
    {
        return createCxSegment(getSession().getSessionContext(), attributeValues);
    }


    public CxSegmentTrigger createCxSegmentTrigger(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPersonalizationservicesConstants.TC.CXSEGMENTTRIGGER);
            return (CxSegmentTrigger)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CxSegmentTrigger : " + e.getMessage(), 0);
        }
    }


    public CxSegmentTrigger createCxSegmentTrigger(Map attributeValues)
    {
        return createCxSegmentTrigger(getSession().getSessionContext(), attributeValues);
    }


    public CxUpdateSegmentsCronJob createCxUpdateSegmentsCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPersonalizationservicesConstants.TC.CXUPDATESEGMENTSCRONJOB);
            return (CxUpdateSegmentsCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CxUpdateSegmentsCronJob : " + e.getMessage(), 0);
        }
    }


    public CxUpdateSegmentsCronJob createCxUpdateSegmentsCronJob(Map attributeValues)
    {
        return createCxUpdateSegmentsCronJob(getSession().getSessionContext(), attributeValues);
    }


    public CxUrlVoterConfig createCxUrlVoterConfig(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPersonalizationservicesConstants.TC.CXURLVOTERCONFIG);
            return (CxUrlVoterConfig)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CxUrlVoterConfig : " + e.getMessage(), 0);
        }
    }


    public CxUrlVoterConfig createCxUrlVoterConfig(Map attributeValues)
    {
        return createCxUrlVoterConfig(getSession().getSessionContext(), attributeValues);
    }


    public CxUserToSegment createCxUserToSegment(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPersonalizationservicesConstants.TC.CXUSERTOSEGMENT);
            return (CxUserToSegment)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CxUserToSegment : " + e.getMessage(), 0);
        }
    }


    public CxUserToSegment createCxUserToSegment(Map attributeValues)
    {
        return createCxUserToSegment(getSession().getSessionContext(), attributeValues);
    }


    public CxVariation createCxVariation(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPersonalizationservicesConstants.TC.CXVARIATION);
            return (CxVariation)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CxVariation : " + e.getMessage(), 0);
        }
    }


    public CxVariation createCxVariation(Map attributeValues)
    {
        return createCxVariation(getSession().getSessionContext(), attributeValues);
    }


    public RegExpConstraint createRegExpConstraint(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedPersonalizationservicesConstants.TC.REGEXPCONSTRAINT);
            return (RegExpConstraint)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RegExpConstraint : " + e.getMessage(), 0);
        }
    }


    public RegExpConstraint createRegExpConstraint(Map attributeValues)
    {
        return createRegExpConstraint(getSession().getSessionContext(), attributeValues);
    }


    public CxConfig getCxConfig(SessionContext ctx, BaseSite item)
    {
        return (CxConfig)item.getProperty(ctx, GeneratedPersonalizationservicesConstants.Attributes.BaseSite.CXCONFIG);
    }


    public CxConfig getCxConfig(BaseSite item)
    {
        return getCxConfig(getSession().getSessionContext(), item);
    }


    public void setCxConfig(SessionContext ctx, BaseSite item, CxConfig value)
    {
        item.setProperty(ctx, GeneratedPersonalizationservicesConstants.Attributes.BaseSite.CXCONFIG, value);
    }


    public void setCxConfig(BaseSite item, CxConfig value)
    {
        setCxConfig(getSession().getSessionContext(), item, value);
    }


    public Collection<CxPersonalizationProcess> getCxPersonalizationProcesses(SessionContext ctx, CatalogVersion item)
    {
        List<CxPersonalizationProcess> items = item.getLinkedItems(ctx, true, GeneratedPersonalizationservicesConstants.Relations.CXPERSPROCTOCATVER, "CxPersonalizationProcess", null, false, false);
        return items;
    }


    public Collection<CxPersonalizationProcess> getCxPersonalizationProcesses(CatalogVersion item)
    {
        return getCxPersonalizationProcesses(getSession().getSessionContext(), item);
    }


    public long getCxPersonalizationProcessesCount(SessionContext ctx, CatalogVersion item)
    {
        return item.getLinkedItemsCount(ctx, true, GeneratedPersonalizationservicesConstants.Relations.CXPERSPROCTOCATVER, "CxPersonalizationProcess", null);
    }


    public long getCxPersonalizationProcessesCount(CatalogVersion item)
    {
        return getCxPersonalizationProcessesCount(getSession().getSessionContext(), item);
    }


    public void setCxPersonalizationProcesses(SessionContext ctx, CatalogVersion item, Collection<CxPersonalizationProcess> value)
    {
        item.setLinkedItems(ctx, true, GeneratedPersonalizationservicesConstants.Relations.CXPERSPROCTOCATVER, null, value, false, false,
                        Utilities.getMarkModifiedOverride(CXPERSPROCTOCATVER_MARKMODIFIED));
    }


    public void setCxPersonalizationProcesses(CatalogVersion item, Collection<CxPersonalizationProcess> value)
    {
        setCxPersonalizationProcesses(getSession().getSessionContext(), item, value);
    }


    public void addToCxPersonalizationProcesses(SessionContext ctx, CatalogVersion item, CxPersonalizationProcess value)
    {
        item.addLinkedItems(ctx, true, GeneratedPersonalizationservicesConstants.Relations.CXPERSPROCTOCATVER, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CXPERSPROCTOCATVER_MARKMODIFIED));
    }


    public void addToCxPersonalizationProcesses(CatalogVersion item, CxPersonalizationProcess value)
    {
        addToCxPersonalizationProcesses(getSession().getSessionContext(), item, value);
    }


    public void removeFromCxPersonalizationProcesses(SessionContext ctx, CatalogVersion item, CxPersonalizationProcess value)
    {
        item.removeLinkedItems(ctx, true, GeneratedPersonalizationservicesConstants.Relations.CXPERSPROCTOCATVER, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CXPERSPROCTOCATVER_MARKMODIFIED));
    }


    public void removeFromCxPersonalizationProcesses(CatalogVersion item, CxPersonalizationProcess value)
    {
        removeFromCxPersonalizationProcesses(getSession().getSessionContext(), item, value);
    }


    public Collection<CxResults> getCxResults(SessionContext ctx, User item)
    {
        return CXUSERTOCXRESULTSCXRESULTSHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<CxResults> getCxResults(User item)
    {
        return getCxResults(getSession().getSessionContext(), item);
    }


    public void setCxResults(SessionContext ctx, User item, Collection<CxResults> value)
    {
        CXUSERTOCXRESULTSCXRESULTSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setCxResults(User item, Collection<CxResults> value)
    {
        setCxResults(getSession().getSessionContext(), item, value);
    }


    public void addToCxResults(SessionContext ctx, User item, CxResults value)
    {
        CXUSERTOCXRESULTSCXRESULTSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToCxResults(User item, CxResults value)
    {
        addToCxResults(getSession().getSessionContext(), item, value);
    }


    public void removeFromCxResults(SessionContext ctx, User item, CxResults value)
    {
        CXUSERTOCXRESULTSCXRESULTSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromCxResults(User item, CxResults value)
    {
        removeFromCxResults(getSession().getSessionContext(), item, value);
    }


    public String getName()
    {
        return "personalizationservices";
    }


    public Collection<CxUserToSegment> getUserToSegments(SessionContext ctx, User item)
    {
        return CXUSERTOSEGMENTRELATIONUSERTOSEGMENTSHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<CxUserToSegment> getUserToSegments(User item)
    {
        return getUserToSegments(getSession().getSessionContext(), item);
    }


    public void setUserToSegments(SessionContext ctx, User item, Collection<CxUserToSegment> value)
    {
        CXUSERTOSEGMENTRELATIONUSERTOSEGMENTSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setUserToSegments(User item, Collection<CxUserToSegment> value)
    {
        setUserToSegments(getSession().getSessionContext(), item, value);
    }


    public void addToUserToSegments(SessionContext ctx, User item, CxUserToSegment value)
    {
        CXUSERTOSEGMENTRELATIONUSERTOSEGMENTSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToUserToSegments(User item, CxUserToSegment value)
    {
        addToUserToSegments(getSession().getSessionContext(), item, value);
    }


    public void removeFromUserToSegments(SessionContext ctx, User item, CxUserToSegment value)
    {
        CXUSERTOSEGMENTRELATIONUSERTOSEGMENTSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromUserToSegments(User item, CxUserToSegment value)
    {
        removeFromUserToSegments(getSession().getSessionContext(), item, value);
    }
}

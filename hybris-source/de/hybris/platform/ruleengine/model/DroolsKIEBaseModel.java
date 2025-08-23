package de.hybris.platform.ruleengine.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ruleengine.enums.DroolsEqualityBehavior;
import de.hybris.platform.ruleengine.enums.DroolsEventProcessingMode;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Set;

public class DroolsKIEBaseModel extends ItemModel
{
    public static final String _TYPECODE = "DroolsKIEBase";
    public static final String _DROOLSKIEMODULE2BASE = "DroolsKIEModule2Base";
    public static final String NAME = "name";
    public static final String EQUALITYBEHAVIOR = "equalityBehavior";
    public static final String EVENTPROCESSINGMODE = "eventProcessingMode";
    public static final String DEFAULTKIESESSION = "defaultKIESession";
    public static final String KIEMODULE = "kieModule";
    public static final String KIESESSIONS = "kieSessions";
    public static final String RULES = "rules";


    public DroolsKIEBaseModel()
    {
    }


    public DroolsKIEBaseModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DroolsKIEBaseModel(DroolsKIEModuleModel _kieModule, String _name)
    {
        setKieModule(_kieModule);
        setName(_name);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DroolsKIEBaseModel(DroolsKIEModuleModel _kieModule, String _name, ItemModel _owner)
    {
        setKieModule(_kieModule);
        setName(_name);
        setOwner(_owner);
    }


    @Accessor(qualifier = "defaultKIESession", type = Accessor.Type.GETTER)
    public DroolsKIESessionModel getDefaultKIESession()
    {
        return (DroolsKIESessionModel)getPersistenceContext().getPropertyValue("defaultKIESession");
    }


    @Accessor(qualifier = "equalityBehavior", type = Accessor.Type.GETTER)
    public DroolsEqualityBehavior getEqualityBehavior()
    {
        return (DroolsEqualityBehavior)getPersistenceContext().getPropertyValue("equalityBehavior");
    }


    @Accessor(qualifier = "eventProcessingMode", type = Accessor.Type.GETTER)
    public DroolsEventProcessingMode getEventProcessingMode()
    {
        return (DroolsEventProcessingMode)getPersistenceContext().getPropertyValue("eventProcessingMode");
    }


    @Accessor(qualifier = "kieModule", type = Accessor.Type.GETTER)
    public DroolsKIEModuleModel getKieModule()
    {
        return (DroolsKIEModuleModel)getPersistenceContext().getPropertyValue("kieModule");
    }


    @Accessor(qualifier = "kieSessions", type = Accessor.Type.GETTER)
    public Collection<DroolsKIESessionModel> getKieSessions()
    {
        return (Collection<DroolsKIESessionModel>)getPersistenceContext().getPropertyValue("kieSessions");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "rules", type = Accessor.Type.GETTER)
    public Set<DroolsRuleModel> getRules()
    {
        return (Set<DroolsRuleModel>)getPersistenceContext().getPropertyValue("rules");
    }


    @Accessor(qualifier = "defaultKIESession", type = Accessor.Type.SETTER)
    public void setDefaultKIESession(DroolsKIESessionModel value)
    {
        getPersistenceContext().setPropertyValue("defaultKIESession", value);
    }


    @Accessor(qualifier = "equalityBehavior", type = Accessor.Type.SETTER)
    public void setEqualityBehavior(DroolsEqualityBehavior value)
    {
        getPersistenceContext().setPropertyValue("equalityBehavior", value);
    }


    @Accessor(qualifier = "eventProcessingMode", type = Accessor.Type.SETTER)
    public void setEventProcessingMode(DroolsEventProcessingMode value)
    {
        getPersistenceContext().setPropertyValue("eventProcessingMode", value);
    }


    @Accessor(qualifier = "kieModule", type = Accessor.Type.SETTER)
    public void setKieModule(DroolsKIEModuleModel value)
    {
        getPersistenceContext().setPropertyValue("kieModule", value);
    }


    @Accessor(qualifier = "kieSessions", type = Accessor.Type.SETTER)
    public void setKieSessions(Collection<DroolsKIESessionModel> value)
    {
        getPersistenceContext().setPropertyValue("kieSessions", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "rules", type = Accessor.Type.SETTER)
    public void setRules(Set<DroolsRuleModel> value)
    {
        getPersistenceContext().setPropertyValue("rules", value);
    }
}

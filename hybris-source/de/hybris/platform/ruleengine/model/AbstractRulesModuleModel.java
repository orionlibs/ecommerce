package de.hybris.platform.ruleengine.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;

public class AbstractRulesModuleModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractRulesModule";
    public static final String NAME = "name";
    public static final String RULETYPE = "ruleType";
    public static final String ACTIVE = "active";
    public static final String VERSION = "version";
    public static final String CATALOGVERSIONS = "catalogVersions";
    public static final String ALLOWEDTARGETS = "allowedTargets";
    public static final String LOCKACQUIRED = "lockAcquired";


    public AbstractRulesModuleModel()
    {
    }


    public AbstractRulesModuleModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractRulesModuleModel(String _name, Long _version)
    {
        setName(_name);
        setVersion(_version);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractRulesModuleModel(String _name, ItemModel _owner, Long _version)
    {
        setName(_name);
        setOwner(_owner);
        setVersion(_version);
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public Boolean getActive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("active");
    }


    @Accessor(qualifier = "allowedTargets", type = Accessor.Type.GETTER)
    public List<AbstractRulesModuleModel> getAllowedTargets()
    {
        return (List<AbstractRulesModuleModel>)getPersistenceContext().getPropertyValue("allowedTargets");
    }


    @Accessor(qualifier = "catalogVersions", type = Accessor.Type.GETTER)
    public Collection<CatalogVersionModel> getCatalogVersions()
    {
        return (Collection<CatalogVersionModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "catalogVersions");
    }


    @Accessor(qualifier = "lockAcquired", type = Accessor.Type.GETTER)
    public Boolean getLockAcquired()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("lockAcquired");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "ruleType", type = Accessor.Type.GETTER)
    public RuleType getRuleType()
    {
        return (RuleType)getPersistenceContext().getPropertyValue("ruleType");
    }


    @Accessor(qualifier = "version", type = Accessor.Type.GETTER)
    public Long getVersion()
    {
        return (Long)getPersistenceContext().getPropertyValue("version");
    }


    @Accessor(qualifier = "active", type = Accessor.Type.SETTER)
    public void setActive(Boolean value)
    {
        getPersistenceContext().setPropertyValue("active", value);
    }


    @Accessor(qualifier = "allowedTargets", type = Accessor.Type.SETTER)
    public void setAllowedTargets(List<AbstractRulesModuleModel> value)
    {
        getPersistenceContext().setPropertyValue("allowedTargets", value);
    }


    @Accessor(qualifier = "lockAcquired", type = Accessor.Type.SETTER)
    public void setLockAcquired(Boolean value)
    {
        getPersistenceContext().setPropertyValue("lockAcquired", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "ruleType", type = Accessor.Type.SETTER)
    public void setRuleType(RuleType value)
    {
        getPersistenceContext().setPropertyValue("ruleType", value);
    }


    @Accessor(qualifier = "version", type = Accessor.Type.SETTER)
    public void setVersion(Long value)
    {
        getPersistenceContext().setPropertyValue("version", value);
    }
}

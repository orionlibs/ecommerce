package de.hybris.platform.ruleengine.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class AbstractRuleEngineRuleModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractRuleEngineRule";
    public static final String _SOURCERULE2DROOLSRULE = "SourceRule2DroolsRule";
    public static final String UUID = "uuid";
    public static final String CODE = "code";
    public static final String ACTIVE = "active";
    public static final String RULECONTENT = "ruleContent";
    public static final String RULETYPE = "ruleType";
    public static final String CHECKSUM = "checksum";
    public static final String CURRENTVERSION = "currentVersion";
    public static final String VERSION = "version";
    public static final String RULEPARAMETERS = "ruleParameters";
    public static final String MAXALLOWEDRUNS = "maxAllowedRuns";
    public static final String RULEGROUPCODE = "ruleGroupCode";
    public static final String MESSAGEFIRED = "messageFired";
    public static final String SOURCERULE = "sourceRule";
    public static final String PROMOTION = "promotion";


    public AbstractRuleEngineRuleModel()
    {
    }


    public AbstractRuleEngineRuleModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractRuleEngineRuleModel(String _code, RuleType _ruleType, String _uuid, Long _version)
    {
        setCode(_code);
        setRuleType(_ruleType);
        setUuid(_uuid);
        setVersion(_version);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractRuleEngineRuleModel(String _code, ItemModel _owner, RuleType _ruleType, String _uuid, Long _version)
    {
        setCode(_code);
        setOwner(_owner);
        setRuleType(_ruleType);
        setUuid(_uuid);
        setVersion(_version);
    }


    @Accessor(qualifier = "active", type = Accessor.Type.GETTER)
    public Boolean getActive()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("active");
    }


    @Accessor(qualifier = "checksum", type = Accessor.Type.GETTER)
    public String getChecksum()
    {
        return (String)getPersistenceContext().getPropertyValue("checksum");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "currentVersion", type = Accessor.Type.GETTER)
    public Boolean getCurrentVersion()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("currentVersion");
    }


    @Accessor(qualifier = "maxAllowedRuns", type = Accessor.Type.GETTER)
    public Integer getMaxAllowedRuns()
    {
        return (Integer)getPersistenceContext().getPropertyValue("maxAllowedRuns");
    }


    @Accessor(qualifier = "messageFired", type = Accessor.Type.GETTER)
    public String getMessageFired()
    {
        return getMessageFired(null);
    }


    @Accessor(qualifier = "messageFired", type = Accessor.Type.GETTER)
    public String getMessageFired(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("messageFired", loc);
    }


    @Accessor(qualifier = "promotion", type = Accessor.Type.GETTER)
    public RuleBasedPromotionModel getPromotion()
    {
        return (RuleBasedPromotionModel)getPersistenceContext().getPropertyValue("promotion");
    }


    @Accessor(qualifier = "ruleContent", type = Accessor.Type.GETTER)
    public String getRuleContent()
    {
        return (String)getPersistenceContext().getPropertyValue("ruleContent");
    }


    @Accessor(qualifier = "ruleGroupCode", type = Accessor.Type.GETTER)
    public String getRuleGroupCode()
    {
        return (String)getPersistenceContext().getPropertyValue("ruleGroupCode");
    }


    @Accessor(qualifier = "ruleParameters", type = Accessor.Type.GETTER)
    public String getRuleParameters()
    {
        return (String)getPersistenceContext().getPropertyValue("ruleParameters");
    }


    @Accessor(qualifier = "ruleType", type = Accessor.Type.GETTER)
    public RuleType getRuleType()
    {
        return (RuleType)getPersistenceContext().getPropertyValue("ruleType");
    }


    @Accessor(qualifier = "sourceRule", type = Accessor.Type.GETTER)
    public AbstractRuleModel getSourceRule()
    {
        return (AbstractRuleModel)getPersistenceContext().getPropertyValue("sourceRule");
    }


    @Accessor(qualifier = "uuid", type = Accessor.Type.GETTER)
    public String getUuid()
    {
        return (String)getPersistenceContext().getPropertyValue("uuid");
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


    @Accessor(qualifier = "checksum", type = Accessor.Type.SETTER)
    public void setChecksum(String value)
    {
        getPersistenceContext().setPropertyValue("checksum", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "currentVersion", type = Accessor.Type.SETTER)
    public void setCurrentVersion(Boolean value)
    {
        getPersistenceContext().setPropertyValue("currentVersion", value);
    }


    @Accessor(qualifier = "maxAllowedRuns", type = Accessor.Type.SETTER)
    public void setMaxAllowedRuns(Integer value)
    {
        getPersistenceContext().setPropertyValue("maxAllowedRuns", value);
    }


    @Accessor(qualifier = "messageFired", type = Accessor.Type.SETTER)
    public void setMessageFired(String value)
    {
        setMessageFired(value, null);
    }


    @Accessor(qualifier = "messageFired", type = Accessor.Type.SETTER)
    public void setMessageFired(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("messageFired", loc, value);
    }


    @Accessor(qualifier = "promotion", type = Accessor.Type.SETTER)
    public void setPromotion(RuleBasedPromotionModel value)
    {
        getPersistenceContext().setPropertyValue("promotion", value);
    }


    @Accessor(qualifier = "ruleContent", type = Accessor.Type.SETTER)
    public void setRuleContent(String value)
    {
        getPersistenceContext().setPropertyValue("ruleContent", value);
    }


    @Accessor(qualifier = "ruleGroupCode", type = Accessor.Type.SETTER)
    public void setRuleGroupCode(String value)
    {
        getPersistenceContext().setPropertyValue("ruleGroupCode", value);
    }


    @Accessor(qualifier = "ruleParameters", type = Accessor.Type.SETTER)
    public void setRuleParameters(String value)
    {
        getPersistenceContext().setPropertyValue("ruleParameters", value);
    }


    @Accessor(qualifier = "ruleType", type = Accessor.Type.SETTER)
    public void setRuleType(RuleType value)
    {
        getPersistenceContext().setPropertyValue("ruleType", value);
    }


    @Accessor(qualifier = "sourceRule", type = Accessor.Type.SETTER)
    public void setSourceRule(AbstractRuleModel value)
    {
        getPersistenceContext().setPropertyValue("sourceRule", value);
    }


    @Accessor(qualifier = "uuid", type = Accessor.Type.SETTER)
    public void setUuid(String value)
    {
        getPersistenceContext().setPropertyValue("uuid", value);
    }


    @Accessor(qualifier = "version", type = Accessor.Type.SETTER)
    public void setVersion(Long value)
    {
        getPersistenceContext().setPropertyValue("version", value);
    }
}

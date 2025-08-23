package de.hybris.platform.ruleengine.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.ruleengine.constants.GeneratedRuleEngineConstants;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedDroolsKIEModule extends AbstractRulesModule
{
    public static final String MVNGROUPID = "mvnGroupId";
    public static final String MVNARTIFACTID = "mvnArtifactId";
    public static final String MVNVERSION = "mvnVersion";
    public static final String DEPLOYEDMVNVERSION = "deployedMvnVersion";
    public static final String DEFAULTKIEBASE = "defaultKIEBase";
    public static final String KIEBASES = "kieBases";
    protected static final OneToManyHandler<DroolsKIEBase> KIEBASESHANDLER = new OneToManyHandler(GeneratedRuleEngineConstants.TC.DROOLSKIEBASE, true, "kieModule", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractRulesModule.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("mvnGroupId", Item.AttributeMode.INITIAL);
        tmp.put("mvnArtifactId", Item.AttributeMode.INITIAL);
        tmp.put("mvnVersion", Item.AttributeMode.INITIAL);
        tmp.put("deployedMvnVersion", Item.AttributeMode.INITIAL);
        tmp.put("defaultKIEBase", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public DroolsKIEBase getDefaultKIEBase(SessionContext ctx)
    {
        return (DroolsKIEBase)getProperty(ctx, "defaultKIEBase");
    }


    public DroolsKIEBase getDefaultKIEBase()
    {
        return getDefaultKIEBase(getSession().getSessionContext());
    }


    public void setDefaultKIEBase(SessionContext ctx, DroolsKIEBase value)
    {
        setProperty(ctx, "defaultKIEBase", value);
    }


    public void setDefaultKIEBase(DroolsKIEBase value)
    {
        setDefaultKIEBase(getSession().getSessionContext(), value);
    }


    public String getDeployedMvnVersion(SessionContext ctx)
    {
        return (String)getProperty(ctx, "deployedMvnVersion");
    }


    public String getDeployedMvnVersion()
    {
        return getDeployedMvnVersion(getSession().getSessionContext());
    }


    public void setDeployedMvnVersion(SessionContext ctx, String value)
    {
        setProperty(ctx, "deployedMvnVersion", value);
    }


    public void setDeployedMvnVersion(String value)
    {
        setDeployedMvnVersion(getSession().getSessionContext(), value);
    }


    public Collection<DroolsKIEBase> getKieBases(SessionContext ctx)
    {
        return KIEBASESHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<DroolsKIEBase> getKieBases()
    {
        return getKieBases(getSession().getSessionContext());
    }


    public void setKieBases(SessionContext ctx, Collection<DroolsKIEBase> value)
    {
        KIEBASESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setKieBases(Collection<DroolsKIEBase> value)
    {
        setKieBases(getSession().getSessionContext(), value);
    }


    public void addToKieBases(SessionContext ctx, DroolsKIEBase value)
    {
        KIEBASESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToKieBases(DroolsKIEBase value)
    {
        addToKieBases(getSession().getSessionContext(), value);
    }


    public void removeFromKieBases(SessionContext ctx, DroolsKIEBase value)
    {
        KIEBASESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromKieBases(DroolsKIEBase value)
    {
        removeFromKieBases(getSession().getSessionContext(), value);
    }


    public String getMvnArtifactId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "mvnArtifactId");
    }


    public String getMvnArtifactId()
    {
        return getMvnArtifactId(getSession().getSessionContext());
    }


    public void setMvnArtifactId(SessionContext ctx, String value)
    {
        setProperty(ctx, "mvnArtifactId", value);
    }


    public void setMvnArtifactId(String value)
    {
        setMvnArtifactId(getSession().getSessionContext(), value);
    }


    public String getMvnGroupId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "mvnGroupId");
    }


    public String getMvnGroupId()
    {
        return getMvnGroupId(getSession().getSessionContext());
    }


    public void setMvnGroupId(SessionContext ctx, String value)
    {
        setProperty(ctx, "mvnGroupId", value);
    }


    public void setMvnGroupId(String value)
    {
        setMvnGroupId(getSession().getSessionContext(), value);
    }


    public String getMvnVersion(SessionContext ctx)
    {
        return (String)getProperty(ctx, "mvnVersion");
    }


    public String getMvnVersion()
    {
        return getMvnVersion(getSession().getSessionContext());
    }


    public void setMvnVersion(SessionContext ctx, String value)
    {
        setProperty(ctx, "mvnVersion", value);
    }


    public void setMvnVersion(String value)
    {
        setMvnVersion(getSession().getSessionContext(), value);
    }
}

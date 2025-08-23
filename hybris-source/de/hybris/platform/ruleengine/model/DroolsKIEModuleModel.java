package de.hybris.platform.ruleengine.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class DroolsKIEModuleModel extends AbstractRulesModuleModel
{
    public static final String _TYPECODE = "DroolsKIEModule";
    public static final String MVNGROUPID = "mvnGroupId";
    public static final String MVNARTIFACTID = "mvnArtifactId";
    public static final String MVNVERSION = "mvnVersion";
    public static final String DEPLOYEDMVNVERSION = "deployedMvnVersion";
    public static final String DEFAULTKIEBASE = "defaultKIEBase";
    public static final String KIEBASES = "kieBases";


    public DroolsKIEModuleModel()
    {
    }


    public DroolsKIEModuleModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DroolsKIEModuleModel(String _mvnArtifactId, String _mvnGroupId, String _mvnVersion, String _name, Long _version)
    {
        setMvnArtifactId(_mvnArtifactId);
        setMvnGroupId(_mvnGroupId);
        setMvnVersion(_mvnVersion);
        setName(_name);
        setVersion(_version);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DroolsKIEModuleModel(String _mvnArtifactId, String _mvnGroupId, String _mvnVersion, String _name, ItemModel _owner, Long _version)
    {
        setMvnArtifactId(_mvnArtifactId);
        setMvnGroupId(_mvnGroupId);
        setMvnVersion(_mvnVersion);
        setName(_name);
        setOwner(_owner);
        setVersion(_version);
    }


    @Accessor(qualifier = "defaultKIEBase", type = Accessor.Type.GETTER)
    public DroolsKIEBaseModel getDefaultKIEBase()
    {
        return (DroolsKIEBaseModel)getPersistenceContext().getPropertyValue("defaultKIEBase");
    }


    @Accessor(qualifier = "deployedMvnVersion", type = Accessor.Type.GETTER)
    public String getDeployedMvnVersion()
    {
        return (String)getPersistenceContext().getPropertyValue("deployedMvnVersion");
    }


    @Accessor(qualifier = "kieBases", type = Accessor.Type.GETTER)
    public Collection<DroolsKIEBaseModel> getKieBases()
    {
        return (Collection<DroolsKIEBaseModel>)getPersistenceContext().getPropertyValue("kieBases");
    }


    @Accessor(qualifier = "mvnArtifactId", type = Accessor.Type.GETTER)
    public String getMvnArtifactId()
    {
        return (String)getPersistenceContext().getPropertyValue("mvnArtifactId");
    }


    @Accessor(qualifier = "mvnGroupId", type = Accessor.Type.GETTER)
    public String getMvnGroupId()
    {
        return (String)getPersistenceContext().getPropertyValue("mvnGroupId");
    }


    @Accessor(qualifier = "mvnVersion", type = Accessor.Type.GETTER)
    public String getMvnVersion()
    {
        return (String)getPersistenceContext().getPropertyValue("mvnVersion");
    }


    @Accessor(qualifier = "defaultKIEBase", type = Accessor.Type.SETTER)
    public void setDefaultKIEBase(DroolsKIEBaseModel value)
    {
        getPersistenceContext().setPropertyValue("defaultKIEBase", value);
    }


    @Accessor(qualifier = "deployedMvnVersion", type = Accessor.Type.SETTER)
    public void setDeployedMvnVersion(String value)
    {
        getPersistenceContext().setPropertyValue("deployedMvnVersion", value);
    }


    @Accessor(qualifier = "kieBases", type = Accessor.Type.SETTER)
    public void setKieBases(Collection<DroolsKIEBaseModel> value)
    {
        getPersistenceContext().setPropertyValue("kieBases", value);
    }


    @Accessor(qualifier = "mvnArtifactId", type = Accessor.Type.SETTER)
    public void setMvnArtifactId(String value)
    {
        getPersistenceContext().setPropertyValue("mvnArtifactId", value);
    }


    @Accessor(qualifier = "mvnGroupId", type = Accessor.Type.SETTER)
    public void setMvnGroupId(String value)
    {
        getPersistenceContext().setPropertyValue("mvnGroupId", value);
    }


    @Accessor(qualifier = "mvnVersion", type = Accessor.Type.SETTER)
    public void setMvnVersion(String value)
    {
        getPersistenceContext().setPropertyValue("mvnVersion", value);
    }
}

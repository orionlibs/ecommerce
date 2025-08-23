package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.bootstrap.ddl.dbtypesystem.Attribute;
import de.hybris.bootstrap.ddl.dbtypesystem.DbTypeSystem;
import de.hybris.bootstrap.ddl.dbtypesystem.Deployment;
import de.hybris.bootstrap.ddl.dbtypesystem.Type;
import java.util.Collection;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;

@UnitTest
public class DbTypeSystemImplTest
{
    private DbTypeSystem typeSystem;


    @Before
    public void prepare()
    {
        this.typeSystem = (DbTypeSystem)new DbTypeSystemImpl((RowsProvider)new TestTypeSystemRows());
        ((DbTypeSystemImpl)this.typeSystem).initialize();
    }


    @Test
    public void shouldBeAbleToFindExistingTypeByCode()
    {
        Type type = this.typeSystem.findTypeByCode("Product");
        assertThatType(type).hasCode("Product");
    }


    @Test
    public void shouldBeAbleToFindExistingTypeByPk()
    {
        Type type = this.typeSystem.findTypeByPK(1900626L);
        assertThatType(type).hasCode("Product");
    }


    @Test
    public void shouldNotBeAbleToFindNotExistingTypeByName()
    {
        Type type = this.typeSystem.findTypeByCode("NotExistingType");
        assertThatType(type).isNull();
    }


    @Test
    public void shouldNotBeAbleToFindNotExistingTypeByPk()
    {
        Type type = this.typeSystem.findTypeByPK(1234554321L);
        assertThatType(type).isNull();
    }


    @Test
    public void shouldBeAbleToFindRootType()
    {
        Type rootType = this.typeSystem.findTypeByCode("Item");
        assertThatType(rootType).isRootType();
    }


    @Test
    public void shouldBeAbleToFindNotRootType()
    {
        Type notRootType = this.typeSystem.findTypeByCode("Product");
        assertThatType(notRootType).isNotRootType();
    }


    @Test
    public void shouldBeAbleToGoUpIntoInheritanceHierarchy()
    {
        Type type = this.typeSystem.findTypeByCode("Product");
        assertThatType(type)
                        .isDirectSubtypeOf("GenericItem")
                        .isDirectSubtypeOf("LocalizableItem")
                        .isDirectSubtypeOf("ExtensibleItem")
                        .isDirectSubtypeOf("Item")
                        .isRootType();
    }


    @Test
    public void shouldBeAbleToFindDeploymentByFullName()
    {
        String packageName = "de.hybris.platform.persistence";
        String deploymentName = "core_Product";
        String fullName = "de.hybris.platform.persistence.core_Product";
        Deployment deployment = this.typeSystem.findDeploymentByFullName("de.hybris.platform.persistence.core_Product");
        assertThatDeployment(deployment)
                        .exists()
                        .hasFullName("de.hybris.platform.persistence.core_Product");
    }


    @Test
    public void shouldBeAbleToFindDeploymentForType()
    {
        Type type = this.typeSystem.findTypeByCode("Product");
        String packageName = "de.hybris.platform.persistence";
        String deploymentName = "core_Product";
        String fullName = "de.hybris.platform.persistence.core_Product";
        Deployment deployment = type.getDeployment();
        assertThatDeployment(deployment)
                        .exists()
                        .hasFullName(type.getItemJndiName())
                        .hasFullName("de.hybris.platform.persistence.core_Product");
    }


    @Test
    public void shouldNotBeAbleToFindNotExistingDeployment()
    {
        Deployment deployment = this.typeSystem.findDeploymentByFullName("not.existing.Deployment");
        assertThatDeployment(deployment).notExist();
    }


    @Test
    public void shouldFindAttributeByPk()
    {
        Attribute attribute = this.typeSystem.findAttributeByPk(296910935L);
        assertThatAttribute(attribute)
                        .hasQualifier("catalog")
                        .isEnclosedInType("Product");
    }


    @Test
    public void shouldNotFindAttributeForNotExistingPk()
    {
        Attribute attribute = this.typeSystem.findAttributeByPk(1234554321L);
        assertThatAttribute(attribute).isNull();
    }


    @Test
    public void shouldFindAttributesForType()
    {
        Type type = this.typeSystem.findTypeByCode("Product");
        Collection<Attribute> attributes = type.getAttributes();
        Assertions.assertThat(attributes).isNotEmpty().has((Condition)new Object(this));
    }


    @Test
    public void typeShouldContainAttributesFromSuperType()
    {
        Type type = this.typeSystem.findTypeByCode("Product");
        Collection<Attribute> attributes = type.getAttributes();
        Collection<Attribute> superAttributes = type.getSuperType().getAttributes();
        Object object = new Object(this);
        ImmutableSet<String> qualifiers = FluentIterable.from(attributes).transform((Function)object).toSet();
        ImmutableSet<String> superQualifiers = FluentIterable.from(superAttributes).transform((Function)object).toSet();
        Assertions.assertThat(qualifiers.containsAll((Collection)superQualifiers)).isTrue();
    }


    private static TypeAssert assertThatType(Type type)
    {
        return new TypeAssert(type);
    }


    private static DeploymentAssert assertThatDeployment(Deployment deployment)
    {
        return new DeploymentAssert(deployment);
    }


    private static AttributeAssert assertThatAttribute(Attribute attribute)
    {
        return new AttributeAssert(attribute);
    }
}

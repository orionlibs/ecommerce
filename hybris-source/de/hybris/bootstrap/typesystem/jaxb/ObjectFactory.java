package de.hybris.bootstrap.typesystem.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory
{
    private static final QName _RelationTypeTargetElement_QNAME = new QName("", "targetElement");
    private static final QName _RelationTypeDescription_QNAME = new QName("", "description");
    private static final QName _RelationTypeDeployment_QNAME = new QName("", "deployment");
    private static final QName _RelationTypeSourceElement_QNAME = new QName("", "sourceElement");
    private static final QName _ItemtypeTypeModel_QNAME = new QName("", "model");
    private static final QName _ItemtypeTypeCustomProperties_QNAME = new QName("", "custom-properties");
    private static final QName _ItemtypeTypeIndexes_QNAME = new QName("", "indexes");
    private static final QName _ItemtypeTypeAttributes_QNAME = new QName("", "attributes");


    public ColumntypeType createColumntypeType()
    {
        return new ColumntypeType();
    }


    public RelationType createRelationType()
    {
        return new RelationType();
    }


    public AttributeType createAttributeType()
    {
        return new AttributeType();
    }


    public CustomPropertyType createCustomPropertyType()
    {
        return new CustomPropertyType();
    }


    public ItemtypeType createItemtypeType()
    {
        return new ItemtypeType();
    }


    public MaptypeType createMaptypeType()
    {
        return new MaptypeType();
    }


    public ItemtypesType createItemtypesType()
    {
        return new ItemtypesType();
    }


    public PersistenceType createPersistenceType()
    {
        return new PersistenceType();
    }


    public Items createItems()
    {
        return new Items();
    }


    public CustomPropertiesType createCustomPropertiesType()
    {
        return new CustomPropertiesType();
    }


    public ModifiersType createModifiersType()
    {
        return new ModifiersType();
    }


    public TypeGroupType createTypeGroupType()
    {
        return new TypeGroupType();
    }


    public RelationsType createRelationsType()
    {
        return new RelationsType();
    }


    public ItemModelType createItemModelType()
    {
        return new ItemModelType();
    }


    public EnumValueType createEnumValueType()
    {
        return new EnumValueType();
    }


    public CollectiontypeType createCollectiontypeType()
    {
        return new CollectiontypeType();
    }


    public IndexKeyType createIndexKeyType()
    {
        return new IndexKeyType();
    }


    public EnumModelType createEnumModelType()
    {
        return new EnumModelType();
    }


    public IndexType createIndexType()
    {
        return new IndexType();
    }


    public IndexesType createIndexesType()
    {
        return new IndexesType();
    }


    public EnumtypesType createEnumtypesType()
    {
        return new EnumtypesType();
    }


    public AtomictypesType createAtomictypesType()
    {
        return new AtomictypesType();
    }


    public AtomictypeType createAtomictypeType()
    {
        return new AtomictypeType();
    }


    public MaptypesType createMaptypesType()
    {
        return new MaptypesType();
    }


    public ModelConstructorType createModelConstructorType()
    {
        return new ModelConstructorType();
    }


    public AttributeModelType createAttributeModelType()
    {
        return new AttributeModelType();
    }


    public ValueType createValueType()
    {
        return new ValueType();
    }


    public RelationElementType createRelationElementType()
    {
        return new RelationElementType();
    }


    public DeploymentType createDeploymentType()
    {
        return new DeploymentType();
    }


    public EnumtypeType createEnumtypeType()
    {
        return new EnumtypeType();
    }


    public ModelMethodType createModelMethodType()
    {
        return new ModelMethodType();
    }


    public CollectiontypesType createCollectiontypesType()
    {
        return new CollectiontypesType();
    }


    public AttributesType createAttributesType()
    {
        return new AttributesType();
    }


    @XmlElementDecl(namespace = "", name = "targetElement", scope = RelationType.class)
    public JAXBElement<RelationElementType> createRelationTypeTargetElement(RelationElementType value)
    {
        return new JAXBElement(_RelationTypeTargetElement_QNAME, RelationElementType.class, RelationType.class, value);
    }


    @XmlElementDecl(namespace = "", name = "description", scope = RelationType.class)
    public JAXBElement<String> createRelationTypeDescription(String value)
    {
        return new JAXBElement(_RelationTypeDescription_QNAME, String.class, RelationType.class, value);
    }


    @XmlElementDecl(namespace = "", name = "deployment", scope = RelationType.class)
    public JAXBElement<DeploymentType> createRelationTypeDeployment(DeploymentType value)
    {
        return new JAXBElement(_RelationTypeDeployment_QNAME, DeploymentType.class, RelationType.class, value);
    }


    @XmlElementDecl(namespace = "", name = "sourceElement", scope = RelationType.class)
    public JAXBElement<RelationElementType> createRelationTypeSourceElement(RelationElementType value)
    {
        return new JAXBElement(_RelationTypeSourceElement_QNAME, RelationElementType.class, RelationType.class, value);
    }


    @XmlElementDecl(namespace = "", name = "model", scope = ItemtypeType.class)
    public JAXBElement<ItemModelType> createItemtypeTypeModel(ItemModelType value)
    {
        return new JAXBElement(_ItemtypeTypeModel_QNAME, ItemModelType.class, ItemtypeType.class, value);
    }


    @XmlElementDecl(namespace = "", name = "custom-properties", scope = ItemtypeType.class)
    public JAXBElement<CustomPropertiesType> createItemtypeTypeCustomProperties(CustomPropertiesType value)
    {
        return new JAXBElement(_ItemtypeTypeCustomProperties_QNAME, CustomPropertiesType.class, ItemtypeType.class, value);
    }


    @XmlElementDecl(namespace = "", name = "description", scope = ItemtypeType.class)
    public JAXBElement<String> createItemtypeTypeDescription(String value)
    {
        return new JAXBElement(_RelationTypeDescription_QNAME, String.class, ItemtypeType.class, value);
    }


    @XmlElementDecl(namespace = "", name = "indexes", scope = ItemtypeType.class)
    public JAXBElement<IndexesType> createItemtypeTypeIndexes(IndexesType value)
    {
        return new JAXBElement(_ItemtypeTypeIndexes_QNAME, IndexesType.class, ItemtypeType.class, value);
    }


    @XmlElementDecl(namespace = "", name = "deployment", scope = ItemtypeType.class)
    public JAXBElement<DeploymentType> createItemtypeTypeDeployment(DeploymentType value)
    {
        return new JAXBElement(_RelationTypeDeployment_QNAME, DeploymentType.class, ItemtypeType.class, value);
    }


    @XmlElementDecl(namespace = "", name = "attributes", scope = ItemtypeType.class)
    public JAXBElement<AttributesType> createItemtypeTypeAttributes(AttributesType value)
    {
        return new JAXBElement(_ItemtypeTypeAttributes_QNAME, AttributesType.class, ItemtypeType.class, value);
    }
}

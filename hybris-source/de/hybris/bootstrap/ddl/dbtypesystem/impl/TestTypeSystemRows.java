package de.hybris.bootstrap.ddl.dbtypesystem.impl;

import java.util.Collections;
import java.util.LinkedList;

public class TestTypeSystemRows implements RowsProvider
{
    private final LinkedList<DeploymentRow> deployments = new LinkedList<>();
    private final LinkedList<TypeRow> types = new LinkedList<>();
    private final LinkedList<AttributeRow> attributes = new LinkedList<>();


    public TestTypeSystemRows()
    {
        addDeployments();
        addTypes();
        addAttributes();
    }


    public Iterable<DeploymentRow> getDeploymentRows()
    {
        return this.deployments;
    }


    public Iterable<TypeRow> getTypeRows()
    {
        return this.types;
    }


    public Iterable<AttributeRow> getAttributeRows()
    {
        return this.attributes;
    }


    public Iterable<AtomicTypeRow> getAtomicTypeRows()
    {
        return Collections.EMPTY_LIST;
    }


    public Iterable<CollectionTypeRow> getCollectionTypeRow()
    {
        return Collections.EMPTY_LIST;
    }


    public Iterable<MapTypeRow> getMapTypeRows()
    {
        return Collections.EMPTY_LIST;
    }


    public Iterable<EnumerationValueRow> getEnumerationValueRows()
    {
        return Collections.EMPTY_LIST;
    }


    public Iterable<NumberSeriesRow> getNumberSeriesRows()
    {
        return Collections.EMPTY_LIST;
    }


    public Iterable<PropsRow> getPropsRows()
    {
        return Collections.EMPTY_LIST;
    }


    private void addDeployments()
    {
        this.deployments.add(new Object(this));
        this.deployments.add(new Object(this));
        this.deployments.add(new Object(this));
        this.deployments.add(new Object(this));
        this.deployments.add(new Object(this));
        this.deployments.add(new Object(this));
        this.deployments.add(new Object(this));
        this.deployments.add(new Object(this));
    }


    private void addTypes()
    {
        this.types.add(new Object(this));
        this.types.add(new Object(this));
        this.types.add(new Object(this));
        this.types.add(new Object(this));
        this.types.add(new Object(this));
        this.types.add(new Object(this));
        this.types.add(new Object(this));
        this.types.add(new Object(this));
    }


    private Long getExtensibleItemTypeCodePK()
    {
        return new Long(65618L);
    }


    private Long getTypeTypeCodePK()
    {
        return new Long(196690L);
    }


    private Long getComposedTypeTypeCodePK()
    {
        return new Long(229458L);
    }


    private Long getTypeManagerManagedTypeCodePK()
    {
        return new Long(163922L);
    }


    private Long getItemTypeCodePK()
    {
        return new Long(32850L);
    }


    private Long getLocalizedItemTypeCodePK()
    {
        return new Long(98386L);
    }


    private Long getGenericItemTypeCodePK()
    {
        return new Long(131154L);
    }


    private Long getProductTypeCodePK()
    {
        return new Long(1900626L);
    }


    private Boolean isSystemType()
    {
        return Boolean.TRUE;
    }


    private Boolean isTypeAutoCreated()
    {
        return Boolean.TRUE;
    }


    private Boolean isPropertyTableStatus()
    {
        return Boolean.TRUE;
    }


    private Boolean isTypeSingleton()
    {
        return Boolean.FALSE;
    }


    private Boolean isTypeRemovable()
    {
        return Boolean.FALSE;
    }


    private void addAttributes()
    {
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
        this.attributes.add(new Object(this));
    }


    Boolean isOrdered()
    {
        return Boolean.TRUE;
    }


    Boolean isNotOrdered()
    {
        return Boolean.FALSE;
    }


    Boolean isUnique()
    {
        return Boolean.TRUE;
    }


    Boolean isNotSource()
    {
        return Boolean.FALSE;
    }


    Boolean isSource()
    {
        return Boolean.TRUE;
    }


    Boolean isGenerate()
    {
        return Boolean.TRUE;
    }


    Boolean isAutoCreate()
    {
        return Boolean.TRUE;
    }


    Boolean isAttributeHidden()
    {
        return Boolean.TRUE;
    }


    Boolean isAttributeShown()
    {
        return Boolean.FALSE;
    }


    Boolean isAttributeNotProperty()
    {
        return Boolean.FALSE;
    }


    Boolean isAttributeProperty()
    {
        return Boolean.TRUE;
    }
}

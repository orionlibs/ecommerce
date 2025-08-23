package de.hybris.bootstrap.ddl.dbtypesystem.impl;

public interface RowsProvider
{
    Iterable<DeploymentRow> getDeploymentRows();


    Iterable<TypeRow> getTypeRows();


    Iterable<AttributeRow> getAttributeRows();


    Iterable<AtomicTypeRow> getAtomicTypeRows();


    Iterable<CollectionTypeRow> getCollectionTypeRow();


    Iterable<MapTypeRow> getMapTypeRows();


    Iterable<EnumerationValueRow> getEnumerationValueRows();


    Iterable<NumberSeriesRow> getNumberSeriesRows();


    Iterable<PropsRow> getPropsRows();
}

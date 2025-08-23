/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.odata2services.odata.processor.reader;

import static de.hybris.platform.odata2services.odata.persistence.ConversionOptions.conversionOptionsBuilder;

import com.google.common.base.Preconditions;
import de.hybris.platform.odata2services.odata.persistence.ConversionOptions;
import de.hybris.platform.odata2services.odata.persistence.ItemLookupRequest;
import de.hybris.platform.odata2services.odata.processor.ExpandedEntity;
import de.hybris.platform.odata2services.odata.processor.NavigationSegmentExplorer;
import java.util.List;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.edm.EdmMultiplicity;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.uri.KeyPredicate;
import org.apache.olingo.odata2.api.uri.NavigationSegment;
import org.apache.olingo.odata2.api.uri.UriInfo;
import org.springframework.beans.factory.annotation.Required;

/**
 * The SingleEntityReader reads from the commerce suite a single entity.
 * The entity is selected by the provided key. The entity can also be
 * a navigation property which has a single multiplicity.
 */
public class SingleEntityReader extends AbstractEntityReader
{
    private NavigationSegmentExplorer navigationSegmentExplorer;


    @Override
    public boolean isApplicable(final UriInfo uriInfo)
    {
        final List<KeyPredicate> keyPredicates = uriInfo.getKeyPredicates();
        final List<NavigationSegment> navigationSegments = uriInfo.getNavigationSegments();
        Preconditions.checkArgument(keyPredicates != null, "Key predicates can't be null when reading entity");
        Preconditions.checkArgument(navigationSegments != null, "Navigation segments can't be null when reading entity");
        return !keyPredicates.isEmpty() &&
                        (navigationSegments.isEmpty() || isSingleMultiplicity(navigationSegments));
    }


    @Override
    public ODataResponse read(final ItemLookupRequest itemLookupRequest) throws ODataException
    {
        validateReadPermission(itemLookupRequest);
        final ConversionOptions options = conversionOptionsBuilder()
                        .withNavigationSegments(itemLookupRequest.getNavigationSegments())
                        .withExpand(itemLookupRequest.getExpand())
                        .build();
        final ODataEntry entry = getPersistenceService().getEntityData(itemLookupRequest, options);
        final ExpandedEntity expandedEntity = getNavigationSegmentExplorer().expandForSingleEntity(itemLookupRequest, entry);
        return getResponseWriter().write(itemLookupRequest, expandedEntity.getEdmEntitySet(), expandedEntity.getODataEntry().getProperties());
    }


    private boolean isSingleMultiplicity(final List<NavigationSegment> navigationSegments)
    {
        try
        {
            final EdmMultiplicity multiplicity = navigationSegments.get(0)
                            .getNavigationProperty()
                            .getMultiplicity();
            return EdmMultiplicity.ZERO_TO_ONE == multiplicity || EdmMultiplicity.ONE == multiplicity;
        }
        catch(final EdmException e)
        {
            return handleAssociationMultiplicityRetrievalError(e);
        }
    }


    protected NavigationSegmentExplorer getNavigationSegmentExplorer()
    {
        return navigationSegmentExplorer;
    }


    @Required
    public void setNavigationSegmentExplorer(final NavigationSegmentExplorer navigationSegmentExplorer)
    {
        this.navigationSegmentExplorer = navigationSegmentExplorer;
    }
}

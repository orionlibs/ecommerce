/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.dnd;

import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DropCompositeHandler implements DropHandler<Object, Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(DropCompositeHandler.class);
    private TypeFacade typeFacade;
    protected Map<String, DropHandler> handlers;


    @Override
    public List<DropOperationData<Object, Object, Object>> handleDrop(final List<Object> dragged, final Object target,
                    final DragAndDropContext context)
    {
        final List<DropOperationData<Object, Object, Object>> dropOperationData = new ArrayList<>();
        dragged.stream().collect(Collectors.groupingBy(element -> typeFacade.getType(element)))
                        .forEach((key, value) -> findHandler(key)
                                        .ifPresent(handler -> dropOperationData.addAll(handler.handleDrop(value, target, context))));
        return dropOperationData;
    }


    protected Optional<DropHandler> findHandler(final String dataType)
    {
        Optional<DataType> foundDataType = loadDataType(dataType);
        while(foundDataType.isPresent())
        {
            final DropHandler dropHandler = handlers.get(foundDataType.get().getCode());
            if(dropHandler != null)
            {
                return Optional.of(dropHandler);
            }
            foundDataType = loadDataType(foundDataType.get().getSuperType());
        }
        return Optional.empty();
    }


    protected Optional<DataType> loadDataType(final String qualifier)
    {
        try
        {
            final DataType foundDataType = typeFacade.load(qualifier);
            return foundDataType != null ? Optional.of(foundDataType) : Optional.empty();
        }
        catch(final TypeNotFoundException e)
        {
            LOG.warn("Unable to load type", e);
        }
        return Optional.empty();
    }


    @Override
    public List<String> findSupportedTypes()
    {
        final Stream<List> supportedTypesStream = handlers.values().stream().map(DropHandler::findSupportedTypes);
        return supportedTypesStream.flatMap((Function<List, Stream<String>>)Collection::stream).distinct().collect(Collectors.toList());
    }


    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    public void setHandlers(final Map<String, DropHandler> handlers)
    {
        this.handlers = handlers;
    }
}

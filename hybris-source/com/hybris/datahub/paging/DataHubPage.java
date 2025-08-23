package com.hybris.datahub.paging;

import java.util.List;
import java.util.function.Consumer;
import javax.validation.constraints.NotNull;

public interface DataHubPage<T>
{
    @NotNull
    List<T> getContent();


    int getNumber();


    int getNumberOfElements();


    int getSize();


    long getTotalElements();


    int getTotalPages();


    boolean isEmpty();


    boolean isFull();


    void forEach(Consumer<T> paramConsumer);
}

package de.hybris.platform.webservicescommons.mapping;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DataMapper
{
    public static final String FIELD_SET_NAME = "FIELD_SET_NAME";
    public static final String MAP_NULLS = "MAP_NULLS";
    public static final String FIELD_PREFIX = "destination";


    <S, D> D map(S paramS, Class<D> paramClass);


    <S, D> D map(S paramS, Class<D> paramClass, String paramString);


    <S, D> D map(S paramS, Class<D> paramClass, Set<String> paramSet);


    <S, D> void map(S paramS, D paramD);


    <S, D> void map(S paramS, D paramD, boolean paramBoolean);


    <S, D> void map(S paramS, D paramD, String paramString);


    <S, D> void map(S paramS, D paramD, String paramString, boolean paramBoolean);


    <S, D> Set<D> mapAsSet(Iterable<S> paramIterable, Class<D> paramClass, String paramString);


    <S, D> List<D> mapAsList(Iterable<S> paramIterable, Class<D> paramClass, String paramString);


    <S, D> void mapAsCollection(Iterable<S> paramIterable, Collection<D> paramCollection, Class<D> paramClass, String paramString);


    <S, D> void mapGeneric(S paramS, D paramD, Type[] paramArrayOfType1, Type[] paramArrayOfType2, String paramString, Map<String, Class> paramMap);
}

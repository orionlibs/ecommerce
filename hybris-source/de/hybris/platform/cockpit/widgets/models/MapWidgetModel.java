package de.hybris.platform.cockpit.widgets.models;

import java.util.Map;

public interface MapWidgetModel<K, V> extends WidgetModel
{
    boolean put(K paramK, V paramV);


    V get(K paramK);


    Map<K, V> getMap();


    V remove(K paramK);


    boolean setMap(Map<K, V> paramMap);
}

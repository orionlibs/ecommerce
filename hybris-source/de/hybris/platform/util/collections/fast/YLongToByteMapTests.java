package de.hybris.platform.util.collections.fast;

import de.hybris.bootstrap.annotations.UnitTest;
import org.junit.Assert;
import org.junit.Test;

@UnitTest
public class YLongToByteMapTests
{
    @Test
    public void shouldBeAbleToCreateByDefaultConstructor()
    {
        new YLongToByteMap();
    }


    @Test
    public void shouldBeAbleToCreateWithInitialCapacity()
    {
        new YLongToByteMap(123);
    }


    @Test
    public void shouldBeAbleToPutValuesForTheSameKey()
    {
        YLongToByteMap map = new YLongToByteMap();
        map.put(10L, (byte)64);
        map.put(10L, -128);
    }


    @Test
    public void shouldNotFailOnGettingNonExistingKey()
    {
        YLongToByteMap map = new YLongToByteMap();
        map.get(12L);
    }


    @Test
    public void shouldBeAbleToGetExistingValue()
    {
        YLongToByteMap map = new YLongToByteMap();
        byte expected = 123;
        map.put(12L, (byte)123);
        Assert.assertEquals(123L, map.get(12L));
    }


    @Test
    public void shouldReturnEmptyValueWhenGettingNotExistingValue()
    {
        YLongToByteMap map = new YLongToByteMap();
        Assert.assertEquals(map.getEmptyValue(), map.get(12L));
    }


    @Test
    public void shouldReturnEmptyValueWhenPuttingKeyForTheFirstTime()
    {
        YLongToByteMap map = new YLongToByteMap();
        Assert.assertEquals(map.getEmptyValue(), map.put(12L, (byte)42));
    }


    @Test
    public void shouldReturnPreviousValueWhenExistingKeyIsBeeingReplaced()
    {
        YLongToByteMap map = new YLongToByteMap();
        byte expected = 123;
        map.put(12L, (byte)123);
        Assert.assertEquals(123L, map.put(12L, (byte)-46));
    }


    @Test
    public void shouldBeAbleToPutEmptyValue()
    {
        YLongToByteMap map = new YLongToByteMap();
        map.put(12L, map.getEmptyValue());
    }
}

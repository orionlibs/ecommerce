package de.hybris.platform.util.collections.fast;

import de.hybris.bootstrap.annotations.UnitTest;
import org.junit.Assert;
import org.junit.Test;

@UnitTest
public class YLongToIntMapTests
{
    @Test
    public void shouldBeAbleToCreateByDefaultConstructor()
    {
        new YLongToIntMap();
    }


    @Test
    public void shouldBeAbleToCreateWithInitialCapacity()
    {
        new YLongToIntMap(123);
    }


    @Test
    public void shouldBeAbleToPutValuesForTheSameKey()
    {
        YLongToIntMap map = new YLongToIntMap();
        map.put(10L, 64);
        map.put(10L, 128);
    }


    @Test
    public void shouldNotFailOnGettingNonExistingKey()
    {
        YLongToIntMap map = new YLongToIntMap();
        map.get(12L);
    }


    @Test
    public void shouldBeAbleToGetExistingValue()
    {
        YLongToIntMap map = new YLongToIntMap();
        int expected = 123;
        map.put(12L, 123);
        Assert.assertEquals(123L, map.get(12L));
    }


    @Test
    public void shouldReturnEmptyValueWhenGettingNotExistingValue()
    {
        YLongToIntMap map = new YLongToIntMap();
        Assert.assertEquals(map.getEmptyValue(), map.get(12L));
    }


    @Test
    public void shouldReturnEmptyValueWhenPuttingKeyForTheFirstTime()
    {
        YLongToIntMap map = new YLongToIntMap();
        Assert.assertEquals(map.getEmptyValue(), map.put(12L, 42));
    }


    @Test
    public void shouldReturnPreviousValueWhenExistingKeyIsBeeingReplaced()
    {
        YLongToIntMap map = new YLongToIntMap();
        int expected = 123;
        map.put(12L, 123);
        Assert.assertEquals(123L, map.put(12L, 210));
    }


    @Test
    public void shouldReturnEmptyValueWhenRemovingNotExistingKey()
    {
        YLongToIntMap map = new YLongToIntMap();
        Assert.assertEquals(map.getEmptyValue(), map.remove(12L));
    }


    @Test
    public void shouldReturnPreviousValueWhenRemovingExistingKey()
    {
        YLongToIntMap map = new YLongToIntMap();
        int expected = 1234;
        map.put(12L, 1234);
        Assert.assertEquals(1234L, map.remove(12L));
    }


    @Test
    public void shouldBeAbleToPutEmptyValue()
    {
        YLongToIntMap map = new YLongToIntMap();
        map.put(12L, map.getEmptyValue());
    }
}

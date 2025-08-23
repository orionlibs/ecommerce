package de.hybris.platform.util.collections.fast;

import de.hybris.bootstrap.annotations.UnitTest;
import org.junit.Assert;
import org.junit.Test;

@UnitTest
public class YLongToLongMapTests
{
    @Test
    public void shouldBeAbleToCreateByDefaultConstructor()
    {
        new YLongToLongMap();
    }


    @Test
    public void shouldBeAbleToPutValuesForTheSameKey()
    {
        YLongToLongMap map = new YLongToLongMap();
        map.put(10L, 64L);
        map.put(10L, 128L);
    }


    @Test
    public void shouldNotFailOnGettingNonExistingKey()
    {
        YLongToLongMap map = new YLongToLongMap();
        map.get(12L);
    }


    @Test
    public void shouldBeAbleToGetExistingValue()
    {
        YLongToLongMap map = new YLongToLongMap();
        int expected = 123;
        map.put(12L, 123L);
        Assert.assertEquals(123L, map.get(12L));
    }


    @Test
    public void shouldReturnEmptyValueWhenGettingNotExistingValue()
    {
        YLongToLongMap map = new YLongToLongMap();
        Assert.assertEquals(map.getEmptyValue(), map.get(12L));
    }


    @Test
    public void shouldReturnEmptyValueWhenPuttingKeyForTheFirstTime()
    {
        YLongToLongMap map = new YLongToLongMap();
        Assert.assertEquals(map.getEmptyValue(), map.put(12L, 42L));
    }


    @Test
    public void shouldReturnPreviousValueWhenExistingKeyIsBeingReplaced()
    {
        YLongToLongMap map = new YLongToLongMap();
        int expected = 123;
        map.put(12L, 123L);
        Assert.assertEquals(123L, map.put(12L, 210L));
    }


    @Test
    public void shouldNotContainNotExistingKey()
    {
        YLongToLongMap map = new YLongToLongMap();
        Assert.assertFalse(map.containsKey(12L));
    }


    @Test
    public void shouldContainExistingKey()
    {
        YLongToLongMap map = new YLongToLongMap();
        map.put(12L, 123L);
        Assert.assertTrue(map.containsKey(12L));
    }


    @Test
    public void shouldBeAbleToPutEmptyValue()
    {
        YLongToLongMap map = new YLongToLongMap();
        map.put(12L, map.getEmptyValue());
    }
}

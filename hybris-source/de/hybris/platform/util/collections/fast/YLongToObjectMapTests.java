package de.hybris.platform.util.collections.fast;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.util.collections.fast.procedures.YLongAndObjectProcedure;
import de.hybris.platform.util.collections.fast.procedures.YLongProcedure;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

@UnitTest
public class YLongToObjectMapTests
{
    @Test
    public void shouldBeAbleToCreateByDefaultConstructor()
    {
        new YLongToObjectMap();
    }


    @Test
    public void shouldBeAbleToCreateWithInitialCapacity()
    {
        new YLongToObjectMap(1234);
    }


    @Test
    public void shouldBeEmptyAfterClear()
    {
        YLongToObjectMap<Object> map = new YLongToObjectMap();
        Assert.assertTrue(map.isEmpty());
        map.put(12L, new Object());
        Assert.assertFalse(map.isEmpty());
        map.clear();
        Assert.assertTrue(map.isEmpty());
    }


    @Test
    public void shouldBeAbleToPutValuesForTheSameKey()
    {
        YLongToObjectMap<Object> map = new YLongToObjectMap();
        map.put(10L, new Object());
        map.put(10L, new Object());
    }


    @Test
    public void shouldNotFailOnGettingNonExistingKey()
    {
        YLongToObjectMap<Object> map = new YLongToObjectMap();
        map.get(12L);
    }


    @Test
    public void shouldBeAbleToGetExistingValue()
    {
        YLongToObjectMap<Object> map = new YLongToObjectMap();
        Object expected = new Object();
        map.put(12L, expected);
        Assert.assertTrue((expected == map.get(12L)));
    }


    @Test
    public void shouldReturnNullWhenGettingNotExistingValue()
    {
        YLongToObjectMap<Object> map = new YLongToObjectMap();
        Assert.assertNull(map.get(12L));
    }


    @Test
    public void shouldReturnNullWhenPuttingKeyForTheFirstTime()
    {
        YLongToObjectMap<Object> map = new YLongToObjectMap();
        Assert.assertNull(map.put(12L, new Object()));
    }


    @Test
    public void shouldReturnPreviousValueWhenExistingKeyIsBeingReplaced()
    {
        YLongToObjectMap<Object> map = new YLongToObjectMap();
        Object expected = new Object();
        map.put(12L, expected);
        Assert.assertTrue((expected == map.put(12L, new Object())));
    }


    @Test
    public void shouldReturnNullWhenRemovingNotExistingKey()
    {
        YLongToObjectMap<Object> map = new YLongToObjectMap();
        Assert.assertNull(map.remove(12L));
    }


    @Test
    public void shouldReturnPreviousValueWhenRemovingExistingKey()
    {
        YLongToObjectMap<Object> map = new YLongToObjectMap();
        Object expected = new Object();
        map.put(12L, expected);
        Assert.assertTrue((expected == map.remove(12L)));
    }


    @Test
    public void shouldBeAbleToPutNullValue()
    {
        YLongToObjectMap<Object> map = new YLongToObjectMap();
        map.put(12L, null);
    }


    @Test
    public void sizeShouldBeZeroWhenSetIsEmpty()
    {
        Assert.assertEquals(0L, (new YLongToObjectMap()).size());
    }


    @Test
    public void sizeShouldBeProperWhenSetIsNotEmpty()
    {
        YLongToObjectMap<Object> map = new YLongToObjectMap();
        map.put(12L, new Object());
        Assert.assertEquals(1L, map.size());
        map.put(12L, new Object());
        Assert.assertEquals(1L, map.size());
        map.put(13L, new Object());
        Assert.assertEquals(2L, map.size());
        map.remove(12L);
        Assert.assertEquals(1L, map.size());
        map.clear();
        Assert.assertEquals(0L, map.size());
    }


    @Test
    public void shouldBeEmptyWhenThereAreNoElements()
    {
        Assert.assertTrue((new YLongToObjectMap()).isEmpty());
    }


    @Test
    public void shouldNotBeEmptyWhenTherIsAnyElement()
    {
        YLongToObjectMap<Object> map = new YLongToObjectMap();
        map.put(12L, new Object());
        Assert.assertFalse(map.isEmpty());
        map.put(12L, new Object());
        Assert.assertFalse(map.isEmpty());
    }


    @Test
    public void shouldContainsExistingKey()
    {
        YLongToObjectMap<Object> map = new YLongToObjectMap();
        Assert.assertFalse(map.containsKey(12L));
        map.put(12L, new Object());
        Assert.assertTrue(map.containsKey(12L));
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNullEntryProcedureHasBeenPassed()
    {
        (new YLongToObjectMap()).forEachEntry(null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenNullKeyProcedureHasBeenPassed()
    {
        (new YLongToObjectMap()).forEachKey(null);
    }


    @Test
    public void procedureShouldNotBeCalledForMapWithoutEntries()
    {
        YLongAndObjectProcedure procedure = (YLongAndObjectProcedure)Mockito.mock(YLongAndObjectProcedure.class);
        (new YLongToObjectMap()).forEachEntry(procedure);
        ((YLongAndObjectProcedure)Mockito.verify(procedure, Mockito.never())).execute(ArgumentMatchers.anyLong(), ArgumentMatchers.anyObject());
    }


    @Test
    public void procedureShouldNotBeCalledForMapWithoutKeys()
    {
        YLongProcedure procedure = (YLongProcedure)Mockito.mock(YLongProcedure.class);
        (new YLongToObjectMap()).forEachKey(procedure);
        ((YLongProcedure)Mockito.verify(procedure, Mockito.never())).execute(ArgumentMatchers.anyLong());
    }


    @Test
    public void procedureShouldBeCalledForMapWithEntries()
    {
        YLongAndObjectProcedure procedure = (YLongAndObjectProcedure)Mockito.mock(YLongAndObjectProcedure.class);
        YLongToObjectMap<Object> map = new YLongToObjectMap();
        Object obj1 = new Object();
        Object obj2 = new Object();
        map.put(1L, obj1);
        map.put(2L, obj2);
        map.forEachEntry(procedure);
        ((YLongAndObjectProcedure)Mockito.verify(procedure, Mockito.times(2))).execute(ArgumentMatchers.anyLong(), ArgumentMatchers.anyObject());
        ((YLongAndObjectProcedure)Mockito.verify(procedure, Mockito.times(1))).execute(1L, obj1);
        ((YLongAndObjectProcedure)Mockito.verify(procedure, Mockito.times(1))).execute(2L, obj2);
    }


    @Test
    public void procedureShouldBeCalledForMapWithKeys()
    {
        YLongProcedure procedure = (YLongProcedure)Mockito.mock(YLongProcedure.class);
        YLongToObjectMap<Object> map = new YLongToObjectMap();
        Object obj1 = new Object();
        Object obj2 = new Object();
        map.put(1L, obj1);
        map.put(2L, obj2);
        map.forEachKey(procedure);
        ((YLongProcedure)Mockito.verify(procedure, Mockito.times(2))).execute(ArgumentMatchers.anyLong());
        ((YLongProcedure)Mockito.verify(procedure, Mockito.times(1))).execute(1L);
        ((YLongProcedure)Mockito.verify(procedure, Mockito.times(1))).execute(2L);
    }
}

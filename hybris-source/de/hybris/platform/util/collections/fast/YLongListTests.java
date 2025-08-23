package de.hybris.platform.util.collections.fast;

import de.hybris.bootstrap.annotations.UnitTest;
import org.junit.Assert;
import org.junit.Test;

@UnitTest
public class YLongListTests
{
    @Test
    public void shouldBeAbleToCreateByDefaultConstructor()
    {
        new YLongList();
    }


    @Test
    public void shouldBeAbleToCreateWithInitialCapacity()
    {
        new YLongList(123);
    }


    @Test
    public void shouldBeAbleToAddValues()
    {
        YLongList list = new YLongList();
        list.add(Long.MAX_VALUE);
        list.add(-1L);
        list.add(0L);
        list.add(1L);
        list.add(Long.MAX_VALUE);
    }


    @Test
    public void shouldReturnZeroSizeForEmptyList()
    {
        Assert.assertEquals((new YLongList()).size(), 0L);
        Assert.assertEquals((new YLongList(1234)).size(), 0L);
    }


    @Test
    public void shouldReturnProperSizeOfNonEmptyList()
    {
        YLongList list = new YLongList();
        list.add(0L);
        Assert.assertEquals(1L, list.size());
        list.add(0L);
        Assert.assertEquals(2L, list.size());
    }


    @Test
    public void shouldBeAbleToGetExistingValueByOffset()
    {
        YLongList list = new YLongList();
        list.add(1L);
        Assert.assertEquals(list.get(0), 1L);
        list.add(2L);
        Assert.assertEquals(list.get(0), 1L);
        Assert.assertEquals(list.get(1), 2L);
        list.add(10L);
        Assert.assertEquals(list.get(0), 1L);
        Assert.assertEquals(list.get(1), 2L);
        Assert.assertEquals(list.get(2), 10L);
    }


    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldThrowExceptionWhenAccessingNotExistingOffset()
    {
        YLongList list = new YLongList();
        list.get(0);
    }
}

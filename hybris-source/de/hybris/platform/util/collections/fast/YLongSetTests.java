package de.hybris.platform.util.collections.fast;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.testframework.Assert;
import java.util.Arrays;
import org.junit.Test;

@UnitTest
public class YLongSetTests
{
    @Test
    public void shouldBeAbleToCreateByDefaultConstructor()
    {
        new YLongSet();
    }


    @Test
    public void shouldBeAbleToCreateWithInitialCapacity()
    {
        new YLongSet(123);
    }


    @Test
    public void shouldBeAbleToAddValue()
    {
        (new YLongSet()).add(10L);
    }


    @Test
    public void shouldBeAbleToAddMultipleDistinctValues()
    {
        YLongSet set = new YLongSet();
        set.add(Long.MIN_VALUE);
        set.add(-1L);
        set.add(0L);
        set.add(1L);
        set.add(Long.MAX_VALUE);
    }


    @Test
    public void shouldBeAbleToAddTheSameValueMultipleTimes()
    {
        YLongSet set = new YLongSet();
        set.add(1L);
        set.add(1L);
    }


    @Test
    public void addingNotExistingValueShouldModifySet()
    {
        YLongSet set = new YLongSet();
        Assert.assertTrue(set.add(1L));
        Assert.assertTrue(set.add(2L));
    }


    @Test
    public void addingExistingValueShouldNotModifySet()
    {
        YLongSet set = new YLongSet();
        set.add(1L);
        Assert.assertFalse(set.add(1L));
    }


    @Test
    public void containsShouldReturnTrueForExistingValue()
    {
        YLongSet set = new YLongSet();
        set.add(10L);
        Assert.assertTrue(set.contains(10L));
    }


    @Test
    public void containsShouldReturnFalseForNotExistingValue()
    {
        Assert.assertFalse((new YLongSet()).contains(11L));
    }


    @Test
    public void setWithoutValuesShouldBeEmpty()
    {
        Assert.assertTrue((new YLongSet()).isEmpty());
    }


    @Test
    public void setWithAnyValueShoulNotdBeEmpty()
    {
        YLongSet set = new YLongSet();
        set.add(10L);
        Assert.assertFalse(set.isEmpty());
    }


    @Test
    public void shouldBeAbleToRemoveNotExistingValue()
    {
        (new YLongSet()).remove(10L);
    }


    @Test
    public void shouldBeAbleToRemoveExistingValue()
    {
        YLongSet set = new YLongSet();
        set.add(10L);
        set.remove(10L);
        Assert.assertFalse(set.contains(10L));
    }


    @Test
    public void removingOfNotExistingValueShouldNotModifySet()
    {
        Assert.assertFalse((new YLongSet()).remove(123L));
    }


    @Test
    public void removingOfExistingObjectShouldModifySet()
    {
        YLongSet set = new YLongSet();
        set.add(10L);
        Assert.assertTrue(set.remove(10L));
    }


    @Test
    public void shouldReturnZeroSizeForEmptySet()
    {
        Assert.assertEquals((new YLongSet()).size(), 0L);
        Assert.assertEquals((new YLongSet(1234)).size(), 0L);
    }


    @Test
    public void shouldReturnProperSizeOfNonEmptySet()
    {
        YLongSet list = new YLongSet();
        list.add(0L);
        Assert.assertEquals(1L, list.size());
        list.add(1L);
        Assert.assertEquals(2L, list.size());
    }


    @Test
    public void shouldReturnEmptyArrayForEmptySet()
    {
        Assert.assertEquals(((new YLongSet()).toArray()).length, 0L);
    }


    @Test
    public void shouldReturnArrayWithValues()
    {
        long[] expected = {1L, 2L, 3L, 4L, 5L};
        YLongSet set = new YLongSet();
        for(int i = 0; i < expected.length * 2; i++)
        {
            set.add(expected[(3 + i) % expected.length]);
        }
        long[] result = set.toArray();
        Arrays.sort(result);
        Assert.assertArrayEquals(expected, result);
    }


    @Test
    public void shouldBeAbleToCompareTwoSets()
    {
        YLongSet set1 = new YLongSet();
        YLongSet set2 = new YLongSet();
        long numberOfElements = 5L;
        for(int i = 0; i < 5L; i++)
        {
            set1.add(i);
            set2.add(4L - i);
        }
        Assert.assertEquals(set1, set2);
    }


    @Test
    public void shouldBeAbleToCompareTwoDifferentSets()
    {
        YLongSet set1 = new YLongSet();
        YLongSet set2 = new YLongSet();
        long numberOfElements = 5L;
        for(int i = 0; i < 5L; i++)
        {
            set1.add(i);
            set2.add(5L + i);
        }
        Assert.assertNotEquals(set1, set2);
    }
}

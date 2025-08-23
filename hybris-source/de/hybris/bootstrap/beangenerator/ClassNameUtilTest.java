package de.hybris.bootstrap.beangenerator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.bootstrap.beangenerator.definitions.model.ClassNamePrototype;
import java.util.Arrays;
import java.util.Collections;
import junit.framework.Assert;
import org.junit.Test;

@UnitTest
public class ClassNameUtilTest
{
    @Test
    public void testPrototypeNone()
    {
        ClassNamePrototype proto = ClassNameUtil.toPrototype("java.lang.String");
        Assert.assertEquals("java.lang.String", proto.getBaseClass());
        Assert.assertEquals(Collections.EMPTY_LIST, proto.getPrototypes());
    }


    @Test(expected = IllegalArgumentException.class)
    public void testPrototypeInvalid1()
    {
        ClassNameUtil.toPrototype("de.hybris.simple.Simple<");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testPrototypeInvalid2()
    {
        ClassNameUtil.toPrototype("de.hybris.simple.Simple<Pojo");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testPrototypeInvalid3()
    {
        ClassNameUtil.toPrototype("de.hybris.simple.Simple<Pojo<");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testPrototypeInvalid4()
    {
        ClassNameUtil.toPrototype("de.hybris.simple.Simple>Pojo");
    }


    @Test
    public void testPrototypeSimple()
    {
        ClassNamePrototype proto = ClassNameUtil.toPrototype("de.hybris.simple.Simple<Pojo>");
        Assert.assertEquals("de.hybris.simple.Simple", proto.getBaseClass());
        Assert.assertEquals(Arrays.asList(new ClassNamePrototype[] {new ClassNamePrototype("Pojo", new ClassNamePrototype[0])}), proto.getPrototypes());
    }


    @Test
    public void testPrototypeSimpleWithExtendsGeneric()
    {
        ClassNamePrototype proto = ClassNameUtil.toPrototype("de.hybris.test.data.Common<T extends de.hybris.test.data.beans.holders.HolderData>");
        Assert.assertEquals("de.hybris.test.data.Common", proto.getBaseClass());
        Assert.assertEquals(Arrays.asList(new ClassNamePrototype[] {new ClassNamePrototype("T extends de.hybris.test.data.beans.holders.HolderData", new ClassNamePrototype[0])}), proto
                        .getPrototypes());
    }


    @Test
    public void testPrototypeMulti()
    {
        ClassNamePrototype proto = ClassNameUtil.toPrototype("de.hybris.simple.Simple<Pojo,Dojo>");
        Assert.assertEquals("de.hybris.simple.Simple", proto.getBaseClass());
        Assert.assertEquals(Arrays.asList(new ClassNamePrototype[] {new ClassNamePrototype("Pojo", new ClassNamePrototype[0]), new ClassNamePrototype("Dojo", new ClassNamePrototype[0])}), proto.getPrototypes());
    }


    @Test
    public void testPrototypeNested()
    {
        ClassNamePrototype proto = ClassNameUtil.toPrototype("de.hybris.simple.Simple<Pojo,Dojo<Mojo,Jojo>>");
        Assert.assertEquals("de.hybris.simple.Simple", proto.getBaseClass());
        Assert.assertEquals(Arrays.asList(new ClassNamePrototype[] {new ClassNamePrototype("Pojo", new ClassNamePrototype[0]),
                        new ClassNamePrototype("Dojo", new ClassNamePrototype[] {new ClassNamePrototype("Mojo", new ClassNamePrototype[0]), new ClassNamePrototype("Jojo", new ClassNamePrototype[0])})}), proto
                        .getPrototypes());
    }


    @Test
    public void testPrototypeWicked()
    {
        ClassNamePrototype proto = ClassNameUtil.toPrototype("de.hybris.simple.Simple<A<B,C>,D<E<F>,G<H,I>>>");
        Assert.assertEquals("de.hybris.simple.Simple", proto.getBaseClass());
        Assert.assertEquals(Arrays.asList(new ClassNamePrototype[] {new ClassNamePrototype("A", new ClassNamePrototype[] {new ClassNamePrototype("B", new ClassNamePrototype[0]), new ClassNamePrototype("C", new ClassNamePrototype[0])}), new ClassNamePrototype("D",
                        new ClassNamePrototype[] {new ClassNamePrototype("E", new ClassNamePrototype[] {new ClassNamePrototype("F", new ClassNamePrototype[0])}),
                                        new ClassNamePrototype("G", new ClassNamePrototype[] {new ClassNamePrototype("H", new ClassNamePrototype[0]), new ClassNamePrototype("I", new ClassNamePrototype[0])})})}), proto
                        .getPrototypes());
    }


    @Test
    public void testShortenedClassnameWithExtendsGeneric()
    {
        ClassNamePrototype proto = ClassNameUtil.toPrototype("de.hybris.test.data.Common<T extends de.hybris.test.data.beans.holders.HolderData>");
        String given = ClassNameUtil.getShortClassName(proto);
        Assert.assertEquals("Common<T extends de.hybris.test.data.beans.holders.HolderData>", given);
    }


    @Test
    public void testShortenedClassname()
    {
        ClassNamePrototype proto = ClassNameUtil.toPrototype("de.hybris.simple.Simple<some.bla.Pojo,other.blah.Dojo<yep.here.also.Mojo,you.wont.see.me.Jojo>>");
        String given = ClassNameUtil.getShortClassName(proto);
        Assert.assertEquals("Simple<Pojo,Dojo<Mojo,Jojo>>", given);
    }
}

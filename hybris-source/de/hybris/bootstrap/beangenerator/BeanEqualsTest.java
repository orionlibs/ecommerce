package de.hybris.bootstrap.beangenerator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.test.beans.TestBean;
import org.junit.Assert;
import org.junit.Test;

@UnitTest
public class BeanEqualsTest
{
    @Test
    public void testEquals()
    {
        TestBean bean = new TestBean();
        bean.setEqualsA("string");
        bean.setEqualsB(Integer.valueOf(1234));
        bean.setEqualsC(Boolean.TRUE);
        TestBean bean2 = new TestBean();
        bean2.setEqualsA("string");
        bean2.setEqualsB(Integer.valueOf(1234));
        bean2.setEqualsC(Boolean.TRUE);
        Assert.assertEquals(bean, bean);
        Assert.assertEquals(bean, bean2);
        bean2.setEqualsA("different");
        Assert.assertEquals(bean, bean);
        Assert.assertNotEquals(bean, bean2);
    }


    @Test
    public void testEqualsCornerCases()
    {
        TestBean bean = new TestBean();
        Assert.assertNotEquals(bean, null);
        Assert.assertNotEquals(bean, new Integer(1234));
    }
}

package io.github.orionlibs.ecommerce.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class OrionEnumerationTest
{
    @Test
    void getValue()
    {
        assertThat(TestEnum.SomeValue1.get()).isEqualTo("some value 1");
        assertThat(TestEnum.SomeValue2.get()).isEqualTo("some value 2");
    }


    @Test
    void is()
    {
        assertThat(TestEnum.SomeValue1.is(TestEnum.SomeValue1)).isTrue();
        assertThat(TestEnum.SomeValue2.is(TestEnum.SomeValue2)).isTrue();
    }


    @Test
    void isNot()
    {
        assertThat(TestEnum.SomeValue1.isNot(TestEnum.SomeValue1)).isFalse();
        assertThat(TestEnum.SomeValue1.isNot(TestEnum.SomeValue2)).isTrue();
        assertThat(TestEnum.SomeValue2.isNot(TestEnum.SomeValue1)).isTrue();
    }


    @Test
    void valueExists()
    {
        assertThat(TestEnum.valueExists("some value 1")).isTrue();
        assertThat(TestEnum.valueExists("some value 2")).isTrue();
        assertThat(TestEnum.valueExists("some value 3")).isFalse();
    }


    @Test
    void getEnumForValue()
    {
        assertThat(TestEnum.getEnumForValue("some value 1")).isEqualTo(TestEnum.SomeValue1);
        assertThat(TestEnum.getEnumForValue("some value 2")).isEqualTo(TestEnum.SomeValue2);
        assertThat(TestEnum.getEnumForValue("some value 3")).isNull();
    }


    public static enum TestEnum implements OrionEnumeration
    {
        SomeValue1("some value 1"),
        SomeValue2("some value 2");
        private String name;


        private TestEnum(String name)
        {
            setName(name);
        }


        @Override
        public String get()
        {
            return getName();
        }


        public String getName()
        {
            return this.name;
        }


        public void setName(String name)
        {
            this.name = name;
        }


        @Override
        public boolean is(OrionEnumeration other)
        {
            return other instanceof TestEnum && this == other;
        }


        @Override
        public boolean isNot(OrionEnumeration other)
        {
            return other instanceof TestEnum && this != other;
        }


        public static boolean valueExists(String other)
        {
            TestEnum[] values = values();
            for(TestEnum value : values)
            {
                if(value.get().equals(other))
                {
                    return true;
                }
            }
            return false;
        }


        public static TestEnum getEnumForValue(String other)
        {
            TestEnum[] values = values();
            for(TestEnum value : values)
            {
                if(value.get().equals(other))
                {
                    return value;
                }
            }
            return null;
        }
    }
}

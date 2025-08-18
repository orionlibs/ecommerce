package io.github.orionlibs.ecommerce.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ConverterTest
{
    @Test
    void convert()
    {
        Class1 temp = new Class1();
        temp.x = 64;
        Class1ToClass2Converter converter = new Class1ToClass2Converter();
        Class2 result = converter.convert(temp);
        assertThat("64").isEqualTo(result.x);
    }


    @Test
    void convert_null()
    {
        Class1ToClass2Converter converter = new Class1ToClass2Converter();
        Class2 result = converter.convert(null);
        assertThat(result).isNull();
    }


    public static class Class1
    {
        public int x;
    }


    public static class Class2
    {
        public String x;


        public Class2()
        {
        }
    }


    public class Class1ToClass2Converter implements Converter<Class1, Class2>
    {
        @Override
        public Class2 convert(Class1 objectToConvert)
        {
            if(objectToConvert == null)
            {
                return null;
            }
            Class2 temp = new Class2();
            temp.x = Integer.toString(objectToConvert.x);
            return temp;
        }
    }
}

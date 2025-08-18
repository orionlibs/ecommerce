package io.github.orionlibs.ecommerce.core;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Logger
{
    public static void info(String message, Object... parameters)
    {
        log.info(message, parameters);
    }


    public static void error(String message, Object... parameters)
    {
        log.error(message, parameters);
    }


    private Logger()
    {
    }
}

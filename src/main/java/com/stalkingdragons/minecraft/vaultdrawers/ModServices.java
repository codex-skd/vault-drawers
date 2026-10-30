package com.stalkingdragons.minecraft.vaultdrawers;

import com.stalkingdragons.minecraft.vaultdrawers.service.ResourceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ServiceLoader;

public class ModServices
{
    public static final Logger log = LogManager.getLogger();

    public static final ResourceFactory RESOURCE_FACTORY = load(ResourceFactory.class);

    private static <T> T load(Class<T> clazz) {
        final T service = ServiceLoader.load(clazz).findFirst().orElseThrow();
        return service;
    }
}
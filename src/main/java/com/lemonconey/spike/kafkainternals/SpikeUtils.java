package com.lemonconey.spike.kafkainternals;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class SpikeUtils {

    public static <T> Map dumpPublicFields(T object) {
        return Arrays.stream(object.getClass().getMethods())
                .filter(method -> method.getParameterCount() == 0)
                .filter(method -> method.getDeclaringClass().equals(object.getClass()))
                .map(method -> Pair.of(method.getName(), callMethod(object, method)))
                .filter(pair -> pair.getRight() != null)
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
    }

    private static <T> Object callMethod(T object, Method method) {
        try {
            return method.invoke(object);
        } catch (InvocationTargetException | IllegalAccessException e) {
            log.info("failed to invoke {} on {}, message: {}", method.getName(), object, e);
            return null;
        }
    }


}

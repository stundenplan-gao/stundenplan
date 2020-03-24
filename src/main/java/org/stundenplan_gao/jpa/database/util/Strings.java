package org.stundenplan_gao.jpa.database.util;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class Strings {

    public static <T> String join(Collection<T> collection, String separator) {
        if (collection == null)
            return "";
        return collection.stream().map(x -> Objects.toString(x)).collect(Collectors.joining(separator));
    }
}

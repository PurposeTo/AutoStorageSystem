package com.chain.autostoragesystem.utils.common;

import lombok.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtils {

    public static boolean isBlank(String str) {
        return Objects.isNull(str) || str.trim().equals("");
    }

    public static boolean isNonBlank(String str) {
        return !isBlank(str);
    }

    public static String requiredNonBlank(String str) {
        if (isBlank(str)) {
            throw new NullPointerException();
        }

        return str;
    }

    public static String toPretty(@NonNull Map<String, String> map) {
        return map.entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining("\n"));
    }

    public static String[] splitAllChars(String str) {
        requiredNonBlank(str);
        return str.split("(?!^)");
    }
}

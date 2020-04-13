package com.changethislater.emojify.utils;

import java.util.Arrays;
import java.util.function.Function;

public class Rule {
    String name;
    Function<String, String> replacement;


    public Rule(String name, Function<String, String> replacement) {
        setName(name);
        setReplacement(replacement);
    }

    public Rule(String name, String to, String... from) {
        setName(name);
        setReplacement(s -> {
            s = s.replaceAll("(" + String.join("|", from) + ")", to);
            return s;
        });
    }

    public String apply(String input) {
        return replacement.apply(input);
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public Function<String, String> getReplacement() {
        return replacement;
    }

    private void setReplacement(Function<String, String> replacement) {
        this.replacement = replacement;
    }
}

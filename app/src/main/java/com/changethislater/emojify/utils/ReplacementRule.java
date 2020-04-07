package com.changethislater.emojify.utils;

import java.util.function.Function;

public class ReplacementRule {
    String name;
    Function<String, String> replacement;

    public ReplacementRule(String name, Function<String, String> replacement) {
        setName(name);
        setReplacement(replacement);
    }

    public String apply(String input){
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

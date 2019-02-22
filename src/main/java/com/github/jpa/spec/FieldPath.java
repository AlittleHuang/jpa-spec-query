package com.github.jpa.spec;

import lombok.Getter;

public class FieldPath {

    @Getter
    private String[] val;

    public FieldPath(String... paths) {
        this.val = paths;
    }

}

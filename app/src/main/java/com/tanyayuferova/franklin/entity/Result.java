package com.tanyayuferova.franklin.entity;

/**
 * Created by Tanya Yuferova on 4/25/2018.
 */

public enum Result {
    POSITIVE(1),
    NEGATIVE(-1),
    NEUTRAL(0);

    int id;

    Result(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

package com.stetel.preferences;

import java.util.Objects;

public class Versions {
    private int previous;
    private int current;

    Versions(int previous, int current) {
        this.previous = previous;
        this.current = current;
    }

    public int getPrevious() {
        return previous;
    }

    public int getCurrent() {
        return current;
    }

    public boolean isUpdated() {
        return previous < current;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Versions versions = (Versions) o;

        if (previous != versions.previous) return false;
        return current == versions.current;
    }

    @Override
    public int hashCode() {
        int result = previous;
        result = 31 * result + current;
        return result;
    }

    @Override
    protected Versions clone() {
        return new Versions(previous, current);
    }
}

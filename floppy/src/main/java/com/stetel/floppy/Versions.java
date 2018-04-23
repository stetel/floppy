package com.stetel.floppy;

/**
 * Versions keeps track of the previous and current app versions.
 */
public class Versions {
    private int previous;
    private int current;

    /**
     * Package private constructor, can only be instantiated by Floppy.
     *
     * @param previous previous version
     * @param current current version
     */
    Versions(int previous, int current) {
        this.previous = previous;
        this.current = current;
    }

    /**
     * Get the previous version of the app
     *
     * @return previous version code
     */
    public int getPrevious() {
        return previous;
    }

    /**
     * Get the current version of the app
     *
     * @return current version code
     */
    public int getCurrent() {
        return current;
    }

    /**
     * Checks if the app is updated
     *
     * @return true if updated
     */
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

package org.opengis.cite.api.processing;

/**
 * An enumerated type defining all recognized test run arguments.
 */
public enum TestRunArg {

    /**
     * An absolute URI that refers to a representation of the test subject or
     * metadata about it.
     */
    IUT, WPS, ROOT;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}

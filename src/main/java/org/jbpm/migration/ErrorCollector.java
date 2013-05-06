package org.jbpm.migration;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/** Convenience class for making the error handling within the parsing and validation processes a little more verbose. */
abstract class ErrorCollector<T extends Exception> {
    private final List<T> warningList = new ArrayList<T>();
    private final List<T> errorList = new ArrayList<T>();
    private final List<T> fatalList = new ArrayList<T>();

    public void warning(final T ex) {
        warningList.add(ex);
    }

    public void error(final T ex) {
        errorList.add(ex);
    }

    public void fatalError(final T ex) {
        fatalList.add(ex);
    }

    public boolean didErrorOccur() {
        // checking warnings might be too restrictive
        return !warningList.isEmpty() || !errorList.isEmpty() ||!fatalList.isEmpty();
    }

    public List<T> getWarningList() {
        return warningList;
    }

    public List<T> getErrorList() {
        return errorList;
    }

    public List<T> getFatalList() {
        return fatalList;
    }

    public void logErrors(final Logger logger) {
        for (final T ex : warningList) {
            logger.warn("==>", ex);
        }
        for (final T ex : errorList) {
            logger.error("==>", ex);
        }
        for (final T ex : fatalList) {
            logger.fatal("==>", ex);
        }
    }
}

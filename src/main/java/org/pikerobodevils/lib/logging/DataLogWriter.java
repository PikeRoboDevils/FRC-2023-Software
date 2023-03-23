package org.pikerobodevils.lib.logging;

import org.tinylog.Level;
import org.tinylog.core.LogEntry;
import org.tinylog.writers.AbstractFormatPatternWriter;
import org.tinylog.writers.Writer;

import java.util.Map;

public class DataLogWriter extends AbstractFormatPatternWriter {

    private final Level logLevel;

    public DataLogWriter(Map<String, String> properties) {
        super(properties);
        logLevel = Level.ERROR;
    }

    @Override
    public void write(LogEntry logEntry) throws Exception {

    }

    @Override
    public void flush() throws Exception {

    }

    @Override
    public void close() throws Exception {

    }
}

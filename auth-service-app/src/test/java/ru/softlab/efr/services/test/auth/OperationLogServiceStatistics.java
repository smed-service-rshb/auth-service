package ru.softlab.efr.services.test.auth;

import ru.softlab.efr.infrastructure.logging.api.model.OperationLogEntry;

import java.util.ArrayList;
import java.util.List;

public class OperationLogServiceStatistics {

    private List<OperationLogEntry> logs = new ArrayList<>();

    public void reset() {
        logs.clear();
    }

    public void append(OperationLogEntry entry) {
        logs.add(entry);
    }

    public List<OperationLogEntry> getLogs() {
        return logs;
    }
}

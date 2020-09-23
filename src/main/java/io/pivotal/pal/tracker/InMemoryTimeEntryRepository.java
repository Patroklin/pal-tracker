package io.pivotal.pal.tracker;

import java.util.HashMap;
import java.util.List;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private HashMap<Long, TimeEntry> repository = new HashMap<>();
    private Long id = (long) 0;

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        id = id + 1;
        timeEntry.setId(id);
        repository.put(timeEntry.getId(), timeEntry);
        return timeEntry;
    }

    @Override
    public TimeEntry find(long id) {
        return repository.get(id);
    }

    @Override
    public List<TimeEntry> list() {
        return List.copyOf(repository.values());
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry timeRecord = find(id);

        if (!(timeRecord == null)) {

            timeRecord.setProjectId(timeEntry.getProjectId());
            timeRecord.setUserId(timeEntry.getUserId());
            timeRecord.setDate(timeEntry.getDate());
            timeRecord.setHours(timeEntry.getHours());

            repository.put(id, timeRecord);

        }
        return timeRecord;

    }

    @Override
    public void delete(long id) {
        repository.remove(id);
    }
}

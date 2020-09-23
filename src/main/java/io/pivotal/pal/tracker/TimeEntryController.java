package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    private TimeEntryRepository repository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.repository = timeEntryRepository;
    }

    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry createdTimeEntry = this.repository.create(timeEntryToCreate);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTimeEntry);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable(name = "id") long timeEntryId) {
        TimeEntry foundEntry = this.repository.find(timeEntryId);

        if (!(foundEntry == null)) {
            return ResponseEntity.status(HttpStatus.OK).body(foundEntry);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> entityList = this.repository.list();
        return ResponseEntity.status(HttpStatus.OK).body(entityList);
    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable long id, @RequestBody TimeEntry updatedTimeEntry) {
        TimeEntry updatedTimeEntryFromRepository = this.repository.update(id, updatedTimeEntry);
        if (!(updatedTimeEntryFromRepository == null)) {
            return ResponseEntity.status(HttpStatus.OK).body(updatedTimeEntryFromRepository);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity delete(@PathVariable( name = "id") long timeEntryId) {
        this.repository.delete(timeEntryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

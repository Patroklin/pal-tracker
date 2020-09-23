package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        int updatedRecords = this.jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement("INSERT INTO time_entries (project_id, user_id, date, hours) " +
                            "VALUES (?, ?, ?, ?)",
                    RETURN_GENERATED_KEYS
            );

            statement.setLong(1, timeEntry.getProjectId());
            statement.setLong(2, timeEntry.getUserId());
            statement.setDate(3, Date.valueOf(timeEntry.getDate()));
            statement.setInt(4, timeEntry.getHours());

            return statement;
        }, generatedKeyHolder);

        return updatedRecords == 1 ? find(Objects.requireNonNull(generatedKeyHolder.getKey()).longValue()) : null;
    }

    @Override
    public TimeEntry find(long id) {
        return this.jdbcTemplate.query("SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = ?", new Object[]{id}, rs -> {

            TimeEntry timeEntry = null;
            if (rs.next()) {
                timeEntry = new TimeEntry();
                timeEntry.setId(rs.getLong(1));
                timeEntry.setProjectId(rs.getLong(2));
                timeEntry.setUserId(rs.getLong(3));
                timeEntry.setDate(rs.getDate(4).toLocalDate());
                timeEntry.setHours(rs.getInt(5));

            }
            return timeEntry;
        });
    }

    @Override
    public List<TimeEntry> list() {
        return this.jdbcTemplate.query("SELECT id, project_id, user_id, date, hours FROM time_entries", rs -> {
            List<TimeEntry> timeEntries = new ArrayList<>();
            TimeEntry timeEntry;
            while (rs.next()) {
                timeEntry = new TimeEntry();
                timeEntry.setId(rs.getLong(1));
                timeEntry.setProjectId(rs.getLong(2));
                timeEntry.setUserId(rs.getLong(3));
                timeEntry.setDate(rs.getDate(4).toLocalDate());
                timeEntry.setHours(rs.getInt(5));

                timeEntries.add(timeEntry);

            }
            return timeEntries;
        });
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry entry = find(id);
        timeEntry.setId(entry.getId());

        int updatedRecords = this.jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement("UPDATE time_entries set project_id = ?, user_id = ?, date = ?, hours = ? WHERE id = ? "
            );
            statement.setLong(1, timeEntry.getProjectId());
            statement.setLong(2, timeEntry.getUserId());
            statement.setDate(3, Date.valueOf(timeEntry.getDate()));
            statement.setInt(4, timeEntry.getHours());
            statement.setLong(5, timeEntry.getId());

            return statement;
        });

        return updatedRecords == 1 ? find(id) : null;
    }

    @Override
    public void delete(long id) {

        this.jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement("DELETE FROM time_entries WHERE id = ? "
            );
            statement.setLong(1, id);

            return statement;
        });

    }
}

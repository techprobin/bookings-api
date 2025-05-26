package org.example.evaluations.models;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class CardinalitiesTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testIfTableWithNameBookingIsCreated() {
        String tableName = "BOOKING";
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";

        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{tableName}, Integer.class);

        assertTrue(count != null && count > 0, "Table with name BOOKING does not exist !");
    }

    @Test
    public void testIfTableWithNameGuestIsCreated() {
        String tableName = "GUEST";
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";

        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{tableName}, Integer.class);

        assertTrue(count != null && count > 0, "Table with name GUEST does not exist !");
    }

    @Test
    public void testIfTableWithNameRoomIsCreated() {
        String tableName = "ROOM";
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";

        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{tableName}, Integer.class);

        assertTrue(count != null && count > 0, "Table with name ROOM does not exist !");
    }

    @Test
    public void testColumnNamesOfBookingTable() throws SQLException {
        String tableName = "BOOKING";
        Set<String> expectedColumns = Set.of("TOTAL_BILL", "CHECK_IN_DATE", "CHECK_OUT_DATE", "ID", "GUEST_EMAIL");

        boolean columnsAreValid = validateColumns(tableName, expectedColumns);

        assertTrue(columnsAreValid, "The table BOOKING does not contain all expected columns like TOTAL_BILL, CHECK_IN_DATE, CHECK_OUT_DATE, ID, GUEST_EMAIL");
    }

    @Test
    public void testColumnNamesOfGuestTable() throws SQLException {
        String tableName = "GUEST";
        Set<String> expectedColumns = Set.of("NAME", "EMAIL");

        boolean columnsAreValid = validateColumns(tableName, expectedColumns);

        assertTrue(columnsAreValid, "The table GUEST does not contain all expected columns like NAME, EMAIL");
    }

    @Test
    public void testColumnNamesOfRoomTable() throws SQLException {
        String tableName = "ROOM";
        Set<String> expectedColumns = Set.of("RENT", "BOOKING_ID", "ID", "ROOM_TYPE");

        boolean columnsAreValid = validateColumns(tableName, expectedColumns);

        assertTrue(columnsAreValid, "The table ROOM does not contain all expected columns like RENT, BOOKING_ID, ID, ROOM_TYPE");
    }

    private Set<String> getColumnNames(String tableName) throws SQLException {
        Set<String> columns = new HashSet<>();
        DatabaseMetaData metaData = jdbcTemplate.getDataSource().getConnection().getMetaData();

        try (ResultSet rs = metaData.getColumns(null, null, tableName, null)) {
            while (rs.next()) {
                columns.add(rs.getString("COLUMN_NAME"));
            }
        }
        return columns;
    }

    private boolean validateColumns(String tableName, Set<String> expectedColumns) throws SQLException {
        Set<String> actualColumns = getColumnNames(tableName);
        return actualColumns.containsAll(expectedColumns);
    }
}



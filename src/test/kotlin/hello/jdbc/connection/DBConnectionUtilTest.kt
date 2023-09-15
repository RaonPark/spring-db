package hello.jdbc.connection

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.sql.Connection

class DBConnectionUtilTest {
    @Test
    fun connection() {
        val connection = DBConnectionUtil.getConnection()
        assertNotNull(connection)
    }
}
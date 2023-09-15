package hello.jdbc.connection

import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DBConnectionUtil {
    companion object {
        val log = LoggerFactory.getLogger(this::class.java)
        fun getConnection(): Connection {
            try {
                val connection =
                    DriverManager.getConnection(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD)
                log.info("get connection={}, class={}", connection, connection.javaClass)
                return connection
            } catch(e: SQLException) {
                throw IllegalStateException(e)
            }
        }
    }


}
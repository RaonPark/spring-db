package hello.jdbc.connection

import com.zaxxer.hikari.HikariDataSource
import lombok.extern.slf4j.Slf4j
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.springframework.jdbc.datasource.DriverManagerDataSource
import java.sql.DriverManager
import javax.sql.DataSource

@Slf4j
class ConnectionTest {
    private val log = KotlinLogging.logger {  }

    @Test
    fun driverManager() {
        val con1 = DriverManager.getConnection(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD)
        val con2 = DriverManager.getConnection(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD)
        log.info { "connection=$con1, class=${con1.javaClass}" }
        log.info { "connection=$con2, class=${con2.javaClass}" }
    }

    @Test
    fun dataSourceDriverManager() {
        // 항상 새로운 커넥션을 가져온다.
        val dataSource = DriverManagerDataSource(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD)
        useDataSource(dataSource)
    }

    private fun useDataSource(dataSource: DataSource) {
        // 드라이버를 사용할 때와 다르게 URL, USERNAME, PASSWORD 를 알 필요가 없다.
        // 설정과 사용을 분리한다.
        val con1 = dataSource.connection
        val con2 = dataSource.connection
        val con3 = dataSource.connection
        val con4 = dataSource.connection
        val con5 = dataSource.connection
        val con6 = dataSource.connection
        val con7 = dataSource.connection
        val con8 = dataSource.connection
        val con9 = dataSource.connection
        val con10 = dataSource.connection
        // MyPool - Connection is not available, request timed out after 30000ms.
        val con11 = dataSource.connection
        log.info { "connection=$con1, class=${con1.javaClass}" }
        log.info { "connection=$con2, class=${con2.javaClass}" }
    }

    @Test
    fun dataSourceConnectionPool() {
        val dataSource = HikariDataSource()
        dataSource.jdbcUrl = ConnectionConst.URL
        dataSource.username = ConnectionConst.USERNAME
        dataSource.password = ConnectionConst.PASSWORD
        dataSource.maximumPoolSize = 10
        dataSource.poolName = "MyPool"

        useDataSource(dataSource)
        Thread.sleep(2000)
    }
}
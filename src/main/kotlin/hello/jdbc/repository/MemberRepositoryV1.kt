package hello.jdbc.repository

import hello.jdbc.connection.DBConnectionUtil
import hello.jdbc.domain.Member
import mu.KotlinLogging
import org.springframework.jdbc.support.JdbcUtils
import java.sql.*
import java.util.NoSuchElementException
import javax.sql.DataSource

/**
 * JDBC - DataSource 사용, JdbcUtils 사용
 */
class MemberRepositoryV1(
    private val dataSource: DataSource
) {
    private val log = KotlinLogging.logger {  }

    fun save(member: Member): Member {
        val sql = "insert into member(member_id, money) values (?, ?)"

        // SQL Injection 을 위해서 PreparedStatement 를 사용해야 한다.

        var con: Connection? = null
        var pstmt: PreparedStatement? = null

        try {
            con = getConnection()
            pstmt = con.prepareStatement(sql)
            pstmt.setString(1, member.memberId)
            pstmt.setInt(2, member.money)
            pstmt.executeUpdate()
            return member
        } catch(e: SQLException) {
            log.error(e) { "db error" }
            throw e
        } finally {
            // 리소스 정리. 정리는 정의의 역순
            close(con, pstmt, null)
        }
    }

    private fun getConnection(): Connection {
        val con = dataSource.connection
        log.info { "get connection=$con, class=${con.javaClass}" }
        return con
    }

    private fun close(con: Connection?, stmt: Statement?, rs: ResultSet?) {
        JdbcUtils.closeResultSet(rs)
        JdbcUtils.closeStatement(stmt)
        JdbcUtils.closeConnection(con)
    }

    fun findById(memberId: String): Member {
        val sql = "select * from member where member_id = ?"

        var con: Connection? = null
        var pstmt: PreparedStatement? = null
        var rs: ResultSet? = null

        try {
            con = getConnection()
            pstmt = con.prepareStatement(sql)
            pstmt.setString(1, memberId)

            rs = pstmt.executeQuery()

            if(rs.next()) {
                return Member(rs.getString("member_id"), rs.getInt("money"))
            } else {
                throw NoSuchElementException("member not found memberId= $memberId")
            }
        } catch (e: SQLException) {
            throw IllegalStateException(e)
        } finally {
            close(con, pstmt, rs)
        }
    }

    fun update(memberId: String, money: Int) {
        val sql = "update member set money=? where member_id=?"

        var con: Connection? = null
        var pstmt: PreparedStatement? = null

        try {
            con = getConnection()
            pstmt = con.prepareStatement(sql)
            pstmt.setInt(1, money)
            pstmt.setString(2, memberId)
            // 영향 받은 row 의 수를 반환
            val resultSize = pstmt.executeUpdate()
            log.info { "resultSize = $resultSize" }
        } catch(e: SQLException) {
            log.error(e) { "db error" }
            throw e
        } finally {
            // 리소스 정리. 정리는 정의의 역순
            close(con, pstmt, null)
        }
    }

    fun delete(memberId: String) {
        val sql = "delete from member where member_id=?"

        var con: Connection? = null
        var pstmt: PreparedStatement? = null

        try {
            con = getConnection()
            pstmt = con.prepareStatement(sql)
            pstmt.setString(1, memberId)
            // 영향 받은 row 의 수를 반환
            val resultSize = pstmt.executeUpdate()
            log.info { "resultSize = $resultSize" }
        } catch(e: SQLException) {
            log.error(e) { "db error" }
            throw e
        } finally {
            // 리소스 정리. 정리는 정의의 역순
            close(con, pstmt, null)
        }
    }
}
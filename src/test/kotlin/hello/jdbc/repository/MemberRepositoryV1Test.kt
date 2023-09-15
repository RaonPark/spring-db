package hello.jdbc.repository

import com.zaxxer.hikari.HikariDataSource
import hello.jdbc.connection.ConnectionConst.Companion.PASSWORD
import hello.jdbc.connection.ConnectionConst.Companion.URL
import hello.jdbc.connection.ConnectionConst.Companion.USERNAME
import hello.jdbc.domain.Member
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.jdbc.datasource.DriverManagerDataSource
import java.sql.SQLException
import java.util.NoSuchElementException

class MemberRepositoryV1Test {
    private var repository: MemberRepositoryV1? = null
    private val log = KotlinLogging.logger {  }

    @BeforeEach
    fun beforeEach() {
//        val dataSource = DriverManagerDataSource(URL, USERNAME, PASSWORD)
        val dataSource = HikariDataSource()
        dataSource.jdbcUrl = URL
        dataSource.username = USERNAME
        dataSource.password = PASSWORD
        repository = MemberRepositoryV1(dataSource)
    }

    @Test
    @Throws(SQLException::class)
    fun crud() {
        val member = Member("memberV5", 10000)
        repository!!.save(member)

        val findMember = repository!!.findById(member.memberId)
        log.info { "findMember=$findMember" } // kotlin 의 data class 는 toString()을 포함한다.
        log.info { "member === findMember : ${findMember === member}" } // false. not same object.
        log.info { "member equals findMember: ${findMember == member}" } // true.
        Assertions.assertEquals(member, findMember)

        // update
        repository!!.update(member.memberId, 20000)
        val updatedMember = repository!!.findById(member.memberId)
        Assertions.assertEquals(20000, updatedMember.money)

        // delete
        repository!!.delete(member.memberId)
        assertThrows<NoSuchElementException> { repository!!.findById(member.memberId) }
    }
}
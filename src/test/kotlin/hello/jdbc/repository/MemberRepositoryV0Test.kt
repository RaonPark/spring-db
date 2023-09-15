package hello.jdbc.repository

import hello.jdbc.domain.Member
import mu.KotlinLogging
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.sql.SQLException
import java.util.NoSuchElementException

class MemberRepositoryV0Test {
    private val repository = MemberRepositoryV0()
    private val log = KotlinLogging.logger {  }

    @Test
    // Kotlin 에는 Checked Exception 이 존재하지 않기 때문에 메소드 단에서 throws 문법을 쓰지 않아도 된다.
    @Throws(SQLException::class)
    fun crud() {
        val member = Member("memberV5", 10000)
        repository.save(member)

        val findMember = repository.findById(member.memberId)
        log.info { "findMember=$findMember" } // kotlin 의 data class 는 toString()을 포함한다.
        log.info { "member === findMember : ${findMember === member}" } // false. not same object.
        log.info { "member equals findMember: ${findMember == member}" } // true.
        assertEquals(member, findMember)

        // update
        repository.update(member.memberId, 20000)
        val updatedMember = repository.findById(member.memberId)
        assertEquals(20000, updatedMember.money)

        // delete
        repository.delete(member.memberId)
        assertThrows<NoSuchElementException> { repository.findById(member.memberId) }
    }
}
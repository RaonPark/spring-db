package hello.jdbc.connection

interface ConnectionConst {
    companion object {
        const val URL = "jdbc:h2:tcp://localhost/~/test"
        const val USERNAME = "sa"
        const val PASSWORD = ""
    }
}
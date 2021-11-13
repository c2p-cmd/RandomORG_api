package main

import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

object JDBC {
    private val statement: Statement
    private val connection: Connection
    private const val DBNAME = "key.db"
    private const val URL = "jdbc:sqlite:src/resources/$DBNAME"

    val myAPIKEY: String
        get() {
            try {
                val query = "SELECT api FROM api_key;"
                val resultSet = statement!!.executeQuery(query)
                return if (resultSet.next()) {
                    resultSet.getString("API")
                } else {
                    throw NullPointerException("Empty db")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                close()
            }
            return "Empty API KEY"
        }

    private fun close() {
        try {
            statement.close()
            connection.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    init {
        try {
            connection = DriverManager.getConnection(URL)
            statement = connection.createStatement()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
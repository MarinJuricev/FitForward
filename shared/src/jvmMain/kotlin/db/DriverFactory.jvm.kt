package db

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.fitforward.data.FitForwardDatabase
import java.util.Properties

actual class DriverFactory {
    actual fun createDriver(): SqlDriver = JdbcSqliteDriver(
        "jdbc:sqlite:test.db",
        Properties(),
        schema = FitForwardDatabase.Schema.synchronous(),
    )
}

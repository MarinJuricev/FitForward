package db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.fitforward.data.Database

actual class DriverFactory {
    actual fun createDriver(): SqlDriver = NativeSqliteDriver(
        Database.Schema,
        DATABASE_NAME,
    )
}
package db

import android.content.Context
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.fitforward.data.FitForwardDatabase

actual class DriverFactory(
    private val context: Context
) {
    actual fun createDriver(): SqlDriver = AndroidSqliteDriver(
        schema = FitForwardDatabase.Schema.synchronous(),
        context = context,
        name = DATABASE_NAME
    )
}
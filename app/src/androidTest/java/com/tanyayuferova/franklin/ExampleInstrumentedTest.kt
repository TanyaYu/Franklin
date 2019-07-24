package com.tanyayuferova.franklin

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.tanyayuferova.franklin.utils.*

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.tanyayuferova.franklin", appContext.packageName)
    }

    @Test
    fun weekToDate() {
        val c = Calendar.getInstance()
        c.set(Calendar.WEEK_OF_YEAR, 29)
        c.get(Calendar.WEEK_OF_YEAR)
        c.set(Calendar.DAY_OF_WEEK, 1)
        assertEquals(
            Date().setUp(2019, 6, 14, 0, 0, 0, 0),
            c.time.removeTime()
        )
    }
}

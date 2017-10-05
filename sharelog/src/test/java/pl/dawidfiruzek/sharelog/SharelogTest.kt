package pl.dawidfiruzek.sharelog

import android.app.Activity
import android.view.MotionEvent
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SharelogTest {

    @Mock
    private lateinit var activity: Activity

    @Mock
    private lateinit var motionEvent: MotionEvent

    private lateinit var sharelog: Sharelog

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        sharelog = Sharelog(activity)
    }

    @After
    fun tearDown() {
        Mockito.verifyNoMoreInteractions()
    }

    @Test
    fun captureManual() {
        TODO("Not implemented")
    }

    @Test
    fun captureGesture() {
        TODO("Not implemented")
    }

    @Test
    fun captureBadGesture() {
        sharelog.capture(motionEvent)

        //nothing should happen because of not set mode
    }
}

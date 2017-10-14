package pl.dawidfiruzek.sharelogsample

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import pl.dawidfiruzek.sharelog.Sharelog
import pl.dawidfiruzek.sharelog.SharelogGestureMode

class MainActivity : AppCompatActivity() {

    private val TAG = "Sharelog"
    private lateinit var sharelog: Sharelog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        sharelog = Sharelog.getInstance(this)
                .setGestureMode(SharelogGestureMode.TRIPLE_TAP)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view ->
            Log.wtf(TAG, "onFabClicked")
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", { _ -> Log.w(TAG, "onSnackbarActionClicked") }).show()
        }

        val root = findViewById<ConstraintLayout>(R.id.main_root)
        root.setOnTouchListener { _, motionEvent ->
            sharelog.capture(motionEvent)
            true
        }

        Log.d(TAG, "onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.v(TAG, "onCreateOptionsMenu")
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_share_logs -> {
            sharelog.capture()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}

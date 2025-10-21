package dzmitryk.codepractice.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dzmitryk.codepractice.recyclerview.databinding.ActivityMainBinding
import dzmitryk.codepractice.recyclerview.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}
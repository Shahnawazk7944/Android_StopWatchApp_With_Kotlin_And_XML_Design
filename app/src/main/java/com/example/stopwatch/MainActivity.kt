package com.example.stopwatch

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Chronometer
import android.widget.NumberPicker
import androidx.core.view.LayoutInflaterCompat
import com.example.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    var isRunning = false
    private var min: String? = "00.00.00"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var lapsList = ArrayList<String>()
        var arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lapsList)
        binding.listView.adapter = arrayAdapter
        binding.lapReset.setOnClickListener {
            if (isRunning) {
                lapsList.add(binding.chronometer.text.toString())
                arrayAdapter.notifyDataSetChanged()
            }
        }
        binding.addtime.setOnClickListener {
            var dialog = Dialog(this)
            dialog.setContentView(R.layout.add_time_dialogue)
            var numberPicker = dialog.findViewById<NumberPicker>(R.id.numberPicker)
            numberPicker.minValue = 1
            numberPicker.maxValue = 50
            dialog.findViewById<Button>(R.id.setTime).setOnClickListener {
                min = numberPicker.value.toString()
                if (isRunning) {
                    isRunning = false
                    binding.chronometer.stop()
                    binding.startStop.text = "Start"
                    var totalMin = min!!.toInt()*60*1000L
                    var countDown = 1000L
                    binding.chronometer.base= SystemClock.elapsedRealtime()+totalMin
                    binding.chronometer.format = "%S %S"
                } else {
                    var totalMin = min!!.toInt()*60*1000L
                    var countDown = 1000L
                    binding.chronometer.base= SystemClock.elapsedRealtime()+totalMin
                    binding.chronometer.format = "%S %S"
                    binding.chronometer.stop()

                }
                binding.addedtime.text =
                    dialog.findViewById<NumberPicker>(R.id.numberPicker).value.toString() + " Min"
                dialog.dismiss()
            }
            dialog.show()
        }
        binding.startStop.setOnClickListener {
            if (!isRunning) {
                isRunning = true
                if (!min.equals("00.00.00")) {
                    var totalMin = min!!.toInt() * 60 * 1000L
                    binding.chronometer.onChronometerTickListener =
                        Chronometer.OnChronometerTickListener {
                            var elepsedTime =
                                SystemClock.elapsedRealtime() - binding.chronometer.base
                            if (elepsedTime >= totalMin) {
                                binding.chronometer.stop()
                                isRunning = false
                                binding.startStop.text = "Start"
                            }
                        }
                    isRunning = true
                    binding.chronometer.start()
                    binding.startStop.text = "Stop"

                } else {
                    isRunning = true
                    binding.chronometer.base = SystemClock.elapsedRealtime()
                    binding.startStop.text = "Stop"
                    binding.chronometer.start()
                }

            } else {
                isRunning = false
                binding.startStop.text = "Start"
                binding.chronometer.stop()
            }



        }
    }
}
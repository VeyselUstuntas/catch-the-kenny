package com.vustuntas.catchthekenny

import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract.CommonDataKinds.Im
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.vustuntas.catchthekenny.databinding.ActivityMainBinding
import java.nio.InvalidMarkException
import java.util.ArrayList
import java.util.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var sharedPreferences : SharedPreferences

    private var randomNumber = Random()
    private lateinit var alertDialog : AlertDialog.Builder

    var runnable : Runnable = Runnable { }
    var handler : Handler = Handler(Looper.getMainLooper())

    private var gorselDizi = ArrayList<ImageView>()

    var score = 0
    var time = -1
    var devamEt = true
    private var counter = object : CountDownTimer(15000,1000){
        override fun onTick(p0: Long) {
            binding.counter.text = "Time: ${p0/1000}"
        }

        override fun onFinish() {
            time = 0
            if(devamEt){
                alertFun(gorselDizi)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = this@MainActivity.getSharedPreferences("com.vustuntas.catchthekenny", MODE_PRIVATE)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var kaydedilenSkor = sharedPreferences.getInt("skor",-1)
        if(kaydedilenSkor != -1){
            binding.score.setText("Score: ${kaydedilenSkor.toString()}")
        }
        gorselDizi.add(binding.r0c0img)
        gorselDizi.add(binding.r0c1img)
        gorselDizi.add(binding.r0c2img)
        gorselDizi.add(binding.r0c3img)
        gorselDizi.add(binding.r1c0img)
        gorselDizi.add(binding.r1c1img)
        gorselDizi.add(binding.r1c2img)
        gorselDizi.add(binding.r1c3img)
        gorselDizi.add(binding.r2c0img)
        gorselDizi.add(binding.r2c1img)
        gorselDizi.add(binding.r2c2img)
        gorselDizi.add(binding.r2c3img)
        gorselDizi.add(binding.r3c0img)
        gorselDizi.add(binding.r3c1img)
        gorselDizi.add(binding.r3c2img)
        gorselDizi.add(binding.r3c3img)
        alertFun(gorselDizi)
    }

    fun alertFun(dizi: ArrayList<ImageView>){
        sharedPreferences = this@MainActivity.getSharedPreferences("com.vustuntas.catchthekenny", MODE_PRIVATE)
        alertDialog = AlertDialog.Builder(this@MainActivity)
        alertDialog.setTitle("GAME OVER")
        alertDialog.setMessage("Restart the Game?")
        alertDialog.setPositiveButton("YES",DialogInterface.OnClickListener { dialogInterface, i ->
            counter.start()
            score = 0
            time = -1
            var rassalSayiLast : ImageView? = null
            runnable = object:Runnable {
                override fun run() {
                    sharedPreferences = this@MainActivity.getSharedPreferences("com.vustuntas.catchthekenny",MODE_PRIVATE)
                    val lastStoredScore = sharedPreferences.getInt("skor",-1)

                    var rassalSayi = randomNumber.nextInt(dizi.size)
                    var imageViewGorsel = dizi.get(rassalSayi)

                    if (rassalSayiLast!=null)
                        rassalSayiLast!!.visibility = View.INVISIBLE

                    imageViewGorsel.visibility = View.VISIBLE

                    if(time != 0){
                        imageViewGorsel.setOnClickListener {
                            score++
                            imageViewGorsel.visibility = View.INVISIBLE
                        }
                        handler.postDelayed(this,500)
                        binding.score.text = "Score: ${score}"
                    }
                    else{
                        handler.removeCallbacks(runnable)
                        imageViewGorsel.visibility = View.INVISIBLE
                        binding.score.setText("Score: ${score}")
                        if(lastStoredScore != -1 && score > lastStoredScore){
                            Toast.makeText(this@MainActivity,"Rekor Kırıldı! Tebrikler...",Toast.LENGTH_SHORT).show()
                            sharedPreferences.edit().putInt("skor",score).apply()
                            devamEt = true
                        }
                    }
                    rassalSayiLast = imageViewGorsel

                }
            }
            handler.post(runnable)
        })
        alertDialog.setNegativeButton("NO",DialogInterface.OnClickListener { dialogInterface, i ->
            binding.counter.text="Time: 0"
            binding.score.text = "Score: ${score}"
            devamEt = false
        })
        alertDialog.show()

    }
}
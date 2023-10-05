package com.tamplate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.jackandphantom.carouselrecyclerview.CarouselLayoutManager
import com.jackandphantom.carouselrecyclerview.CarouselRecyclerview
import java.util.Timer
import java.util.TimerTask


class GameActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        var position1: Int = 2
        var position2: Int = 1
        var position3: Int = 3

        var bet : Int = 10                      //начальный размер ставки
        var balance : Int = 100                 //баланс на начало гейма

        val spinBtn: ImageButton = findViewById(R.id.spinBtn)  //кнопка кручения

        val backBtn: ImageButton = findViewById(R.id.backBtn)   //кнопка назад
        backBtn.setOnClickListener {
            val i = Intent(Intent.ACTION_MAIN)
            i.addCategory(Intent.CATEGORY_HOME)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(i)        //нажимается home
            onDestroy()
        }

        val moneyBalance : TextView = findViewById(R.id.moneyBalnce)
        moneyBalance.text = balance.toString()                   // начальный баланс
        val betTxt: TextView = findViewById(R.id.betTxt)
        betTxt.text = bet.toString()                            // минимальная ставка

        val minusBtn : ImageButton = findViewById(R.id.minusBtn)
        val plusBtn : ImageButton = findViewById(R.id.plusBtn)

        plusBtn.setOnClickListener {            //увеличение ставки
            bet+=5
            betTxt.text=bet.toString()
        }
        minusBtn.setOnClickListener {   //уменьшение ставки
            if (bet>=10){                   //минимальная ставка 5 чего-то там)
                bet-=5
                betTxt.text=bet.toString()
            }
        }

        val historyBtn : ImageButton = findViewById(R.id.historyBtn)            //фрагмент истории
        historyBtn.setOnClickListener {
            val historyFragment = HistoryFragment()

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainre, historyFragment)
                addToBackStack(null)
                commit()
            }
        }

        //настройка списка icon
        val adapter = Adapter(IconList().listImage)

        val carouselRecyclerview1 = findViewById<CarouselRecyclerview>(R.id.carouselRecyclerview1)
        carouselRecyclerview1.adapter = adapter
        carouselRecyclerview1.set3DItem(true)
        carouselRecyclerview1.setInfinite(true)
        carouselRecyclerview1.setAlpha(true)
        carouselRecyclerview1.setFlat(true)
        carouselRecyclerview1.setOrientation(1)
        carouselRecyclerview1.setIsScrollingEnabled(false)

        val carouselRecyclerview2 = findViewById<CarouselRecyclerview>(R.id.carouselRecyclerview2)
        carouselRecyclerview2.adapter = adapter
        carouselRecyclerview2.set3DItem(true)
        carouselRecyclerview2.setInfinite(true)
        carouselRecyclerview2.setAlpha(true)
        carouselRecyclerview2.setFlat(true)
        carouselRecyclerview2.setOrientation(1)
        carouselRecyclerview2.setIsScrollingEnabled(false)

        val carouselRecyclerview3 = findViewById<CarouselRecyclerview>(R.id.carouselRecyclerview3)
        carouselRecyclerview3.adapter = adapter
        carouselRecyclerview3.set3DItem(true)
        carouselRecyclerview3.setInfinite(true)
        carouselRecyclerview3.setAlpha(true)
        carouselRecyclerview3.setFlat(true)
        carouselRecyclerview3.setOrientation(1)
        carouselRecyclerview3.setIsScrollingEnabled(false)

        val carouselLayoutManager = carouselRecyclerview1.getCarouselLayoutManager()
        val vibe: Vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val dataBase : DataBase = DataBase(this)


        //крутим
        spinBtn.setOnClickListener {
            if(bet<=balance){
            vibe.vibrate(50)
                //запрещаем клик во время спина
                spinBtn.isClickable=false
                plusBtn.isClickable=false
                minusBtn.isClickable=false
                //крутим
            spin(carouselRecyclerview1, carouselRecyclerview2, carouselRecyclerview3, carouselLayoutManager)

            //получаем результат после кручения
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    //carouselRecyclerview1.getSelectedPosition() возвращает текующую позицию
                    balance=checkWin(carouselRecyclerview1.getSelectedPosition(), carouselRecyclerview2.getSelectedPosition(), carouselRecyclerview3.getSelectedPosition(), spinBtn, bet, balance, plusBtn, minusBtn, dataBase)
                    moneyBalance.text = balance.toString()
                },
                4500
            )
        }else {
                Toast.makeText(this, "Top up your balance", Toast.LENGTH_SHORT)   //Если ставка выше баланса
                    .show()
            }
        }

    }



    //проверяем результат
    fun checkWin(
        position1: Int,
        position2: Int,
        position3: Int,
        spinBtn: ImageButton,
        bet: Int,
        balance: Int,
        plusBtn: ImageButton,
        minusBtn: ImageButton,
        dataBase: DataBase
    ): Int{

        val anim: Animation = AnimationUtils.loadAnimation(this, R.anim.alpha)
        val animEnd: Animation = AnimationUtils.loadAnimation(this, R.anim.anim_end)
        var balanceAfter : Int

        if (position1 == position2 && position2 == position3){          //если выйграл
            balanceAfter=balance+bet*2
            findViewById<ImageView>(R.id.winImage).startAnimation(anim)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    dataBase.insertData(R.drawable.win_ico.toString(), position1.toString(), position2.toString(), position3.toString())
                }
                override fun onAnimationEnd(animation: Animation) {
                    Handler(Looper.getMainLooper()).postDelayed(        //ждем пока аниамция закончится
                        {
                            spinBtn.isClickable=true
                            plusBtn.isClickable=true
                            minusBtn.isClickable=true
                        },
                        100
                    )
                }
                override fun onAnimationRepeat(animation: Animation) {}
            })

        } else {                                                    //если проиграл
            balanceAfter=balance-bet
            findViewById<ImageView>(R.id.loseImage).startAnimation(anim)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    dataBase.insertData(R.drawable.lose_ico.toString(), position1.toString(), position2.toString(), position3.toString())
                }
                override fun onAnimationEnd(animation: Animation) {
                    Handler(Looper.getMainLooper()).postDelayed(        //ждем пока аниамция закончится
                        {
                            spinBtn.isClickable=true
                            plusBtn.isClickable=true
                            minusBtn.isClickable=true

                        },
                        100
                    )
                }
                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
        if (balanceAfter==0){                                               //анимаия завершения игры при 0 балансе
            findViewById<ImageView>(R.id.endImage).startAnimation(animEnd)
            animEnd.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            spinBtn.isClickable=true
                            plusBtn.isClickable=true
                            minusBtn.isClickable=true
                        },
                        100
                    )
                }
                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
        return balanceAfter                             //возвращаем баланс после игры
    }

    //логика кручения
    fun spin(
        carouselRecyclerview1: CarouselRecyclerview,
        carouselRecyclerview2: CarouselRecyclerview,
        carouselRecyclerview3: CarouselRecyclerview,
        carouselLayoutManager: CarouselLayoutManager,
    ) {

        val handler = Handler()
        val runnable1: Runnable = object : Runnable {//
            override fun run() {
                runOnUiThread {
                carouselRecyclerview1.smoothSnapToPosition((0 + Math.random() * 7).toInt(), 1) //логика кручения спина
                carouselRecyclerview2.smoothSnapToPosition((0 + Math.random() * 7).toInt(), 1) //smoothSnapToPosition()переход на нужную позицию
                carouselRecyclerview3.smoothSnapToPosition((0 + Math.random() * 7).toInt(), 1)
                carouselLayoutManager.centerPosition()
                handler.postDelayed(this, 10L)
                    }
            }
        }
        handler.postDelayed(runnable1, 0)

        val myTimer: Timer
        myTimer = Timer()

        myTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.removeCallbacks(runnable1)
            }

        }, 3990, 4000)
    }


    //плавный переход на позицию по авто скролу
    fun RecyclerView.smoothSnapToPosition(position: Int, snapMode: Int = LinearSmoothScroller.SNAP_TO_START) {
        val smoothScroller = object : LinearSmoothScroller(this.context) {
            override fun getVerticalSnapPreference(): Int = snapMode
            override fun getHorizontalSnapPreference(): Int = snapMode
        }
        smoothScroller.targetPosition = position
        layoutManager?.startSmoothScroll(smoothScroller)
    }

}
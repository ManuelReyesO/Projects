package com.example.conectaconmigo.Juegos

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.conectaconmigo.R


private var offsetX = 0f
private var offsetY = 0f

@SuppressLint("ClickableViewAccessibility")

class DrawLetterGame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_letters_game)

        val button = findViewById<Button>(R.id.btnLetter)
        val drawingView = findViewById<DrawingView>(R.id.Letter)


        button.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        offsetX = event.rawX - v.x
                        offsetY = event.rawY - v.y

                        drawingView.drawLine(offsetX, offsetY)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        var newX = event.rawX - offsetX
                        var newY = event.rawY - offsetY

                        if (newX < drawingView.left) {
                            newX = drawingView.left.toFloat()
                        } else if (newX + v.width > drawingView.right) {
                            newX = (drawingView.right - v.width).toFloat()
                        }

                        if (newY < drawingView.top) {
                            newY = drawingView.top.toFloat()
                        } else if (newY + v.height > drawingView.bottom) {
                            newY = (drawingView.bottom - v.height).toFloat()
                        }

                        v.x = newX
                        v.y = newY

                        drawingView.drawLine(v.x, v.y)
                    }
                }
                return true
            }
        })
    }
}

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs){
    private val paint = Paint()
    private val path = Path()

    init {
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
    }

    fun drawLine(x1: Float, y1: Float) {
        path.moveTo(x1, y1)
        path.lineTo(x1, y1)
        invalidate()
    }
}






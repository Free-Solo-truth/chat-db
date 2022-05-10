package com.example.foodrecipes.ui.Activity1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.foodrecipes.R
import com.example.foodrecipes.model.Save_temp
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_show_food.*
import kotlinx.android.synthetic.main.popupwindowlayout.*
import kotlin.properties.Delegates

class show_foodActivity : BaseActivity() {
    var position:Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_food)

        position  = intent.getStringExtra("position")?.toInt()
        Log.v("pppq","litenfei")
        init(position!!)

    }
    fun init(position:Int){

        Save_temp.foodinfo?.get(position)?.apply {
            Picasso.with(showFood_imageView.context).load(image).into(showFood_imageView)
            title1.setText(title)
            desc.setText("\t\t\t"+summary.replace(Regex("\\<b>|\\</b>|\\<a>|\\</a>"),""))
            if(lowFodmap){
                lowFodmap1.checkMarkDrawable = getDrawable(R.drawable.type_check)
            }
            if(vegan){
                vegan1.checkMarkDrawable = getDrawable(R.drawable.type_check)
            }
            if(vegetarian){
                vegetarian1.checkMarkDrawable = getDrawable(R.drawable.type_check)
            }
            if(veryPopular){
                veryPopular1.checkMarkDrawable = getDrawable(R.drawable.type_check)
            }
            if(sustainable){
                sustainable1.checkMarkDrawable = getDrawable(R.drawable.type_check)
            }
            if(dairyFree){
                dairyFree1.checkMarkDrawable = getDrawable(R.drawable.type_check)
            }
        }

    }
}
package com.example.foodrecipes.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipes.R
import com.example.foodrecipes.model.OneFavorityMessage
import com.example.foodrecipes.model.Save_temp
import com.example.foodrecipes.model.SendList
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.favority_layout.view.*

class FavorityAdapter(var FavInfo:SendList?): RecyclerView.Adapter<FavorityAdapter.favorityViewHolder>() {
    inner class favorityViewHolder(val viewitem:View):RecyclerView.ViewHolder(viewitem){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): favorityViewHolder {
        return   favorityViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.favority_layout,parent,false))

    }

    override fun getItemCount(): Int {
        if (FavInfo!=null){
            Log.v("Main",FavInfo!!.favMeg.size.toString())
            return FavInfo!!.favMeg.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: favorityViewHolder, position: Int) {
        Log.v("FavorityAdapter",position.toString())
        holder.viewitem.favoritytitle.setText(FavInfo?.favMeg?.get(position)?.favoritytitle)
        Save_temp.favfoodTitle.add(FavInfo?.favMeg?.get(position)?.favoritytitle)
        Save_temp.title_position.set(FavInfo?.favMeg?.get(position)!!.favoritytitle,position.toString())
        Picasso.with(holder.viewitem.circleImageView.context)
                .load(FavInfo?.favMeg?.get(position)?.favorityimage)
                .resize(100,100)
                .into(holder.viewitem.circleImageView)
        holder.viewitem.favoritysubit.setText(FavInfo?.favMeg?.get(position)?.favoritysubit?.replace("</b>","\n")
        )
    }
}
package ru.hh.photo_picker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ru.hh.photo_picker.PhotoAdapter.ViewHolder


internal class PhotoAdapter(
    private val onPhotoClicked: (Photo) -> Unit
) : ListAdapter<Photo, ViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val imageView = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false) as ImageView
        return ViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = getItem(position)
        with(holder.imageView) {
            load(photo.url)
            setOnClickListener { onPhotoClicked(photo) }
        }
    }

    class ViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    class DiffUtilCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }

}
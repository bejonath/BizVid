package com.example.project.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner // Import LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.data.VideoItem
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView



class VideoListAdapter(
    private var videos: List<VideoItem>,
    private val lifecycleOwner: LifecycleOwner // Pass LifecycleOwner from Activity/Fragment
) : RecyclerView.Adapter<VideoListAdapter.VideoViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.video_list_item, parent, false) // Use updated layout
        return VideoViewHolder(view, lifecycleOwner)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]
        holder.bind(video)
    }

    override fun getItemCount(): Int = videos.size

    fun updateData(newVideos: List<VideoItem>) {
        videos = newVideos
        notifyDataSetChanged()
    }

    // ViewHolder handles initializing and binding the player
    class VideoViewHolder(
        itemView: View,
        lifecycleOwner: LifecycleOwner
    ) : RecyclerView.ViewHolder(itemView) {

        // Find views using the new IDs from the layout
        private val titleTextView: TextView = itemView.findViewById(R.id.videoTitleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.videoDescriptionTextView)
        private val youtubePlayerView: YouTubePlayerView = itemView.findViewById(R.id.youtubePlayerView)

        private var currentVideoId: String? = null
        private var youTubePlayer: YouTubePlayer? = null

        init {
            // Add player view to lifecycle observer
            lifecycleOwner.lifecycle.addObserver(youtubePlayerView)

            // Add the listener *once* when the ViewHolder is created
            youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(player: YouTubePlayer) {
                    // Store the player instance
                    youTubePlayer = player
                    // If a video ID was already set by bind(), load it now
                    currentVideoId?.let {
                        youTubePlayer?.cueVideo(it, 0f)
                    }
                }
            })
        }

        fun bind(videoItem: VideoItem) {
            // Update descriptive views
            titleTextView.text = videoItem.title
            descriptionTextView.text = videoItem.description
            // Store the new video ID.
            currentVideoId = videoItem.id
            youTubePlayer?.cueVideo(currentVideoId!!, 0f)

        }
    }
    override fun onViewRecycled(holder: VideoViewHolder) {
        super.onViewRecycled(holder)

    }
} 
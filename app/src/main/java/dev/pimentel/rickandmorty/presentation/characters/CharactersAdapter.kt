package dev.pimentel.rickandmorty.presentation.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import dev.pimentel.rickandmorty.R
import dev.pimentel.rickandmorty.databinding.CharactersItemBinding
import dev.pimentel.rickandmorty.presentation.characters.dto.CharactersItem

class CharactersAdapter : ListAdapter<CharactersItem, CharactersAdapter.ViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            CharactersItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class ViewHolder(
        private val binding: CharactersItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(character: CharactersItem, position: Int) {
            binding.apply {
                image.load(character.image)
                status.text = character.status
                name.text = character.name

                val layoutParams = root.layoutParams as ViewGroup.MarginLayoutParams

                val biggerMargin = root.resources.getDimensionPixelSize(
                    R.dimen.characters_item_bigger_margin
                )
                val smallerMargin = root.resources.getDimensionPixelSize(
                    R.dimen.characters_item_smaller_margin
                )

                if (position % 2 == 0) {
                    layoutParams.marginStart = biggerMargin
                    layoutParams.marginEnd = smallerMargin
                } else {
                    layoutParams.marginStart = smallerMargin
                    layoutParams.marginEnd = biggerMargin
                }
            }
        }
    }

    private companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CharactersItem>() {
            override fun areItemsTheSame(
                oldItem: CharactersItem,
                newItem: CharactersItem
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: CharactersItem,
                newItem: CharactersItem
            ) = oldItem.id == newItem.id
        }
    }
}
package dev.syafii.chatapps.controller.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.syafii.chatapps.databinding.CustomMyChatBinding
import dev.syafii.chatapps.databinding.CustomOtherChatBinding
import dev.syafii.chatapps.model.ChatModel

class MainAdapter(
    private val viewModel: MainViewModel,
    private val userName: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val MY_CHAT = 0
    private val OTHER_CHAT = 1

    private var chaList: List<ChatModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == MY_CHAT) {
            val view = CustomMyChatBinding.inflate(inflater, parent, false)
            MyChatHolder(view.root, view)
        } else {
            val view = CustomOtherChatBinding.inflate(inflater, parent, false)
            OtherChatHolder(view.root, view)

        }
    }

    override fun getItemCount(): Int {
        return chaList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = chaList[position]
        if (holder is MyChatHolder) {
            holder.bindView(chat)
        } else {
            if (holder is OtherChatHolder) {
                holder.bindView(chat)
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        val chat = chaList[position]
        return if (chat.sender.orEmpty() == userName) {
            MY_CHAT
        } else OTHER_CHAT
    }

    fun setChatDataList(dataChatList: List<ChatModel>){
        this.chaList = dataChatList
        notifyDataSetChanged()
    }

    inner class MyChatHolder(
        view: View,
        val binding: CustomMyChatBinding
    ) : RecyclerView.ViewHolder(view) {
        fun bindView(chat: ChatModel) {
            binding.tvSender.text = chat.sender.orEmpty()
            binding.tvMessage.text = chat.message.orEmpty()

        }
    }

    inner class OtherChatHolder(
        view: View,
        val binding: CustomOtherChatBinding
    ) : RecyclerView.ViewHolder(view) {
        fun bindView(chat: ChatModel) {
            binding.tvSender.text = chat.sender.orEmpty()
            binding.tvMessage.text = chat.message.orEmpty()
        }
    }


}
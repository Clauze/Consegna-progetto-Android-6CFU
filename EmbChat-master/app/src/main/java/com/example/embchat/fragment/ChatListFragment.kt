package com.example.embchat.fragment

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.embchat.adapter.ChatListAdapter
import com.example.embchat.data.ChatApplication
import com.example.embchat.viewModel.ChatViewModel
import com.example.embchat.viewModel.ChatViewModelFactory
import com.example.embchat.R
import com.example.embchat.data.entity.Chat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class ChatListFragment : Fragment() {

    private val chatViewModel: ChatViewModel by viewModels {
        val application = requireNotNull(this.activity).application
        ChatViewModelFactory((application as ChatApplication).repositoryChat)
    }
    private lateinit var view : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        view = inflater.inflate(R.layout.fragment_chat_list, container, false)

        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.title=""
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        val help = toolbar.findViewById<ImageView>(R.id.help_button)

        help.setOnClickListener{
            AlertDialog.Builder(view.context)
                .setTitle("Info")
                .setMessage("Designed by Claudio Battistin")
                .setPositiveButton("ok", null)
                .show()
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        val adapter = ChatListAdapter (
            onDeleteClick = {chat ->
                chatViewModel.delete(chat)
            }
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        val fabAdd = view.findViewById<FloatingActionButton>(R.id.newChat)
        fabAdd.setOnClickListener{
             showAddPhoneNumberDialog()
        }

        chatViewModel.allChats.observe(viewLifecycleOwner){chat ->
            chat.let { adapter.submitList(it) }
        }

        return view
    }

    /**
     * Apre un dialog in cui è possibile inserire un nuovo numero di telefono
     */
    private fun showAddPhoneNumberDialog() {
        val dialogView = LayoutInflater.from(view.context).inflate(R.layout.create_chat_dialog_box, null)

        val dialog = Dialog(view.context)
        dialog.setContentView(dialogView)
        val editTextPhoneNumber = dialog.findViewById<EditText>(R.id.edit_text_phone_number)
        val btnSave = dialog.findViewById<Button>(R.id.btn_save)
        val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel)


        dialog.setTitle("Add Phone Number")
        dialog.setContentView(dialogView)
        btnSave.setOnClickListener{

            val phoneNumber = editTextPhoneNumber.text.toString()
            val pattern = "\\d*"
            if (phoneNumber.isNotEmpty() && Regex(pattern).matches(phoneNumber)) {
                lifecycleScope.launch {
                    val existChat = chatViewModel.findChat(phoneNumber)

                    if(existChat == null) {

                        val newChat = Chat(phoneNumber = phoneNumber)
                        chatViewModel.insert(newChat)
                        Toast.makeText(context, "Phone number added", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    } else{
                        Toast.makeText(context, "Phone number already exist", Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
                Toast.makeText(context, "Please enter a phone number", Toast.LENGTH_SHORT).show()
            }
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_box_background)

        dialog.show()
    }
}
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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.embchat.R
import com.example.embchat.adapter.MessageListAdapter
import com.example.embchat.data.ChatApplication
import com.example.embchat.data.entity.Message
import com.example.embchat.viewModel.MessagesViewModel
import com.example.embchat.viewModel.MessagesViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

/**
 * Fragment interfaccia con messaggi
 */
class MessagesListFragment : Fragment() {

    private val messageViewModel: MessagesViewModel by viewModels {
        val application = requireNotNull(this.activity).application
        MessagesViewModelFactory((application as ChatApplication).repositoryMessage)
    }
    private lateinit var inflaterView : View
    private var chatId = -1
    private var phoneNumber = String()
    private val args: MessagesListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val chat = args.chatId
        chatId = chat.chatId
        phoneNumber = chat.phoneNumber
        MessagesViewModel.setChatId(chatId)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        inflaterView = inflater.inflate(R.layout.fragment_chat_messages, container, false)

        val toolbar: Toolbar = inflaterView.findViewById(R.id.toolbar)
        toolbar.title = ""
        val phoneNumberTextView = toolbar.findViewById<TextView>(R.id.phone_number)
        phoneNumberTextView.text = phoneNumber
        val backButton = toolbar.findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener{
            findNavController().navigateUp()
        }
        val deleteButton = toolbar.findViewById<ImageView>(R.id.delete_messages)
        deleteButton.setOnClickListener {
            deleteMessages()
        }
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val recyclerView: RecyclerView = inflaterView.findViewById(R.id.messages_list)
        val adapter = MessageListAdapter()
        recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(inflater.context)
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager

        val fabInsertMessage : FloatingActionButton = inflaterView.findViewById(R.id.send_message)
        val editTextMessage : EditText = inflaterView.findViewById(R.id.text_insert)

        fabInsertMessage.setOnClickListener{
            val message = editTextMessage.text
            if(message.isNotEmpty()){
                val time = System.currentTimeMillis()
                messageViewModel.insert(Message(chat = chatId, sentByMe = true, text = message.toString(), timestamp = time))
                messageViewModel.insert(Message(chat = chatId, sentByMe = false, text = "Hi! Sorry I can't help you", timestamp = (time+1)))
                editTextMessage.text.clear()
            }
            else{
                Toast.makeText(context, "Insert a valid message", Toast.LENGTH_SHORT).show()
            }
        }

        messageViewModel.allMessage.observe(viewLifecycleOwner){messages ->
            messages?.let {
                Log.d("MessagesListFragment", "Messages updated: ${it.size}")
                adapter.submitList(it)

                if(it.isNotEmpty()) {
                    recyclerView.post {
                        recyclerView.smoothScrollToPosition(it.size )
                        Log.d("MessagesListFragment", "RecyclerView scrolled to position: ${it.size }")
                    }

                }
            }
        }

        return inflaterView
    }

    /**
     * Apre un dialog in cui viene richiesto all'utente quanti messaggi eliminare
     */
    private fun deleteMessages() {

            val dialogView = LayoutInflater.from(inflaterView.context).inflate(R.layout.delete_chat_dialog_box, null)

            val dialog = Dialog(inflaterView.context)
            dialog.setContentView(dialogView)
            val editNumber = dialog.findViewById<EditText>(R.id.number_delete)
            val btnDelete = dialog.findViewById<Button>(R.id.btn_delete)
            val btnCancel = dialog.findViewById<Button>(R.id.btn_cancel)

            dialog.setTitle("Delete messages")
            dialog.setContentView(dialogView)
            btnDelete.setOnClickListener{
                try {
                    val numberDeleteString =  editNumber.text.toString()

                    if (numberDeleteString.isNotEmpty()) {

                        val numberDelete = numberDeleteString.toInt()

                        if(numberDelete != 0) {

                            messageViewModel.delete(numberDelete)

                            if (numberDelete == 1) {
                                Toast.makeText(
                                    context,
                                    "Delete $numberDelete message",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Delete $numberDelete messages or entire chat",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        else{
                            Toast.makeText(context, "No messages deleted", Toast.LENGTH_SHORT).show()
                        }

                        dialog.dismiss()

                    } else {
                        Toast.makeText(context, "Please enter a number", Toast.LENGTH_SHORT).show()
                    }
                }
                catch (e: NumberFormatException){
                    Toast.makeText(context, "Please enter a valid number", Toast.LENGTH_SHORT).show()
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
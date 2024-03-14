import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.todo_list1.Item
import com.example.todo_list1.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class MyAdapter(private val dataList: MutableList<Item>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    // Firebase database reference
    private lateinit var myRecord: DatabaseReference

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.textView)
        var fabEdit: FloatingActionButton = itemView.findViewById(R.id.fabEdit)
        var fabDelete: FloatingActionButton = itemView.findViewById(R.id.fabDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.textView.text = "${currentItem.task} - ${currentItem.description}"

        holder.fabDelete.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("Confirm Delete")
            builder.setMessage("Are you sure you want to delete this item?")

            builder.setPositiveButton("Confirm") { _, _ ->
                // Delete the item from Firebase database
                val task = currentItem.task
                if (task != null) {
                    myRecord.child(task).removeValue()
                }

                // Delete the item from RecyclerView
                dataList.removeAt(position)
                notifyItemRemoved(position)
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        holder.fabEdit.setOnClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            val inflater = LayoutInflater.from(holder.itemView.context)
            val dialogView = inflater.inflate(R.layout.edit_dialog, null)
            builder.setView(dialogView)

            val updateButton: Button = dialogView.findViewById(R.id.updateButton)
            val cancelButton: Button = dialogView.findViewById(R.id.cancelButton)
            val editText: EditText = dialogView.findViewById(R.id.editText1)
            val editTxt : EditText = dialogView.findViewById(R.id.editText2)

            editText.setText(currentItem.task)
            editTxt.setText(currentItem.description)

            val dialog = builder.create()
            dialog.show()

            updateButton.setOnClickListener {
                val newData1 = editText.text.toString()
                val newData2 = editTxt.text.toString()

                // Update the item in Firebase database
                val task = currentItem.task
                val updatedItem = Item(newData1, newData2)
                if (task != null) {
                    myRecord.child(task).setValue(updatedItem)
                }

                // Update the item in RecyclerView
                currentItem.task = newData1
                currentItem.description = newData2
                notifyItemChanged(position)

                dialog.dismiss()
            }

            cancelButton.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    init {
        // Initialize Firebase database reference
        myRecord = FirebaseDatabase.getInstance().getReference("Todo List")
        myRecord.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataList.clear()
                for (postSnapshot in dataSnapshot.children) {
                    val item = postSnapshot.getValue(Item::class.java)
                    dataList.add(item!!)
                }
                notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
            }
        })
    }
}

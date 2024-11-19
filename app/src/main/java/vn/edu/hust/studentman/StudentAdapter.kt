package vn.edu.hust.studentman

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.edu.hust.studentman.databinding.LayoutStudentItemBinding

class StudentAdapter(
  private val students: List<StudentModel>,
  private val onAction: (StudentModel, String) -> Unit
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

  inner class StudentViewHolder(val binding: LayoutStudentItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(student: StudentModel) {
      binding.textStudentName.text = student.studentName
      binding.textStudentId.text = student.studentId

      binding.imageEdit.setOnClickListener {
        onAction(student, "edit")
      }

      binding.imageRemove.setOnClickListener {
        onAction(student, "delete")
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
    val binding = LayoutStudentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return StudentViewHolder(binding)
  }

  override fun getItemCount(): Int = students.size

  override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
    holder.bind(students[position])
  }
}

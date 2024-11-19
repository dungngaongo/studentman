package vn.edu.hust.studentman

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import vn.edu.hust.studentman.databinding.ActivityMainBinding
import vn.edu.hust.studentman.databinding.DialogAddEditStudentBinding

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private lateinit var studentAdapter: StudentAdapter
  private val students = mutableListOf<StudentModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    initializeStudents()

    studentAdapter = StudentAdapter(students) { student, action ->
      when (action) {
        "edit" -> showAddEditDialog(student)
        "delete" -> confirmDeleteStudent(student)
      }
    }

    binding.recyclerViewStudents.apply {
      adapter = studentAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    binding.btnAddNew.setOnClickListener {
      showAddEditDialog(null)
    }
  }

  private fun initializeStudents() {
    students.addAll(
      listOf(
        StudentModel("Nguyễn Văn An", "SV001"),
        StudentModel("Trần Thị Bảo", "SV002"),
        StudentModel("Lê Văn Vũ", "SV020")
      )
    )
  }

  private fun showAddEditDialog(student: StudentModel?) {
    val dialogBinding = DialogAddEditStudentBinding.inflate(LayoutInflater.from(this))
    val dialogBuilder = AlertDialog.Builder(this)
      .setView(dialogBinding.root)
      .setCancelable(false)

    if (student != null) {
      dialogBinding.editStudentName.setText(student.studentName)
      dialogBinding.editStudentId.setText(student.studentId)
      dialogBuilder.setTitle("Chỉnh sửa sinh viên")
    } else {
      dialogBuilder.setTitle("Thêm sinh viên mới")
    }

    val dialog = dialogBuilder.create()

    dialogBinding.btnSave.setOnClickListener {
      val name = dialogBinding.editStudentName.text.toString()
      val id = dialogBinding.editStudentId.text.toString()

      if (name.isNotEmpty() && id.isNotEmpty()) {
        if (student != null) {
          student.studentName = name
          student.studentId = id
          studentAdapter.notifyDataSetChanged()
        } else {
          val newStudent = StudentModel(name, id)
          students.add(newStudent)
          studentAdapter.notifyItemInserted(students.size - 1)
        }
        dialog.dismiss()
      } else {
        Snackbar.make(binding.root, "Vui lòng nhập đầy đủ thông tin", Snackbar.LENGTH_SHORT).show()
      }
    }

    dialogBinding.btnCancel.setOnClickListener {
      dialog.dismiss()
    }

    dialog.show()
  }

  private fun confirmDeleteStudent(student: StudentModel) {
    AlertDialog.Builder(this)
      .setTitle("Xóa sinh viên")
      .setMessage("Bạn có chắc chắn muốn xóa sinh viên này không?")
      .setPositiveButton("Xóa") { _, _ ->
        val position = students.indexOf(student)
        students.removeAt(position)
        studentAdapter.notifyItemRemoved(position)

        Snackbar.make(binding.root, "Đã xóa ${student.studentName}", Snackbar.LENGTH_LONG)
          .setAction("Hoàn tác") {
            students.add(position, student)
            studentAdapter.notifyItemInserted(position)
          }.show()
      }
      .setNegativeButton("Hủy", null)
      .show()
  }
}

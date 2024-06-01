package com.dacs.vku.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dacs.vku.R
import com.dacs.vku.models.Schedule

class ScheduleAdapter(
    private val scheduleList: MutableList<Schedule>,
    private val onEditClick: (Schedule) -> Unit,
    private val onDeleteClick: (Schedule) -> Unit,
    private var groupedSchedules: Map<String, List<Schedule>>

) : RecyclerView.Adapter<ScheduleAdapter.GroupedScheduleViewHolder>() {

    class ScheduleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayOfWeek: TextView = view.findViewById(R.id.tvDayOfWeek)
        val time: TextView = view.findViewById(R.id.tvTime)
        val room: TextView = view.findViewById(R.id.tvRoom)
        val subject: TextView = view.findViewById(R.id.tvSubject)
        val editButton: ImageView = view.findViewById(R.id.editButton)
        val deleteButton: ImageView = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupedScheduleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grouped_schedule, parent, false)
        return GroupedScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupedScheduleViewHolder, position: Int) {
        val dayOfWeek = groupedSchedules.keys.elementAt(position)
        val schedules = groupedSchedules[dayOfWeek]

        holder.tvDayOfWeek.text = dayOfWeek
        schedules?.let {
            holder.tvSchedules.text = it.joinToString("\n") { schedule ->
                "${schedule.time} - ${schedule.room} - ${schedule.subject}"
            }
        }
    }

    override fun getItemCount(): Int = scheduleList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateScheduleList(newList: List<Schedule>) {
        scheduleList.clear()
        scheduleList.addAll(newList)
        notifyDataSetChanged()
    }
    class GroupedScheduleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDayOfWeek: TextView = view.findViewById(R.id.tvDayOfWeek)
        val tvSchedules: TextView = view.findViewById(R.id.tvSchedules)
    }
}
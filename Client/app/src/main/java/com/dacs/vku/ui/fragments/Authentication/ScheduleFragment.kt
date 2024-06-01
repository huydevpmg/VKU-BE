package com.dacs.vku

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dacs.vku.adapters.ScheduleAdapter
import com.dacs.vku.databinding.FragmentScheduleBinding
import com.dacs.vku.models.Schedule


class ScheduleFragment : Fragment() {
    private lateinit var binding: FragmentScheduleBinding
    private lateinit var scheduleAdapter: ScheduleAdapter
    private val scheduleList = mutableListOf<Schedule>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dummy data
        scheduleList.add(Schedule("Thứ 2", "09:00", "Room 101", "Math"))
        scheduleList.add(Schedule("Thứ 3", "10:00", "Room 102", "Science"))
        scheduleList.add(Schedule("Thứ 4", "11:00", "Room 103", "History"))
        scheduleList.add(Schedule("Thứ 5", "12:00", "Room 104", "Geography"))
        scheduleList.add(Schedule("Thứ 6", "13:00", "Room 105", "Physics"))
        scheduleList.add(Schedule("Thứ 7", "14:00", "Room 106", "Chemistry"))
        scheduleList.add(Schedule("Chủ Nhật", "15:00", "Room 107", "English"))

        // Sort scheduleList by dayOfWeek
        val sortedList = scheduleList.sortedBy { getDayOfWeekIndex(it.dayOfWeek) }

        scheduleAdapter = ScheduleAdapter(sortedList.toMutableList(),
            onEditClick = { schedule -> editSchedule(schedule) },
            onDeleteClick = { schedule -> deleteSchedule(schedule) }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = scheduleAdapter
        }
    }

    private fun getDayOfWeekIndex(dayOfWeek: String): Int {
        return when (dayOfWeek) {
            "Thứ 2" -> 1
            "Thứ 3" -> 2
            "Thứ 4" -> 3
            "Thứ 5" -> 4
            "Thứ 6" -> 5
            "Thứ 7" -> 6
            "Chủ Nhật" -> 7
            else -> 0
        }
    }

    private fun editSchedule(schedule: Schedule) {
        // Implement edit functionality here, maybe show a dialog with form to edit the schedule
        // Once edited, send the updated schedule to the server and update the local list
    }

    private fun deleteSchedule(schedule: Schedule) {
        // Send request to server to delete the schedule
        // On success, update the local list and notify the adapter
        scheduleList.remove(schedule)
        scheduleAdapter.updateScheduleList(scheduleList)
    }
}
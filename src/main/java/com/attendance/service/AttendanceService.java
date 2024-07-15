package com.attendance.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.attendance.model.Attendance;
import com.attendance.model.User;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.UserRepository;

@Service
public class AttendanceService {

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	UserRepository userRepository;

	public void saveAttendance(Attendance attendance) {
		attendanceRepository.save(attendance);
	}

//	public Attendance findLastClockIn(Long userId) {
//		List<Attendance> attendances = attendanceRepository.findByUserId(userId);
//		return attendances.stream().filter(a -> a.getClockOutTime() == null).findFirst().orElse(null);
//	}

	public void checkIn(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        LocalTime currentTime = now.toLocalTime();

        // Check if the user has already checked in today
        Optional<Attendance> existingAttendance = attendanceRepository.findByUserAndDate(user, today);

        if (existingAttendance.isPresent()) {
            throw new IllegalStateException("User has already checked in today.");
        }

        // Proceed with check-in if no existing record is found
        Attendance attendance = new Attendance();
        attendance.setDate(today);
        attendance.setUser(user);
        attendance.setClockInTime(currentTime);  // Store only the time component
        attendance.setStatus("PRESENT");

        attendanceRepository.save(attendance);
    }

	public void checkOut(User user) {
	    LocalDateTime now = LocalDateTime.now();
	    LocalDate today = now.toLocalDate();
	    LocalTime currentTime = now.toLocalTime();

	    // Find the most recent attendance record for the user where clock out time is null
	    Optional<Attendance> existingAttendanceOpt = attendanceRepository.findByUserAndDate(user, today);

	    if (!existingAttendanceOpt.isPresent()) {
	        throw new IllegalStateException("User has not checked in today.");
	    }

	    Attendance attendance = existingAttendanceOpt.get();

	    // Check if the user has already checked out
	    if (attendance.getClockOutTime() != null) {
	        throw new IllegalStateException("User has already checked out today.");
	    }

	    // Proceed with check-out
	    attendance.setClockOutTime(currentTime); // Store only the time component
	    attendanceRepository.save(attendance);
	}


    public List<Attendance> getAttendanceByUserId(Long userId) {
    	
    	
        return attendanceRepository.findByUserId(userId);
    }

    public List<Map<String, Object>> getAllAttendances() {
        List<Attendance> attendanceList = attendanceRepository.findAll();
        List<Map<String, Object>> attendances = new ArrayList<>();

        for (Attendance attendance : attendanceList) {
            Map<String, Object> attendanceData = new HashMap<>();
            attendanceData.put("userName", attendance.getUser().getUsername());
            attendanceData.put("clockInTime", attendance.getClockInTime());
            attendanceData.put("clockOutTime", attendance.getClockOutTime());
            attendanceData.put("date", attendance.getDate());
            attendances.add(attendanceData);
        }

        return attendances;
    }	

}

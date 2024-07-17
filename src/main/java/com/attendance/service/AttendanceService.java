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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.attendance.model.Attendance;
import com.attendance.model.User;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AttendanceService {

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;

	public void saveAttendance(Attendance attendance) {
		attendanceRepository.save(attendance);
	}
	
	
	@Scheduled(cron = "0 0 6 * * *") 
	public void markAbsentUsers() {
	    LocalDateTime now = LocalDateTime.now();
	    LocalDate today = LocalDate.now();
	    LocalDateTime fiveMinutesAgo = now.minusMinutes(5);

	    List<User> allUsers = userService.getAllUsers();
	    // Implement userService.getAllUsers() as needed
	    log.info("ALL user"+ allUsers);

	    for (User user : allUsers) {
	        Optional<Attendance> attendanceOpt = attendanceRepository.findByUserAndDate(user, today);

	        if (!attendanceOpt.isPresent()) {
	            // User has not checked in at all today
	            Attendance absentAttendance = new Attendance();
	            absentAttendance.setUser(user);
	            absentAttendance.setDate(today);
	            absentAttendance.setClockInTime(null);
	            absentAttendance.setClockOutTime(null);
	            absentAttendance.setStatus("ABSENT");
	            attendanceRepository.save(absentAttendance);
	        }
	    }
	}

	


	public void checkIn(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        LocalTime currentTime = now.toLocalTime();

      
        Optional<Attendance> existingAttendance = attendanceRepository.findByUserAndDate(user, today);

        if (existingAttendance.isPresent()) {
            throw new IllegalStateException("User has already checked in today.");
        }

     
        Attendance attendance = new Attendance();
        attendance.setDate(today);
        attendance.setUser(user);
        attendance.setClockInTime(currentTime);  
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

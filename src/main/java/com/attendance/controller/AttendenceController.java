package com.attendance.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.attendance.model.Attendance;
import com.attendance.model.User;
import com.attendance.repository.AttendanceRepository;
import com.attendance.security.JWTService;
import com.attendance.service.AttendanceService;
import com.attendance.service.UserService;
import com.attendance.util.Util;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/attendance")
@Slf4j
public class AttendenceController {
	
	@Autowired
	JWTService jwtService;

	@Autowired
	private UserService userService;

	@Autowired
	private AttendanceService attendanceService;
	
	@Autowired
	private AttendanceRepository attendanceRepository;
//
//	@GetMapping("/user")
//	public User getCurrentUser() {
//		String username = Util.getCurrentUsername();
//		return userService.findByUsername(username);
//	}

	@PostMapping("/user/checkin")
	public String checkIn() {
		String username = Util.getCurrentUsername();
		User user = userService.findByUsername(username);

		attendanceService.checkIn(user);
		return "Checked in successfully";
	}

	@PostMapping("/user/checkout")
	public String checkOut() {
		String username = Util.getCurrentUsername();
		System.out.println(username);
		User user = userService.findByUsername(username);
		attendanceService.checkOut(user);
		return "Checked out successfully";
	}
	
	
	 @GetMapping("/user")
	    public ResponseEntity<List<Attendance>> getAttendance(@RequestHeader("Authorization") String token) {
	        String jwtToken = token.substring(7); // Remove "Bearer " prefix
	        Long userId = jwtService.extractUserId(jwtToken);
	        
	        List<Attendance> attendanceRecords = attendanceService.getAttendanceByUserId(userId);
	        log.info("attendance record"  +attendanceRecords);
	        return ResponseEntity.ok(attendanceRecords);
	    }
	
	  @GetMapping("/admin/all")
	    public ResponseEntity<List<Map<String, Object>>> getAllAttendances() {
	        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

	        if (!user.getRole().equalsIgnoreCase("ADMIN")) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	        }

	        List<Map<String, Object>> attendances = attendanceService.getAllAttendances();
	        return ResponseEntity.ok(attendances);
	    }

}

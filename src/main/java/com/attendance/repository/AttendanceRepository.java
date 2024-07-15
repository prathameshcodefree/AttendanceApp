package com.attendance.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.attendance.model.Attendance;
import com.attendance.model.User;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	@Query("SELECT new map(a.id as id, a.user.username as username, a.clockInTime as clockInTime, a.clockOutTime as clockOutTime) FROM Attendance a")
	List<Map<String, Object>> findAllAttendances();
	
	
		
	
	
	Optional<Attendance> findByUserAndDate(User user, LocalDate date);


	Attendance findTopByUserIdAndClockOutTimeIsNullOrderByClockInTimeDesc(Long userId);


	List<Attendance> findByUserId(Long userId);
	

}

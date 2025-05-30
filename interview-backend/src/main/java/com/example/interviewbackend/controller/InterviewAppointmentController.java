package com.example.interviewbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.interviewbackend.entity.InterviewAppointment;
import com.example.interviewbackend.service.InterviewAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class InterviewAppointmentController {

    @Autowired
    private InterviewAppointmentService appointmentService;

    // Simple Response Wrapper
    private static class ApiResponse<T> {
        public int code;
        public String message;
        public T data;

        public ApiResponse(int code, String message, T data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }
    }

    @PostMapping("/")
    public ResponseEntity<ApiResponse<InterviewAppointment>> addAppointment(@RequestBody InterviewAppointment appointment) {
        // Ensure interviewTime is not null and is in the future
        if (appointment.getInterviewTime() == null || appointment.getInterviewTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Interview time must be in the future.", null));
        }
        boolean success = appointmentService.addAppointment(appointment);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(HttpStatus.CREATED.value(), "Appointment added successfully.", appointment));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to add appointment.", null));
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<Page<InterviewAppointment>>> getAllAppointments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String candidateName) {
        Page<InterviewAppointment> pageRequest = new Page<>(page, size);
        Page<InterviewAppointment> appointments = appointmentService.getAllAppointments(pageRequest, candidateName);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Appointments retrieved successfully.", appointments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InterviewAppointment>> getAppointmentById(@PathVariable Long id) {
        InterviewAppointment appointment = appointmentService.getAppointmentById(id);
        if (appointment != null) {
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Appointment found.", appointment));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Appointment not found.", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InterviewAppointment>> updateAppointment(@PathVariable Long id, @RequestBody InterviewAppointment appointment) {
        appointment.setId(id); // Ensure ID is set from path
        // Ensure interviewTime is not null and is in the future if provided
        if (appointment.getInterviewTime() != null && appointment.getInterviewTime().isBefore(LocalDateTime.now())) {
             return ResponseEntity.badRequest().body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Interview time must be in the future.", null));
        }
        boolean success = appointmentService.updateAppointment(appointment);
        if (success) {
            InterviewAppointment updatedAppointment = appointmentService.getAppointmentById(id); // Fetch the updated record
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Appointment updated successfully.", updatedAppointment));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Appointment not found or failed to update.", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteAppointment(@PathVariable Long id) {
        boolean success = appointmentService.deleteAppointment(id);
        if (success) {
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Appointment deleted successfully.", null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Appointment not found or failed to delete.", null));
    }
}

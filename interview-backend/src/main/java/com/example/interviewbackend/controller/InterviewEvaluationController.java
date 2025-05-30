package com.example.interviewbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.interviewbackend.entity.InterviewEvaluation;
import com.example.interviewbackend.service.InterviewEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/evaluations")
public class InterviewEvaluationController {

    @Autowired
    private InterviewEvaluationService evaluationService;

    // Re-using a similar simple Response Wrapper (ideally this would be a shared class)
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
    public ResponseEntity<ApiResponse<InterviewEvaluation>> addEvaluation(@RequestBody InterviewEvaluation evaluation) {
        if (evaluation.getAppointmentId() == null) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Appointment ID is required.", null));
        }
        // evaluationTime is set in the service
        boolean success = evaluationService.addEvaluation(evaluation);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(HttpStatus.CREATED.value(), "Evaluation added successfully.", evaluation));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to add evaluation.", null));
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<Page<InterviewEvaluation>>> getAllEvaluations(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long appointmentId) {
        Page<InterviewEvaluation> pageRequest = new Page<>(page, size);
        Page<InterviewEvaluation> evaluations = evaluationService.getAllEvaluations(pageRequest, appointmentId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Evaluations retrieved successfully.", evaluations));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InterviewEvaluation>> getEvaluationById(@PathVariable Long id) {
        InterviewEvaluation evaluation = evaluationService.getEvaluationById(id);
        if (evaluation != null) {
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Evaluation found.", evaluation));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Evaluation not found.", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InterviewEvaluation>> updateEvaluation(@PathVariable Long id, @RequestBody InterviewEvaluation evaluation) {
        evaluation.setId(id); // Ensure ID is set from path
         if (evaluation.getAppointmentId() == null) { // Basic validation
            return ResponseEntity.badRequest().body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Appointment ID is required for update.", null));
        }
        // Prevent evaluationTime from being client-modifiable for existing records if needed,
        // or ensure it's handled appropriately (e.g., service layer sets it if it's a true update of time)
        // For now, we allow it to be updated if passed in the body.
        boolean success = evaluationService.updateEvaluation(evaluation);
        if (success) {
            InterviewEvaluation updatedEvaluation = evaluationService.getEvaluationById(id);
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Evaluation updated successfully.", updatedEvaluation));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Evaluation not found or failed to update.", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteEvaluation(@PathVariable Long id) {
        boolean success = evaluationService.deleteEvaluation(id);
        if (success) {
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Evaluation deleted successfully.", null));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Evaluation not found or failed to delete.", null));
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<ApiResponse<Page<InterviewEvaluation>>> getEvaluationsByAppointmentId(
            @PathVariable Long appointmentId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<InterviewEvaluation> pageRequest = new Page<>(page, size);
        Page<InterviewEvaluation> evaluations = evaluationService.getEvaluationsByAppointmentId(appointmentId, pageRequest);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Evaluations for appointment retrieved successfully.", evaluations));
    }
}

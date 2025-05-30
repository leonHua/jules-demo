package com.example.interviewbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("interview_evaluation")
public class InterviewEvaluation {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("appointment_id")
    private Long appointmentId;

    @TableField("interviewer_comments")
    private String interviewerComments;

    private Integer rating; // e.g., 1-5 stars

    private String result; // e.g., "Hired", "Rejected", "Pending"

    @TableField("evaluation_time")
    private LocalDateTime evaluationTime;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getInterviewerComments() {
        return interviewerComments;
    }

    public void setInterviewerComments(String interviewerComments) {
        this.interviewerComments = interviewerComments;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDateTime getEvaluationTime() {
        return evaluationTime;
    }

    public void setEvaluationTime(LocalDateTime evaluationTime) {
        this.evaluationTime = evaluationTime;
    }
}

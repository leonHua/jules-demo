package com.example.interviewbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.interviewbackend.entity.InterviewEvaluation;
import com.example.interviewbackend.mapper.InterviewEvaluationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class InterviewEvaluationService {

    @Autowired
    private InterviewEvaluationMapper evaluationMapper;

    public boolean addEvaluation(InterviewEvaluation evaluation) {
        evaluation.setEvaluationTime(LocalDateTime.now()); // Set evaluation time automatically
        return evaluationMapper.insert(evaluation) > 0;
    }

    public Page<InterviewEvaluation> getAllEvaluations(Page<InterviewEvaluation> page, Long appointmentId) {
        QueryWrapper<InterviewEvaluation> queryWrapper = new QueryWrapper<>();
        if (appointmentId != null) {
            queryWrapper.eq("appointment_id", appointmentId);
        }
        return evaluationMapper.selectPage(page, queryWrapper);
    }

    public InterviewEvaluation getEvaluationById(Long id) {
        return evaluationMapper.selectById(id);
    }

    public boolean updateEvaluation(InterviewEvaluation evaluation) {
        // evaluationTime is generally not updated, but if it is, it should be handled here or prevented
        return evaluationMapper.updateById(evaluation) > 0;
    }

    public boolean deleteEvaluation(Long id) {
        return evaluationMapper.deleteById(id) > 0;
    }

    public Page<InterviewEvaluation> getEvaluationsByAppointmentId(Long appointmentId, Page<InterviewEvaluation> page) {
        QueryWrapper<InterviewEvaluation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("appointment_id", appointmentId);
        return evaluationMapper.selectPage(page, queryWrapper);
    }
}

package com.example.interviewbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.interviewbackend.entity.InterviewAppointment;
import com.example.interviewbackend.mapper.InterviewAppointmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class InterviewAppointmentService {

    @Autowired
    private InterviewAppointmentMapper appointmentMapper;

    public boolean addAppointment(InterviewAppointment appointment) {
        return appointmentMapper.insert(appointment) > 0;
    }

    public Page<InterviewAppointment> getAllAppointments(Page<InterviewAppointment> page, String candidateName) {
        QueryWrapper<InterviewAppointment> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(candidateName)) {
            queryWrapper.like("candidate_name", candidateName);
        }
        return appointmentMapper.selectPage(page, queryWrapper);
    }

    public InterviewAppointment getAppointmentById(Long id) {
        return appointmentMapper.selectById(id);
    }

    public boolean updateAppointment(InterviewAppointment appointment) {
        return appointmentMapper.updateById(appointment) > 0;
    }

    public boolean deleteAppointment(Long id) {
        return appointmentMapper.deleteById(id) > 0;
    }
}

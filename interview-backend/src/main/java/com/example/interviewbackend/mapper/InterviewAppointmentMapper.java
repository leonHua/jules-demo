package com.example.interviewbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.interviewbackend.entity.InterviewAppointment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InterviewAppointmentMapper extends BaseMapper<InterviewAppointment> {
}

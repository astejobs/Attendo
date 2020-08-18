package com.aste.attendo.dao;

import org.springframework.data.repository.CrudRepository;

import com.aste.attendo.model.Attendance;

public interface AttendanceDao extends CrudRepository<Attendance, Long> {

}

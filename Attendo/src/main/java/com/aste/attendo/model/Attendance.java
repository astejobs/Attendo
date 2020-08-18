package com.aste.attendo.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Attendance {

	@Id@GeneratedValue(strategy=GenerationType.IDENTITY)
	long id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	AppUser appUser;
	
	@JsonFormat(pattern="dd-MM-yyyy HH:mm" )
	@DateTimeFormat(pattern="dd-MM-yyyy HH:mm")
	LocalDateTime checkInTime;
	
	@JsonFormat(pattern="dd-MM-yyyy HH:mm" )
	@DateTimeFormat(pattern="dd-MM-yyyy HH:mm")
	LocalDateTime checkOutTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	public LocalDateTime getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(LocalDateTime checkInTime) {
		this.checkInTime = checkInTime;
	}

	public LocalDateTime getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(LocalDateTime checkOutTime) {
		this.checkOutTime = checkOutTime;
	}
	
	
}

package com.aste.attendo.dao;

import org.springframework.data.repository.CrudRepository;

import com.aste.attendo.model.AppUser;

public interface AppUserDao extends CrudRepository<AppUser, Long> {

	AppUser findByNRIC(String nric);
}

package com.aste.attendo.dao;

import org.springframework.data.repository.CrudRepository;

import com.aste.attendo.model.Location;

public interface LocationDao extends CrudRepository<Location, Long> {
	Location findByName(String name);
	Location findByIdNotAndName(long id,String name);
}

package com.aste.attendo.controller;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GlobalPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.aste.attendo.dao.AppUserDao;
import com.aste.attendo.dao.AttendanceDao;
import com.aste.attendo.dao.LocationDao;
import com.aste.attendo.model.AppUser;
import com.aste.attendo.model.Attendance;
import com.aste.attendo.model.Location;

@RestController
@CrossOrigin
public class HomeController {
	
	@Autowired
	LocationDao locationDao;
	
	@Autowired
	AppUserDao appUserDao;
	@Autowired
	AttendanceDao attendanceDao;
	public static final double RADIUS = 100;

	@PostMapping("/location/check")
	public ResponseEntity<Boolean> checkLocation(@RequestBody Location location){
		
		
		Iterable<Location> locations = locationDao.findAll();
		for (Location loc : locations) {
				System.err.println("Distance "+getDistance(loc,location));
				boolean isWithinRadius = false;
				if(RADIUS >= getDistance(loc,location)){
					isWithinRadius=true;
					return ResponseEntity.ok(isWithinRadius);
				}
		}
		return ResponseEntity.ok(false);
	}
	
	@PostMapping("/location")
	public ResponseEntity<?> saveLocation(@RequestBody Location location){
		if(location.getId()>0){
			if(locationDao.findByName(location.getName()) == null){
				Location loc = locationDao.save(location);
				return ResponseEntity.ok(loc);
			}
		}else{
			if(locationDao.findByIdNotAndName(location.getId(),location.getName()) == null){
				Location loc = locationDao.save(location);
				return ResponseEntity.ok(loc);
			}
		}
		
		return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(null);
	}
	
	@PostMapping("/attendance")
	public ResponseEntity<?> saveAttendance(@RequestBody Attendance attendance){
		AppUser appUser =  appUserDao.findByNRIC(attendance.getAppUser().getNRIC());
		if(appUser == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
		attendance.setAppUser(appUser);
		attendance = attendanceDao.save(attendance);
		return ResponseEntity.ok(attendance);
	}
	
	public double getDistance(Location pointLocation,Location userLocation){
		GeodeticCalculator geoCalc = new GeodeticCalculator();
		Ellipsoid reference = Ellipsoid.WGS84;
		GlobalPosition pointA = new GlobalPosition(pointLocation.getLatitude(), pointLocation.getLongitude(), 0.0);
		GlobalPosition userPos = new GlobalPosition(userLocation.getLatitude(), userLocation.getLongitude(), 0.0);
		double distance = geoCalc.calculateGeodeticCurve(reference, userPos, pointA).getEllipsoidalDistance();
		return distance;
	}
	
	
}

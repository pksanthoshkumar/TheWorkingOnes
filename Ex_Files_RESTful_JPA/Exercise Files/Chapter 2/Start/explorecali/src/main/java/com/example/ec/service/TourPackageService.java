package com.example.ec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ec.domain.TourPackage;
import com.example.ec.repo.TourPackageRepository;

@Service
public class TourPackageService {

	@Autowired
	private TourPackageRepository tourRepository;

	TourPackageService(TourPackageRepository tourRepository) {
		this.tourRepository = tourRepository;
	}
	
	public TourPackage createTourPackage (String tourCode, String tourName){
		if (!tourRepository.exists(tourCode)) {
			tourRepository.save(new TourPackage (tourCode, tourName));
		}
		return null;
	}
	
	public Iterable <TourPackage> lookup (){
		return tourRepository.findAll();
	}
	
	public long getContOfAllToutPackage (){
		return tourRepository.count();
	}
}

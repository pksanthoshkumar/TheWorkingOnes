package com.example.ec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ec.domain.Difficulty;
import com.example.ec.domain.Region;
import com.example.ec.domain.Tour;
import com.example.ec.domain.TourPackage;
import com.example.ec.repo.TourPackageRepository;
import com.example.ec.repo.TourRepository;

@Service
public class TourService {

	private TourRepository tourRepository;
	private TourPackageRepository tourPackageRepository;
	
	@Autowired
	public TourService(TourRepository tourRepository, TourPackageRepository tourPackageRepository) {
		this.tourRepository = tourRepository;
		this.tourPackageRepository = tourPackageRepository;
	}

	public Tour createTour (String title, String description, String blurb, Integer price, String duration, String bullets,
            String keywords, String tourPackageCode, Difficulty difficulty, Region region){
		
		TourPackage aTourPackage = tourPackageRepository.findOne(tourPackageCode);
		Tour aTour = null;
		
		if (aTourPackage != null){
			aTour = tourRepository.save(new Tour ( title,  description,  blurb,  price,  duration,  bullets,
	                 keywords,  aTourPackage,  difficulty,  region));
		}else{
			throw new RuntimeException ("The Tour Package does not exists");
		}
		
		return aTour;
	}
	
	public Iterable<Tour>  getAllTours (){
		return tourRepository.findAll();
	}
	
	public long getToursCount (){
		return tourRepository.count();
	}
}

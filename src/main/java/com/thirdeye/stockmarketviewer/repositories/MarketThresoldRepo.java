package com.thirdeye.stockmarketviewer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thirdeye.stockmarketviewer.entity.MarketThresold;

public interface MarketThresoldRepo extends JpaRepository<MarketThresold, Long> {
	
}

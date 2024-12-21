package com.thirdeye.stockmarketviewer.services;

import java.util.List;

import com.thirdeye.stockmarketviewer.entity.MarketThresold;

public interface MarketThresoldService {
	void getUpdatedAllMarketThresold() throws Exception;
	Integer getAllMarketThresoldSize();
	List<MarketThresold> getAllMarketThresold();
}

package com.thirdeye.stockmarketviewer.services;

import java.util.List;
import com.thirdeye.stockmarketviewer.pojos.LiveStockPayload;

public interface LiveMarketService {
    boolean processListMarketData(List<LiveStockPayload> liveStockData, String uniqueMachineCode);
}

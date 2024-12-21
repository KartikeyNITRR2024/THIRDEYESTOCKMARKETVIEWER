package com.thirdeye.stockmarketviewer.utils;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TimeManagementUtil {
	
    @Autowired
    PropertyLoader propertyLoader;
    
    private static final Logger logger = LoggerFactory.getLogger(TimeManagementUtil.class);
	
	public Timestamp getCurrentTime() {
        ZonedDateTime indianTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        LocalDateTime localDateTime = indianTime.toLocalDateTime();
        return Timestamp.valueOf(localDateTime);
    }
	
//	public Timestamp getNextIterationTime(Timestamp currentTime) {
//	    long milliseconds = currentTime.getTime();
//	    long additionalMillis = propertyLoader.timeGap * 1000L;
//	    long newTimeInMillis = milliseconds + additionalMillis;
//	    Timestamp nextIterationTime = new Timestamp(newTimeInMillis);
//	    nextIterationTime.setNanos(0); 
//	    return nextIterationTime;
//	}
	
	public Boolean giveAccess(Timestamp currentTime) {
//		return Boolean.TRUE;
		ZonedDateTime currentZonedTime = currentTime.toInstant().atZone(ZoneId.of("Asia/Kolkata"));
		currentZonedTime = currentZonedTime.minusHours(5).minusMinutes(30);
	    LocalTime currentLocalTime = currentZonedTime.toLocalTime();
	    LocalTime startLocalTime = propertyLoader.startTime.toLocalTime();
	    LocalTime endLocalTime = propertyLoader.endTime.toLocalTime();
	    if(currentZonedTime.getDayOfWeek() == DayOfWeek.SATURDAY || currentZonedTime.getDayOfWeek() == DayOfWeek.SUNDAY)
	    {
	    	return Boolean.FALSE;
	    }
	    if(currentLocalTime.isAfter(startLocalTime) && currentLocalTime.isBefore(endLocalTime))
	    {
	    	return Boolean.TRUE;
	    }
	    return Boolean.FALSE;
	}
	
	public Timestamp getNextIterationTime(Timestamp currentTime) {
		ZonedDateTime currentZonedTime = currentTime.toInstant().atZone(ZoneId.of("Asia/Kolkata"));
		currentZonedTime = currentZonedTime.minusHours(5).minusMinutes(30);
	    LocalTime currentLocalTime = currentZonedTime.toLocalTime();
	    LocalTime startLocalTime = propertyLoader.startTime.toLocalTime();
	    LocalTime endLocalTime = propertyLoader.endTime.toLocalTime();
	    
	    logger.info("Current time {}, start time {}. end time {} ",currentLocalTime,startLocalTime,endLocalTime);
	    
	    
	    
	    if(currentZonedTime.getDayOfWeek() == DayOfWeek.SUNDAY || currentZonedTime.getDayOfWeek() == DayOfWeek.SATURDAY)
	    {
	    	ZonedDateTime nextDay = currentZonedTime.plusDays(1);
	    	if(currentZonedTime.getDayOfWeek() == DayOfWeek.SATURDAY)
	    	{
	    		nextDay = currentZonedTime.plusDays(2);
	    	}
	    	return Timestamp.valueOf(startLocalTime.atDate(nextDay.toLocalDate()));
	    }
	    
	    if (currentLocalTime.isBefore(startLocalTime)) {
	        return Timestamp.valueOf(startLocalTime.atDate(currentZonedTime.toLocalDate()));
	    }
	    else if (currentLocalTime.isAfter(endLocalTime)) {
	        ZonedDateTime nextDay = currentZonedTime.plusDays(1);
	        if (currentZonedTime.getDayOfWeek() == DayOfWeek.FRIDAY) {
	            nextDay = currentZonedTime.plusDays(3);
	        }
	        return Timestamp.valueOf(startLocalTime.atDate(nextDay.toLocalDate()));
	    }
	    
	    long milliseconds = currentTime.getTime();
	    long additionalMillis = propertyLoader.timeGap * 1000L;
	    long newTimeInMillis = milliseconds + additionalMillis;
	    Timestamp nextIterationTime = new Timestamp(newTimeInMillis);
	    nextIterationTime.setNanos(0); 
	    return nextIterationTime;
	}
}

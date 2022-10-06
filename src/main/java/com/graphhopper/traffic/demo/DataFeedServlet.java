package com.graphhopper.traffic.demo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.graphhopper.http.GraphHopperServlet;

import java.awt.*;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Peter Karich
 */
public class DataFeedServlet extends GraphHopperServlet {

    @Inject
    private ObjectMapper mapper;

    @Inject
    private DataUpdater updater;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        RoadData data = mapper.readValue(req.getInputStream(), RoadData.class);
        System.out.println("data:" + data);

        for (RoadEntry road:
             data) {
            RoadData r = new RoadData();
            r.add(road);
            try {
                if (road.getTime() == "") updater.feed(r);

                ArrayList<RoadEntry.DAYOFWEEK> dayOfWeeks = new ArrayList<>();

                String time = road.getTime();
                if (road.getDayOfWeeks() != null) dayOfWeeks = new ArrayList<>(road.getDayOfWeeks());
                String[] timeData = time.split(" - ");
                LocalTime requestTime = LocalTime.parse(timeData[0]);
                // validate
                String[] beginTime = timeData[0].split(":");
                String[] endTime = timeData[1].split(":");
                Thread thread = new Thread(() -> updater.feed(r));

                if (!scheduleForSomeDayInWeek(r,dayOfWeeks, beginTime, endTime, thread)) {
                    //ban daily
                    if (requestTime.compareTo(LocalTime.now()) <= 0) updater.feed(r);

                    Timer timer = new Timer();
                    Calendar date = Calendar.getInstance();
                    scheduleBanRoad(timer, date, thread, beginTime, 1000 * 24 * 60 * 60);
                    // get old value end re-update route
                    scheduleBanRoad(timer, date, thread, endTime, 1000 * 24 * 60 * 60);
                }
            } catch (Exception e) {
                logger.info(e.toString());
                updater.feed(r);
            }
        }
    }

    private boolean scheduleForSomeDayInWeek(RoadData r, ArrayList<RoadEntry.DAYOFWEEK> dayOfWeeks,
                                             String[] beginTime, String[] endTime, Thread thread) {
        if (dayOfWeeks == null || dayOfWeeks.size() == 0)
            return false;

        Timer timer = new Timer();
        Calendar date = Calendar.getInstance();
        for (RoadEntry.DAYOFWEEK d: dayOfWeeks) {
            date.set(Calendar.DATE, d.ordinal());
            scheduleBanRoad(timer, date, thread, beginTime, 7 * 1000 * 24 * 60 * 60);
            // get old value end re-update route
            scheduleBanRoad(timer, date, thread, endTime, 7 * 1000 * 24 * 60 * 60);
        }

        return true;
    }
    private void scheduleBanRoad(Timer timer, Calendar calendar, Thread thread, String[] timeData, long period) {
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeData[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeData[1]));
        calendar.set(Calendar.SECOND, Integer.parseInt(timeData[2]));
        logger.info(calendar.getTime().toString());
        timer.scheduleAtFixedRate(new MyTimerTask(thread), calendar.getTime(), period);
    }
}


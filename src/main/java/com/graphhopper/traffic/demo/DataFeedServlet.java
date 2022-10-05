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
                if (road.getTime() == "") {
                    updater.feed(r);
                }

                ArrayList<RoadEntry.DAYOFWEEK> dayOfWeeks = new ArrayList<>(road.getDayOfWeeks());

                String time = road.getTime();
                LocalTime requestTime = LocalTime.parse(time);
                String[] timeData = time.split(":");
                Thread thread = new Thread(() -> updater.feed(r));

                if (requestTime.compareTo(LocalTime.now()) <= 0) {
                    updater.feed(r);
                } else {
                    Timer timer = new Timer();
                    Calendar date = Calendar.getInstance();
                    date.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeData[0]));
                    date.set(Calendar.MINUTE, Integer.parseInt(timeData[1]));
                    date.set(Calendar.SECOND, Integer.parseInt(timeData[2]));

                    timer.scheduleAtFixedRate(new MyTimerTask(thread), date.getTime(), 1000 * 60 * 60 * 24);
                }
            } catch (Exception e) {
                logger.info(e.toString());
                updater.feed(r);
            }
        }
    }

    private boolean scheduleForSomeDayInWeek(RoadData r, ArrayList<RoadEntry.DAYOFWEEK> dayOfWeeks, String time) {
        if (dayOfWeeks == null || dayOfWeeks.size() == 0)
            return false;

        Timer timer = new Timer();
        Calendar date = Calendar.getInstance();
        for (RoadEntry.DAYOFWEEK d: dayOfWeeks) {
            date.set(Calendar.DATE, d.ordinal());
            String[] timeData = time.split(":");
            date.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeData[0]));
            date.set(Calendar.MINUTE, Integer.parseInt(timeData[1]));
            date.set(Calendar.SECOND, Integer.parseInt(timeData[2]));
            Thread thread = new Thread(() -> updater.feed(r));
            timer.scheduleAtFixedRate(new MyTimerTask(thread), date.getTime(), 7 * 1000 * 60 * 60 * 24);
        }

        return true;
    }
}


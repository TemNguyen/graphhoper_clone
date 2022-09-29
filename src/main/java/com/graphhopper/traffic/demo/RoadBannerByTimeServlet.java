package com.graphhopper.traffic.demo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.graphhopper.http.GraphHopperServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RoadBannerByTimeServlet extends GraphHopperServlet {
    @Inject
    private ObjectMapper mapper;

    @Inject
    private DataUpdater updater;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        RoadData data = mapper.readValue(req.getInputStream(), RoadData.class);
        System.out.println("data:" + data);

        updater.feed(data);
    }
}

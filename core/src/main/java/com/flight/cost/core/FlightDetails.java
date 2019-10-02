package com.flight.cost.core;

import com.adobe.cq.sightly.WCMUsePojo;
import com.flight.cost.core.models.FlightDetailsModel;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;

public class FlightDetails extends WCMUsePojo {

    private static final Logger logger = LoggerFactory.getLogger(FlightDetails.class);

    public static java.util.List<FlightDetailsModel> flightDetailsList;

    /**
     * Send flight times and cost to UI and populate "Flight Details" dropdown
     * @return
     */

    public java.util.List<FlightDetailsModel> getFlights() {
        Resource childResource = getResource().getChild("flightTableChildren");
        logger.info("getFlights");
        if (childResource != null) {
            flightDetailsList = setFlightDetailsModel(childResource);
        }
        return flightDetailsList;
    }

    /**
     * Set properties value to model
     * @param resource
     * @return
     */

    private java.util.List<FlightDetailsModel> setFlightDetailsModel(Resource resource) {
        flightDetailsList = new ArrayList<>();

        logger.info("setFlightDetailsModel");

        if (resource != null) {
            Iterator<Resource> linkResources = resource.listChildren();
            while (linkResources.hasNext()) {
                FlightDetailsModel link = linkResources.next().adaptTo(FlightDetailsModel.class);
                if (link != null)
                    flightDetailsList.add(link);
            }
        }
        return flightDetailsList;
    }

    @Override
    public void activate() throws Exception {
    }

}
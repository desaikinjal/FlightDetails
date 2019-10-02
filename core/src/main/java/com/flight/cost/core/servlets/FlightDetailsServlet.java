/**
 *
 */
package com.flight.cost.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

@Component(service= Servlet.class,
        property={
                Constants.SERVICE_DESCRIPTION + "=Flight Details Servlet",
                "sling.servlet.methods=" + HttpConstants.METHOD_GET,
                "sling.servlet.paths="+ "/bin/flightDetails",
                "sling.servlet.extensions=" + "json",
                "metatype=true"
        })
/*
@SlingServlet(
        label = "Flight Details Servlet",
        paths = { "/bin/flightDetails" },
        methods = { "GET", "POST" },
        resourceTypes = { },
        selectors = { "print.a4" },
        extensions = { "json", "html" }
)*/

/**
 *
 */
public class FlightDetailsServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUid = 1L;

    private static final String NONE = "none";
    private static final String BRONZE = "bronze";
    private static final String SILVER = "silver";
    private static final String GOLD = "gold";

    private  static int DOLLARS_POINTS_15 = 15;
    private  static int DOLLARS_POINTS_12 = 12;
    private  static int DOLLARS_POINTS_10 = 10;


    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Method is used to get request and set response
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */

    @Override
    protected void doGet(final SlingHttpServletRequest request,
                          final SlingHttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");

        logger.info("HIIiiii");
        JSONObject fObj = new JSONObject();
        final Resource resource = request.getResource();

        try {
            String price = request.getParameter("price");
            String clubLevel = request.getParameter("clubLevel");
            String userAge = request.getParameter("userAge");
            String dollarPoints = request.getParameter("dollarPoints");

            fObj.put("actualCost", price);
            fObj.put("discountedCost", calculateDiscount(Integer.parseInt(userAge), clubLevel, Integer.parseInt(price)));
            fObj.put("rewardPoints", calculatePoints(clubLevel, Integer.parseInt(price)));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        logger.info("Final Object " ,fObj.toString());
        logger.info("Final Object " + fObj.toString());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(fObj.toString());
    }

    /**
     * Calculate discount based on actual price and age criteria
     * @param userAge
     * @param clubLevel
     * @param actualCost
     * @return
     */

    private String calculateDiscount(Integer userAge, String clubLevel, Integer actualCost){

        if (userAge >= 0 && userAge <= 2 && (NONE.equalsIgnoreCase(clubLevel) || BRONZE.equalsIgnoreCase(clubLevel) || SILVER.equalsIgnoreCase(clubLevel))){
            return (String.valueOf(actualCost));
        } else if (userAge >= 3 && userAge <= 16 && (BRONZE.equalsIgnoreCase(clubLevel) || SILVER.equalsIgnoreCase(clubLevel))) {
            return (String.valueOf((actualCost*75)/100));
        } else if (userAge >= 9 && userAge <= 16 && GOLD.equalsIgnoreCase(clubLevel)){
            return (String.valueOf((actualCost*75)/100));
        } else if (userAge >= 0 && userAge <= 8 && GOLD.equalsIgnoreCase(clubLevel)){
            return (String.valueOf(actualCost));
        }
        return "";
    }

    private String calculatePoints(String clubLevel, Integer actualCost){

        if(BRONZE.equalsIgnoreCase(clubLevel)) return String.valueOf(actualCost*DOLLARS_POINTS_15);
        else if (SILVER.equalsIgnoreCase(clubLevel)) return String.valueOf(actualCost*DOLLARS_POINTS_12);
        else if (GOLD.equalsIgnoreCase(clubLevel)) return String.valueOf(actualCost*DOLLARS_POINTS_10);

        return String.valueOf(actualCost);
    }


}

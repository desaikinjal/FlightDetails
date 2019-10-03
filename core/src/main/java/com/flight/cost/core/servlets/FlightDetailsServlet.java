/**
 *
 */
package com.flight.cost.core.servlets;

import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
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

    private static  final  String EMPTY="";

    private static final String PRICE = "price";
    private static final String RETURN_PRICE = "returnPrice";
    private static final String CLUB_LEVEL = "clubLevel";
    private static final String USER_AGE = "userAge";

    private static final String ACTUAL_COST = "actualCost";
    private static final String DISCOUNTED_COST = "discountedCost";
    private static final String REWARD_POINTS = "rewardPoints";

    private static final String APPLICATION_JSON = "application/json";
    private static final String UTF_8 = "UTF-8";

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

        JsonObject fObj = new JsonObject();
        final Resource resource = request.getResource();

        String price = request.getParameter(PRICE);
        String returnPrice = request.getParameter(RETURN_PRICE);
        String clubLevel = request.getParameter(CLUB_LEVEL);
        String userAge = request.getParameter(USER_AGE);

        int combinedPrice = Integer.parseInt(price) + Integer.parseInt(returnPrice);
        fObj.addProperty(ACTUAL_COST, combinedPrice);

        int discountedCost = calculateDiscount(Integer.parseInt(userAge), clubLevel, combinedPrice);
        fObj.addProperty(DISCOUNTED_COST, discountedCost);
        fObj.addProperty(REWARD_POINTS, calculatePoints(clubLevel, discountedCost));

        response.setContentType(APPLICATION_JSON);
        response.setCharacterEncoding(UTF_8);

        response.getWriter().write(fObj.toString());
    }

    /**
     * Calculate discount based on actual price and age criteria
     * @param userAge
     * @param clubLevel
     * @param actualCost
     * @return
     */

    private Integer calculateDiscount(Integer userAge, String clubLevel, Integer actualCost){

        if (userAge >= 0 && userAge <= 2 && (NONE.equalsIgnoreCase(clubLevel) || BRONZE.equalsIgnoreCase(clubLevel) || SILVER.equalsIgnoreCase(clubLevel))){
            return 0;
        } else if (userAge >= 3 && userAge <= 16 && (BRONZE.equalsIgnoreCase(clubLevel) || SILVER.equalsIgnoreCase(clubLevel))) {
            return (actualCost*75)/100;
        } else if (userAge >= 9 && userAge <= 16 && GOLD.equalsIgnoreCase(clubLevel)){
            return (actualCost*75)/100;
        } else if (userAge >= 0 && userAge <= 8 && GOLD.equalsIgnoreCase(clubLevel)){
            return 0;
        }
        return actualCost;
    }

    /**
     * Calculate reward points based on club level
     * @param clubLevel
     * @param discountedCost
     * @return
     */

    private String calculatePoints(String clubLevel, Integer discountedCost){

        if(BRONZE.equalsIgnoreCase(clubLevel)) return String.valueOf(discountedCost*DOLLARS_POINTS_15);
        else if (SILVER.equalsIgnoreCase(clubLevel)) return String.valueOf(discountedCost*DOLLARS_POINTS_12);
        else if (GOLD.equalsIgnoreCase(clubLevel)) return String.valueOf(discountedCost*DOLLARS_POINTS_10);

        return String.valueOf(0);
    }
}

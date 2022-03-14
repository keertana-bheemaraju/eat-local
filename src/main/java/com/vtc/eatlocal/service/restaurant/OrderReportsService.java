package com.vtc.eatlocal.service.restaurant;

import com.vtc.eatlocal.entity.OrderValidation;
import com.vtc.eatlocal.repository.OrderValidationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class OrderReportsService {

    @Autowired
    OrderValidationRepository orderValidationRepository;

    private  SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy'T'HH:mm:ss");

    public List<OrderValidation> generateReport(int restaurantId, String reportType) throws Exception{

        List<OrderValidation> report = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);

        switch (reportType) {
            case "today" :
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                return generateReport(restaurantId, calendar.getTime());
            case "this-week" :
                calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
                return generateReport(restaurantId, calendar.getTime());
            case "this-month" :
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                return generateReport(restaurantId, calendar.getTime());
        }

        return report;
    }


    private List<OrderValidation> generateReport(int restaurantId, Date startTimestamp) throws Exception{

        List<OrderValidation> report = new ArrayList<>();

        Date endTimestamp = new Date();

        // fetch records from OrderValidation table between start and end dates
        List<OrderValidation> all = orderValidationRepository.findAll();

        for(OrderValidation orderValidation : all) {
            if(orderValidation.getRestaurantId() == restaurantId) {
                String validationTimestamp_string = orderValidation.getTimestamp();
                Date validationTimestamp = formatter.parse(validationTimestamp_string);
                if(validationTimestamp.after(startTimestamp) && validationTimestamp.before(endTimestamp)) {
                    report.add(orderValidation);
                }
            }
        }

        return report;
    }
}

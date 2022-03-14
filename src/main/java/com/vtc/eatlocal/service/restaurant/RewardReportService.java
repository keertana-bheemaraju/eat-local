package com.vtc.eatlocal.service.restaurant;

import com.vtc.eatlocal.entity.OrderValidation;
import com.vtc.eatlocal.entity.RewardHistory;
import com.vtc.eatlocal.repository.RewardHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class RewardReportService {

    @Autowired
    RewardHistoryRepository rewardHistoryRepository;

    private SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy'T'HH:mm:ss");

    public List<RewardHistory> generateRewardReport(int restaurantId, String reportType) throws Exception{

        List<RewardHistory> report = new ArrayList<>();

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


    private List<RewardHistory> generateReport(int restaurantId, Date startTimestamp) throws Exception{

        List<RewardHistory> report = new ArrayList<>();

        Date endTimestamp = new Date();

        // fetch records from RewardHistory table between start and end dates
        List<RewardHistory> all = rewardHistoryRepository.findAll();

        for(RewardHistory rewardHistory : all) {
            if(rewardHistory.getRestaurantId() == restaurantId) {
                String rewardHistoryRedeemDate = rewardHistory.getRedeemDate();
                Date date = formatter.parse(rewardHistoryRedeemDate);
                if(date.after(startTimestamp) && date.before(endTimestamp)) {
                    report.add(rewardHistory);
                }
            }
        }

        return report;
    }
}

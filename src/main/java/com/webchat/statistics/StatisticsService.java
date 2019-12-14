package com.webchat.statistics;

import com.webchat.model.User;

public interface StatisticsService {

    UserStats getUserStatistics(User user);
}

package com.web3sosial.blockchain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RewardSummaryResponse {
    private String wallet;
    private String totalEarned;
    private String todayEarned;
    private String dailyLimit;
    private int postsCount;
    private int likesCount;
    private int commentsCount;
    private int followersCount;
}

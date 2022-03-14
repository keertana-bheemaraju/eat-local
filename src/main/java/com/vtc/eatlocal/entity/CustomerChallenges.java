//package com.vtc.eatlocal.entity;
//
//import javax.persistence.*;
//import java.util.List;
//
//@Entity
//@Table(name= "customer_challenges")
//public class CustomerChallenges {
//
//    @Id
//    private Integer customerId;
//
////    private String challengeIdList;
////
////    private String challengeNameList;
////
////    public Integer getCustomerId() {
////        return customerId;
////    }
////
////    public void setCustomerId(Integer customerId) {
////        this.customerId = customerId;
////    }
////
////    public String getChallengeIdList() {
////        return challengeIdList;
////    }
////
////    public void setChallengeIdList(String challengeIdList) {
////        this.challengeIdList = challengeIdList;
////    }
////
////    public String getChallengeNameList() {
////        return challengeNameList;
////    }
////
////    public void setChallengeNameList(String challengeNameList) {
////        this.challengeNameList = challengeNameList;
////    }
//
//    @OneToMany(mappedBy = )
//    List<LiveChallenge> customerChallenges;
//
//
//    public Integer getCustomerId() {
//        return customerId;
//    }
//
//    public void setCustomerId(Integer customerId) {
//        this.customerId = customerId;
//    }
//
//    public List<LiveChallenge> getCustomerChallenges() {
//        return customerChallenges;
//    }
//
//    public void setCustomerChallenges(List<LiveChallenge> customerChallenges) {
//        this.customerChallenges = customerChallenges;
//    }
//}

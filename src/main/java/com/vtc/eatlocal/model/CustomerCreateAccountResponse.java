package com.vtc.eatlocal.model;

public class CustomerCreateAccountResponse {

    private String customerCreateAccountMessage;

    private boolean createAccountStatus;

    private Integer customerId;

    public String getCustomerCreateAccountMessage() {
        return customerCreateAccountMessage;
    }

    public void setCustomerCreateAccountMessage(String customerCreateAccountMessage) {
        this.customerCreateAccountMessage = customerCreateAccountMessage;
    }

    public boolean isCreateAccountStatus() {
        return createAccountStatus;
    }

    public void setCreateAccountStatus(boolean createAccountStatus) {
        this.createAccountStatus = createAccountStatus;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
}

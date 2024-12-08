package com.example.wallet;

public class Wallet {
    private String address;
    private String tokenName;
    private String mintTime;
    private String deployTime;
    private String supply;
    private String holdPercentage;
    private String ath;
    private String duration;

    public Wallet(String address, String tokenName, String mintTime, String deployTime, String supply, String ath,
                  String duration, String holdPercentage) {
        this.address = address;
        this.tokenName = tokenName;
        this.mintTime = mintTime;
        this.deployTime = deployTime;
        this.supply = supply;
        this.ath = ath;
        this.duration = duration;
        this.holdPercentage = holdPercentage;
    }

    public String getAddress() {
        return address;
    }

    public String getTokenName() {
        return tokenName;
    }

    public String getMintTime() {
        return mintTime;
    }

    public String getDeployTime() {
        return deployTime;
    }

    public String getSupply() {
        return supply;
    }

    public String getHoldPercentage() {
        return holdPercentage;
    }

    public String getAth() {
        return ath;
    }

    public String getDuration() {
        return duration;
    }
}

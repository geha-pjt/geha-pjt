package com.bit.geha.dto;

import java.util.Date;

import lombok.Data;

@Data
public class GuestHouseDto {
	private int guestHouseCode;
	private String guestHouseName;
	private String guestHouseInfo;
	private String rules;
	private String parkingInfo;
	private String address;
	private String directions;
	private Date registerDate;
	private Date approvalDate;
	private int memberCode;
	private double avgRating;
	private boolean businessTrip;
	private boolean gourmet;
	private boolean trip;
	private boolean shopping;
	private String bankName;
	private String accountNumber;
	
}
package com.bit.geha.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class FileDto {
	@NonNull
	private String savedName;
	@NonNull
	private String originalName;
	@NonNull
	private int guestHouseCode;
	private int roomCode;
	
	@NonNull
	private boolean isMainImage;
}

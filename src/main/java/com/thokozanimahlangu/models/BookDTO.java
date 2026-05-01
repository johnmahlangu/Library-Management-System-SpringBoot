package com.thokozanimahlangu.Library.Management.System.models;

import java.time.Year;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookDTO {

	private UUID id;
	@NotBlank
	private String Title;
	@NotNull
	private Year publicationYear;
	@NotBlank
	private String ISBN;
	
}

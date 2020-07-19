package com.ficticiousclean.fcapi.vos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorMessageVO {

	private String message;
	private String exception;

}

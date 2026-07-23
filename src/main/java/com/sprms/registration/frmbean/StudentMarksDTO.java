package com.sprms.registration.frmbean;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class StudentMarksDTO {

	@NotBlank(message = "Subject name is required")
	private String subjectName;

	@Min(value = 0, message = "Mark cannot be negative")
	@Max(value = 100, message = "Mark cannot exceed 100")
	private Integer mark;

	@NotNull(message = "Grade code is required")
	private Integer gradeCode;

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Integer getMark() {
		return mark;
	}

	public void setMark(Integer mark) {
		this.mark = mark;
	}

	public Integer getGradeCode() {
		return gradeCode;
	}

	public void setGradeCode(Integer gradeCode) {
		this.gradeCode = gradeCode;
	}

}

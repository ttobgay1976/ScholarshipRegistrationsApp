package com.sprms.registration.hbmbean;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name="tbl_sch_student_marks")
public class StudentMarks {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String subjectName;
	private Integer mark;
	private Integer gradeCode;
	
    @ManyToOne
    @JoinColumn(name = "student_profile_id")
    private StudentProfile studentProfile;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public StudentProfile getStudentProfile() {
		return studentProfile;
	}

	public void setStudentProfile(StudentProfile studentProfile) {
		this.studentProfile = studentProfile;
	}
	
	//GETTER AND SETTER
	
    
	
}

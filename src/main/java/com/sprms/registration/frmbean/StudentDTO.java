package com.sprms.registration.frmbean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentDTO {

	private String studentName;
    private String dob;           // keep as String for now
    private String sex;
    private String indexNumber;   // IMPORTANT: keep as String (leading zero)
    private Long cidNumber;       // IMPORTANT: use Long
    private String examYear;      // keep as String
    private String stream;
    
    private String type;
    
    private String schoolName;
    private String dzongkhag;
    private String subjectName;
    private Integer mark;
    private Integer gradeCode;
    private String supw;
    private String result;
    
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIndexNumber() {
		return indexNumber;
	}
	public void setIndexNumber(String indexNumber) {
		this.indexNumber = indexNumber;
	}
	public Long getCidNumber() {
		return cidNumber;
	}
	public void setCidNumber(Long cidNumber) {
		this.cidNumber = cidNumber;
	}

	public String getStream() {
		return stream;
	}
	public void setStream(String stream) {
		this.stream = stream;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getDzongkhag() {
		return dzongkhag;
	}
	public void setDzongkhag(String dzongkhag) {
		this.dzongkhag = dzongkhag;
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
	public String getSupw() {
		return supw;
	}
	public void setSupw(String supw) {
		this.supw = supw;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getExamYear() {
		return examYear;
	}
	public void setExamYear(String examYear) {
		this.examYear = examYear;
	}

       
    

}

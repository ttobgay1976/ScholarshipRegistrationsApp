package com.sprms.registration.hbmbean;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="tbl_sch_student_profile")
public class StudentProfile {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	private String studentName;
	private String indexNumber;
	private Long cidNumber;
	private String schoolName;
	private String dzongkhag;
	private String stream;

	@Column(name = "stream_code")
	private String streamCode;
	
	@Column(name = "exam_year")
	private Integer examYear;
	
	@Column(name="student_type")
	private String type;
	
    @OneToOne
    @JoinColumn(name = "registration_id") // 🔥 FK in this table
    private ScholarshipRegistration registration;

    @OneToMany(mappedBy = "studentProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentMarks> studentMarks;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
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

	public String getStream() {
		return stream;
	}

	public void setStream(String stream) {
		this.stream = stream;
	}

	public List<StudentMarks> getStudentMarks() {
		return studentMarks;
	}

	public void setStudentMarks(List<StudentMarks> studentMarks) {
		this.studentMarks = studentMarks;
	}

	public ScholarshipRegistration getRegistration() {
		return registration;
	}

	public void setRegistration(ScholarshipRegistration registration) {
		this.registration = registration;
	}

	public String getStreamCode() {
		return streamCode;
	}

	public void setStreamCode(String streamCode) {
		this.streamCode = streamCode;
	}

	public Integer getExamYear() {
		return examYear;
	}

	public void setExamYear(Integer examYear) {
		this.examYear = examYear;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}



	
}

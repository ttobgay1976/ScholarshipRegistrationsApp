package com.sprms.registration.api.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sprms.registration.frmbean.StudentResultDTO;
import com.sprms.registration.hbmbean.StudentMarks;
import com.sprms.registration.hbmbean.StudentProfile;

@Service
public class StudentResultService {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(StudentResultService.class);
	
	
	public double calculateAverage(List<StudentMarks> marks) {
		
		logger.info("@@@Calling the calculateAverage proc........");

		if (marks == null || marks.isEmpty()) {
			return 0.0;
		}

		double total = marks.stream().mapToDouble(StudentMarks::getMark).sum();

		return total / marks.size();
	}

	/**
	 * MAIN METHOD - STREAM WISE RANKING
	 */
	public List<StudentResultDTO> getStreamWiseRanking(List<StudentProfile> students) {
		
		logger.info("@@@Calling the getStreamWiseRanking proc........");
		

		List<StudentResultDTO> resultList = new ArrayList<>();

		// STEP 1: Convert entity → DTO with average
		for (StudentProfile student : students) {

			StudentResultDTO dto = new StudentResultDTO();

			dto.setIndexNumber(student.getIndexNumber());
			dto.setStudentName(student.getStudentName());
			dto.setStream(student.getStream());
			
			double avg = calculateAverage(student.getStudentMarks());
			dto.setAverage(avg);

			resultList.add(dto);
		}

		// STEP 2: GROUP BY STREAM
		Map<String, List<StudentResultDTO>> groupedByStream = resultList.stream()
				.collect(Collectors.groupingBy(StudentResultDTO::getStream));

		List<StudentResultDTO> finalResult = new ArrayList<>();

		// STEP 3: PROCESS EACH STREAM SEPARATELY
		for (Map.Entry<String, List<StudentResultDTO>> entry : groupedByStream.entrySet()) {

			List<StudentResultDTO> streamList = entry.getValue();

			// SORT DESC by average
			streamList.sort((a, b) -> Double.compare(b.getAverage(), a.getAverage()));

			// STEP 4: ASSIGN RANK
			int rank = 1;
			double prevAvg = -1;
			int sameRankCount = 0;

			for (int i = 0; i < streamList.size(); i++) {

				StudentResultDTO current = streamList.get(i);

				if (i > 0 && current.getAverage() == prevAvg) {
					current.setRank(rank);
					sameRankCount++;
				} else {
					rank = rank + sameRankCount;
					sameRankCount = 1;
					current.setRank(rank);
					rank++;
				}

				prevAvg = current.getAverage();

				finalResult.add(current);
			}
		}

		return finalResult;
	}

}

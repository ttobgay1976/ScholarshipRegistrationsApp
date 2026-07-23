package com.sprms.registration.frmbean;

import java.util.ArrayList;
import java.util.List;

public class EligibilityResultDTO {

    private boolean eligible;

    private String message;

    // All eligible scholarship rules
    private List<Long> eligibleRuleIds = new ArrayList<>();

    // All eligible scholarship names
    private List<String> eligibleScholarships = new ArrayList<>();

    // Failure reasons (used only if not eligible)
    private List<String> reasons = new ArrayList<>();

    public void addEligibleRule(Long ruleId, String scholarshipName) {

        this.eligibleRuleIds.add(ruleId);
        this.eligibleScholarships.add(scholarshipName);
    }

    public void addReason(String reason) {
        this.reasons.add(reason);
    }

	public boolean isEligible() {
		return eligible;
	}

	public void setEligible(boolean eligible) {
		this.eligible = eligible;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Long> getEligibleRuleIds() {
		return eligibleRuleIds;
	}

	public void setEligibleRuleIds(List<Long> eligibleRuleIds) {
		this.eligibleRuleIds = eligibleRuleIds;
	}

	public List<String> getEligibleScholarships() {
		return eligibleScholarships;
	}

	public void setEligibleScholarships(List<String> eligibleScholarships) {
		this.eligibleScholarships = eligibleScholarships;
	}

	public List<String> getReasons() {
		return reasons;
	}

	public void setReasons(List<String> reasons) {
		this.reasons = reasons;
	}


}

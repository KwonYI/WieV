package com.web.project.model.interview;

// 면접관이 할당한 모든 인터뷰 정보 가져오기
public class Interview {
	String comName; // 회사이름

	String groupDate; // 해당 그룹 면접 날짜
	String groupStartTime; // 해당 그룹 면접 시작 시간
	
	int recruitYear; // 공고 년도
	String recruitStartDate; // 공고 종료 날짜
	String recruitEndDate; // 공고 종료 날짜
	String recruitFlag; // 상/하반기
	String recruitStatus; // 신입/경력
	
	String careerName;// 직군
	String partName; // 부서
	String interviewType; // 면접 종류
	String waitSessionName;	// 대기방세션이름
	String interviewSessionName; // 면접방세션이름

	public Interview() {}

	public Interview(String comName, String groupDate, String groupStartTime, int recruitYear, String recruitStartDate,
			String recruitEndDate, String recruitFlag, String recruitStatus, String careerName, String partName,
			String interviewType, String waitSessionName, String interviewSessionName) {
		super();
		this.comName = comName;
		this.groupDate = groupDate;
		this.groupStartTime = groupStartTime;
		this.recruitYear = recruitYear;
		this.recruitStartDate = recruitStartDate;
		this.recruitEndDate = recruitEndDate;
		this.recruitFlag = recruitFlag;
		this.recruitStatus = recruitStatus;
		this.careerName = careerName;
		this.partName = partName;
		this.interviewType = interviewType;
		this.waitSessionName = waitSessionName;
		this.interviewSessionName = interviewSessionName;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getGroupDate() {
		return groupDate;
	}

	public void setGroupDate(String groupDate) {
		this.groupDate = groupDate;
	}

	public String getGroupStartTime() {
		return groupStartTime;
	}

	public void setGroupStartTime(String groupStartTime) {
		this.groupStartTime = groupStartTime;
	}

	public int getRecruitYear() {
		return recruitYear;
	}

	public void setRecruitYear(int recruitYear) {
		this.recruitYear = recruitYear;
	}

	public String getRecruitStartDate() {
		return recruitStartDate;
	}

	public void setRecruitStartDate(String recruitStartDate) {
		this.recruitStartDate = recruitStartDate;
	}

	public String getRecruitEndDate() {
		return recruitEndDate;
	}

	public void setRecruitEndDate(String recruitEndDate) {
		this.recruitEndDate = recruitEndDate;
	}

	public String getRecruitFlag() {
		return recruitFlag;
	}

	public void setRecruitFlag(String recruitFlag) {
		this.recruitFlag = recruitFlag;
	}

	public String getRecruitStatus() {
		return recruitStatus;
	}

	public void setRecruitStatus(String recruitStatus) {
		this.recruitStatus = recruitStatus;
	}

	public String getCareerName() {
		return careerName;
	}

	public void setCareerName(String careerName) {
		this.careerName = careerName;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public String getInterviewType() {
		return interviewType;
	}

	public void setInterviewType(String interviewType) {
		this.interviewType = interviewType;
	}

	public String getWaitSessionName() {
		return waitSessionName;
	}

	public void setWaitSessionName(String waitSessionName) {
		this.waitSessionName = waitSessionName;
	}

	public String getInterviewSessionName() {
		return interviewSessionName;
	}

	public void setInterviewSessionName(String interviewSessionName) {
		this.interviewSessionName = interviewSessionName;
	}

	@Override
	public String toString() {
		return "Interview [comName=" + comName + ", groupDate=" + groupDate + ", groupStartTime=" + groupStartTime
				+ ", recruitYear=" + recruitYear + ", recruitStartDate=" + recruitStartDate + ", recruitEndDate="
				+ recruitEndDate + ", recruitFlag=" + recruitFlag + ", recruitStatus=" + recruitStatus + ", careerName="
				+ careerName + ", partName=" + partName + ", interviewType=" + interviewType + ", waitSessionName="
				+ waitSessionName + ", interviewSessionName=" + interviewSessionName + "]";
	}
}

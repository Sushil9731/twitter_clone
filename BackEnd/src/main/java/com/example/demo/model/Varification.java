package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;


@Entity
public class Varification {
	
	private boolean status=false;
	private LocalDateTime startedAt;
	private LocalDateTime endsAt;
	private String planType;
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public LocalDateTime getStartedAt() {
		return startedAt;
	}
	public void setStartedAt(LocalDateTime startedAt) {
		this.startedAt = startedAt;
	}
	public LocalDateTime getEndsAt() {
		return endsAt;
	}
	public void setEndsAt(LocalDateTime endsAt) {
		this.endsAt = endsAt;
	}
	public String getPlanType() {
		return planType;
	}
	public void setPlanType(String planType) {
		this.planType = planType;
	}

}

package com.thokozanimahlangu.models;

/**
 * Represents the current status of a book issue record.
 * This status tracks whether a book is currently out, has been successfully  brought back, or has passed its expected return deadline.
 */
public enum IssueStatus {
	
	ISSUED, RETURNED, OVERDUE
}

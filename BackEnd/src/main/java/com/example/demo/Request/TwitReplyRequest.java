package com.example.demo.Request;

import java.time.LocalDateTime;

import lombok.Data;
@Data
public class TwitReplyRequest {
	private String content;
	private Long TwitId;
	private LocalDateTime createdAt;
	private String image;
}

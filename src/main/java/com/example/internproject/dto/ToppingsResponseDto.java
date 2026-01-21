package com.example.internproject.dto;

public class ToppingsResponseDto {
	   private Long id;
	    private String name;
	    

	    public ToppingsResponseDto(Long id, String name) {
	        this.id = id;
	        this.name = name;
	        
	    }

	    public Long getId() { return id; }
	    public String getName() { return name; }
	}

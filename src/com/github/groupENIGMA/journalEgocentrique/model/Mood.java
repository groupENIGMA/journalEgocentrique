package com.github.groupENIGMA.journalEgocentrique.model;

public class Mood implements MoodInterface {
	
	//Mood image paths
    public static final String HAPPY_IMAGE_PATH = ;
    public static final String SAD_IMAGE_PATH = ;
    public static final String ANGRY_IMAGE_PATH = ;
    public static final String BORED_IMAGE_PATH = ;
    public static final String APATHETIC_IMAGE_PATH = ;
    public static final String DEPRESSED_IMAGE_PATH = ;
	
	private String Name;
	private String Path;
	
	public Mood(String name){
		Name = name;
		if(Name.equals("Happy"))
			Path = HAPPY_IMAGE_PATH;
		else if(Name.equals("Sad"))
			Path = SAD_IMAGE_PATH;
		else if(Name.equals("Angry"))
			Path = ANGRY_IMAGE_PATH;
		else if(Name.equals("Bored"))
			Path = BORED_IMAGE_PATH;
		else if(Name.equals("Apathetic"))
			Path = APATHETIC_IMAGE_PATH;
		else if(Name.equals("Depressed"))
			Path = DEPRESSED_IMAGE_PATH;
	}

	@Override
	public String getName() {
		
		return Name;
	}

	@Override
	public String getPathImage() {

		return Path;
	}

}

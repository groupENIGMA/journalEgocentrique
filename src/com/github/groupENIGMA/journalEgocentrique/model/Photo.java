package com.github.groupENIGMA.journalEgocentrique.model;

public class Photo implements PhotoInterface {
    
    private String Path;
    
    public Photo(String path){
        Path = path;
    }

    /**
     * Create the thumbnail of the Photo
     * This method is called when it's created a new Photo object
     * 
     * @return a Photo that represent the thumbnail of this one
     */
    private Photo createThumb(){
        return null;
    }

    @Override
    public boolean canBeUpdated() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean canBeDeleted() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String getPath() {
        
        return Path;
    }

    @Override
    public String getPathThumb() {
        // TODO Auto-generated method stub
        return null;
    }
    
}

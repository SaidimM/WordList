package com.example.WordList.list;
public class Word {
    private String query;
    private String translation;
   public  Word(String query){this.query=query;}
    public String getQuery() {
        return query;
    }
    public Word(String query,String translation){
       this.query=query;
       this.translation=translation;
    }
    public void setQuery(String query) {
        this.query = query;
    }
    public String getTranslation() {
        return translation;
    }
    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
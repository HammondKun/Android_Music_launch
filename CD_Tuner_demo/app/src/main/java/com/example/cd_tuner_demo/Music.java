package com.example.cd_tuner_demo;

public class Music {
    private final String songName;
    private final String albumName;
    private final String artistName;
    private final int songResId;

    public Music(String songName,String albumName,String artistName,int songResId){
       this.songName = songName;
       this.albumName = albumName;
       this.artistName = artistName;
       this.songResId = songResId;
    }

    public String getSongName() {return songName;}
    public String getAlbumName() {return  albumName;}
    public String getArtistName() {return artistName;}
    public int getSongResId() {return songResId;}

}

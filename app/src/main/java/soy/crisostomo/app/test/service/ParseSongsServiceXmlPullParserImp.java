package soy.crisostomo.app.test.service;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

import soy.crisostomo.app.test.model.Song;

/**
 * Created by darcusfenix on 10/22/15.
 */
public class ParseSongsServiceXmlPullParserImp implements ParseSongsService {
    private String data;
    private ArrayList<Song> songs;

    public ParseSongsServiceXmlPullParserImp(String xmlData){
        this.data = xmlData;
        songs = new ArrayList<Song>();

    }

    public ArrayList<Song> getSongs() {
        return songs;
    }
    @Override
    public boolean process(){

        boolean operationStatus = true;
        Song currentSong = null;
        boolean inEntry = false;
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(this.data));
            Integer evenType = xpp.getEventType();

            while (evenType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();

                if (evenType == XmlPullParser.START_TAG){
                    if(tagName.equalsIgnoreCase("entry")){
                        inEntry = true;
                        currentSong = new Song();
                    }
                }else if (evenType == XmlPullParser.TEXT){
                    textValue = xpp.getText();
                }else if (evenType == XmlPullParser.END_TAG){
                    if (inEntry){
                        if (tagName.equalsIgnoreCase("entry")){
                            songs.add(currentSong);
                            inEntry = false;
                        }
                        if (tagName.equalsIgnoreCase("title")){
                            currentSong.setTitle(textValue);
                        }
                        if (tagName.equalsIgnoreCase("artist")){
                            currentSong.setArtist(textValue);
                        }
                        if (tagName.equalsIgnoreCase("releaseDate")){
                            currentSong.setReleaseDate(textValue);
                        }
                    }
                }
                evenType = xpp.next();
            }
        }catch (Exception e){
            e.printStackTrace();
            operationStatus = false;
        }
        return operationStatus;
    }
}

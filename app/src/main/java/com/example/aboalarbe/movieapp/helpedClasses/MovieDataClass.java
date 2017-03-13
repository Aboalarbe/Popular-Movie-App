package com.example.aboalarbe.movieapp.helpedClasses;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by m_abo on 2/16/2017.
 */
public class MovieDataClass implements Parcelable{
    String poster_Path;
    String overview;
    String release_date;
    String original_title;
    double vote_average;
    int moiveId;

    public MovieDataClass()
    {

    }

    public MovieDataClass(double vote_average, String original_title, String release_date, String overview, String poster_Path, int movieId) {
        this.vote_average = vote_average;
        this.original_title = original_title;
        this.release_date = release_date;
        this.overview = overview;
        this.poster_Path = poster_Path;
        this.moiveId=movieId;
    }

    private MovieDataClass(Parcel in)
    {
        vote_average=in.readDouble();
        original_title=in.readString();
        release_date=in.readString();
        overview=in.readString();
        poster_Path=in.readString();
        moiveId=in.readInt();
    }



    public String getPoster_Path() {
        return poster_Path;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public double getVote_average() {
        return vote_average;
    }

    public int getMoiveId(){return moiveId;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(vote_average);
        parcel.writeString(original_title);
        parcel.writeString(release_date);
        parcel.writeString(overview);
        parcel.writeString(poster_Path);
        parcel.writeInt(moiveId);
    }
    public static final Parcelable.Creator CREATOR=new Parcelable.Creator<MovieDataClass>(){

        @Override
        public MovieDataClass createFromParcel(Parcel parcel) {
            return new MovieDataClass(parcel);
        }

        @Override
        public MovieDataClass[] newArray(int i) {
            return new MovieDataClass[i];
        }
    };
}
package com.example.aboalarbe.movieapp.helpedClasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by m_abo on 2/22/2017.
 */

public class ReviewClass implements Parcelable {
    String reviewAuthor,reviewContent;
    public ReviewClass(String reviewAuthor, String reviewContent) {
        this.reviewAuthor = reviewAuthor;
        this.reviewContent = reviewContent;
    }
    private ReviewClass(Parcel in)
    {
        reviewAuthor=in.readString();
        reviewContent=in.readString();
    }
    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(reviewAuthor);
        parcel.writeString(reviewContent);
    }

    public static final Creator<ReviewClass> CREATOR= new Creator<ReviewClass>() {
        @Override
        public ReviewClass createFromParcel(Parcel parcel) {
            return new ReviewClass(parcel);
        }

        @Override
        public ReviewClass[] newArray(int i) {
            return new ReviewClass[i];
        }
    };
}

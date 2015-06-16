package de.rheinfabrik.heimdalldroid.network.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Model describing a TraktTvList.
 */
public class TraktTvList implements Serializable {

    // Properties

    @SerializedName("name")
    public String name;

    @SerializedName("description")
    public String description;

    @SerializedName("likes")
    public int numberOfLikes;
}

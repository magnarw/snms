package no.snms.app.domain;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsItem implements Serializable {

	String title;
	String text;
	DateTime createdDate;
	String ingress;
	String imageText;
	String author;
	String imgUrl;
	String articleImageUrl;
	String articleImageText;
	String _id;
	Integer cat;
	String address;
	DateTime from;
	DateTime to;
	Double lat;
	Double lng;
	Boolean showMap;
	
	/** These should be moved into a wrapper object.. **/
	Boolean hasMoreElements;
	Integer nextPage; 
	

	public Boolean getHasMoreElements() {
		return hasMoreElements;
	}

	public void setHasMoreElements(Boolean hasMoreElements) {
		this.hasMoreElements = hasMoreElements;
	}

	public String getIngress() {
		return ingress;
	}

	public void setIngress(String ingress) {
		this.ingress = ingress;
	}

	public String getImageText() {
		return imageText;
	}

	public void setImageText(String imageText) {
		this.imageText = imageText;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imageUrl) {
		this.imgUrl = imageUrl;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public DateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}

	public int getCat() {
		return cat;
	}

	public void setCat(int cat) {
		this.cat = cat;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public DateTime getFrom() {
		return from;
	}

	public void setFrom(DateTime from) {
		this.from = from;
	}

	public DateTime getTo() {
		return to;
	}

	public void setTo(DateTime to) {
		this.to = to;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Boolean getShowMap() {
		return showMap;
	}

	public void setShowMap(Boolean showMap) {
		this.showMap = showMap;
	}

	public void setCat(Integer cat) {
		this.cat = cat;
	}

	public String getArticleImageUrl() {
		return articleImageUrl;
	}

	public void setArticleImageUrl(String articleImageUrl) {
		this.articleImageUrl = articleImageUrl;
	}

	public String getArticleImageText() {
		return articleImageText;
	}

	public void setArticleImageText(String articleImageText) {
		this.articleImageText = articleImageText;
	}

	public Integer getNextPage() {
		return nextPage;
	}

	public void setNextPage(Integer nextPage) {
		this.nextPage = nextPage;
	}


	
	

}

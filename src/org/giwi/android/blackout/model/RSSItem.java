package org.giwi.android.blackout.model;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;

/**
 * @author Xavier Marin | Giwi Softwares
 * 
 *         The Class RSSItem.
 */
@EBean(scope = Scope.Singleton)
public class RSSItem {

	/** The _title. */
	private String _title = null;

	/** The _description. */
	private String _description = null;

	/** The _link. */
	private String _link = null;

	/** The _category. */
	private List<String> _category = new ArrayList<String>();

	/** The _pubdate. */
	private String _pubdate = null;
	private String imgUrl;
	/** The _media. */
	private String _media;

	private String creator;

	public void addCategory(final String cat) {
		_category.add(cat);
	}

	/**
	 * Gets the _media.
	 * 
	 * @return the _media
	 */
	public String getMedia() {
		return _media;
	}

	/**
	 * Sets the _media.
	 * 
	 * @param media
	 *            the new _media
	 */
	public void setMedia(final String media) {
		_media = media;
	}

	/**
	 * Instantiates a new rSS item.
	 */
	public RSSItem() {
	}

	/**
	 * Sets the title.
	 * 
	 * @param title
	 *            the new title
	 */
	public void setTitle(final String title) {
		_title = title;
	}

	/**
	 * Sets the description.
	 * 
	 * @param description
	 *            the new description
	 */
	public void sanitizeDesc() {
		// <img src="http://localhost/blackout/wordpress/wp-content/gallery/content/thumbs/thumbs_artmanialogo.png" alt="artmanialogo" title="Content" class="wp-post-image ngg-image-289 " />Nous
		// serons en Showcase chez ArtMania à Quimper. Venez nous rendre visite, quelques bonus vous attendront.
		final int first = _description.indexOf("<img src=");
		final int next = _description.indexOf(" alt", first + 10);
		final String img = _description.substring(first + 10, next - 1);
		final String content = _description.substring(_description.indexOf("/>") + 2);
		setImgUrl(img);

		_description = content.trim();
		Log.i("RSS", _description + " | " + imgUrl);
	}

	public void setDescription(final String desc) {
		_description = desc;
	}

	/**
	 * Sets the link.
	 * 
	 * @param link
	 *            the new link
	 */
	public void setLink(final String link) {
		_link = link;
	}

	/**
	 * Sets the category.
	 * 
	 * @param category
	 *            the new category
	 */
	public void setCategory(final List<String> category) {
		_category = category;
	}

	/**
	 * Sets the pub date.
	 * 
	 * @param pubdate
	 *            the new pub date
	 */
	public void setPubDate(final String pubdate) {
		_pubdate = pubdate;
	}

	/**
	 * Gets the title.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return _title;
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return _description;
	}

	/**
	 * Gets the link.
	 * 
	 * @return the link
	 */
	public String getLink() {
		return _link;
	}

	/**
	 * Gets the category.
	 * 
	 * @return the category
	 */
	public List<String> getCategory() {
		return _category;
	}

	/**
	 * Gets the pub date.
	 * 
	 * @return the pub date
	 */
	public String getPubDate() {
		return _pubdate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// limit how much text we display
		if (_title.length() > 42) {
			return _title.substring(0, 42) + "...";
		}
		return _title;
	}

	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator
	 *            the creator to set
	 */
	public void setCreator(final String creator) {
		this.creator = creator;
	}

	/**
	 * @return the imgUrl
	 */
	public String getImgUrl() {
		return imgUrl;
	}

	/**
	 * @param imgUrl
	 *            the imgUrl to set
	 */
	public void setImgUrl(final String imgUrl) {
		this.imgUrl = imgUrl;
	}
}

package org.giwi.android.blackout.model;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;

/**
 * @author Xavier Marin | Giwi Softwares
 */
@EBean(scope = Scope.Singleton)
public class RSSHandler extends DefaultHandler {

	// nom des tags XML
	private final String ITEM = "item";
	private final String TITLE = "title";
	private final String LINK = "link";
	private final String CATEGORY = "category";
	private final String PUBDATE = "pubDate";
	private final String CREATOR = "creator";
	private final String DESCRIPTION = "description";
	private final String MEDIA = "media:content";

	private boolean inItem;

	private RSSFeed feed;
	private RSSItem currentFeed;
	private StringBuilder buffer;
	private String media_url;

	/*
	 * Constructor
	 */
	public RSSHandler() {
	}

	@Override
	public void processingInstruction(final String target, final String data) throws SAXException {
		super.processingInstruction(target, data);
	}

	/*
	 * getFeed - this returns our feed when all of the parsing is complete
	 */
	public RSSFeed getFeed() {
		return feed;
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		// initialize our RSSFeed object - this will hold our parsed contents
		feed = new RSSFeed();
		// initialize the RSSItem object - we will use this as a crutch to grab the info from the channel
		// because the channel and items have very similar entries..
		currentFeed = new RSSItem();

	}

	@Override
	public void endDocument() throws SAXException {
	}

	@Override
	public void startElement(final String namespaceURI, final String localName, final String qName, final Attributes atts) throws SAXException {
		buffer = new StringBuilder();

		// Ci dessous, localName contient le nom du tag rencontré

		// Nous avons rencontré un tag ITEM, il faut donc instancier un nouveau feed
		if (localName.equalsIgnoreCase(ITEM)) {
			currentFeed = new RSSItem();
			inItem = true;
		}

		// Vous pouvez définir des actions à effectuer pour chaque item rencontré
		if (localName.equalsIgnoreCase(TITLE)) {
			// Nothing to do
		}
		if (localName.equalsIgnoreCase(LINK)) {
			// Nothing to do
		}
		if (localName.equalsIgnoreCase(PUBDATE)) {
			// Nothing to do
		}
		if (localName.equalsIgnoreCase(CREATOR)) {
			// Nothing to do
		}
		if (localName.equalsIgnoreCase(DESCRIPTION)) {
			// Nothing to do
		}
		if (qName.equals(MEDIA)) {
			media_url = atts.getValue("url");
		}
	}

	@Override
	public void endElement(final String namespaceURI, final String localName, final String qName) throws SAXException {
		if (localName.equalsIgnoreCase(TITLE)) {
			if (inItem) {
				// Les caractères sont dans l'objet buffer
				currentFeed.setTitle(buffer.toString());
				buffer = null;
			} else {
				feed.setTitle(buffer.toString());
				buffer = null;
			}
		}
		if (localName.equalsIgnoreCase(LINK)) {
			if (inItem) {
				currentFeed.setLink(buffer.toString());
				buffer = null;
			}
		}
		if (localName.equalsIgnoreCase(CATEGORY)) {
			if (inItem) {
				currentFeed.addCategory(buffer.toString());
				buffer = null;
			}
		}
		if (localName.equalsIgnoreCase(PUBDATE)) {
			if (inItem) {
				currentFeed.setPubDate(buffer.toString());
				buffer = null;
			} else {
				feed.setPubDate(buffer.toString());
				buffer = null;
			}
		}
		if (localName.equalsIgnoreCase(CREATOR)) {
			if (inItem) {
				currentFeed.setCreator(buffer.toString());
				buffer = null;
			}
		}
		if (localName.equalsIgnoreCase(DESCRIPTION)) {
			if (inItem) {
				currentFeed.setDescription(buffer.toString());
				buffer = null;
			}
		}
		if (qName.equals(MEDIA)) {
			currentFeed.setMedia(media_url);
			buffer = null;
		}
		if (localName.equalsIgnoreCase(ITEM)) {
			feed.addItem(currentFeed);
			inItem = false;
		}

	}

	@Override
	public void characters(final char ch[], final int start, final int length) {
		final String lecture = new String(ch, start, length);
		if (buffer != null) {
			buffer.append(lecture);
		}
	}

}

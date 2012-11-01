/**
 * 
 */
package org.giwi.android.blackout.tools;

import java.util.List;

import org.giwi.android.blackout.R;
import org.giwi.android.blackout.model.RSSItem;
import org.giwi.android.blackout.model.RSSTypes;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author Xavier Marin | Giwi Softwares
 */
public class NewsAdapter extends ArrayAdapter<RSSItem> {
	private final List<RSSItem> items;
	private final RSSTypes rssType;
	private final Context context;

	/**
	 * @param context
	 *            le contexte
	 * @param textViewResourceId
	 *            la ressource
	 * @param items
	 *            les items
	 * @param rssType
	 *            le type de flux
	 */
	public NewsAdapter(final Context context, final int textViewResourceId, final List<RSSItem> items, final RSSTypes rssType) {
		super(context, textViewResourceId, items);
		this.items = items;
		this.rssType = rssType;
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			final LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (RSSTypes.MEDIA.equals(rssType)) {
				v = vi.inflate(R.layout.media_row, null);
			} else if (RSSTypes.DATES.equals(rssType)) {
				v = vi.inflate(R.layout.date_row, null);
			} else {
				v = vi.inflate(R.layout.row, null);
			}
		}
		final RSSItem o = items.get(position);
		if (o != null) {
			final TextView tt = (TextView) v.findViewById(R.id.toptext);
			final TextView bt = (TextView) v.findViewById(R.id.bottomtext);

			if (!RSSTypes.MEDIA.equals(rssType) && tt != null && o.getPubDate() != null) {
				tt.setText(Html.fromHtml("<b>Le " + DateFormats.convertRSSDate(o.getPubDate()) + "</b>"));
			}
			if (bt != null) {
				bt.setText(o.getTitle());
			}
		}
		return v;
	}
}

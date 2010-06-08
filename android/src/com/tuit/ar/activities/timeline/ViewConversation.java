package com.tuit.ar.activities.timeline;

import android.os.Bundle;

import com.tuit.ar.R;
import com.tuit.ar.api.Twitter;


public class ViewConversation extends Status {
	static public com.tuit.ar.models.Status status;
	private com.tuit.ar.models.timeline.ViewConversation timeline;

	public void onCreate(Bundle savedInstanceState) {
		timeline = new com.tuit.ar.models.timeline.ViewConversation(Twitter.getInstance().getDefaultAccount(), status);		
		super.onCreate(savedInstanceState);
		setTitle(R.string.viewConversation);
	}

    protected boolean shouldOverrideLazyMode() {
		return true;
	}

	@Override
	protected com.tuit.ar.models.timeline.Status getTimeline() {
		return timeline;
	}

	public void timelineHasChanged(com.tuit.ar.models.Timeline timeline) {
		this.tweets.clear();
		this.filteredTweets.clear();
		addTweets(getTimeline().getTweets());

		if (isVisible)
			timelineAdapter.notifyDataSetChanged();
	}
}

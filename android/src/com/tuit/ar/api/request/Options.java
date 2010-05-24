package com.tuit.ar.api.request;

public enum Options {
	LOGIN {
		public String toString() {
			return "account/verify_credentials";
		}

		@Override
		public boolean mustBeUnique() {
			return false;
		}
	}, FRIENDS_TIMELINE {
		public String toString() {
			return "statuses/home_timeline";
		}

		@Override
		public boolean mustBeUnique() {
			return true;
		}
	}, REPLIES_TIMELINE {
		public String toString() {
			return "statuses/mentions";
		}

		@Override
		public boolean mustBeUnique() {
			return true;
		}
	}, POST_TWEET {
		public String toString() {
			return "statuses/update";
		}

		@Override
		public boolean mustBeUnique() {
			return false;
		}
	}, POST_TWEET_WITH_PHOTO {
		public String toString() {
			return "statuses/update_with_photo";
		}

		@Override
		public boolean mustBeUnique() {
			return false;
		}
	};
	public abstract boolean mustBeUnique();
}

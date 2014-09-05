App.SoundcloudPlayerComponent = Em.Component.extend({
	layoutName: "components/soundcloud-player",
	tagName: "div",
	classNames: ["col-md-6", "soundcloud-player"],
	track: null,

	authStreamUrl: function() {
		return this.track.stream_url + "?consumer_key=" + App.SOUNDCLOUD_ID;
	}.property("track"),
});

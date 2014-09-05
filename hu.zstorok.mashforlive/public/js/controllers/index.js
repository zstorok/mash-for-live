App.IndexController = Ember.Controller.extend({
	
	results: null,
	
	init: function() {

	},

	searchInput: function(){
		Em.run.debounce(this, this.search, 400);
	}.observes("soundCloudSearch"),

	search: function() {
		var val = this.soundCloudSearch;
		console.log("should search for", val, "now!");
		SC.get("/tracks", {q: val}, function(tracks, error){
			if (error) {
				console.error(error);
				return;
			}
			var nres = []
			for (i in tracks) { if (tracks.hasOwnProperty(i)){
				var t = tracks[i];
				if (t.downloadable) {
					nres.push(t);
				}
			}}
			console.debug(nres)
			this.set("results", nres);
		}.bind(this));
	},
	actions: {
		submitTrack: function(track) {
			$.post("/service/als", {soundCloudTrackId: track.id});
		},
	},

});

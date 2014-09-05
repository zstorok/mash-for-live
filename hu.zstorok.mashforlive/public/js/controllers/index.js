App.IndexController = Ember.Controller.extend({
	
	results: null,
	downloading: false,
	
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
					nres.push(Ember.Object.create(t));
				}
			}}
			console.debug(nres)
			this.set("results", nres);
		}.bind(this));
	},
	actions: {
		submitTrack: function(track) {
			track.set("downloading", true);
			$.post("/service/zip", {soundCloudTrackId: track.id})
				.done(function() {
					console.log("done");
				})
				.fail(function(err){
					console.log("failed", err);
				})
				.always(function(){
					track.set("downloading", false);
				});
		},
	},

});

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
			console.debug(tracks)
			this.set("results", tracks);
		}.bind(this));
	},
	actions: {

	},

});

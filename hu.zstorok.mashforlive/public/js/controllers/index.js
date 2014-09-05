App.IndexController = Ember.Controller.extend({

    init: function() {
    	
    },
    
    searchInput: function(){
    	Em.run.debounce(this, this.search, 200);
    }.observes("soundCloudSearch"),
    
    search: function() {
    	var val = this.soundCloudSearch;
    	console.log("should search for", val, "now!");
    },
    actions: {

    },
    
});

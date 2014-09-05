App = Ember.Application.create({
    LOG_TRANSITIONS: true,
    SOUNDCLOUD_ID: "f30741a5f09f5411b4768bac02327967",
});

App.Router.reopen({
    location: 'history'
});

App.Constants = Ember.Object.extend({
    name: 'Mash for Live',
});

SC.initialize({
	client_id: App.SOUNDCLOUD_ID,
	redirect_uri: "http://nope.nope.nope/",
});

App.Router.map(function() {
    // index route is implicit
});

App.IndexRoute = Ember.Route.extend({
    model: function() { // dummy model
        return ['red', 'yellow', 'blue'];
    }
});

App.ApplicationView = Ember.View.extend({
    templateName: 'application',
    nameBinding: 'App.Constants.name'
});

Handlebars.registerHelper('loop', function(count, options) {
    var out = "";

    for (i=0;i<count;i++) {
        out+= options.fn({i: i});
    }

    return out;
});

Ember.Handlebars.helper('noteToKey', function(value, options) {
    return MIDI.noteToKey[value+21]; // example handlebars helper
});

App.LinkLiComponent = Em.Component.extend({
    tagName: 'li',
    classNameBindings: ['active'],
    active: function() {
      return this.get('childViews').anyBy('active');
    }.property('childViews.@each.active')
  });

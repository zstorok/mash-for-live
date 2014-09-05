App = Ember.Application.create({
    LOG_TRANSITIONS: true
});

App.Router.reopen({
    location: 'history'
});

App.constants = Ember.Object.extend({
    name: 'Mash for Live'
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
    nameBinding: 'App.constants.name'
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

var mongoose = require('mongoose');  

var Schema = mongoose.Schema;  

var taskSchema = mongoose.Schema({
	taskName: String,
	taskScore: String,
	suggestedBy : String,
	approvedByUser : [String],
	approved: Boolean,
	flatPIN: String,
	taskHistory: [{
		username: String,
		date: {type: Date, default: Date.now} 
		}]
});

// mongoose.connect('mongodb://localhost:27017/node-android'); 
module.exports = mongoose.model('tasks', taskSchema);

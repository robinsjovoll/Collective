var mongoose = require('mongoose');  

var Schema = mongoose.Schema;  

var flatSchema = mongoose.Schema({
	flatName: String,
	flatPIN: String,
	period: String,
	periodStartDate: Date,
	flatPeriodCount: Number,
	prize: String,
	flatMates: [String],
	previousWinners: [{
		username: String,
		date: {type: Date, default: Date.now} 
	}]
});

// mongoose.connect('mongodb://localhost:27017/node-android'); 
module.exports = mongoose.model('flats', flatSchema);

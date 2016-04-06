var mongoose = require('mongoose');  

var Schema = mongoose.Schema;  

var userSchema = mongoose.Schema({    
     token : String,     
     email: String,  
	 username: String,
	 score: Number,
	 userPeriodCount: Number,
	 admin: Boolean,
     hashed_password: String,    
     salt : String,  
     temp_str:String 
});



mongoose.connect('mongodb://localhost:27017/node-android'); 
module.exports = mongoose.model('users', userSchema);


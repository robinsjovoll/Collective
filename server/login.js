var crypto = require('crypto'); 
var rand = require('csprng'); 
var mongoose = require('mongoose'); 
var gravatar = require('gravatar'); 
var user = require('config/user'); 
var flat = require('config/flat');

exports.login = function(email,password,callback) {  

user.find({email: email},function(err,users){  

if(users.length != 0){  

var temp = users[0].salt; 
var hash_db = users[0].hashed_password; 
var id = users[0].token; 
var newpass = temp + password; 
// console.log(hash_db);
var hashed_password = crypto.createHash('sha512').update(newpass).digest("hex"); 
// console.log(hashed_password);
var grav_url = gravatar.url(email, {s: '200', r: 'pg', d: '404'}); 
if(hash_db == hashed_password){

flat.find({flatMates:email},function(err,flats){

var flatpin;

	if(flats.length > 0)
	{
		flatpin = flats[0].flatPIN;
	}

callback({'response':"Login Success",'res':true,'token':id,'grav':grav_url, 'flatpin':flatpin,'isAdmin':users[0].admin});  
});

}else{  

callback({'response':"Invalid Password",'res':false}); 
 
/* callback({'response':"Email already Registered"});   */

} 
}else {  

callback({'response':"User not exist",'res':false});  
} 
}); 
} 
var crypto = require('crypto'); 
var rand = require('csprng'); 
var mongoose = require('mongoose'); 
var user = require('config/user');    


exports.register = function(email,username,password,callback) {  

var x = email; 
console.log("Username: " + username);
// console.log("Username: " + username);
/* if (username.length < 20 && username.length > 5){ */
/* if(!(x.indexOf("@")==x.length)){  */
/* if (password.match(/([a-z].*[A-Z])|([A-Z].*[a-z])/) && password.length > 4 && password.match(/[0-9]/)) { */

var temp =rand(160, 36); 
var newpass = temp + password;
var token = crypto.createHash('sha512').update(email +rand).digest("hex"); 
var hashed_password = crypto.createHash('sha512').update(newpass).digest("hex"); 

var newuser = new user({    
     token: token,   
     email: email, 
	 username: username,
	 admin: false,
     hashed_password: hashed_password,   
     salt :temp });  

user.find({email: email},function(err,users){  

var len = users.length;  

if(len == 0){   
     newuser.save(function (err) {   

     callback({'response':"Sucessfully Registered", 'res':true});  

}); 
}else{    

     callback({'response':"Email already Registered", 'res':false});  
}});
/* }else{      

     callback({'response':"Password Weak"});  

} */
/* }else{    

callback({'response':"Email Not Valid"});  

} */
/* }else{    

callback({'response':"username Not Valid"});  
}  */
}  
var crypto = require('crypto'); 
var rand = require('csprng'); 
var mongoose = require('mongoose'); 
var nodemailer = require('nodemailer'); 
var user = require('config/user');  

var smtpTransport = nodemailer.createTransport("SMTP",{     
     auth: {         
          user: "kollektivet.ntnu@gmail.com",   
          pass: "kollektivet123"        
          } 
});   

exports.cpass = function(id,opass,npass,callback) {  

var temp1 =rand(160, 36); 
var newpass1 = temp1 + npass; 
var hashed_passwordn = crypto.createHash('sha512').update(newpass1).digest("hex");  

user.find({token: id},function(err,users){  

if(users.length != 0){  

var temp = users[0].salt; 
var hash_db = users[0].hashed_password; 
var newpass = temp + opass; 
var hashed_password = crypto.createHash('sha512').update(newpass).digest("hex");   

if(hash_db == hashed_password){ 
if (npass.match(/([a-z].*[A-Z])|([A-Z].*[a-z])/) && npass.length > 4 && npass.match(/[0-9]/)) {  

user.findOne({ token: id }, function (err, doc){   
     doc.hashed_password = hashed_passwordn;   
     doc.salt = temp1;   
     doc.save();  

callback({'response':"Passord endret",'res':true});  
}); 
}else{  

callback({'response':"Nytt passord for svakt!",'res':false});  

} 
}else{  

callback({'response':"Passordene matcher ikke, prøv igjen!",'res':false});  

} 
}else{  

callback({'response':"Feil under endring av passord",'res':false});  

}  

}); 
}  

exports.respass_init = function(email,callback) {  

var temp =rand(24, 24); 
user.find({email: email},function(err,users){  

if(users.length != 0){   

user.findOne({ email: email }, function (err, doc){   
     doc.temp_str= temp;   
     doc.save();  

var mailOptions = {     
     from: "TEAM COLLECTIVE  <INSERT MAIL>",     
     to: email,     
     subject: "Reset Password ",     
     text: "Hello "+email+".  Code to reset your Password is "+temp+" Regards, TEAM COLLECTIVE.",  

}  

smtpTransport.sendMail(mailOptions, function(error, response){     
     if(error){  
console.log(error);
callback({'response':"Feil under reseting av passord, prøv igjen !",'res':false});      

     }else{  

callback({'response':"Sjekk din epost for å finne koden som må skrives inn for å endre passord.",'res':true});      

     } 

}); 
}); 
}else{  

callback({'response':"Emailen eksisterer ikke.",'res':false});  

} 
}); 
}  

exports.respass_chg = function(email,code,npass,callback) {   

user.find({email: email},function(err,users){  

if(users.length != 0){  

var temp = users[0].temp_str; 
var temp1 =rand(160, 36); 
var newpass1 = temp1 + npass; 
var hashed_password = crypto.createHash('sha512').update(newpass1).digest("hex");  

if(temp == code){ 
if (npass.match(/([a-z].*[A-Z])|([A-Z].*[a-z])/) && npass.length > 4 && npass.match(/[0-9]/)) { 

user.findOne({ email: email }, function (err, doc){   
     doc.hashed_password= hashed_password;   
     doc.salt = temp1;   
     doc.temp_str = "";   
     doc.save();  

callback({'response':"Passord endret",'res':true});  

});}else{  

callback({'response':"Nytt passord for svakt!",'res':false});  

} 
}else{  

callback({'response':"Koden matcher ikke, prøv igjen !",'res':false});  

} 
}else{  

callback({'response':"Feil",'res':true});  

} 
}); 
}  
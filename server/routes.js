/* app.post('/register',function(req,res){ 
var email = req.body.email;
var username = req.body.username; // Getting the parameters 
var password = req.body.password;  

register.user(username,password, email,function (found) { //Register function to perform register event 
console.log(found); // Prints the results in Console.(Optional) 
res.json(found); // Returns the result back to user in JSON format 
}); 
}); */

var chgpass = require('config/chgpass'); 
var register = require('config/register'); 
var login = require('config/login');
var taskReq = require('config/taskReq');
var flatReq = require('config/flatReq');

module.exports = function(app) {        


     app.get('/', function(req, res) {       

          res.end("Node-Android-Project");    
     });     
	 
	 //LOGIN
     app.post('/login',function(req,res){        
          var email = req.body.email;      
               var password = req.body.password;       

          login.login(email,password,function (found) {           
               console.log(found);             
               res.json(found);    
     });    
     });     

	 //REGISTER NEW USER
     app.post('/register',function(req,res){         
          var email = req.body.email;   
			var username = req.body.username;
               var password = req.body.password;       

          register.register(email,username,password,function (found) {             
               console.log(found);             
               res.json(found);    
     });     
     });     

	 //CHANGE PASSWORD
      app.post('/api/chgpass', function(req, res) {       
          var id = req.body.id;                 
               var opass = req.body.oldpass;         
          var npass = req.body.newpass;       

          chgpass.cpass(id,opass,npass,function(found){           
               console.log(found);             
               res.json(found);    
     });     
     });     

	 //RESET PASSWORD
     app.post('/api/resetpass', function(req, res) {         

          var email = req.body.email;         

          chgpass.respass_init(email,function(found){             
               console.log(found);             
               res.json(found);    
     });     
     });     

	 //RESET PASSWORD AND CHANGE
     app.post('/api/resetpass/chg', function(req, res) {         
          var email = req.body.email;         
          var code = req.body.code;       
          var npass = req.body.newpass;       

     chgpass.respass_chg(email,code,npass,function(found){           
          console.log(found);             
          res.json(found);    
     
     });     
     });   
	 
	 // ADD NEW TASK
	 app.post('/addTask', function(req, res) {
		 var taskName = req.body.taskName;
		 var taskScore = req.body.taskScore;
		 var flatPIN = req.body.flatPIN;
		 
		 taskReq.addTask(taskName, taskScore, flatPIN, function(found) {
			 console.log(found);
			 res.json(found);
		 });
	 });
	 
	 // GET ALL TASKS IN FLAT
	 app.post('/getTasks', function(req, res){
		 var flatPIN = req.body.flatPIN;
		 taskReq.getTasks(flatPIN, function(found){
			 console.log(found);
			 res.json(found);
		 });
		
	 });
	 
	 // ADD NEW TASK HISTORY EVENT
	 app.post('/addTaskHistoryEvent', function(req,res){
		var taskName = req.body.taskName;
		var username = req.body.username;
		var date = req.body.date;
		
		taskReq.addHistoryEvent(taskName,username,date, function(found){
			console.log(found);
			 res.json(found);
		});
		
	 });
	 
	 // ADD NEW FLAT
	 app.post('/addFlat', function(req, res) {
		 var flatName = req.body.flatName;
		 var period = req.body.period;
		 var prize = req.body.prize;
		 var email = req.body.email;
		 
		 flatReq.addFlat(flatName, period, prize, email, function(found) {
			 console.log(found);
			 res.json(found);
		 });
	 });
	 
	 // ADD USER TO FLAT
	 app.post('/addUserToFlat', function(req,res){
		var flatPIN = req.body.flatPIN;
		var email = req.body.email;
		
		flatReq.addUser(flatPIN,email, function(found){
			console.log(found);
			 res.json(found);
		});
		
	 });
};
var mongoose = require('mongoose'); 
var task = require('config/task'); 
var flat = require('config/flat');
var user = require('config/user');

exports.addTask = function(taskName,taskScore, flatPIN, email,callback) { 


user.find({email:email}, function(err,users){
	if(users.length > 0){
		var username = users[0].username;
		var newTask = new task({
			taskName: taskName,
			taskScore: taskScore,
			suggestedBy : username,
			taskHistory: [],
			approvedByUser: [email],
			approved: false,
			flatPIN: flatPIN
		});



		task.find({
					taskName:taskName, flatPIN: flatPIN
			}, function(err, tasks) {
			var len = tasks.length;
			// console.log(tasks.length);
			if(len == 0){
				newTask.save(function(err){
					callback({'response':"Ny oppgave laget",'res':true});
				});
			}else {
				callback({'response':"Oppgaven finnes allerede", 'res':false});
			}
		});
	}else{
		callback({'response':"Brukeren eksisterer ikke", 'res':false});
	}
});


}

exports.addPredefinedTask = function(taskName,taskScore, flatPIN) { 

		var newTask = new task({
			taskName: taskName,
			taskScore: taskScore,
			taskHistory: [],
			approved: true,
			flatPIN: flatPIN
		});

		task.find({
					taskName:taskName, flatPIN: flatPIN
			}, function(err, tasks) {
			var len = tasks.length;
			// console.log(tasks.length);
			if(len == 0){
				newTask.save(function(err){
					// callback({'response':"Successfully added task",'res':true});
				});
			}else {
				// callback({'response':"Task already exists", 'res':false});
			}
		});

}

exports.doTask = function(taskName,email,flatPIN,date,callback) {
	
	task.find({taskName:taskName, flatPIN: flatPIN}, function(err,tasks) {
		if(tasks.length > 0){ 
			
			user.find({email:email}, function(err,users) {
				if(users.length > 0){
					var thisUser = users[0];
					var thisTask = tasks[0];
					thisUser.score = thisUser.score + parseInt(thisTask.taskScore);
					var historyEvent = {
						username: thisUser.username,
						date: new Date().addHours(2)
					}
					thisTask.taskHistory.push(historyEvent);
					thisTask.save();
					thisUser.save();
					console.log(thisTask.taskHistory[thisTask.taskHistory.length-1]);
					callback({"response":"Oppgave gjennomført","res":true});
					
				}else{
					callback({"response":"Brukeren eksisterer ikke", "res":false});
				}
			});
		
		}else {
			callback({'response':"Oppgaven eksisterer ikke", 'res':false});
		}
		
		
	});
	
}

exports.getTasks = function(flatPIN, callback) {
	task.find({flatPIN:flatPIN},function(err, tasks){
		if(tasks.length > 0){
			callback({"response":tasks,"res":true});
		}else {
			callback({"response":"Ingen oppgaver eksisterer", "res":false})
		}
		
	});
}


exports.approveTask = function(flatPIN, taskName, email, callback) {
	task.find({flatPIN: flatPIN, taskName:taskName}, function(err,tasks){
		if(tasks.length > 0){
			tasks[0].approvedByUser.push(email);
			flat.find({flatPIN: flatPIN}, function(err, flats){
				if(flats.length > 0){
					if(tasks[0].approvedByUser.length > flats[0].flatMates.length/2){
						tasks[0].approved = true;
						tasks[0].save();
						callback({"response":"Godkjent", "res":true});
					}else{
						
					callback({"response":taskName + " trenger fortsatt " + Math.ceil(flats[0].flatMates.length/2 - tasks[0].approvedByUser.length) + " godkjenninger","res":true});
					}
				}else{
					
				callback({"response":"Kollektivet eksisterer ikke","res":false});
				}
				
			});
			tasks[0].save();
		}else{
			
		callback({"response":"Oppgaven eksisterer ikke","res":false});
		}
		
	});

}

exports.disapproveTask = function(flatPIN, taskName, email, callback) {
	task.find({flatPIN: flatPIN, taskName:taskName}, function(err,tasks){
		if(tasks.length > 0){
			var index = tasks[0].approvedByUser.indexOf(email);
			if (index > -1) {
				tasks[0].approvedByUser.splice(index, 1);
				tasks[0].save();
				callback({"response":taskName + " er nå ikke godkjent","res":true});
			}else{
				callback({"response":"Brukeren eksisterer ikke", "res":false});
			}
		}else{
			
		callback({"response":"Oppgaven eksisterer ikke","res":false});
		}
	});
}

exports.getTaskHistory = function(flatPIN, taskName, callback){
	task.find({flatPIN: flatPIN, taskName:taskName}, function(err,tasks){
		if(tasks.length > 0){
			callback({"response": tasks[0].taskHistory, "res":true});
		}else {
			callback({"response":"Oppgaven eksisterer ikke","res":false});
		}
	});
}

exports.getFeedHistory = function(flatPIN, numberOfHistories, callback){
	var feedHistory = [];
	task.find({flatPIN:flatPIN}, function(err,tasks){
		if(tasks.length > 0){
			for(i = 0; i < tasks.length; i++){
				for(j = 0; j < tasks[i].taskHistory.length; j++){
					var tempHistoryElement = tasks[i].taskHistory[j];
					var historyElement = {
						username: tempHistoryElement.username,
						date: tempHistoryElement.date,
						taskName: tasks[i].taskName,
						taskScore: tasks[i].taskScore
					}
					feedHistory.push(historyElement);
				}
			}	
			feedHistory.sort(function(a,b){
				// Turn your strings into dates, and then subtract them
				// to get a value that is either negative, positive, or zero.
				return new Date(b.date) - new Date(a.date);
			});
			if(parseInt(numberOfHistories) >= feedHistory.length){
				callback({"response": feedHistory,"res":true});
			}else {
				feedHistory.slice(0,parseInt(numberOfHistories)+1);
				callback({"response": feedHistory,"res":true});
			}
		}else{
			callback({"response":"Oppgaven eksisterer ikke","res":false});
		}
		
	});
}

exports.getTasksFeedBasedOnTaskName = function(flatPIN,numberOfHistories, taskName, callback){
	var feedHistory = [];
	task.find({flatPIN:flatPIN, taskName: taskName}, function(err, tasks){
		if(tasks.length > 0){
			for(i = 0; i < tasks.length; i++){
				for(j = 0; j < tasks[i].taskHistory.length; j++){
					var tempHistoryElement = tasks[i].taskHistory[j];
					var historyElement = {
						username: tempHistoryElement.username,
						date: tempHistoryElement.date,
						taskName: tasks[i].taskName,
						taskScore: tasks[i].taskScore
					}
					feedHistory.push(historyElement);
				}
			}	
			feedHistory.sort(function(a,b){
				// Turn your strings into dates, and then subtract them
				// to get a value that is either negative, positive, or zero.
				return new Date(b.date) - new Date(a.date);
			});
			if(parseInt(numberOfHistories) >= feedHistory.length){
				callback({"response": feedHistory,"res":true});
			}else {
				feedHistory.slice(0,parseInt(numberOfHistories)+1);
				callback({"response": feedHistory,"res":true});
			}
		}else{
			callback({"response":"Oppgaven eksisterer ikke","res":false});
		}
	});
}

exports.getTasksFeedBasedOnUsername = function(flatPIN,numberOfHistories, username, callback){
	var feedHistory = [];
	task.find({flatPIN: flatPIN}, function(err, tasks){
		if(tasks.length > 0){
			for(i = 0; i < tasks.length; i++){
				for(j = 0; j < tasks[i].taskHistory.length; j++){
					var tempHistoryElement = tasks[i].taskHistory[j];
					if(tempHistoryElement.username == username){
						var historyElement = {
							username: tempHistoryElement.username,
							date: tempHistoryElement.date,
							taskName: tasks[i].taskName,
							taskScore: tasks[i].taskScore
						}
						feedHistory.push(historyElement);
					}
				}
			}	
			feedHistory.sort(function(a,b){
				// Turn your strings into dates, and then subtract them
				// to get a value that is either negative, positive, or zero.
				return new Date(b.date) - new Date(a.date);
			});
			if(parseInt(numberOfHistories) >= feedHistory.length){
				callback({"response": feedHistory,"res":true});
			}else {
				feedHistory.slice(0,parseInt(numberOfHistories)+1);
				callback({"response": feedHistory,"res":true});
			}
		}else{
			callback({"response":"Oppgaven eksisterer ikke","res":false});
		}
	});
}

exports.getTasksFeedBasedOnUsernameAndTaskName = function(flatPIN,numberOfHistories,username,taskName, callback){
	var feedHistory = [];
	task.find({flatPIN: flatPIN, taskName: taskName}, function(err, tasks){
		if(tasks.length > 0){
			for(i = 0; i < tasks.length; i++){
				for(j = 0; j < tasks[i].taskHistory.length; j++){
					var tempHistoryElement = tasks[i].taskHistory[j];
					if(tempHistoryElement.username == username){
						var historyElement = {
							username: tempHistoryElement.username,
							date: tempHistoryElement.date,
							taskName: tasks[i].taskName,
							taskScore: tasks[i].taskScore
						}
						feedHistory.push(historyElement);
					}
				}
			}	
			feedHistory.sort(function(a,b){
				// Turn your strings into dates, and then subtract them
				// to get a value that is either negative, positive, or zero.
				return new Date(b.date) - new Date(a.date);
			});
			if(parseInt(numberOfHistories) >= feedHistory.length){
				callback({"response": feedHistory,"res":true});
			}else {
				feedHistory.slice(0,parseInt(numberOfHistories)+1);
				callback({"response": feedHistory,"res":true});
			}
		}else{
			callback({"response":"Oppgaven eksisterer ikke","res":false});
		}
	});
}

exports.deleteTask = function(flatPIN, taskName, callback){
	 task.remove({flatPIN: flatPIN, taskName: taskName},callback({"response": "Oppgaven ble fjernet"})).exec();
}

exports.editTask = function(flatPIN, oldTaskName, newTaskName, taskScore, callback){
	task.update({flatPIN: flatPIN, taskName:oldTaskName}, { taskName : newTaskName, taskScore: taskScore}, callback({"response": "Oppgaven ble oppdatert", "res": true})).exec();
}


// RETURNS THE CURRENT DATE + H HOURS
Date.prototype.addHours = function(h) {    
   this.setTime(this.getTime() + (h*60*60*1000)); 
   return this;   
}

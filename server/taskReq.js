var mongoose = require('mongoose'); 
var task = require('config/task'); 

exports.addTask = function(taskName,taskScore, flatPIN,callback) { 

var newTask = new task({
	taskName: taskName,
	taskScore: taskScore,
	approved: false,
	flatPIN: flatPIN
});

task.find({taskName: taskName, flatPIN: flatPIN}, function(err, tasks) {
	var len = tasks.length;
	
	if(len == 0){
		newTask.save(function(err){
			callback({'response':"Successfully added task",'res':true});
		});
	}else {
		callback({'response':"Task allready exists", 'res':false});
	}
});

}

exports.addHistoryEvent = function(taskName,username,date,callback) {
	
	task.find({taskName:taskName}, function(err,tasks) {
		
		if(tasks.length > 0){
			var historyTask = tasks[0];
			var historyEvent = {
				username: username,
				date: date
			}
			historyTask.taskHistory.push(historyEvent);
			historyTask.save(function(err){
				callback({'response':"HistoryEvent registered","res":true});
			});
		}else {
			callback({'response':"Task does not exist", 'res':false});
		}
		
		
	});
	
}

exports.getTasks = function(flatPIN, callback) {
	task.find({flatPIN: flatPIN},function(err, tasks){
		if(tasks.length > 0){
			callback({"response":tasks,"res":true});
		}else {
			callback({"response":"No tasks exists", "res:":false})
		}
		
	});
}


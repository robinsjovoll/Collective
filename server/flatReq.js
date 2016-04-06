var mongoose = require('mongoose'); 
var flat = require('config/flat');
var user = require('config/user');
var taskReq = require('config/taskReq');
var userScoreArray = [];
var preDefinedTaskNames = ["Ukentlig vask", "Ta ut søpla", "Ta ut av oppvaskmaskinen", "Vask vindu","Lag felles middag","Rydd/vask kjøleskapet","Lag felles morgenkaffe","Pant flasker","Bak kake til felleskapet","Vann felles planter"];
var preDefinedTaskScores = ["100","10","10","40","50","50","10","10","20","10"];
GLOBAL.tempID = "test";

exports.addFlat = function(flatName,period,prize,email,callback) { 
	checkFlatPIN();
	console.log(GLOBAL.tempID);
var newFlat = new flat({
	flatName: flatName,
	flatPIN: GLOBAL.tempID,
	period: period,
	periodStartDate: new Date(),
	flatPeriodCount : 0,
	prize: prize,
	flatMates: [email]
}); 
	setInterval(function(){ checkPeriod("123");}, 600000); //CHECKS IF THE PERIOD IS OUT EVERY 10 MINUTES - 600000

	for(var i = 0; i < preDefinedTaskNames.length; i++){
		
		taskReq.addPreDefinedTask(preDefinedTaskNames[i],preDefinedTaskScores[i], newFlat.flatPIN);
	}
	
newFlat.save(function(err) {
	callback({'response':"Successfully created flat", 'res':true});
});

}

exports.addUser = function(flatPIN,email,callback) {
	
	flat.find({flatPIN:flatPIN}, function(err,flats) {
		
		if(flats.length > 0){
			var currFlat = flats[0];
			currFlat.flatMates.push(email);
			currFlat.save(function(err){
				callback({'response':"You have now joined " + currFlat.flatName,"res":true});
			});
		}else {
			callback({'response':"Wrong PIN code", 'res':false});
		}
		
	});
}



exports.getScores = function(flatPIN, callback) {
flat.find({flatPIN:flatPIN}, function(err, flats){
	if(flats.length > 0){
	var currFlat = flats[0];
	for(var i = 0; i < currFlat.flatMates.length; i++)
	{
		user.find({
			email: currFlat.flatMates[i]
		}, function(err, users){
			if(users.length > 0)
			{
			//	console.log(users[i].username);
					var userScoreObject = {
					username: users[0].username,
					score: users[0].score
					};
					
					// Workaround to avoid duplicates
						var found = false;
						
						for(var i = 0; i < userScoreArray.length; i++) {
							if (userScoreArray[i].username == userScoreObject.username) {
								found = true;
								break;
							}
						}
						if(!found)
						{				
						userScoreArray.push(userScoreObject);
						}
					
					
			}
			else
			{
				callback({"response": "No such user exists", "res": false});
			}
		});		
	}
				userScoreArray.sort(function(a,b){
				// Turn your strings into dates, and then subtract them
				// to get a value that is either negative, positive, or zero.
				return b.score - a.score;
			});
			
	callback({"response": userScoreArray, "res": true});
	}
	else
	{
		callback({"response": "No such flat exists", "res": false});
	}
});
}

exports.getLastPeriodWinner = function(flatPIN, callback){
	flat.find({flatPIN:flatPIN}, function(err,flats){
		if(flats.length > 0){
			if(flats[0].previousWinners.length > 0){
				callback({"response":flats[0].previousWinners[0], "res": true});
			}else {
				callback({"response": "No previous winners", "res": false});
			}
		}else{
			callback({"response": "No such flat exists", "res": false});
		}
		
	});
}

// ADDS A SPECIFIC NUMBER OF DAYS TO A DATE
function addDays(date, days) {
    var result = new Date(date);
    result.setDate(result.getDate() + days);
    return result;
}

// CREATES A NEW FLATPIN
function checkFlatPIN(){
	GLOBAL.tempID = makeid();
	flat.find({flatPIN:GLOBAL.tempID}, function(err,flats){
		if(flats.length > 0){
			console.log("flatsExists = true");
			checkFlatPIN();
		}else {
			console.log("flatsExists = false");
			return GLOBAL.tempID;
		}
	});
}


// CHECKS IF THE CURRENT DATE IS LATER THAN THE LAST DATE OF THE PERIOD AND UPDATES THE FLATPERIODCOUNTER
function checkPeriod(flatPIN){
	flat.find({flatPIN:flatPIN},function(err, flats){
		if(flats.length > 0){
			var currDate = new Date();
			var tempPeriodDate = new Date(flats[0].periodStartDate);
			var periodDate = addDays(tempPeriodDate, parseInt(flats[0].period));
			
			// console.log("currDate: " + currDate + " periodDate: " + periodDate);
			console.log(currDate > periodDate);
			if(currDate > periodDate){
				// for(var j = 0; j < flats[0].flatMates.length; j++){
					// user.find({email: flats[0].flatMates[j]}, function(err,users){
						// if(users.length > 0){
							// var winningFlatMate = users[0];
							
						// }
					// });
				// }
				user.find({email:{$in:flats[0].flatMates}}, function(err,users){
					if(users.length > 0){
						var winningFlatMate = users[0];
						for(var i = 0; i < users.length; i++){
							if(users[i].score > winningFlatMate.score){
								winningFlatMate = users[i];
							}
						}
						// console.log("And the winner is: " + winningFlatMate.username);
						var winningJson = {
							username: winningFlatMate.username,
							date: new Date()
						}
						flats[0].previousWinners.push(winningJson);
						flats[0].save();
						console.log(flats[0].previousWinners);
						
						for(var i = 0; i < users.length; i++){
							
							users[i].score = 0;
							users[i].save();
						}
			}
				
				});
				
				
				flats[0].flatPeriodCount = flats[0].flatPeriodCount + 1;
				var newPeriodDate = addDays(periodDate, 1);
				flats[0].periodStartDate = newPeriodDate;
				flats[0].save();
			}
		}
	});
}


// CREATES A SUGGESTED FLATPIN
function makeid()
{
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for( var i=0; i < 5; i++ )
        text += possible.charAt(Math.floor(Math.random() * possible.length));

    return text;
}
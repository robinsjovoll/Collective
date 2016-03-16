var mongoose = require('mongoose'); 
var flat = require('config/flat');
var user = require('config/user');

exports.addFlat = function(flatName,period,prize,email,callback) { 
	
	var tempFlats = [0,0];
	while(tempFlats.length > 0){
		tempID = makeid();
		flat.find({flatPIN: tempID}, function(err,flats){
			tempFlats = flats;
		});
	}

var newFlat = new flat({
	flatName: flatName,
	flatPIN: tempID ,
	period: period,
	prize: prize,
	flatMates: [email]
});

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

function makeid()
{
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for( var i=0; i < 5; i++ )
        text += possible.charAt(Math.floor(Math.random() * possible.length));

    return text;
}

exports.getScores = function(flatPIN, callback) {
flat.find({flatPIN:flatPIN}, function(err, flats){
	if(flats.length > 0){
	var currFlat = flats[0];
	var userScoreArray = [];
	for(var i = 0; i < currFlat.flatMates.length; i++)
	{
		user.find({
			email: currFlat.flatMates[i]
		}, function(err, users){
			if(users.length > 0)
			{
				var userScoreObject = {
					username: users[0].username,
					score: users[0].score
				}
				userScoreArray.push(userScoreObject);
			}
			callback({"response": "No such user exists"});
		});
	}
	callback({"response": userScoreArray, "res": true});
	}
	callback({"response": "No such flat exists", "res": false});
});
}
var mongoose = require('mongoose'); 
var flat = require('config/flat'); 

exports.addFlat = function(flatName,period,prize,email,callback) { 


var newFlat = new flat({
	flatName: flatName,
	flatPIN: makeid() ,
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
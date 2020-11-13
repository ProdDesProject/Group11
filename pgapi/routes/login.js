var express = require('express');
var router = express.Router();
const bcrypt = require('bcrypt');
var db = require('../database');

router.post('/', function(request,response) {
    if(request.body.username && request.body.password) {
        var username = request.body.username;
        var password = request.body.password;
        db.query('SELECT * FROM schema1.users WHERE username = $1',[username],
        function(error, dbResults, fields) {
            if (dbResults.rows.length > 0) {
                bcrypt.compare(password,dbResults.rows[0].userpassword, function(err,res) {
                    console.log(res);
                    if (res) {
                        console.log("succes");
                        response.send(true);
                    } else {
                        console.log("Wrong password");
                        response.send(false);
                    }
                    response.end();
                });
            }
            else {
                console.log("User does not exist.");
                response.send(false);
            }
        });
    }
    else {
        console.log("Give the username and password");
        response.send(false);
    }
});

module.exports = router;
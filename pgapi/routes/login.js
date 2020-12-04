var express = require('express');
var router = express.Router();
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken')
var db = require('../database');
const user = require('../models/user_model');
const { v4: uuidv4 } = require('uuid');


router.post('/', function(request,response) {
    if(request.body.username && request.body.password) {
        var username = request.body.username;
        var password = request.body.password;
        var userId

        user.getUserId(username, function(err, result){
            if (err) {
                res.json(err);
            } else {
                try {
                    userId = result.rows[0].userid;
                } catch (error) {
                    
                }               
            }          
        })
        db.query('SELECT * FROM schema1.users WHERE username = $1',[username],
        function(error, dbResults, fields) {
            if (dbResults.rows.length > 0) {
                bcrypt.compare(password,dbResults.rows[0].userpassword, function(err,res) {
                    console.log(res);
                    if (res) {
                        console.log("succes");

                        var payload = {username: request.body.username}

                        var accessToken = jwt.sign(payload, process.env.ACCESS_TOKEN_SECRET, {
                            algorithm: "HS256",
                            expiresIn: process.env.ACCESS_TOKEN_LIFE
                        })

                        var refreshToken = jwt.sign(payload, process.env.REFRESH_TOKEN_SECRET, {
                            algorithm: "HS256",
                            expiresIn: process.env.REFRESH_TOKEN_LIFE
                        })
                        
                        /* not used in current versio
                        //save refreshtoken to database
                        user.getUserId(username).then((dbQueryResult)=>{
                            userId = dbQueryResult.rows[0].userid;
                            //console.log(userId);

                           //user.saveRefrestToken(refreshToken,'2020-11-23 15:00:00-07', userId);
                        })*/


                        var jsonAccessToken = {userID: userId, token: accessToken}

                        response.cookie("refreshToken", refreshToken, { httpOnly: true});
                        response.json(jsonAccessToken);

                    } else {
                        console.log("Wrong password");
                        response.status(401).send('Unauthorized: Wrong password');
                    }
                    response.end();
                });
            }
            else {
                console.log("User does not exist.");
                response.status(404).send('User not found');
            }
        });
    }
    else {
        console.log("Give the username and password");
        response.send(false);
    }
});

module.exports = router;
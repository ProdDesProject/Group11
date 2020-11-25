var express = require('express');
var router = express.Router();
const user = require('../models/user_model');
const jwt = require('jsonwebtoken')

router.post('/', function(req, res, next) {


    /* //not sure if jwt_verifying is used so below code is in comments 
    //get jwt_token from header
    let accessToken
    let bearerHeader = req.headers['authorization']
    if (typeof bearerHeader !== 'undefined'){
        const bearer = bearerHeader.split(' ');
        accessToken = bearer[1];
    }

    if (!accessToken){
        console.log("jwt");
        return res.status(403).send()
    }

    //verify jwt_token
    
    let payload
    try{
        payload = jwt.verify(accessToken, process.env.ACCESS_TOKEN_SECRET)
    }
    catch(e){
        console.log("jwt");
        return res.status(401).send()       
    }*/

    //get refreshtoken from cookie
    let refreshToken = req.cookies.refreshToken

    if (!refreshToken){
        console.log("refresh");
        return res.status(403).send()
    }

    //retrieve the refresh token from the database, not sure if database is used so below code is in comments
    //let tokenFromDatabase = user.getRefrestToken(userId);

    //get verify refreshtoken
    try{
        payload =  jwt.verify(refreshToken, process.env.REFRESH_TOKEN_SECRET)
    }
    catch(e){
        console.log("refresh");
        console.log(e);
        return res.status(401).send()
    }

    //make new jwt_token and refresh token
    let newpayload = {username: payload.username}

    let newAccessToken = jwt.sign(newpayload, process.env.ACCESS_TOKEN_SECRET, 
    {
        algorithm: "HS256",
        expiresIn: process.env.ACCESS_TOKEN_LIFE
    })

    let newRefreshToken = jwt.sign(newpayload, process.env.REFRESH_TOKEN_SECRET, {
        algorithm: "HS256",
        expiresIn: process.env.REFRESH_TOKEN_LIFE
    })

    //send jwt_token in response body and refreshtoken in cookie
    res.cookie("refreshToken", newRefreshToken, {httpOnly: true})
    res.json(newAccessToken);


});

module.exports = router;
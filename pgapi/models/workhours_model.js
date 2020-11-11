var db = require('../database');

var user = {
    get: function(callback) {
        return db.query('select * from schema1.users', callback);
    },
    postNewUser: function(newUser, callback){
        console.log(newUser);
        return db.query('insert into schema1.users(email, username, userpassword) values($1,$2,$3)',        
        [newUser.email, newUser.username, newUser.userpassword],
        callback
        );
    }

};



module.exports = user;
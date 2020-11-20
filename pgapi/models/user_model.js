var db = require('../database');

var user = {
    get: function(callback) {
        return db.query('select * from schema1.users', callback);
    },
    postNewUser: function(newUser, callback){
        console.log(newUser);
        return db.query('insert into schema1.users(userid, email, username, userpassword) values($1,$2,$3, $4)',        
        [newUser.id ,newUser.email, newUser.username, newUser.password],
        callback
        );
    },
    saveRefrestToken: function(newRefrestToken, username, callback){
        return db.query('update schema1.users set refrestToken=$1 where username=$2',[newRefrestToken,username],
        callback
        );
    },
    getRefrestToken: function(username, callback){
        return db.query('select refrestToken from schema1.users where username=\'$1\'',[username],callback
        );
    }
};



module.exports = user;
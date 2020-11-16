var db = require('../database');

var workhour = {
    getUsersWorkhours: function(userid, callback) {
        return db.query('select * from schema1.workhours where userid=$1', [userid], callback);
    },
    postNewWorkhour: function(newWorkhour, callback) {
        console.log(newWorkhour);
        return db.query('insert into schema1.workhours(userid, startTime, endTime, description) values($1,$2,$3,$4)',
        [newWorkhour.userid, newWorkhour.startTime, newWorkhour.endTime, newWorkhour.description],
        callback
        );
    }
};

module.exports = workhour;
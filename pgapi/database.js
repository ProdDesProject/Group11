const Pool = require('pg').Pool

const connection = new Pool({
  user: 'postgres',
  host: 'localhost',
  database: 'workhours',
  password: 'admin',
  port: 5432
})
module.exports = connection;
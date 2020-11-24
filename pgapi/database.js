const Pool = require('pg').Pool

const connection = new Pool({
  user: 'byhoimtegvorco',
  host: 'ec2-54-217-224-85.eu-west-1.compute.amazonaws.com',
  database: 'd200vf06fd9cqp',
  password: '6a546cf67abc9807e232b7d93c9db48027c6a8446c7bcd04aafe10e3b9b95f72',
  port: 5432
})
module.exports = connection;